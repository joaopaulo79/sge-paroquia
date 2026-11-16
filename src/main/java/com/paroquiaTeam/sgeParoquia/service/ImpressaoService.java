package com.paroquiaTeam.sgeParoquia.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.paroquiaTeam.sgeParoquia.model.dto.DadosEstadia;
import com.paroquiaTeam.sgeParoquia.utils.StringUtil;

public class ImpressaoService {

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Nome da impressora instalada no Windows/Linux (Ex: "Elgin i9", "Bematech MP-4200 TH")
    private String nomeImpressora;

    public ImpressaoService(String nomeImpressora) {
        this.nomeImpressora = nomeImpressora;
    }

    private PrintService encontrarImpressora() {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().equalsIgnoreCase(nomeImpressora)) {
                return printService;
            }
        }

        StringBuilder sb = new StringBuilder("Impressora '" + nomeImpressora + "' não encontrada. Disponíveis: ");
        for (PrintService ps : printServices) sb.append("[").append(ps.getName()).append("] ");
        throw new RuntimeException(sb.toString());
    }

    public void imprimirTicketEntrada(DadosEstadia dados) {
        PrintService printService = encontrarImpressora();

        if (printService == null) {
            throw new RuntimeException("Nenhuma impressora térmica encontrada ou configurada.");
        }

        try (PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream)) {

        	escpos.setCharacterCodeTable(CharacterCodeTable.CP860_Portuguese);

            // Estilos de texto
            Style tituloStyle = new Style()
                    .setFontSize(FontSize._2, FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);

            Style normalStyle = new Style()
                    .setFontSize(FontSize._1, FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);

            // Montagem do Ticket
            escpos.writeLF(tituloStyle, "MONGAGUÁ - PARÓQUIA");
            escpos.writeLF(tituloStyle, "NOSSA SENHORA APARECIDA");
            escpos.writeLF(tituloStyle, "ESTACIONAMENTO");
            escpos.feed(1);

            escpos.writeLF(normalStyle, "--------------------------------");
            escpos.writeLF(normalStyle, "COMPROVANTE DE ENTRADA");
            escpos.writeLF(normalStyle, "--------------------------------");

            escpos.writeLF(normalStyle, "Entrada: " + dados.dataHoraEntrada().format(formatter));
            escpos.writeLF(normalStyle, "Placa:   " + dados.placaVeiculo());
            if (dados.valor() != null) {
	            escpos.writeLF(normalStyle, "Valor pago: " + StringUtil.formatarMoeda(dados.valor()));
            }
            escpos.writeLF(normalStyle, "--------------------------------");

            escpos.feed(1);
            escpos.writeLF(new Style().setJustification(EscPosConst.Justification.Center), "Guarde este ticket para a saida.");
            escpos.feed(3); // Espaço para o corte

            // Comando para cortar o papel automaticamente (se a impressora suportar guilhotina)
            escpos.cut(EscPos.CutMode.FULL);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao imprimir ticket: " + e.getMessage());
        }
    }

    public void imprimirTicketSaida(DadosEstadia dados) {
        PrintService printService = encontrarImpressora();

        if (printService == null) {
            throw new RuntimeException("Nenhuma impressora térmica encontrada ou configurada.");
        }

        try (PrinterOutputStream printerOutputStream = new PrinterOutputStream(printService);
            EscPos escpos = new EscPos(printerOutputStream)) {

        	escpos.setCharacterCodeTable(CharacterCodeTable.CP860_Portuguese);

            // Estilos de texto
            Style tituloStyle = new Style()
                    .setFontSize(FontSize._2, FontSize._2)
                    .setJustification(EscPosConst.Justification.Center);

            Style normalStyle = new Style()
                    .setFontSize(FontSize._1, FontSize._1)
                    .setJustification(EscPosConst.Justification.Left_Default);

            // Montagem do Ticket
            escpos.writeLF(tituloStyle, "MONGAGUÁ - PARÓQUIA");
            escpos.writeLF(tituloStyle, "NOSSA SENHORA APARECIDA");
            escpos.writeLF(tituloStyle, "ESTACIONAMENTO");
            escpos.feed(1);

            escpos.writeLF(normalStyle, "--------------------------------");
            escpos.writeLF(normalStyle, "COMPROVANTE DE SAÍDA");
            escpos.writeLF(normalStyle, "--------------------------------");

            escpos.writeLF(normalStyle, "Entrada: " + dados.dataHoraEntrada().format(formatter));
            escpos.writeLF(normalStyle, "Saída: " + dados.dataHoraSaida().format(formatter));
            escpos.writeLF(normalStyle, "Placa:   " + dados.placaVeiculo());
	        escpos.writeLF(normalStyle, "Valor pago: " + StringUtil.formatarMoeda(dados.valor()));
            escpos.writeLF(normalStyle, "--------------------------------");

            escpos.feed(1);
            escpos.writeLF(new Style().setJustification(EscPosConst.Justification.Center), "Guarde este ticket para a saida.");
            escpos.feed(3); // Espaço para o corte

            // Comando para cortar o papel automaticamente (se a impressora suportar guilhotina)
            escpos.cut(EscPos.CutMode.FULL);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao imprimir ticket: " + e.getMessage());
        }
    }

    public void imprimirTeste() {
        PrintService ps = encontrarImpressora();

        try (PrinterOutputStream out = new PrinterOutputStream(ps);
             EscPos escpos = new EscPos(out)) {

            escpos.setCharacterCodeTable(EscPos.CharacterCodeTable.CP860_Portuguese);
            escpos.writeLF("TESTE DE IMPRESSORA");
            escpos.writeLF("Impressora: " + ps.getName());
            escpos.writeLF("Acentos: áéíóú ãõ ç ÁÉÍÓÚ ÃÕ Ç");
            escpos.writeLF("Data e hora: " + LocalDateTime.now().format(formatter));
            escpos.writeLF("Valor formatado: " + StringUtil.formatarMoeda(10.54));
            escpos.writeLF("--------------------------------");
            escpos.feed(3);
            escpos.cut(EscPos.CutMode.FULL);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao imprimir teste: " + e.getMessage(), e);
        }
    }
}

