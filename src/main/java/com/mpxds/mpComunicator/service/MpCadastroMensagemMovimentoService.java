package com.mpxds.mpComunicator.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.model.MpAuditoriaObjeto;
import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;
import com.mpxds.mpComunicator.security.MpSeguranca;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpCadastroMensagemMovimentoService implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpMensagemMovimento salvar(MpMensagemMovimento mpMensagemMovimento)
																		throws MpNegocioException {
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto;
		if (null == mpMensagemMovimento.getId()) { 
			mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpMensagemMovimento.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpMensagemMovimento.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpMensagemMovimentos.guardar(mpMensagemMovimento);
	}

	public MpMensagemMovimento navegar(String acao, String nome) {
		return mpMensagemMovimentos.porNavegacao(acao, nome);	
	}
	
}
