package my.app.imshop.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import my.app.imshop.domain.JwtAuthentication;
import my.app.imshop.exception.AuthException;
import my.app.imshop.model.Role;
import my.app.imshop.service.JwtProvider;
import my.app.imshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RoleService roleService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null && jwtProvider.validateAccessToken(token)) {
            final Claims claims = jwtProvider.getAccessClaims(token);
            final JwtAuthentication jwtInfoToken = generate(claims);
            jwtInfoToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
        }
        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    public JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setFirstName(claims.get("firstName", String.class));
        jwtInfoToken.setUsername(claims.getSubject());
        return jwtInfoToken;
    }

    private Set<Role> getRoles(Claims claims) {
        final List<?> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(r -> RoleService.convertFromMap((Map<?, ?>) r))
                .collect(Collectors.toSet());
    }
}
