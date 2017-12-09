package com.realestate.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import com.realestate.exceptions.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

public class JwtFilter extends GenericFilterBean {

	public static final String AUTHORIZATION = "Authorization";
	private final String secret;

	public JwtFilter(String secret) {
		this.secret = secret;
	}

	
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
			
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		String authHeader = req.getHeader(AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new ServletException("Not a valid authentication authHeader");
		}

		String compactJws = authHeader.substring(7);

		try {
			Claims token = Jwts.parser().setSigningKey(secret)
					.parseClaimsJws(compactJws).getBody();
		
			// TODO(asn): extract user for this token and append it to the
			// request.

			servletRequest.setAttribute("token", token);
		} catch (SignatureException ex) {
			throw new ServletException("Invalid Token");
		} catch (MalformedJwtException ex) {
			throw new ServletException("JWT token is malformed");
		} catch (ExpiredJwtException ex) {
			throw new ServletException("JWT token expired, So please do login");
		}catch (Exception ex) {
			throw new ServletException(ex.getMessage());
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	/*@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		 HttpServletRequest request = (HttpServletRequest) req;
		    HttpServletResponse response = (HttpServletResponse) res;

		    response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		    response.setHeader("Access-Control-Allow-Credentials", "true");
		    response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		    response.setHeader("Access-Control-Max-Age", "3600");
		    response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");

		    chain.doFilter(req, res);
	}*/
}
