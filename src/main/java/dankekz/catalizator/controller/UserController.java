package dankekz.catalizator.controller;

import dankekz.catalizator.config.JwtUtil;
import dankekz.catalizator.domain.User;
import dankekz.catalizator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final ReactiveUserDetailsService userService;
    private final JwtUtil jwtUtil;
    private final static ResponseEntity<Object> UNAUTHORIZED = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(ServerWebExchange webExchange) {
        return webExchange.getFormData().flatMap(credentials ->
            userService.findByUsername(credentials.getFirst("username"))
                    .cast(User.class)
                    .map(user -> Objects.equals(credentials.getFirst("password"), user.getPassword())
                            ? ResponseEntity.ok(jwtUtil.generateToken(user))
                            : UNAUTHORIZED
                    )
                .defaultIfEmpty(UNAUTHORIZED)
        );
    }
}
