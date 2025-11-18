
package springSecuriry.controlles;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springSecuriry.dto.AuthResponse;
import springSecuriry.dto.LoginRequest;
import springSecuriry.dto.RegisterRequest;
import springSecuriry.jwt.JwtService;
import springSecuriry.models.Role;
import springSecuriry.models.User;
import springSecuriry.repository.UserRepository;


@Service
class AuthService {
    private final JwtService jwtService;
    private final  UserRepository  userRepository;
    private final AuthenticationManager authenticationManager;
    private  final PasswordEncoder passwordEncoder;

    public AuthService(JwtService jwtService, UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

   
    

    
   
    
    public AuthResponse login(LoginRequest req ){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword()));
        
        var user=userRepository.findByUsername(req.getUsername()).orElseThrow(()-> new UsernameNotFoundException("no se encontro el usuario ") );
        
        var token=jwtService.getToken(user);
        
        
         return new AuthResponse(token);
        
      
    
    
  
        
    
    
    }
    
       public User register(RegisterRequest req ){
           
           var user =new User();
           
           user.setUsername(req.getUsername());
           user.setPassword(passwordEncoder.encode(req.getPassword()));
           user.setCiudad(req.getCiudad());
           user.setEmail(req.getEmail());
           user.setRole(Role.USER);
           
           
           
           userRepository.save(user);
          
        return user;
           
           
           
    
    
       
    
    
    }

   
}
