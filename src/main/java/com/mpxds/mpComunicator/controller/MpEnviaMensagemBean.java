package com.mpxds.mpComunicator.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
		String msg = "";
		//
		if (null == this.mpContato) msg = msg + "\n(Informar Contato)";
		if (null == this.mensagem || this.mensagem.isEmpty()) msg = msg + "\n(Informar Mensagem)";
		if (null == this.mpTipoContato) msg = msg + "\n(Informar Tipo)";
		//
		if (!msg.isEmpty()) {
			MpFacesUtil.addInfoMessage(msg);
			return;
		}		
		//
		if (mpTipoContato.equals(MpTipoContato.EMAIL))
			this.enviarEMAIL();
		else
			if (mpTipoContato.equals(MpTipoContato.SMS))
				this.enviarSMS();
			else
				if (mpTipoContato.equals(MpTipoContato.PUSH))
					this.enviarPUSH();
				else
					if (mpTipoContato.equals(MpTipoContato.TELEGRAM))
						this.enviarTELEGRAM();
					else
						if (mpTipoContato.equals(MpTipoContato.ANDROID))
							this.enviarANDROID();
		//
		this.gravaMensagemMovimento();
		//
		MpFacesUtil.addInfoMessage("Mensagem ( " + mpTipoContato.getNome() +
																	" )... enviada com sucesso!");
	}
	
	public void enviarEMAIL() {
		//
		MailMessage message = mpMailer.novaMensagem();
		
		message.to(this.mpContato.getEmail())
				.subject("MPXDS MpComunicator")
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpComunicator.template")))
				.put("mpUsuario", this.mpSeguranca.getMpUsuarioLogado().getMpUsuario().getNome())
				.put("mensagem", this.mensagem)
				.put("locale", new Locale("pt", "BR"))
				.send();
	}
	
	public void enviarSMS() {
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
		MpMensagemMovimento mpMensagemMovimento = new MpMensagemMovimento();
		
		mpMensagemMovimento.setMpUsuario(mpSeguranca.getMpUsuarioLogado().getMpUsuario());
		mpMensagemMovimento.setDataMovimento(new Date());
		mpMensagemMovimento.setMpContato(this.mpContato);
		mpMensagemMovimento.setMensagem(this.mensagem);
		mpMensagemMovimento.setMpTipoContato(this.mpTipoContato);
		mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.NOVA);
		
		this.mpCadastroMensagemMovimentoService.salvar(mpMensagemMovimento);		
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