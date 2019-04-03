//package com.ebm.auth;
//
//import java.util.Collection;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//public class UserSS implements UserDetails {
//
//	private static final long serialVersionUID = 1L;
//	
//	private Integer id;
//	private String login;
//	private String senha;
//
//	private Collection<? extends GrantedAuthority> authorities;
//	
//	public UserSS() {}
//	public UserSS(Integer id, String login, String senha) {
//		this.id = id;
//		this.login = login;
//		this.senha = senha;
//	}
//	public UserSS(Usuario user) {
//		this(user.getId(), user.getLogin(), user.getSenha());
//	}
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getPassword() {
//		// TODO Auto-generated method stub
//		return senha;
//	}
//
//	@Override
//	public String getUsername() {
//		// TODO Auto-generated method stub
//		return login;
//	}
//	
//
//	public Integer getId() {
//		return id;
//	}
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//}
