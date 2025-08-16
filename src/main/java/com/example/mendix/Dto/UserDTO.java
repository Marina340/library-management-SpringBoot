package com.example.mendix.Dto;
import java.util.Set;

public class UserDTO {
    private String username;
    private String password;  // plain text during creation or update
    private boolean enabled;
    private Set<String> roleNames; // e.g., "ADMIN", "LIBRARIAN", "STAFF"

    // ===== Getters & Setters =====
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Set<String> getRoleNames() { return roleNames; }
    public void setRoleNames(Set<String> roleNames) { this.roleNames = roleNames; }
}
