
package springSecuriry.controlles;

import org.springframework.stereotype.Service;
import springSecuriry.dto.AuthResponse;
import springSecuriry.dto.LoginRequest;
import springSecuriry.dto.RegisterRequest;
import springSecuriry.models.Role;
import springSecuriry.models.User;
import springSecuriry.repository.UserRepository;


@Service
class AuthService {
    
    private final  UserRepository  userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    public AuthResponse login(LoginRequest req ){
        
    
    
        return null;
        
    
    
    }
    
       public AuthResponse register(RegisterRequest req ){
           
           var user =new User();
           
           user.setUsername(req.getUsername());
           user.setPassword(req.getPassword());
           user.setCiudad(req.getCiudad());
           user.setEmail(req.getEmail());
           user.setRole(Role.USER);
           
           
           
           userRepository.save(user);
        return new AuthResponse("creado");
           
           
           
    
    
       
    
    
    }
}
