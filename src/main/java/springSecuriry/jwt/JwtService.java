package springSecuriry.jwt;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String getToken(UserDetails user) {

        return getToken(new HashMap<>(), user);

    }

    private String getToken(Map<String,Object> extraClaims , UserDetails user) {
return Jwts
        
        
        }

}
