package com.paroquiaTeam.sgeParoquia.service;

import java.util.Optional;

import com.paroquiaTeam.sgeParoquia.dao.UsuarioDAO;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;

import jakarta.persistence.EntityNotFoundException;

public class UsuarioService {
	UsuarioDAO dao = new UsuarioDAO();
	
	public boolean existePorId(Long id) {
		return dao.existsById(id);
	}
	
	public Optional<Usuario> buscarPorId(Long id) {
		return dao.getById(id);
	}
	
	public Usuario obterPorId(Long id) {
		return buscarPorId(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuárop não encontrado para o id "+id));
	}
}
