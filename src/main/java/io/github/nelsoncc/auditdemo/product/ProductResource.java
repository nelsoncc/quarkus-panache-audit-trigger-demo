package io.github.nelsoncc.auditdemo.product;

import io.github.nelsoncc.auditdemo.product.dto.BulkOperationResponse;
import io.github.nelsoncc.auditdemo.product.dto.BulkPrefixRequest;
import io.github.nelsoncc.auditdemo.product.dto.CreateProductRequest;
import io.github.nelsoncc.auditdemo.product.dto.ProductResponse;
import io.github.nelsoncc.auditdemo.product.dto.RenameProductRequest;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Products")
public class ProductResource {

    @Inject
    ProductService productService;

    @POST
    @Operation(summary = "Create a product")
    public RestResponse<ProductResponse> create(@Valid CreateProductRequest request) {
        ProductResponse response = productService.create(request.name());
        return RestResponse.status(RestResponse.Status.CREATED, response);
    }

    @GET
    @Operation(summary = "List all products")
    public List<ProductResponse> listAll() {
        return productService.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Fetch a single product")
    public ProductResponse getById(@PathParam("id") Long id) {
        return productService.getById(id);
    }

    @PUT
    @Path("/{id}/rename-managed")
    @Operation(summary = "Rename a product through the managed-entity path")
    public ProductResponse renameManaged(@PathParam("id") Long id, @Valid RenameProductRequest request) {
        return productService.renameManaged(id, request.newName());
    }

    @POST
    @Path("/archive/panache")
    @Operation(summary = "Archive products through Panache.update(...)")
    public BulkOperationResponse archiveByPrefixWithPanache(@Valid BulkPrefixRequest request) {
        return productService.archiveByPrefixWithPanache(request.prefix());
    }

    @POST
    @Path("/unarchive/native")
    @Operation(summary = "Unarchive products through native SQL")
    public BulkOperationResponse unarchiveByPrefixWithNativeSql(@Valid BulkPrefixRequest request) {
        return productService.unarchiveByPrefixWithNativeSql(request.prefix());
    }
}
