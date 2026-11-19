package com.paroquiaTeam.sgeParoquia.service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openpdf.text.Chunk;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.draw.DottedLineSeparator;
import org.openpdf.text.pdf.draw.LineSeparator;

import com.paroquiaTeam.sgeParoquia.model.entity.Caixa;
import com.paroquiaTeam.sgeParoquia.model.entity.Estadia;
import com.paroquiaTeam.sgeParoquia.model.entity.MovimentoCaixa;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoMovimento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoPagamento;
import com.paroquiaTeam.sgeParoquia.model.enums.TipoVeiculo;
import com.paroquiaTeam.sgeParoquia.utils.StringUtil;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

public class RelatoriosService {
	public enum TipoRelatorio {
		FECHAMENTOCAIXA,
		MOVIMENTOS
	}

	private static final Font FONT_TITULO = new Font(Font.HELVETICA, 14, Font.BOLD);
	private static final Font FONT_SUBTITULO = new Font(Font.HELVETICA, 12, Font.BOLD);
	private static final Font FONT_DATA = new Font(Font.HELVETICA, 11, Font.NORMAL);
	private static final Font FONT_ITEM_BOLD = new Font(Font.HELVETICA, 11, Font.BOLD);
	private static final Font FONT_ITEM_NORMAL = new Font(Font.HELVETICA, 10, Font.NORMAL);
	private static final Font FONT_TOTAL = new Font(Font.HELVETICA, 12, Font.BOLD);

	public void gerarRelatorioFechamento(Long idCaixa) {
		// Task que irá gerar o pdf de forma assincrona
		Task<Void> taskGerarPdf = new Task<>() {
			@Override
			protected Void call() throws Exception {
				gerarPDFRelatorioFechamento(idCaixa);
				return null;
			}
		};

		rodarTask(taskGerarPdf);
	}

	public void gerarRelatorioMovimentos(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
		// Task que irá gerar o pdf de forma assincrona
		Task<Void> taskGerarPdf = new Task<>() {
			@Override
			protected Void call() throws Exception {
				gerarPDFRelatorioMovimentos(dataHoraInicio, dataHoraFim);;
				return null;
			}
		};

		rodarTask(taskGerarPdf);
	}
	
	public void gerarRelatorioExtrato(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
		// Task que irá gerar o pdf de forma assincrona
		Task<Void> taskGerarPdf = new Task<>() {
			@Override
			protected Void call() throws Exception {
				gerarPDFRelatorioExtrato(dataHoraInicio, dataHoraFim);;
				return null;
			}
		};

		rodarTask(taskGerarPdf);
	}
	
	public void gerarRelatorioFluxo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
		// Task que irá gerar o pdf de forma assincrona
		Task<Void> taskGerarPdf = new Task<>() {
			@Override
			protected Void call() throws Exception {
				gerarPDFRelatorioFluxo(dataHoraInicio, dataHoraFim);;
				return null;
			}
		};

		rodarTask(taskGerarPdf);
	}

	private void rodarTask(Task<Void> task) {
		Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
		loadingAlert.setTitle("Processando");
		loadingAlert.setHeaderText("Gerando relatório...");
		loadingAlert.setContentText("Por favor, aguarde um instante.");

		// O que acontece quando a Task termina com SUCESSO
		task.setOnSucceeded(event -> {
			loadingAlert.close(); // Fecha o popup de carregamento
		});

		// O que acontece se ocorrer algum ERRO
		task.setOnFailed(event -> {
			loadingAlert.close();

			Throwable erro = task.getException();
			erro.printStackTrace();

			// Mostra o erro para o operador de forma amigável na Thread do JavaFX
			Alert errorAlert = new Alert(Alert.AlertType.ERROR);
			errorAlert.setTitle("Erro");
			errorAlert.setHeaderText("Não foi possível gerar o relatório.");
			errorAlert.setContentText(erro.getMessage());
			errorAlert.showAndWait();
		});

		// Inicia a Task em uma nova Thread do sistema
		new Thread(task).start();

		loadingAlert.show();
	}

	private void gerarPDFRelatorioFechamento(Long idCaixa) {
		Caixa cx = new CaixaService().obterPorIdComMovimentosEOperador(idCaixa);

		if (cx == null) {
			throw new IllegalArgumentException("Caixa não encontrado.");
		}

		List<MovimentoCaixa> movs = cx.getMovimentos();

		double dinheiro = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DINHEIROFISICO);
		double debito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DEBITO);
		double credito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.CREDITO);;
		double pix = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIX);
		double pixMaquininha = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIXMAQUINA);
		double totalEntradas = arredondar(dinheiro + debito + credito + pix + pixMaquininha);

		double sangrias = somarPorTipo(movs, TipoMovimento.SANGRIA);
		double despesas = somarPorTipo(movs, TipoMovimento.DESPESA);
		double totalSaidas = arredondar(sangrias + despesas);


		double fundoCaixa = cx.getValorInicial();
		double reforcos = somarPorTipo(movs, TipoMovimento.REFORCO);
		double totalAbertura = fundoCaixa + reforcos;

		double fechamento = arredondar(totalAbertura + totalEntradas - totalSaidas);
		double saldoTotal = arredondar(fechamento - totalAbertura + sangrias);
		double saldoParcial = arredondar(fechamento - totalAbertura);

		try {
			String nomeArquivo = "fechamento_caixa_" + StringUtil.extrairEFormatarDataParaArquivo(cx.getDataHoraFechamento());

			File pdfTemp = File.createTempFile(nomeArquivo, ".pdf");
			pdfTemp.deleteOnExit();

			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, new FileOutputStream(pdfTemp));
			document.open();

			// Cabeçalho
			adicionarParagrafoCentralizado(document, "PARÓQUIA NOSSA SENHORA APARECIDA - MONGAGUÁ", FONT_TITULO, 6);
			adicionarDivisor(document);

			adicionarParagrafoCentralizado(document, "FECHAMENTO DE CAIXA", FONT_SUBTITULO, 4);
			adicionarParagrafoCentralizado(document, StringUtil.formatarDataExtenso(cx.getDataHoraFechamento()), FONT_DATA, 8);
			adicionarDivisor(document);

			// Abertura e Entradas
			adicionarLinhaValorPontilhada(document, "ABERTURA", totalAbertura, FONT_ITEM_BOLD, 10);
			adicionarLinhaValorPontilhada(document, "    Fundo de caixa", fundoCaixa, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Reforços", reforcos, FONT_ITEM_NORMAL, 3);

			adicionarLinhaValorPontilhada(document, "TOTAL DE ENTRADAS", totalEntradas, FONT_ITEM_BOLD, 4);
			adicionarLinhaValorPontilhada(document, "    Dinheiro", dinheiro, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Cartão de Débito", debito, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Cartão de Crédito", credito, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Pix", pix, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Pix Maquininha", pixMaquininha, FONT_ITEM_NORMAL, 10);

			// Saídas
			adicionarLinhaValorPontilhada(document, "TOTAL DE SAÍDAS", totalSaidas, FONT_ITEM_BOLD, 4);
			adicionarLinhaValorPontilhada(document, "    Sangrias", sangrias, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Despesas", despesas, FONT_ITEM_NORMAL, 8);

			adicionarDivisor(document);

			// Fechamento e Saldos
			adicionarLinhaValorPontilhada(document, "FECHAMENTO", fechamento, FONT_ITEM_BOLD, 5);
			adicionarLinhaValorPontilhada(document, "SALDO PARCIAL", saldoParcial, FONT_ITEM_BOLD, 5);
			adicionarLinhaValorPontilhada(document, "SALDO TOTAL", saldoTotal, FONT_TOTAL, 8);

			adicionarDivisor(document);

			// Responsável
			Paragraph pRespTitulo = new Paragraph("RESPONSÁVEL PELO FECHAMENTO:", FONT_ITEM_BOLD);
			pRespTitulo.setSpacingBefore(6);
			pRespTitulo.setSpacingAfter(2);
			document.add(pRespTitulo);

			document.add(new Paragraph(cx.getOperador().getNome(), FONT_DATA));

			document.close();

			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(pdfTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar PDF de fechamento: " + e.getMessage());
		}
	}

	private void gerarPDFRelatorioMovimentos(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {

		List<Caixa> cxs = new CaixaService().obterPorPeriodoComMovimentosEOperador(dataHoraInicio, dataHoraFim);

		if (cxs == null) {
			throw new IllegalArgumentException("Caixas não encontrados para o período informado.");
		}

		List<MovimentoCaixa> movs = cxs.stream()
				.flatMap(c -> c.getMovimentos().stream())
				.toList();


		double dinheiro = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DINHEIROFISICO);
		double debito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DEBITO);
		double credito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.CREDITO);;
		double pix = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIX);
		double pixMaquininha = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIXMAQUINA);
		double totalEntradas = arredondar(dinheiro + debito + credito + pix + pixMaquininha);

		double sangrias = somarPorTipo(movs, TipoMovimento.SANGRIA);
		double despesas = somarPorTipo(movs, TipoMovimento.DESPESA);
		double totalSaidas = arredondar(sangrias + despesas);

		try {
			String nomeArquivo = "movimentos_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraInicio)+ "_a_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraFim);
			File pdfTemp = File.createTempFile(nomeArquivo, ".pdf");
			pdfTemp.deleteOnExit();

			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, new FileOutputStream(pdfTemp));
			document.open();

			// Cabeçalho
			adicionarParagrafoCentralizado(document, "PARÓQUIA NOSSA SENHORA APARECIDA - MONGAGUÁ", FONT_TITULO, 6);
			adicionarDivisor(document);

			adicionarParagrafoCentralizado(document, "MOVIMENTOS DE CAIXA", FONT_SUBTITULO, 4);

			String periodo = StringUtil.formatarDataExtenso(dataHoraInicio) + " à " + StringUtil.formatarDataExtenso(dataHoraFim);
			adicionarParagrafoCentralizado(document, periodo, FONT_SUBTITULO, 4);
			adicionarDivisor(document);


			adicionarLinhaValorPontilhada(document, "TOTAL DE ENTRADAS", totalEntradas, FONT_ITEM_BOLD, 4);
			adicionarLinhaValorPontilhada(document, "    Dinheiro", dinheiro, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Cartão de Débito", debito, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Cartão de Crédito", credito, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Pix", pix, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Pix Maquininha", pixMaquininha, FONT_ITEM_NORMAL, 10);

			// Saídas
			adicionarLinhaValorPontilhada(document, "TOTAL DE SAÍDAS", totalSaidas, FONT_ITEM_BOLD, 4);
			adicionarLinhaValorPontilhada(document, "    Sangrias", sangrias, FONT_ITEM_NORMAL, 3);
			adicionarLinhaValorPontilhada(document, "    Despesas", despesas, FONT_ITEM_NORMAL, 8);

			adicionarDivisor(document);

			document.close();

			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(pdfTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar PDF de movimentos: " + e.getMessage());
		}
	}

	private void gerarPDFRelatorioExtrato(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
		List<Caixa> cxs = new CaixaService().obterPorPeriodoComMovimentosEOperador(dataHoraInicio, dataHoraFim);

		int caixasNoDia = 0;
		LocalDate ultimaData = dataHoraInicio.toLocalDate().minusDays(1);

		try {
			String nomeArquivo = "extrato_caixas_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraInicio)+ "_a_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraFim);
			File pdfTemp = File.createTempFile(nomeArquivo, ".pdf");
			pdfTemp.deleteOnExit();

			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, new FileOutputStream(pdfTemp));
			document.open();

			// Cabeçalho
			adicionarParagrafoCentralizado(document, "PARÓQUIA NOSSA SENHORA APARECIDA - MONGAGUÁ", FONT_TITULO, 6);
			adicionarDivisor(document);

			adicionarParagrafoCentralizado(document, "EXTRATO DE CAIXAS", FONT_SUBTITULO, 4);
			String periodo = StringUtil.formatarDataExtenso(dataHoraInicio) + " à " + StringUtil.formatarDataExtenso(dataHoraFim);
			adicionarParagrafoCentralizado(document, periodo, FONT_SUBTITULO, 4);
			adicionarDivisor(document);

			for (Caixa cx : cxs) {
				if (cx.getDataHoraAbertura().toLocalDate().isEqual(ultimaData)) {
					caixasNoDia += 1;
				} else {
					caixasNoDia = 0;
					ultimaData = cx.getDataHoraAbertura().toLocalDate();
				}
				
				String tituloCaixa = StringUtil.extrairEFormatarDataParaArquivo(cx.getDataHoraAbertura())
						+ " - Caixa " + caixasNoDia;
				Paragraph pCaixa = new Paragraph(tituloCaixa, FONT_ITEM_BOLD);
				pCaixa.setSpacingBefore(6);
				pCaixa.setSpacingAfter(2);
				document.add(pCaixa);
				
				List<MovimentoCaixa> movs = cx.getMovimentos();

				//    		double dinheiro = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DINHEIROFISICO);
				//    	    double debito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.DEBITO);
				//    	    double credito = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.CREDITO);;
				//    	    double pix = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIX);
				//    	    double pixMaquininha = somarPorTipoEForma(movs, TipoMovimento.ENTRADA, TipoPagamento.PIXMAQUINA);
				double totalEntradas = somarPorTipo(movs, TipoMovimento.ENTRADA);

				double sangrias = somarPorTipo(movs, TipoMovimento.SANGRIA);
				double despesas = somarPorTipo(movs, TipoMovimento.DESPESA);
				double totalSaidas = arredondar(sangrias + despesas);


				double fundoCaixa = cx.getValorInicial();
				double reforcos = somarPorTipo(movs, TipoMovimento.REFORCO);
				double totalAbertura = fundoCaixa + reforcos;

				double fechamento = arredondar(totalAbertura + totalEntradas - totalSaidas);
				double saldoTotal = arredondar(fechamento - totalAbertura + sangrias);
				double saldoParcial = arredondar(fechamento - totalAbertura);

				// Abertura e Entradas
				adicionarLinhaValorPontilhada(document, "ABERTURA", totalAbertura, FONT_ITEM_BOLD, 10);

				// Fechamento e Saldos
				adicionarLinhaValorPontilhada(document, "FECHAMENTO", fechamento, FONT_ITEM_BOLD, 5);
				adicionarLinhaValorPontilhada(document, "SALDO PARCIAL", saldoParcial, FONT_ITEM_BOLD, 5);
				adicionarLinhaValorPontilhada(document, "SALDO TOTAL", saldoTotal, FONT_TOTAL, 8);

				// Responsável
				Paragraph pRespTitulo = new Paragraph("RESPONSÁVEL PELO FECHAMENTO:", FONT_ITEM_BOLD);
				pRespTitulo.setSpacingBefore(6);
				pRespTitulo.setSpacingAfter(2);
				document.add(pRespTitulo);

				document.add(new Paragraph(cx.getOperador().getNome(), FONT_DATA));
				
				adicionarDivisor(document);
			}

			document.close();
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(pdfTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar PDF de extrato de caixas: " + e.getMessage());
		}
	}
	
	
	private record ResumoFluxoDia(LocalDate data, long carros, long motos) {
	    public long total() {
	        return carros + motos;
	    }
	}
	
	private void gerarPDFRelatorioFluxo(LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
		List<Estadia> ests = new EstadiaService().obterPorPeriodoComMovimentosEOperador(dataHoraInicio, dataHoraFim);
		
		List<ResumoFluxoDia> dias = calcularFluxoDiario(ests, dataHoraInicio.toLocalDate(), dataHoraFim.toLocalDate());
		
		try {
			String nomeArquivo = "fluxo_veiculos_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraInicio)+ "_a_"
					+ StringUtil.extrairEFormatarDataParaArquivo(dataHoraFim);
			File pdfTemp = File.createTempFile(nomeArquivo, ".pdf");
			pdfTemp.deleteOnExit();

			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, new FileOutputStream(pdfTemp));
			document.open();

			// Cabeçalho
			adicionarParagrafoCentralizado(document, "PARÓQUIA NOSSA SENHORA APARECIDA - MONGAGUÁ", FONT_TITULO, 6);
			adicionarDivisor(document);

			adicionarParagrafoCentralizado(document, "FLUXO DE VEÍCULOS", FONT_SUBTITULO, 4);
			String periodo = StringUtil.formatarDataExtenso(dataHoraInicio) + " à " + StringUtil.formatarDataExtenso(dataHoraFim);
			adicionarParagrafoCentralizado(document, periodo, FONT_SUBTITULO, 4);
			adicionarDivisor(document);
			
			for (ResumoFluxoDia dia : dias) {
				String tituloDia = StringUtil.formatarDataExtenso(dia.data.atStartOfDay());
				Paragraph pCaixa = new Paragraph(tituloDia, FONT_ITEM_BOLD);
				pCaixa.setSpacingBefore(6);
				pCaixa.setSpacingAfter(2);
				document.add(pCaixa);
				
				adicionarLinhaContagemPontilhada(document, "TOTAL", dia.total(), FONT_ITEM_BOLD, 5);
				adicionarLinhaContagemPontilhada(document, "CARROS", dia.carros, FONT_ITEM_NORMAL, 5);
				adicionarLinhaContagemPontilhada(document, "MOTOS", dia.motos, FONT_ITEM_NORMAL, 5);

				adicionarDivisor(document);
			}
			
			document.close();
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().open(pdfTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao gerar PDF de extrato de caixas: " + e.getMessage());
		}
	}

	private void adicionarParagrafoCentralizado(Document doc, String texto, Font fonte, float espacoDepois) {
		Paragraph p = new Paragraph(texto, fonte);
		p.setAlignment(Element.ALIGN_CENTER);
		p.setSpacingAfter(espacoDepois);
		doc.add(p);
	}

	private void adicionarDivisor(Document doc) {
		LineSeparator linha = new LineSeparator(1f, 100f, null, Element.ALIGN_CENTER, -2f);
		Paragraph p = new Paragraph();
		p.add(new Chunk(linha));
		p.setSpacingBefore(4);
		p.setSpacingAfter(8);
		doc.add(p);
	}
	
	private void adicionarLinhaContagemPontilhada(Document doc, String rotulo, long numero, Font fonte, float espacoDepois) {
		DottedLineSeparator pontilhado = new DottedLineSeparator();
		pontilhado.setGap(3f);

		Paragraph p = new Paragraph();
		p.setFont(fonte);
		p.add(new Chunk(rotulo + " ", fonte));
		p.add(new Chunk(pontilhado));
		p.add(new Chunk(" " + numero, fonte));
		p.setSpacingAfter(espacoDepois);

		doc.add(p);
	}

	private void adicionarLinhaValorPontilhada(Document doc, String rotulo, double valor, Font fonte, float espacoDepois) {
		String valorFormatado = StringUtil.formatarMoeda(valor);

		DottedLineSeparator pontilhado = new DottedLineSeparator();
		pontilhado.setGap(3f);

		Paragraph p = new Paragraph();
		p.setFont(fonte);
		p.add(new Chunk(rotulo + " ", fonte));
		p.add(new Chunk(pontilhado));
		p.add(new Chunk(" " + valorFormatado, fonte));
		p.setSpacingAfter(espacoDepois);

		doc.add(p);
	}

	
	public List<ResumoFluxoDia> calcularFluxoDiario(List<Estadia> estadias, LocalDate dataInicio, LocalDate dataFim) {
	    // 1. Agrupa apenas os dias que possuem estadias
	    Map<LocalDate, List<Estadia>> estadiasPorDia = estadias.stream()
	            .filter(e -> e.getDataHoraEntrada() != null)
	            .collect(Collectors.groupingBy(e -> e.getDataHoraEntrada().toLocalDate()));

	    // 2. Percorre TODOS os dias do período (inclusive dias sem movimento)
	    return dataInicio.datesUntil(dataFim.plusDays(1))
	            .map(dia -> {
	                List<Estadia> estadiasDoDia = estadiasPorDia.getOrDefault(dia, List.of());

	                long carros = estadiasDoDia.stream()
	                        .filter(e -> e.getTipoVeiculo() == TipoVeiculo.CARRO)
	                        .count();

	                long motos = estadiasDoDia.stream()
	                        .filter(e -> e.getTipoVeiculo() == TipoVeiculo.MOTO)
	                        .count();

	                return new ResumoFluxoDia(dia, carros, motos);
	            })
	            .toList();
	}

	private double somarPorTipo(List<MovimentoCaixa> movimentos, TipoMovimento tipo) {
		double soma = movimentos.stream()
				.filter(m -> m.getTipoMovimento() == tipo)
				.mapToDouble(MovimentoCaixa::getValor)
				.sum();
		return arredondar(soma);
	}

	private double somarPorTipoEForma(List<MovimentoCaixa> movimentos, TipoMovimento tipo, TipoPagamento forma) {
		double soma = movimentos.stream()
				.filter(m -> m.getTipoMovimento() == tipo && m.getFormaPagamento() == forma)
				.mapToDouble(MovimentoCaixa::getValor)
				.sum();
		return arredondar(soma);
	}

	private double arredondar(double valor) {
		return Math.round(valor * 100.0) / 100.0;
	}
}
