package io.github.nelsoncc.auditdemo.product.dto;

import java.time.OffsetDateTime;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "ProductResponse")
public record ProductResponse(
        Long id,
        String name,
        boolean archived,
        OffsetDateTime createdAt,
        String createdBy,
        OffsetDateTime updatedAt,
        String updatedBy) {}
