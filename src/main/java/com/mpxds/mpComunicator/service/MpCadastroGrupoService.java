package com.mpxds.mpComunicator.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.model.MpAuditoriaObjeto;
import com.mpxds.mpComunicator.model.MpGrupo;
import com.mpxds.mpComunicator.repository.MpGrupos;
import com.mpxds.mpComunicator.security.MpSeguranca;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpCadastroGrupoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MpGrupos mpGrupos;

	@Inject
	private MpSeguranca mpSeguranca;

	// -------------
	
	@MpTransactional
	public MpGrupo salvar(MpGrupo mpGrupo) throws MpNegocioException {
		MpGrupo mpGrupoExistente = mpGrupos.porNome(mpGrupo.getNome());
		
		if (mpGrupoExistente != null && !mpGrupoExistente.equals(mpGrupo)) {
			throw new MpNegocioException("Já existe um registro com o Nome... informado.");
		}

		// INICIO - Trata dados auditoria ! -----------------------------------
		MpAuditoriaObjeto mpAuditoriaObjeto;
		if (null == mpGrupo.getId()) { 
			mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrInc(new Date());
			mpAuditoriaObjeto.setUserInc(mpSeguranca.getLoginUsuario());
		} else {
			mpAuditoriaObjeto = mpGrupo.getMpAuditoriaObjeto();
			if (null == mpAuditoriaObjeto) mpAuditoriaObjeto = new MpAuditoriaObjeto();
			mpAuditoriaObjeto.setDtHrAlt(new Date());
			mpAuditoriaObjeto.setUserAlt(mpSeguranca.getLoginUsuario());				
		}
		mpGrupo.setMpAuditoriaObjeto(mpAuditoriaObjeto);
		// FIM - Trata dados auditoria ! -----------------------------------
		//
		return mpGrupos.guardar(mpGrupo);
	}

	public MpGrupo navegar(String acao, String parametro) {
		return mpGrupos.porNavegacao(acao, parametro);
	}

	public MpGrupo porNome(String nome) {
		return mpGrupos.porNome(nome);
	}
		
}
