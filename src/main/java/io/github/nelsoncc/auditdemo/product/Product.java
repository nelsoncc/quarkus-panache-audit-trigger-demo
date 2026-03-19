package io.github.nelsoncc.auditdemo.product;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "demo_product")
public class Product extends PanacheEntityBase {

    @Id
    @SequenceGenerator(name = "demoProductSeq", sequenceName = "demo_product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "demoProductSeq")
    public Long id;

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "archived", nullable = false)
    public boolean archived;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    public OffsetDateTime createdAt;

    @Column(name = "created_by", nullable = false, insertable = false, updatable = false)
    public String createdBy;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    public OffsetDateTime updatedAt;

    @Column(name = "updated_by", nullable = false, insertable = false, updatable = false)
    public String updatedBy;
}
