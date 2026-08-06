package com.paroquiaTeam.sgeParoquia.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;

@Entity
@Table(name="USUARIO")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;
	
	@Column(name = "nome_usuario", nullable = false)
	private String nome;
	
	@Column(name = "login_usuario", unique = true, nullable = false)
	private String login;
	
	@Column(name = "senha_usuario", nullable = false)
	private String senha;
	
	@Column(name = "status_usuario", nullable = false)
	private boolean status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_usuario", nullable = false)
	private TipoUsuario tipo;
	
	@OneToMany(mappedBy = "operador")
	private List<Caixa> caixas;
	
	
	
	public Usuario() {}
	
	public Usuario(String nome, String login, String senha, 
					boolean status, TipoUsuario tipo) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		this.status = status;
		this.tipo = tipo;
	}
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	
	public TipoUsuario getTipo() {
		return tipo;
	}
	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	
	
	public List<Caixa> getCaixas() {
		return caixas;
	}
	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", login=" + login + ", senha=" + senha + ", status=" + status
				+ ", tipo=" + tipo + ", caixas=" + caixas + "]";
	}
}
