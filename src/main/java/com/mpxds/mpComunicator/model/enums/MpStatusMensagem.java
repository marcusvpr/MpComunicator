package com.mpxds.mpComunicator.model.enums;

public enum MpStatusMensagem {

	NOVA("Nova"),
	ENVIADA("Enviada"),
	CANCELADA("Cancelada"),
	ADIADA("Adiada"),
	PENDENTE("Pendente");
	
	private String descricao;
	
	// ---

	MpStatusMensagem(String descricao) { 
		this.descricao = descricao;
	}
	
	public String getDescricao() { return descricao; }

}