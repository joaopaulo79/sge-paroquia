package com.paroquiaTeam.sgeParoquia.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TempoUtils {
	public static long calcularDiferencaMinutos(LocalDateTime inicio, LocalDateTime fim) {
		long diferencaSegundos = Duration.between(inicio, fim).getSeconds();
		long minutos = diferencaSegundos / 60;
		long segundos = diferencaSegundos % 60;
		
		return minutos + (segundos > 0 ? 1 : 0);
	}
}