package com.mpxds.mpComunicator.security;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Utils;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.repository.MpUsuarios;
import com.mpxds.mpComunicator.service.MpCadastroUsuarioService;

import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;

@Named
@ViewScoped //  @RequestScoped
public class MpSeguranca implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ExternalContext externalContext;

	@Inject
	private MpUsuarios mpUsuarios;

	@Inject
	private MpCadastroUsuarioService mpCadastroUsuarioService;
		
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	private MpUsuario mpUsuario = new MpUsuario();
	private List<MpUsuario> mpUsuarioList = new ArrayList<MpUsuario>();
		
	private String mensagem;
	
	private String trocaSenhaAtual;
	private String trocaSenhaNova;
	private String trocaSenhaNovaConfirma;
	//
	private static final Logger logger = Logger.getLogger(MpSeguranca.class);
    
	// ----	
	
	public String getLoginUsuario() {
		String login = null;
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (mpUsuarioLogado != null)
			login = mpUsuarioLogado.getMpUsuario().getLogin();
		//
		return login;
	}

	public String getUltimoLoginUsuario() {
		//
//		System.out.println("MpSeguranca.getUltimoLoginUsuario() - Entrou!");
		//
    	@SuppressWarnings("unused")
		byte[] imageByte = null;
		try {
			imageByte = Utils.toByteArray(Faces.getResourceAsStream("/resources/images/blank.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		if (this.mpUsuarioList.size() == 0) 
			this.mpUsuarioList = this.mpUsuarios.mpUsuarioAtivos();

		//
		String ultimoLogin = "";
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (mpUsuarioLogado != null)
			ultimoLogin = sdf.format(mpUsuarioLogado.getMpUsuario().getDataUltimoLogin());
		//
		return ultimoLogin;
	}

	public String getUltimoLoginUsuarioAnt() {
		//
		String ultimoLoginAnt = "";
		//
		MpUsuarioSistema mpUsuarioLogado = this.getMpUsuarioLogado();
		//
		if (null == mpUsuarioLogado)
			ultimoLoginAnt = "";
		else
		if (null == mpUsuarioLogado.getMpUsuario().getDataUltimoLoginAnt())
			ultimoLoginAnt = "";
		else
			ultimoLoginAnt = sdf.format(mpUsuarioLogado.getMpUsuario().getDataUltimoLoginAnt());
		//
		return ultimoLoginAnt;
	}

	public String getNumeroIP() {
		String ipAddress = null;

		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().
																getExternalContext().getRequest();
		ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (null == ipAddress)
			ipAddress = request.getRemoteAddr();
		//
		return ipAddress;
	}
	
	@Produces
	@MpUsuarioLogado
	public MpUsuarioSistema getMpUsuarioLogado() {
		MpUsuarioSistema mpUsuarioSistema = null;
		
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) 
				FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
		
		if (auth != null && auth.getPrincipal() != null)
			mpUsuarioSistema = (MpUsuarioSistema) auth.getPrincipal();
		//
		return mpUsuarioSistema;
	}
		
	public Date getDataSistema() {
		//
		return new Date();
	}
		
	public void enviaSenhaTroca() {
		//
//		System.out.println("MpSeguranca.enviaMpNotificacao() - Entrou!");
		//
		String mensagem = "";
		if (this.trocaSenhaAtual.isEmpty()) mensagem = mensagem + "(Informar Senha Atual)";
		if (this.trocaSenhaNova.isEmpty()) mensagem = mensagem + "(Informar Senha Nova)";
		if (this.trocaSenhaNovaConfirma.isEmpty()) mensagem = mensagem + 
															"(Informar Senha Nova Confirma)";
		if (!mensagem.isEmpty()) {
			//		
			MpFacesUtil.addInfoMessage(mensagem);
			return;
		}
		//
		if (!this.trocaSenhaNova.equals(this.trocaSenhaNovaConfirma)) {
			MpFacesUtil.addInfoMessage("Senha Inválida!");			
			return;
		}
		//
		MpUsuarioSistema mpUsuarioSistema = this.getMpUsuarioLogado();

		this.mpUsuario = mpUsuarioSistema.getMpUsuario();
		
		if (this.trocaSenhaAtual.equals(this.mpUsuario.getSenha())) {
			//
			this.mpUsuario.setSenha(this.trocaSenhaNova);
			//
			// Trata armazanamento das últimas 5 (cinco) senhas !
			//
			String senhaLog = this.mpUsuario.getSenhaLog();
			if (null == senhaLog) senhaLog = "";
		
			if (senhaLog.split(" ").length > 4)
				senhaLog = this.mpUsuario.getSenha()  + " " + senhaLog.substring(
																		senhaLog.lastIndexOf(" "));
			else
				senhaLog = this.mpUsuario.getSenha() + " " + senhaLog;

			this.mpUsuario.setSenhaLog(senhaLog);
			//	
			this.mpUsuario = this.mpCadastroUsuarioService.salvar(mpUsuario);
			//
			MpFacesUtil.addInfoMessage("Troca de senha... efetuada!");
			// logs info
			logger.info("Usuário... Troca de senha! (Login = " + mpUsuario.getLogin());
		} else
			MpFacesUtil.addInfoMessage("Senha Inválida!");			
	}

	// ==============
	
	public boolean isAdministradores() {
		return externalContext.isUserInRole("ADMINISTRADORES");
	}

	// ========
	
	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) {	this.mpUsuario = mpUsuario; }
	public List<MpUsuario> getMpUsuarioList() {	return mpUsuarioList; }
	
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	public String getTrocaSenhaAtual() { return trocaSenhaAtual; }
	public void setTrocaSenhaAtual(String trocaSenhaAtual) { 
													this.trocaSenhaAtual = trocaSenhaAtual; }

	public String getTrocaSenhaNova() { return trocaSenhaNova; }
	public void setTrocaSenhaNova(String trocaSenhaNova) { 
													this.trocaSenhaNova = trocaSenhaNova; }
	public String getTrocaSenhaNovaConfirma() { return trocaSenhaNovaConfirma; }
	public void setTrocaSenhaNovaConfirma(String trocaSenhaNovaConfirma) { 
										this.trocaSenhaNovaConfirma = trocaSenhaNovaConfirma; }

}
