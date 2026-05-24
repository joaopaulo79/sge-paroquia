package com.paroquiaTeam.sgeParoquia.utils;

import com.paroquiaTeam.sgeParoquia.model.Caixa;
import com.paroquiaTeam.sgeParoquia.model.Usuario;

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
	
	
	
	public Caixa getCaixa() {
		return caixaAtual;
	}
	
	public void setCaixa(Caixa caixa) {
		caixaAtual = caixa;
	}
	
	
	
	public boolean isCaixaAberto() {
        return caixaAtual != null && caixaAtual.getDataHoraFechamento() == null;
    }
	
	public void encerrarSessao() {
		this.userLogado = null;
		this.caixaAtual = null;
	}
}
