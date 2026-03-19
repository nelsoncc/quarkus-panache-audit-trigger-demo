package io.github.nelsoncc.auditdemo.product;

import io.github.nelsoncc.auditdemo.product.dto.BulkOperationResponse;
import io.github.nelsoncc.auditdemo.product.dto.ProductResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepository;

    @Inject
    ProductMapper productMapper;

    @Inject
    EntityManager entityManager;

    @Transactional
    public ProductResponse create(String name) {
        Product product = new Product();
        product.name = name;
        product.archived = false;

        productRepository.persist(product);
        entityManager.flush();
        entityManager.refresh(product);

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse renameManaged(Long id, String newName) {
        Product product = productRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " was not found"));

        product.name = newName;

        entityManager.flush();
        entityManager.refresh(product);

        return productMapper.toResponse(product);
    }

    @Transactional
    public BulkOperationResponse archiveByPrefixWithPanache(String prefix) {
        long affectedRows = productRepository.archiveByPrefixWithPanache(prefix);

        entityManager.clear();

        return new BulkOperationResponse(affectedRows, listAll());
    }

    @Transactional
    public BulkOperationResponse unarchiveByPrefixWithNativeSql(String prefix) {
        long affectedRows = productRepository.unarchiveByPrefixWithNativeSql(prefix);

        entityManager.clear();

        return new BulkOperationResponse(affectedRows, listAll());
    }

    public List<ProductResponse> listAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Product " + id + " was not found"));

        return productMapper.toResponse(product);
    }
}
