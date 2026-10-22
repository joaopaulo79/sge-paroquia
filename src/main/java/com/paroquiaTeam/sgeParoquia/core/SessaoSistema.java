package com.paroquiaTeam.sgeParoquia.core;

import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Usuario;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoUsuario;

public class SessaoSistema {
	private static SessaoSistema instancia;

	private Usuario userLogado;
	private Caixa caixaAtual;

	private SessaoSistema() {}

	public static SessaoSistema getInstancia() {
		if (instancia == null) {
			instancia = new SessaoSistema();
		}
		return instancia;
	}

	public Usuario getUserLogado() {
		return userLogado;
	}

	public void setUserLogado(Usuario userLogado) {
		this.userLogado = userLogado;
	}

	public boolean isUserLogadoAdministrador() {
		return userLogado.getTipo() == TipoUsuario.ADMINISTRADOR;
	}

	public boolean isIdDoUserLogado(Long id) {
		return id == userLogado.getId();
	}



	public Caixa getCaixa() {
		return caixaAtual;
	}

	public void setCaixa(Caixa caixa) {
		caixaAtual = caixa;
	}



	public boolean temCaixaAberto() {
        return caixaAtual != null && caixaAtual.getDataHoraFechamento() == null;
    }

	public void encerrarSessao() {
		this.userLogado = null;
		this.caixaAtual = null;
	}
}
