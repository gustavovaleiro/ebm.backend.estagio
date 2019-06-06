package com.ebm.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ebm.pessoal.domain.Funcionario;
import com.ebm.pessoal.domain.HistoricoCadastral;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false, length = 30)
	private String login;


	@Column(nullable = false)
	private String senha;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Integer> permissoes = new HashSet<>();
	@MapsId
	@OneToOne
	private Funcionario funcionario;
	@Embedded
	private HistoricoCadastral historico= new HistoricoCadastral();
	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	public Usuario() {
	}

	public Usuario(Integer id, String login, String senha) {
		this.id = id;
		this.login = login;
		this.senha = senha;
	
	}

	public String getEmail() {
		return this.funcionario.getPessoa().getEmailPrincipal().getEmail();
	}

	public Usuario(Integer id, String login, String senha, Set<PermissaoE> permissoes) {
		this.id = id;
		this.login = login;
		this.senha = senha;
		this.authorities = permissoes.stream().map(x -> new SimpleGrantedAuthority(x.getNome()))
				.collect(Collectors.toSet());

	}

	public Usuario(Usuario user) {
		this(user.getId(), user.getLogin(), user.getSenha(), user.getPermissoes());
	}

	public Integer getId() {
		return id;
	}
	
	
	@Transient
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Transient
	@Override
	public String getPassword() {
		return senha;
	}

	@Transient
	@Override
	public String getUsername() {
		return login;
	}

	@Transient
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public Set<PermissaoE> getPermissoes() {
		return permissoes.stream().map(p -> PermissaoE.toEnum(p)).collect(Collectors.toSet());
	}
	public void addPermissao(PermissaoE permissao) {
		this.permissoes.add(permissao.getId());
	}

	public void removePermissao(PermissaoE permissao) {
		this.permissoes.remove(permissao.getId());
	}


	public void setPermissao(Set<PermissaoE> permissao) {
		this.permissoes = permissao.stream().map(p -> p.getId()).collect(Collectors.toSet());
	}
	
}
