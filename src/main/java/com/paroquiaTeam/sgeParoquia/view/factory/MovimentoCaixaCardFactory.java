package com.paroquiaTeam.sgeParoquia.view.factory;

import java.util.function.Consumer;

import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.utils.StringUtil;
import com.paroquiaTeam.sgeParoquia.view.components.CardGenerico;
import com.paroquiaTeam.sgeParoquia.view.components.CardHeader;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MovimentoCaixaCardFactory {
	public CardGenerico criarCardExpandivel(MovimentoCaixa m, Consumer<Long> onExcluir, boolean caixaAberto) {
		CardHeader header = new CardHeader(m.getId());
		
		HBox linhaPrincipal = new HBox();
		linhaPrincipal.getChildren().add(new Label(StringUtil.formatarMoeda(m.getValor())));
		linhaPrincipal.getChildren().add(new Label(m.getTipoMovimento().toString()));
		
		if (caixaAberto) {
			if (onExcluir != null) {
				Button btnExcluir = new Button("Excluir");
				btnExcluir.setOnAction(e -> onExcluir.accept(m.getId()));
				linhaPrincipal.getChildren().add(btnExcluir);
			} else {
				throw new IllegalArgumentException("onExcluir é obrigatório quando caixa está aberto");
			}
		}
		
		VBox linhaSecundaria = new VBox();
		linhaSecundaria.getChildren().addAll(
				new Label(m.getFormaPagamento().toString()),
				new Label(m.getDescricao()));
		
		CardGenerico card = new CardGenerico.Builder(header)
				.comConteudo(linhaPrincipal)
				.adicionarLinhaExtra(linhaSecundaria)
				.build();
		
		return card;
	}
}
