package com.mpxds.mpComunicator.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.mpxds.mpComunicator.model.enums.MpContato;
import com.mpxds.mpComunicator.model.enums.MpTipoContato;
import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpEnviaMensagemBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private MpContato mpContato;
	private List<MpContato> mpContatoList;
	
	private String mensagem;

	private MpTipoContato mpTipoContato;
	private List<MpTipoContato> mpTipoContatoList;
	
	// ------
	
	public MpEnviaMensagemBean() {
		if (null == this.mpContato)
			limpar();
		//
	}
	
	public void inicializar() {
		if (null == this.mpContato)
			limpar();
		//
		this.mpContatoList = Arrays.asList(MpContato.values());
		this.mpTipoContatoList = Arrays.asList(MpTipoContato.values());
		//
	}
	
	private void limpar() {
		//
		this.setMensagem("");		
	}
	
	public void enviar() {
		//			
		MpFacesUtil.addInfoMessage("Mensagem... enviada com sucesso!");
		//
	}
	
	// ---
	
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }
		
	public MpContato getMpContato() { return mpContato; }
	public void setMpContato(MpContato mpContato) {	this.mpContato = mpContato; }
	public List<MpContato> getMpContatoList() {	return mpContatoList; }
	
	public MpTipoContato getMpTipoContato() { return mpTipoContato; }
	public void setMpTipoContato(MpTipoContato mpTipoContato) {	this.mpTipoContato = mpTipoContato; }
	public List<MpTipoContato> getMpTipoContatoList() {	return mpTipoContatoList; }

}