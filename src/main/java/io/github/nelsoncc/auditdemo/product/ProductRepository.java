package io.github.nelsoncc.auditdemo.product;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ProductRepository implements PanacheRepositoryBase<Product, Long> {

    @Inject
    EntityManager entityManager;

    public long archiveByPrefixWithPanache(String prefix) {
        return update("archived = true where archived = false and name like ?1", likePattern(prefix));
    }

    public int unarchiveByPrefixWithNativeSql(String prefix) {
        return entityManager
                .createNativeQuery("""
				update demo_product
				   set archived = false
				 where archived = true
				   and name like :pattern
				""")
                .setParameter("pattern", likePattern(prefix))
                .executeUpdate();
    }

    private String likePattern(String prefix) {
        return prefix + "%";
    }
}
