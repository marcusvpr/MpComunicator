package com.mpxds.mpComunicator.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpComunicator.model.enums.MpContato;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.model.enums.MpTipoContato;

@Entity
@Table(name = "mp_mensagem_movimento")
public class MpMensagemMovimento extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;

	private MpUsuario mpUsuario;
	private Date dataMovimento;
	private MpContato mpContato;
	private String mensagem;
	private MpTipoContato mpTipoContato;
	private MpStatusMensagem mpStatusMensagem;

	// ---

	@OneToOne(fetch = FetchType.EAGER) 
	@JoinColumn(name = "mpUsuarioId")
	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }

	@NotNull(message = "Por favor, informe a DATA")
	@Temporal(TemporalType.TIMESTAMP)
  	@Column(name = "data_movimento", nullable = false)
	public Date getDataMovimento() { return dataMovimento; }
	public void setDataMovimento(Date dataMovimento) { this.dataMovimento = dataMovimento; }

	@NotBlank(message = "Por favor, informe a MENSAGEM")
	@Column(nullable = false)
	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }

	@NotNull(message = "Por favor, informe o CONTATO")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	public MpContato getMpContato() { return mpContato; }
	public void setMpContato(MpContato mpContato) {	this.mpContato = mpContato; }
	
	@NotNull(message = "Por favor, informe o TIPO")
	@Enumerated(EnumType.STRING)
	@Column(name = "mpTipo_contato", nullable = false)
	public MpTipoContato getMpTipoContato() { return mpTipoContato; }
	public void setMpTipoContato(MpTipoContato mpTipoContato) {	
															this.mpTipoContato = mpTipoContato; }
	
	@NotNull(message = "Por favor, informe o STATUS")
	@Enumerated(EnumType.STRING)
	@Column(name = "mpStatus_mensagem", nullable = false)
	public MpStatusMensagem getMpStatusMensagem() { return mpStatusMensagem; }
	public void setMpStatusMensagem(MpStatusMensagem mpStatusMensagem) {	
														this.mpStatusMensagem = mpStatusMensagem; }
}