package com.paroquiaTeam.sgeParoquia.view.factory;

import java.util.List;

import com.paroquiaTeam.sgeParoquia.controller.PrecificacaoController;
import com.paroquiaTeam.sgeParoquia.view.PainelPrecificacao;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class PainelPrecificacaoPorHoraFactory {
	
	private final PrecificacaoController controller;
	
	public PainelPrecificacaoPorHoraFactory(PrecificacaoController controller) {
		this.controller = controller;
	}
	
	public PainelPrecificacao criar() {
		
		TextField campoTolerancia = new TextField();
		
		TextField campoEntrada = new TextField();
		TextField campoHora = new TextField();
		TextField campoDiaria = new TextField();
		
		TextField campoEntradaMoto = new TextField();
		TextField campoHoraMoto = new TextField();
		TextField campoDiariaMoto = new TextField();
		
		// TAMANHO DOS CAMPOS
        campoTolerancia.setPrefWidth(150);

        campoEntrada.setPrefWidth(150);
        campoHora.setPrefWidth(150);
        campoDiaria.setPrefWidth(150);

        campoEntradaMoto.setPrefWidth(150);
        campoHoraMoto.setPrefWidth(150);
        campoDiariaMoto.setPrefWidth(150);
		
		controller.buscarPorHora().ifPresent((p) -> {
			
			campoTolerancia.setText(Integer.toString(p.getTolerancia()));
			
			campoEntrada.setText(Double.toString(p.getValorEntrada()));
			campoHora.setText(Double.toString(p.getValorHora()));
			campoDiaria.setText(Double.toString(p.getValorDiaria()));
			
			campoEntradaMoto.setText(Double.toString(p.getValorEntradaMoto()));
			campoHoraMoto.setText(Double.toString(p.getValorHoraMoto()));
			campoDiariaMoto.setText(Double.toString(p.getValorDiariaMoto()));
		});
		
		// GRID CARROS
        GridPane gridCarros = new GridPane();

        gridCarros.setHgap(25);
        gridCarros.setVgap(10);
        gridCarros.setPadding(new Insets(10, 0, 10, 0));

        gridCarros.add(new Label("Entrada:"), 0, 0);
        gridCarros.add(new Label("Hora:"), 1, 0);
        gridCarros.add(new Label("Diária:"), 2, 0);

        gridCarros.add(campoEntrada, 0, 1);
        gridCarros.add(campoHora, 1, 1);
        gridCarros.add(campoDiaria, 2, 1);
        
        // GRID MOTOS
        GridPane gridMotos = new GridPane();

        gridMotos.setHgap(25);
        gridMotos.setVgap(10);
        gridMotos.setPadding(new Insets(10, 0, 10, 0));

        gridMotos.add(new Label("Entrada:"), 0, 0);
        gridMotos.add(new Label("Hora:"), 1, 0);
        gridMotos.add(new Label("Diária:"), 2, 0);

        gridMotos.add(campoEntradaMoto, 0, 1);
        gridMotos.add(campoHoraMoto, 1, 1);
        gridMotos.add(campoDiariaMoto, 2, 1);
        
        // BOTÃO
		Button btnSalvar = new Button("Salvar");
		VBox.setMargin(btnSalvar, new Insets(25, 0, 0, 0));
		btnSalvar.setOnAction(e -> {
			int tolerancia = Integer.parseInt(campoTolerancia.getText());
			
			double valorEntrada = 
					Double.parseDouble(campoEntrada.getText());
			double valorHora = 
					Double.parseDouble(campoHora.getText());
			double valorDiaria = 
					Double.parseDouble(campoDiaria.getText());
			double valorEntradaMoto = 
					Double.parseDouble(campoEntradaMoto.getText());
			double valorHoraMoto = 
					Double.parseDouble(campoEntradaMoto.getText());
			double valorDiariaMoto = 
					Double.parseDouble(campoDiariaMoto.getText());
			controller.salvarPrecificacaoPorHora(
					tolerancia, 
					valorEntrada, 
					valorHora, 
					valorDiaria, 
					valorEntradaMoto, 
					valorHoraMoto, 
					valorDiariaMoto
			);
		});
		
		Label tituloTolerancia = new Label("TOLERÂNCIA (MIN):");
        tituloTolerancia.setStyle(
        		"-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );

        Label tituloCarros = new Label("CARROS");
        tituloCarros.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );

        Label tituloMotos = new Label("MOTOS");
        tituloMotos.setStyle(
                "-fx-font-size: 17px;" +
                "-fx-font-weight: bold;"
        );

        List<Node> elementos = List.of(

                tituloTolerancia,
                campoTolerancia,

                new Separator(),

                tituloCarros,
                gridCarros,

                new Separator(),

                tituloMotos,
                gridMotos
        );
        
        return new PainelPrecificacao(elementos, btnSalvar);
		
	}
}
