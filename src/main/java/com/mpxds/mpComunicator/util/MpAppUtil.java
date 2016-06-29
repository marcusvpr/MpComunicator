package com.mpxds.mpComunicator.util; 

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.mpxds.mpComunicator.repository.filter.MpMensagemMovimentoFilter;
import com.mpxds.mpComunicator.util.MpAppUtil;

public class MpAppUtil {

	// ----
	
	public static MpMensagemMovimentoFilter capturaMensagemMovimento(Date dataMensagemMovimento) {
		//
		String diaSemana = MpAppUtil.diaSemana(dataMensagemMovimento);
		
		MpMensagemMovimentoFilter mpMensagemMovimentoFilter = new MpMensagemMovimentoFilter();

		switch(diaSemana.toUpperCase()) {
			case "DOMINGO" : mpMensagemMovimentoFilter.setIndDomingo(true); break;
			case "SEGUNDA-FEIRA" : mpMensagemMovimentoFilter.setIndSegunda(true); break;
			case "TERÇA-FEIRA" : mpMensagemMovimentoFilter.setIndTerca(true); break;
			case "QUARTA-FEIRA" : mpMensagemMovimentoFilter.setIndQuarta(true); break;
			case "QUINTA-FEIRA" : mpMensagemMovimentoFilter.setIndQuinta(true); break;
			case "SEXTA-FEIRA" : mpMensagemMovimentoFilter.setIndSexta(true); break;
			case "SÁBADO" : mpMensagemMovimentoFilter.setIndSabado(true); break;
		}
		//
		return mpMensagemMovimentoFilter;
	}
	
	public static String diaSemana(Date dataX) {
		//
		Locale objLocale = new Locale("pt", "BR");
		//
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataX);
		//
	  	String diaSemana = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, objLocale);
		//
		return diaSemana;
	}
        
}