
package springSecuriry.dto;


public class RegisterRequest {
   private String username;
   private String password;
   private String email;
   private String ciudad;

    public RegisterRequest(String username, String password, String email, String ciudad) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.ciudad = ciudad;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
   
   
   
   
}
