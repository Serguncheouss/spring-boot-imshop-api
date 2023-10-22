package my.app.imshop.controller;

import my.app.imshop.exception.AuthException;
import my.app.imshop.model.jwt.JwtRegistrationRequest;
import my.app.imshop.model.jwt.JwtRequest;
import my.app.imshop.model.jwt.JwtResponse;
import my.app.imshop.model.jwt.RefreshJwtRequest;
import my.app.imshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("registration")
    public ResponseEntity<JwtResponse> registration(@RequestBody JwtRegistrationRequest authRequest) {
        final JwtResponse token = authService.registration(authRequest);
        return ResponseEntity.ok(token);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) {
        try {
            final JwtResponse token = authService.login(authRequest);
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("token")
    public ResponseEntity<?> getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.getAccessToken(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("refresh")
    public ResponseEntity<?> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        try {
            final JwtResponse token = authService.refresh(request.getRefreshToken());
            return ResponseEntity.ok(token);
        } catch (AuthException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
