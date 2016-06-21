package com.mpxds.mpComunicator.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;
import com.mpxds.mpComunicator.util.telegram.MpTelegramUtil;

@Named
@RequestScoped
public class MpTelegramBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mensagem;

	// ---
	
	public void enviar() {
		//
//		MpTelegramUtil.MpTelegramUtilSend("teste mensagem mpxds!");
		
		MpTelegramUtil.MpTelegramExecute("teste mensagem mpxds!");
		
		MpFacesUtil.addErrorMessage("Telegram... mensagem enviada !");
	}

	@NotNull
	public String getMensagem() { return mensagem; }
	public void setmensagem(String mensagem) { this.mensagem = mensagem; }

}