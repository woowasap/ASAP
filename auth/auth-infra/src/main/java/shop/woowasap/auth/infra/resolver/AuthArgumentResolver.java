package shop.woowasap.auth.infra.resolver;

import javax.security.auth.login.LoginException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shop.woowasap.auth.domain.in.LoginUser;
import shop.woowasap.auth.domain.in.TokenProvider;

@Slf4j
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION = "Authorization";

    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class) &&
            parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        final String bearerToken = webRequest.getHeader(AUTHORIZATION);
        log.info("bearerToken : " + bearerToken);
        final String accessToken = tokenProvider.extractAccessToken(bearerToken)
            .orElseThrow(LoginException::new);
        return tokenProvider.getUserId(accessToken);
    }
}
