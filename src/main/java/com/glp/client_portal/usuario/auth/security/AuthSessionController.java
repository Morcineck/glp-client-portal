package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.usuario.auth.dto.AuthSessionResponse;
import com.glp.client_portal.usuario.auth.dto.CsrfResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.csrf.CsrfToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
public class AuthSessionController {

    private final boolean cookieSecure;
    private final String cookieSameSite;

    public AuthSessionController(
            @Value("${app.auth.cookie-secure:false}") boolean cookieSecure,
            @Value("${app.auth.cookie-same-site:Lax}") String cookieSameSite
    ) {
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthSessionResponse> me(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String role = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("");

        return ResponseEntity.ok(
                new AuthSessionResponse(userDetails.getUsername(), role)
        );
    }

    @GetMapping("/csrf")
    public ResponseEntity<CsrfResponse> csrf(CsrfToken csrfToken) {
        return ResponseEntity.ok(new CsrfResponse(csrfToken.getToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie authCookie = ResponseCookie.from(AuthController.AUTH_COOKIE, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        ResponseCookie csrfCookie = ResponseCookie.from("XSRF-TOKEN", "")
                .httpOnly(false)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(Duration.ZERO)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, authCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, csrfCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
