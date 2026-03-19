package io.github.nelsoncc.auditdemo.product.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CreateProductRequest", description = "Payload used to create a product")
public record CreateProductRequest(
        @Schema(examples = "tmp-alpha") @NotBlank(message = "name is required")
        String name) {}
