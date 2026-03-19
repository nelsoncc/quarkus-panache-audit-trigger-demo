package io.github.nelsoncc.auditdemo.audit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuditUserProvider {

    @Inject
    Instance<AuditRequestContext> auditRequestContext;

    public String currentUser() {
        try {
            AuditRequestContext ctx = auditRequestContext.get();
            String user = ctx.currentUser();
            if (user != null && !user.isBlank()) {
                return user;
            }
        } catch (ContextNotActiveException ignored) {
            // No request scope (e.g., scheduler, batch job) — fall back to system.
        }
        return "system";
    }
}
