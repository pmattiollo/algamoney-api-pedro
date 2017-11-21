package com.algaworks.algamoneyapi.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algamoneyapi.config.property.AlgamoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Autowired
    private AlgamoneyApiProperty property;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {

	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	HttpServletResponse httpServletResponse = (HttpServletResponse) response;

	httpServletResponse.setHeader("Access-Control-Allow-Origin", property.getOriginPermitida());
	httpServletResponse.setHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());

	if (httpServletRequest.getMethod().equals("OPTIONS")
		&& httpServletRequest.getHeader("Origin").equals(property.getOriginPermitida())) {
	    httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
	    httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorizations, Content-Type, Accept");
	    httpServletResponse.setHeader("Access-Control-Max-Age", "3600");

	    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
	} else {
	    chain.doFilter(request, response);
	}
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
