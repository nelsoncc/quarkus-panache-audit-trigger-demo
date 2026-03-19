package io.github.nelsoncc.auditdemo.product.dto;

import java.util.List;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "BulkOperationResponse")
public record BulkOperationResponse(long affectedRows, List<ProductResponse> products) {}
