package com.paroquiaTeam.sgeParoquia.model.dto;

import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;

public record DadosUsuario(
		String nome, 
		String login, 
		String senha,
		boolean status, 
		TipoUsuario tipo) {
	public Usuario paraUsuario(String senhaHasheada) {
		return new Usuario(this.nome,
			this.login,
			senhaHasheada,
			this.status,
			this.tipo);
	}
}
