package io.github.nelsoncc.auditdemo.audit;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class AuditRequestContext {

    private String currentUser = "system";

    public String currentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser == null || currentUser.isBlank() ? "system" : currentUser;
    }
}
