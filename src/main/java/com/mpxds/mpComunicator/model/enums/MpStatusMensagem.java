package com.mpxds.mpComunicator.model.enums;

public enum MpStatusMensagem {

	OPCAO_INVALIDA("Opção Invalida"),
	ERRO_ENVIO("Erro Envio"),
	NOVA("Nova"),
	ENVIADA("Enviada"),
	CANCELADA("Cancelada"),
	ADIADA("Adiada"),
	LIDA("Lida"),
	CONFIRMADA("Confirmada"),
	PENDENTE("Pendente");
	
	private String descricao;
	
	// ---

	MpStatusMensagem(String descricao) { 
		this.descricao = descricao;
	}
	
	public String getDescricao() { return descricao; }

}