package io.github.nelsoncc.auditdemo.product.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "BulkPrefixRequest", description = "Payload used by bulk update endpoints")
public record BulkPrefixRequest(
        @Schema(examples = "tmp-") @NotBlank(message = "prefix is required")
        String prefix) {}
