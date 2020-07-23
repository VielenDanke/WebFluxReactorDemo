package dankekz.catalizator.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;

        try {
            username = jwtUtil.extractUsername(authToken);
        } catch (Exception e) {
            username = null;
            log.error(e.getLocalizedMessage());
        }

        if (username != null && jwtUtil.validateToken(authToken)) {
            Claims claimsFromToken = jwtUtil.getClaimsFromToken(authToken);

            List<SimpleGrantedAuthority> auth = (List<SimpleGrantedAuthority>) claimsFromToken.get("role", List.class)
                    .stream()
                    .map(r -> new SimpleGrantedAuthority((String) r))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    username, null, auth
            );
            return Mono.just(authentication);
        } else {
            return Mono.empty();
        }
    }
}
