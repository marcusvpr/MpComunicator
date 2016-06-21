package com.mpxds.mpComunicator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;

import com.mpxds.mpComunicator.model.enums.MpStatus;

@Entity
@Table(name = "mp_usuario")
public class MpUsuario extends MpBaseEntity {
	//
	private static final long serialVersionUID = 1L;
	
	private String login;
	private String loginGrupo;
	private String nome;
	private String email;
	private String senha;
	private String senhaLog = "";
	private Date dataNascimento;
	private String observacao;
	
	private String numIpUltimoLogin;
	private Date dataUltimoLogin;
	private Date dataUltimoLoginAnt;  
	
	private MpStatus mpStatus;
	
	private List<MpGrupo> mpGrupos = new ArrayList<MpGrupo>();
		
	// ----------
	
	@NotBlank(message = "Por favor, informe o LOGIN")
	@Column(nullable = false, length = 15, unique = true)
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	
	@Column(nullable = true, length = 50)
	public String getLoginGrupo() { return loginGrupo; }
	public void setLoginGrupo(String loginGrupo) { this.loginGrupo = loginGrupo; }
	
	@NotBlank(message = "Por favor, informe o NOME")
	@Column(nullable = false, length = 100)
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }
	
	@NotBlank(message = "Por favor, informe o E-MAIL")
	@Column(nullable = false, unique = true, length = 255)
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	@NotBlank(message = "Por favor, informe a SENHA")
	@Column(nullable = false, length = 20)
	public String getSenha() { return senha; }
	public void setSenha(String senha) { this.senha = senha; }

	// Guarda últimas 5 (cinco) senhas !
	@Column(nullable = true, length = 200, name = "senha_log")
	public String getSenhaLog() { return senhaLog; }
	public void setSenhaLog(String senhaLog) { this.senhaLog = senhaLog; }

	@Past(message="Data futuro inválida!")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento", nullable = true)
	public Date getDataNascimento() { return dataNascimento; }
	public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

	@Column(nullable = true, length = 255)
	public String getObservacao() { return observacao; }
	public void setObservacao(String observacao) { this.observacao = observacao; }
	  	  
	@Column(nullable = true, name = "num_ip_ultimo_login")
	public String getNumIpUltimoLogin() { return this.numIpUltimoLogin; }
	public void setNumIpUltimoLogin(String newNumIpUltimoLogin) {
													this.numIpUltimoLogin = newNumIpUltimoLogin; }
	  
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, name = "data_ultimo_login")
	public Date getDataUltimoLogin() { return this.dataUltimoLogin; }
	public void setDataUltimoLogin(Date newDataUltimoLogin) {
													this.dataUltimoLogin = newDataUltimoLogin; }
	  
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = true, name = "data_ultimo_login_ant")
	public Date getDataUltimoLoginAnt() { return this.dataUltimoLoginAnt; }
	public void setDataUltimoLoginAnt(Date newDataUltimoLoginAnt) {
												this.dataUltimoLoginAnt = newDataUltimoLoginAnt; }

	@NotNull(message = "Por favor, informe o STATUS")
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 15)
	public MpStatus getMpStatus() { return mpStatus; }
	public void setMpStatus(MpStatus mpStatus) { this.mpStatus = mpStatus; }
	
	@ManyToMany(cascade = CascadeType.ALL) // , fetch = FetchType.EAGER)
	@JoinTable(name = "mp_usuario_grupo", joinColumns = @JoinColumn(name="mpUsuario_id"),
									inverseJoinColumns = @JoinColumn(name = "mpGrupo_id"))
	public List<MpGrupo> getMpGrupos() { return mpGrupos; }
	public void setMpGrupos(List<MpGrupo> mpGrupos) { this.mpGrupos = mpGrupos; }
	
}