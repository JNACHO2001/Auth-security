package springSecuriry.controlles;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springSecuriry.dto.ApiResponse;
import springSecuriry.dto.AuthResponse;
import springSecuriry.dto.LoginRequest;
import springSecuriry.dto.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authservice;

    public AuthController(AuthService authservice) {
        this.authservice = authservice;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        
        var token =authservice.login(req);

        return ResponseEntity.ok(token);

    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest req) {
   var registeredData = authservice.register(req);

            // 2. Construir el ApiResponse con los 3 campos: success, message, y data
            var response = new ApiResponse(
                true,
                "Registro exitoso. El usuario ha sido creado.",
                registeredData // <-- ¡Aquí se incluye el objeto de respuesta!
            );
            
            // 3. Devolver la respuesta con código HTTP 201 (Created)
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
