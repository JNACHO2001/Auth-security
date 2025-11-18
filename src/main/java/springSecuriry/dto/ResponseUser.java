
package springSecuriry.dto;

import springSecuriry.models.Role;

public class ResponseUser {
      private Long id;
    private String username;
    private String email;
    private String ciudad;
    private Role role;

    public ResponseUser(Long id, String username, String email, String ciudad, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.ciudad = ciudad;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    
}
