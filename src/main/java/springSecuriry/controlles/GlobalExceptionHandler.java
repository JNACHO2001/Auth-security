package springSecuriry.controlles;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springSecuriry.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<ApiResponse> handleAuthenticationExceptions(RuntimeException ex) {
        
        // El mensaje de error puede ser genérico por seguridad
        String message = "Credenciales inválidas: nombre de usuario o contraseña incorrectos.";
        
        // Si quieres diferenciar:
        if (ex instanceof UsernameNotFoundException) {
             message = "Usuario no encontrado.";
        }
        
        ApiResponse response = new ApiResponse(
            false,
            message,
            null
        );
        
        // Devolver HTTP 401 (Unauthorized)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleValidationException(IllegalArgumentException ex) {
        
        // Captura la excepción que lanzaste para la contraseña débil en tu AuthService
        ApiResponse response = new ApiResponse(
            false,
            ex.getMessage(),
            null
        );
        
        // Devolver HTTP 400 (Bad Request)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Puedes agregar más @ExceptionHandler para otros errores (ej. 500, 409, etc.)
}