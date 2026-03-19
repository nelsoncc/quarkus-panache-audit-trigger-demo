package io.github.nelsoncc.auditdemo.product;

import io.github.nelsoncc.auditdemo.product.dto.ProductResponse;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.id,
                product.name,
                product.archived,
                product.createdAt,
                product.createdBy,
                product.updatedAt,
                product.updatedBy);
    }
}
