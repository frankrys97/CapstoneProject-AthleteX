package francescocristiano.CapstoneProject.security;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenInvalidateService {

    private final Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    public boolean isTokenInvalidated(String token) {
        return invalidatedTokens.contains(token);
    }

    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }
}
