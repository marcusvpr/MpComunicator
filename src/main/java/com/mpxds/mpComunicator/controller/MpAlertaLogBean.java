package com.mpxds.mpComunicator.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;

@Named
@SessionScoped
public class MpAlertaLogBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;
	
	@Inject
	private MpUsuario mpUsuario;

	// Id MensagemMOvimento ...
	@ManagedProperty(value = "#{param.idMM}")
	private String idMM;

	// ======================================
	
	private static final Logger logger = Logger.getLogger(MpAlertaLogBean.class);
	
	//----------------
		
	public void preRender() {
		//		
		// --- logs debug
		if (logger.isDebugEnabled()) logger.debug("MpAlertaLogBean.preRender()");
		
		this.trataAlertaLog();
		//
	}

	public void trataAlertaLog() {
		//	
		System.out.println("MpAlertaLogBean.trataAlertaLog() - (" + this.idMM);
		
		MpMensagemMovimento mpMensagemMovimento = mpMensagemMovimentos.porId(Long.parseLong(this.idMM));
		if (null == mpMensagemMovimento) 
			assert(true); // Ignora !
		else {
			mpMensagemMovimento.setMpStatusMensagemUsuario(MpStatusMensagem.CONFIRMADA);
			
			mpMensagemMovimentos.guardar(mpMensagemMovimento);		
		}
	}
		
	// ---------------------------

	public String getIdMM() { return idMM; }
	public void setIdMM(String newIdMM) { this.idMM = newIdMM; }
	
	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }
}
