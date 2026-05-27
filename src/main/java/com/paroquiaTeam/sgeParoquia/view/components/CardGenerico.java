package com.paroquiaTeam.sgeParoquia.view.components;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.VBox;

public class CardGenerico extends VBox {
    
    // Construtor privado: apenas o Builder pode instanciar
    private CardGenerico(Builder builder) {
        CardHeader header = builder.header;
        getChildren().add(header);
        
        if (builder.conteudoPrincipal != null) {
            getChildren().add(builder.conteudoPrincipal);
        }
        
        if (!builder.linhasExtras.isEmpty()) {
            VBox conteudoExpandido = new VBox();
            for (int i = 0; i < builder.linhasExtras.size(); i++) {
            	VBox linha = builder.linhasExtras.get(i);
            	
                if (i == builder.linhasExtras.size() - 1) {
                    linha.getStyleClass().add("borda-inferior-arredondada");
                }
            	
            	conteudoExpandido.getChildren().add(linha);
			}

            conteudoExpandido.setVisible(false);
            conteudoExpandido.setManaged(false);
            
            header.setOnMouseClicked(e -> {
                boolean expandido = !conteudoExpandido.isVisible();
                conteudoExpandido.setVisible(expandido);
                conteudoExpandido.setManaged(expandido);
            });
            getChildren().add(conteudoExpandido);
        }
    }

    // Classe estática interna Builder
    public static class Builder {
        private CardHeader header;
        private javafx.scene.layout.Pane conteudoPrincipal;
        private List<VBox> linhasExtras = new ArrayList<VBox>();

        public Builder(CardHeader header) {
            this.header = header;
        }

        public Builder comConteudo(javafx.scene.layout.Pane conteudo) {
            this.conteudoPrincipal = conteudo;
            return this;
        }

        public Builder adicionarLinhaExtra(VBox linha) {
            this.linhasExtras.add(linha);
            return this;
        }

        public CardGenerico build() {
            return new CardGenerico(this);
        }
    }
}
