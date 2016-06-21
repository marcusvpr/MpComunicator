package com.mpxds.mpComunicator.model.enums;

public enum MpTipoContato {

	EMAIL("E-mail"),
	SMS("Mensagem SMS"),
	TELEGRAM("Telegram API"),
	PUSH("Push Notification Google"),
	ANDROID("Android MpComunicator");
	
	private String nome;
	
	// ---

	MpTipoContato(String nome) {
		this.nome = nome;
	}
	
	public String getNome() { return nome; }

}