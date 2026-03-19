package io.github.nelsoncc.auditdemo.audit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.TransactionScoped;
import java.sql.PreparedStatement;
import org.hibernate.Session;

@ApplicationScoped
public class PostgresAuditTransactionListener {

    private static final String SET_AUDIT_USER_SQL = "select set_config('app.user', ?, true)";

    @Inject
    EntityManager entityManager;

    @Inject
    AuditUserProvider auditUserProvider;

    void onTransactionBegin(@Observes @Initialized(TransactionScoped.class) Object ignored) {
        String auditUser = auditUserProvider.currentUser();

        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(SET_AUDIT_USER_SQL)) {
                preparedStatement.setString(1, auditUser);
                preparedStatement.execute();
            }
        });
    }
}
