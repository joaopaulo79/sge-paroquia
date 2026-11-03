package com.paroquiaTeam.sgeParoquia.utils;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class StringUtil {
	public String formatarMoeda(double valor) {
		Locale ptBr = Locale.of("pt", "BR");
	    NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(ptBr);

	    return formatoMoeda.format(valor);
	}
	
	public static String extrairEFormatarData(LocalDateTime dataHora) {
		DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return dataHora.format(formatoData);
	}
	
	public static String extrairEFormatarHora(LocalDateTime dataHora) {
		DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
		return dataHora.format(formatoHora);
	}
}
