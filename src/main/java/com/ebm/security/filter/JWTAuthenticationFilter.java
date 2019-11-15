package com.ebm.security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ebm.security.JWTUtil;
import com.ebm.security.Usuario;
import com.ebm.security.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	private AuthenticationManager authenticationManager;
	
	private JWTUtil jwtUtil;
	
	public JWTAuthenticationFilter( AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;

	}
	
	
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			CredenciaisDTO creds = new ObjectMapper().readValue(request.getInputStream(), CredenciaisDTO.class);
			System.out.println(creds.getLogin() + "|"+creds.getPassword());
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getLogin(), creds.getPassword(), new HashSet<>());
			Authentication auth = this.authenticationManager.authenticate(authToken);
			return auth;
		} catch (IOException e) 
		{throw new RuntimeException(e);}
	}

	

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
	
		String username = ((Usuario) authResult.getPrincipal()).getUsername();
		String token = jwtUtil.generatedToken(username);
		response.addHeader("Authorization",  "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		
	}



	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler{

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
			
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": "+date+","
					+ "\"status\": 401,"
					+ "\"error\": \"Não Autorizado\","
					+ "\"message\": \"Email ou senha invalidos\","
					+ "\"patch\": \"/login\"}";
		}
		
	}
	
	
	
}
