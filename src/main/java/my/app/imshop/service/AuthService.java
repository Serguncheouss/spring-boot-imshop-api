package my.app.imshop.service;

import io.jsonwebtoken.Claims;
import lombok.NonNull;
import my.app.imshop.domain.JwtAuthentication;
import my.app.imshop.exception.AuthException;
import my.app.imshop.model.User;
import my.app.imshop.model.jwt.JwtRegistrationRequest;
import my.app.imshop.model.jwt.JwtRequest;
import my.app.imshop.model.jwt.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AuthService {
    private final Map<String, String> refreshStorage = new HashMap<>();
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    public JwtResponse registration(@NonNull JwtRegistrationRequest authRequest) {
        var user = new User();
        user.setLogin(authRequest.getLogin());
        user.setPassword(authRequest.getPassword());
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());

        var roles = Stream.of(roleService.getByTitle("USER")
                .orElseThrow(() -> new AuthException("Группа USER не найдена")))
                .collect(Collectors.toSet());

        // First user always admin
        if (userService.count() == 0) {
            roles.add(roleService.getByTitle("ADMIN")
                    .orElseThrow(() -> new AuthException("Группа ADMIN не найдена")));
        }

        user.setRoles(roles);

        if (userService.create(user)) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        }

        var loginRequest = new JwtRequest();
        loginRequest.setLogin(authRequest.getLogin());
        loginRequest.setPassword(authRequest.getPassword());
        return login(loginRequest);
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) {
        final User user = userService.getByLogin(authRequest.getLogin())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.getByLogin(login)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
