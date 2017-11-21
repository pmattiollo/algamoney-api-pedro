package com.algaworks.algamoneyapi.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.algaworks.algamoneyapi.config.property.AlgamoneyApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

    @Autowired
    private AlgamoneyApiProperty property;

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
	    MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
	    ServerHttpRequest request, ServerHttpResponse response) {
	HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
	HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

	DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
	String refreshToken = body.getRefreshToken().getValue();

	adicionarRefreshTokenNoCookie(refreshToken, servletRequest, servletResponse);
	removerRefreshTokenDoBody(token);

	return body;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
	return returnType.getMethod().getName().equals("postAccessToken");
    }

    private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest servletRequest,
	    HttpServletResponse servletResponse) {
	Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);

	refreshTokenCookie.setHttpOnly(true);
	refreshTokenCookie.setSecure(property.getSeguranca().isEnableHttps());
	refreshTokenCookie.setPath(servletRequest.getContextPath() + "/oauth/token");
	refreshTokenCookie.setMaxAge(2592000);

	servletResponse.addCookie(refreshTokenCookie);
    }

    private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
	token.setRefreshToken(null);
    }

}
