package com.mpxds.mpComunicator.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.model.enums.MpContato;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.model.enums.MpTipoContato;
import com.mpxds.mpComunicator.security.MpSeguranca;
import com.mpxds.mpComunicator.service.MpCadastroMensagemMovimentoService;
import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;
import com.mpxds.mpComunicator.util.mail.MpMailer;
import com.mpxds.mpComunicator.util.sms.MpUtilSMS;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class MpEnviaMensagemBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private MpCadastroMensagemMovimentoService mpCadastroMensagemMovimentoService;
	
	@Inject
	private MpSeguranca mpSeguranca;
	
	@Inject
	private MpMailer mpMailer;
	
	private MpMensagemMovimento mpMensagemMovimento;	
	
	private List<MpContato> mpContatoList;
	private List<MpTipoContato> mpTipoContatoList;
	
	private Boolean indErroEnvio;
		
//	private ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
	
	// ------
	
	public void inicializar() {
		if (null == this.mpMensagemMovimento)
			this.mpMensagemMovimento = new MpMensagemMovimento() ;
		//
		this.mpContatoList = Arrays.asList(MpContato.values());
		this.mpTipoContatoList = Arrays.asList(MpTipoContato.values());
		//
	}
	
	public void enviar() {
		//
		String msg = "";
		//
		if (null == this.mpMensagemMovimento.getMpContato())
			msg = msg + "<br>(Informar Contato)";
		if (null == this.mpMensagemMovimento.getMensagem()
		||  this.mpMensagemMovimento.getMensagem().isEmpty())
			msg = msg + "<br>(Informar Mensagem)";
		if (null == this.mpMensagemMovimento.getMpTipoContato())
			msg = msg + "<br>(Informar Tipo)";
		//
		if (!msg.isEmpty()) {
			MpFacesUtil.addInfoMessage(msg);
			return;
		}		
		//
		this.gravaMensagemMovimento();
		//
		this.indErroEnvio = false;
		
		if (this.mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.EMAIL)) {
			if (null == this.mpMensagemMovimento.getDataProgramada())
				assert(true); // nop
			else
				this.enviarEMAIL();
		} else
			if (this.mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.SMS))
				this.enviarSMS();
			else
				if (this.mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.PUSH))
					this.enviarPUSH();
				else
					if (this.mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.TELEGRAM))
						this.enviarTELEGRAM();
					else
						if (this.mpMensagemMovimento.getMpTipoContato().equals(
																			MpTipoContato.ANDROID))
							this.enviarANDROID();
		//
		if (this.indErroEnvio == false)
			MpFacesUtil.addInfoMessage("Mensagem ( " + 
					this.mpMensagemMovimento.getMpTipoContato().getNome() +
					" )... enviada com sucesso ! (Id = " + this.mpMensagemMovimento.getId());
		//
		this.mpMensagemMovimento = new MpMensagemMovimento();
	}
	
	public void enviarEMAIL() {
		//		
		MailMessage message = mpMailer.novaMensagem();
		
		String url = "http://localhost:8080/MpComunicator/";
//		String url = "http://www.mpxds.com/MpComunicator/";
//		String url = ectx.getRequestScheme() + "://" + ectx.getRequestServerName()
//		  				+ ":" + ectx.getRequestServerPort()  + ectx.getRequestContextPath();		
		//
		try {
			if (this.mpMensagemMovimento.getIndRespostaUsuario()) {
				String urlUsuarioResposta = "<a href=" + "\"" + url +
					"/MpAlertaLogMensagem?idMM=" + this.mpMensagemMovimento.getId() +
					"\" target=\"_blank\">Clique aqui para confirmar!</a>";
				//
				message.to(this.mpMensagemMovimento.getMpContato().getEmail())
				.subject("MPXDS MpComunicator : " + mpMensagemMovimento.getId())
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
													"/emails/mpComunicatorResposta.template")))
				.put("mpMensagemMovimento", this.mpMensagemMovimento)
				.put("urlUsuarioResposta", urlUsuarioResposta)
				.put("locale", new Locale("pt", "BR"))
				.send();
			} else
				message.to(this.mpMensagemMovimento.getMpContato().getEmail())
				.subject("MPXDS MpComunicator : " + mpMensagemMovimento.getId())
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
													"/emails/mpComunicator.template")))
				.put("mpMensagemMovimento", this.mpMensagemMovimento)
				.put("locale", new Locale("pt", "BR"))
				.send();
			//
			this.mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ENVIADA);
			//
		} catch(Exception e) {
			MpFacesUtil.addInfoMessage(
				"Erro envio do E-mail... Verificar o Anti-Virus/Firewall (ID = " +
									+ this.mpMensagemMovimento.getId() + " / Exception = " + e);
			//
			this.mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ERRO_ENVIO);
			
			this.indErroEnvio = true;
		}
		//
		this.mpMensagemMovimento = this.mpCadastroMensagemMovimentoService.
																		salvar(mpMensagemMovimento);		
		//
//		System.out.println("MpEnviaMensagemBean.enviarEMAIL() (From = " + 
//									this.mpSeguranca.getMpUsuarioLogado().getMpUsuario().getNome());
	}
	
	public void enviarSMS() {
		//
		try {
			String celular = this.mpMensagemMovimento.getMpContato().getCelular();
			String mensagem = "MPXDS MpComunicator(" + this.mpMensagemMovimento.getId() + ") = " +
															this.mpMensagemMovimento.getMensagem();
			//
			if (this.mpMensagemMovimento.getIndRespostaUsuario())
				mensagem = mensagem + " ( Responda? : CONFIRMAR ADIAR CANCELAR )";
			//
			String codigoRetorno = MpUtilSMS.simple(celular, mensagem);
			//
			this.mpMensagemMovimento.setCodigoRetorno(codigoRetorno);
			
			this.mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ENVIADA);
			//
		} catch(Exception e) {
			MpFacesUtil.addInfoMessage(
					"Erro envio do SMS... (ID = " + this.mpMensagemMovimento.getId() + 
																		" / Exception = " + e);
			//
			this.mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ERRO_ENVIO);
				
			this.indErroEnvio = true;
		}
		//
		this.mpMensagemMovimento = this.mpCadastroMensagemMovimentoService.
																	salvar(mpMensagemMovimento);		
		//
	}
	
	public void enviarPUSH() {
		//
	}
	
	public void enviarTELEGRAM() {
		//
	}
	
	public void enviarANDROID() {
		//
	}
	
	public void gravaMensagemMovimento() {
		//
		this.mpMensagemMovimento.setMpUsuario(mpSeguranca.getMpUsuarioLogado().getMpUsuario());
		this.mpMensagemMovimento.setDataMovimento(new Date());
		
		if (null == mpMensagemMovimento.getDataProgramada())
			this.mpMensagemMovimento.setDataProgramada(new Date());
		//
		this.mpMensagemMovimento = this.mpCadastroMensagemMovimentoService.salvar(
																				mpMensagemMovimento);		
	}
	
	// ---
	
	public MpMensagemMovimento getMpMensagemMovimento() { return mpMensagemMovimento; }
	public void setMpMensagemMovimento(MpMensagemMovimento mpMensagemMovimento) { 
												this.mpMensagemMovimento = mpMensagemMovimento; }
			
	public List<MpContato> getMpContatoList() {	return mpContatoList; }
	
	public List<MpTipoContato> getMpTipoContatoList() {	return mpTipoContatoList; }
	
}