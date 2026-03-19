package io.github.nelsoncc.auditdemo.product.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "RenameProductRequest", description = "Payload used to rename a product through the managed-entity path")
public record RenameProductRequest(
        @Schema(examples = "tmp-alpha-renamed") @NotBlank(message = "newName is required")
        String newName) {}
