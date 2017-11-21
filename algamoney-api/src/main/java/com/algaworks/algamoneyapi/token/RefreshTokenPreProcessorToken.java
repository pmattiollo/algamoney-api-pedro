package com.algaworks.algamoneyapi.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenPreProcessorToken implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;

	if (httpServletRequest.getRequestURI().equalsIgnoreCase("/oauth/token")
		&& httpServletRequest.getParameter("grant_type").equals("refresh_token")
		&& httpServletRequest.getCookies() != null) {

	    for (Cookie cookie : httpServletRequest.getCookies()) {
		if (cookie.getName().equals("refreshToken")) {
		    String refreshToken = cookie.getValue();
		    httpServletRequest = new MyHttpServletRequestWrapper(httpServletRequest, refreshToken);
		}
	    }
	}

	chain.doFilter(httpServletRequest, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    static class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private String refreshToken;

	public MyHttpServletRequestWrapper(HttpServletRequest httpServletRequest, String refreshToken) {
	    super(httpServletRequest);
	    this.refreshToken = refreshToken;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
	    ParameterMap<String, String[]> parameterMap = new ParameterMap<>(getRequest().getParameterMap());
	    parameterMap.put("refresh_token", new String[] { refreshToken });
	    parameterMap.setLocked(true);
	    return parameterMap;
	}

    }

}
