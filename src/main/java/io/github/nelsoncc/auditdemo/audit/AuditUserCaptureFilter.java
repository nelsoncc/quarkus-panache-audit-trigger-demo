package io.github.nelsoncc.auditdemo.audit;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION + 10)
public class AuditUserCaptureFilter implements ContainerRequestFilter {

    public static final String AUDIT_USER_HEADER = "X-Audit-User";

    @Inject
    AuditRequestContext auditRequestContext;

    @Inject
    SecurityIdentity securityIdentity;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authenticatedUser = extractAuthenticatedUser();
        String headerUser = normalize(requestContext.getHeaderString(AUDIT_USER_HEADER));

        auditRequestContext.setCurrentUser(
                authenticatedUser != null ? authenticatedUser : headerUser != null ? headerUser : "system");
    }

    private String extractAuthenticatedUser() {
        if (securityIdentity == null || securityIdentity.isAnonymous() || securityIdentity.getPrincipal() == null) {
            return null;
        }

        return normalize(securityIdentity.getPrincipal().getName());
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
