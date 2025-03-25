package com.mohand.SchoolManagmentSystem.config;

import com.mohand.SchoolManagmentSystem.enums.Role;
import com.mohand.SchoolManagmentSystem.model.Teacher;
import com.mohand.SchoolManagmentSystem.model.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.util.UriTemplate;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
public class TeacherRequestAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    private String extractUserIdFromUri(String uri) {
        Pattern pattern = Pattern.compile(".*/(?<userId>[^/?]+)(\\?.*)?$");
        Matcher matcher = pattern.matcher(uri);
        return matcher.find() ? matcher.group("userId") : null;
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext object) {

        String userIdFromRequestUri = extractUserIdFromUri(object.getRequest().getRequestURI());


        Authentication authentication = authenticationSupplier.get();
        Long userIdFromJwt = ((Teacher) authentication.getPrincipal()).getId();

        boolean hasTeacherRole = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(Role.ROLE_TEACHER.getValue()));

        boolean userIdsMatch = userIdFromRequestUri != null && userIdFromRequestUri.equals(userIdFromJwt.toString());

        return new AuthorizationDecision(hasTeacherRole && userIdsMatch);
    }
}
