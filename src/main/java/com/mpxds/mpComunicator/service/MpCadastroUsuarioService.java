package com.mpxds.mpComunicator.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.model.MpAuditoriaObjeto;
import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.repository.MpUsuarios;
import com.mpxds.mpComunicator.security.MpSeguranca;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpCadastroUsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MpUsuarios mpUsuarios;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpUsuario salvar(MpUsuario mpUsuario) throws MpNegocioException {
		//
		MpUsuario mpUsuarioExistente = mpUsuarios.porLogin(mpUsuario.getLogin());	
		if (mpUsuarioExistente != null && !mpUsuarioExistente.equals(mpUsuario))
			throw new MpNegocioException("Já existe uma usuario com o LOGIN informado.");
		mpUsuarioExistente = mpUsuarios.porEmail(mpUsuario.getEmail());	
		if (mpUsuarioExistente != null && !mpUsuarioExistente.equals(mpUsuario))
			throw new MpNegocioException("Já existe uma usuario com o EMAIL informado.");
		mpUsuarioExistente = mpUsuarios.porNome(mpUsuario.getNome());	
		if (mpUsuarioExistente != null && !mpUsuarioExistente.equals(mpUsuario))
			throw new MpNegocioException("Já existe uma usuario com o NOME informado.");
		//
		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto;
		if (null == mpUsuario.getId()) { 
			mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpUsuario.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpUsuario.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpUsuarios.guardar(mpUsuario);
	}

	public MpUsuario navegar(String acao, String nome) {
		return mpUsuarios.porNavegacao(acao, nome);	
	}

	public MpUsuario porLogin(String login) {
		return mpUsuarios.porLogin(login);
	}

	public MpUsuario porLoginEmail(String loginEmail) {
		return mpUsuarios.porLoginEmail(loginEmail);
	}
	public MpUsuario porLoginEmailAmbiente(String loginEmail, String ambiente) {
		return mpUsuarios.porLoginEmailAmbiente(loginEmail, ambiente);
	}

	public MpUsuario porEmail(String Email) {
		return mpUsuarios.porEmail(Email);
	}

	public List<MpUsuario> capturaUsuarioAtivos() {
		return mpUsuarios.mpUsuarioAtivos();
	}
	
}
