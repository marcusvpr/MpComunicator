package com.mpxds.mpComunicator.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.mpxds.mpComunicator.model.enums.MpStatus;
import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.service.MpCadastroGrupoService;
import com.mpxds.mpComunicator.service.MpCadastroUsuarioService;
import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;
import com.mpxds.mpComunicator.util.mail.MpMailer;
import com.mpxds.mpComunicator.security.MpSeguranca;
import com.mpxds.mpComunicator.model.MpGrupo;
import com.mpxds.mpComunicator.model.enums.MpMenuGlobalGrupo;
import com.mpxds.mpComunicator.model.enums.MpMenuGlobalUsuario;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@SessionScoped
public class MpLoginBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private HttpServletResponse response;
		
	@Inject
	private MpCadastroUsuarioService mpCadastroUsuarioService;
	
	@Inject
	private MpCadastroGrupoService mpCadastroGrupoService;
	
	@Inject
	private MpSeguranca mpSeguranca;
		
	@Inject
	private MpMailer mpMailer;

	@Inject
//	@MpUsuarioEdicao
	private MpUsuario mpUsuario;
	
	private List<MpUsuario> mpUsuarioAtivoList = new ArrayList<MpUsuario>();

	// Trata parametros recebidos via URL ...
	// ======================================
	@ManagedProperty(value = "#{param.idI}")
	private String idI;
	@ManagedProperty(value = "#{param.idU}")
	private String idU;

	// ======================================
	
	private String loginAmbiente = "";
	
	private String loginEmail = "";
	private String loginEmailAnt = ".";
	
	private Integer contloginErro = 0;
	private Integer contloginErroRegistro = 0;
	
	private String mensagemSistema = "";

	private String transacaoSistema;

	private String emailSenha = "";

	private String usuarioRegistro = "";
	private String nomeRegistro = "";
	private String emailRegistro = "";
	private String senhaRegistro = "";
	private String senhaConfirmaRegistro = "";
	//
    private Boolean indEsqueciSenha = false;
    private Boolean indVisivelRegistro = true;
    
	private static final Logger logger = Logger.getLogger(MpLoginBean.class);
	
	//----------------
			
	public void preRender() {
		//
		// --- logs debug ---
		if (logger.isDebugEnabled()) logger.debug("MpLoginBean.preRender() ( Ip= " + 
																this.mpSeguranca.getNumeroIP());

		// Trata Confirmação Registro Usuário ...
		if (null == this.idI) this.idI = "null";
		if (null == this.idU) this.idU = "null";
		//
		if (this.idI.equals("null") || this.idU.equals("null"))
			this.contloginErroRegistro = 0;
		//
//		System.out.println("MpCadastroDolarBean.preRender - ( idI/idU = " +	idI + "/"+ idU );
		
		if (!this.idI.equals("null") && !this.idU.equals("null")) {
			//
			MpUsuario mpUsuario = this.mpCadastroUsuarioService.porLogin(this.idU);
			if (null == mpUsuario)
				mpUsuario = new MpUsuario();
			else {
				if (mpUsuario.getMpStatus().equals(MpStatus.REGISTRO)) {
					//
					if (mpUsuario.getId().equals(Long.parseLong(this.idI))) {
						//
						mpUsuario.setMpStatus(MpStatus.ATIVO);
						//
						mpUsuario = this.mpCadastroUsuarioService.salvar(mpUsuario);
						//
						MpFacesUtil.addErrorMessage("Registro... efetuado com sucesso !");
						//
						this.contloginErroRegistro = 0;
						//
						return;
					}
				} else {
					this.contloginErroRegistro++;
					if (this.contloginErroRegistro > 5) {
						mpUsuario.setMpStatus(MpStatus.BLOQUEADO);
						//
						mpUsuario = this.mpCadastroUsuarioService.salvar(mpUsuario);
						//
						MpFacesUtil.addErrorMessage(
										"Tentativas Registro... inválida! Contactar o Suporte!");
						// logs exception
						logger.error("Tentativas Registro... inválida! (Login=" + mpUsuario.getLogin(),
																new Exception("Violação"));
						//
						return;
					}
				}
			}
		}
		//
		if ("true".equals(request.getParameter("invalid"))) {			
			//
			if (this.loginEmail.isEmpty()) {
				MpFacesUtil.addErrorMessage("Informar Usuário (Login/E-mail)!");
				return;
			}
			//
			this.contloginErro++;
			if (this.contloginErro > 5) {
				//
				MpUsuario mpUsuario = mpCadastroUsuarioService.porLoginEmailAmbiente(this.loginEmail,
																			this.loginAmbiente);
				if (null == mpUsuario) {
					MpFacesUtil.addErrorMessage("Usuário inválido! (rc=0010)");
					//
					return;
				} else {
					mpUsuario.setMpStatus(MpStatus.BLOQUEADO);
					//
					mpUsuario = this.mpCadastroUsuarioService.salvar(mpUsuario);
				}
				//
				this.contloginErro = 0;
				//
				MpFacesUtil.addErrorMessage(
								"Excedido o número de tentativas no LOGIN... Usuário Bloqueado!");
				// logs exception
				logger.error("Usuário BLOQUEADO! (Login=" + mpUsuario.getLogin(),
																		new Exception("Violação"));
				//
				return;
			}
			//
			MpFacesUtil.addErrorMessage("Usuário ou senha inválido! (" + this.contloginErro);
		}
	}
		
	public void login() throws ServletException, IOException {
		//
		System.out.println("MpLoginBean.login() - 000");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/j_spring_security_check");

		if (null == dispatcher)
			MpFacesUtil.addErrorMessage("Usuário ou senha inválido! Null Dispatcher");
		else {
			//
			String usuario = request.getParameter("j_username");
			if (null == usuario || usuario.isEmpty()) {
				MpFacesUtil.addErrorMessage("Usuário inválido! Erro(001) " + usuario);
				//
				return;
			}
			//
			this.mpUsuario = mpCadastroUsuarioService.porLoginEmailAmbiente(usuario, loginAmbiente);
			if (null == this.mpUsuario) {
				//
				if (usuario.toLowerCase().equals("master")) { // Gera Usuário+Dados DEFAULTs ...
					//
					this.trataGeracaoDefault();
					// -------------------
					MpFacesUtil.addErrorMessage("Usuário inválido! Erro(002) - Default.Create!");
					//
					return;
				}
				//
				MpFacesUtil.addErrorMessage("Usuário inválido! Erro(002)");
				//
				return;
			} else {
				if (!this.mpUsuario.getMpStatus().toString().equals("ATIVO")) {
					MpFacesUtil.addErrorMessage("Usuário erro! - Status! ( " +
										this.mpUsuario.getMpStatus() + " ) - Contate o SUPORTE !");
					return;
				}
				//
				this.mpUsuario.setDataUltimoLoginAnt(this.mpUsuario.getDataUltimoLogin());
				
				this.mpUsuario = this.mpCadastroUsuarioService.salvar(this.mpUsuario);
				//
			}
			//
			dispatcher.forward(request, response);
		}
		//
		facesContext.responseComplete();
	}

	public void trataGeracaoDefault() {
		//
    	// Trata Grupos ...
        List<MpGrupo> mpGrupoList =	new ArrayList<MpGrupo>();

        List<MpMenuGlobalGrupo> mpMenuGlobalGrupoList = 
											Arrays.asList(MpMenuGlobalGrupo.values());
        //
//      System.out.println("MpLoginBean.login() - 000 ( " + mpMenuGlobalGrupoList.size());

        Iterator<MpMenuGlobalGrupo> itrG = mpMenuGlobalGrupoList.iterator(); 
        //
        while(itrG.hasNext()) {
        	//
        	MpMenuGlobalGrupo mpMenuGlobalGrupo = (MpMenuGlobalGrupo) itrG.next();

        	MpGrupo mpGrupo = mpCadastroGrupoService.porNome(mpMenuGlobalGrupo.getNome());
        	if (null == mpGrupo) {
        		//
    			mpGrupo = new MpGrupo();
    			
    			mpGrupo.setNome(mpMenuGlobalGrupo.getNome());
    			mpGrupo.setDescricao(mpMenuGlobalGrupo.getDescricao());
    			
    			mpGrupoList.add(mpGrupo);
    			
    			// mpGrupo = mpCadastroGrupoService.salvar(mpGrupo);
		        //
//		        System.out.println("MpLoginBean.login() - 001 ( " +	mpGrupo.getNome());
        	}
        }
		// Trata Usuários...
        List<MpMenuGlobalUsuario> mpMenuGlobalUsuarioList = 
        											Arrays.asList(MpMenuGlobalUsuario.values());
		//
//		System.out.println("MpLoginBean.login() - 002 ( " + mpMenuGlobalUsuarioList.size());
        
    	Iterator<MpMenuGlobalUsuario> itrU = mpMenuGlobalUsuarioList.iterator(); 
    	//
    	while(itrU.hasNext()) {
	    	//
    		MpMenuGlobalUsuario mpMenuGlobalUsuario = (MpMenuGlobalUsuario) itrU.next();
    		
    		MpUsuario mpUsuario = mpCadastroUsuarioService.porLogin(
    															mpMenuGlobalUsuario.getLogin());
    		if (null == mpUsuario) {
    			//
    			mpUsuario = new MpUsuario();
    			
    			mpUsuario.setLogin(mpMenuGlobalUsuario.getLogin());
    			mpUsuario.setNome(mpMenuGlobalUsuario.getNome());
    			mpUsuario.setEmail(mpMenuGlobalUsuario.getEmail());
    			mpUsuario.setSenha(mpMenuGlobalUsuario.getSenha());
    			mpUsuario.setImagem(mpMenuGlobalUsuario.getImagem());
    			mpUsuario.setMpStatus(MpStatus.valueOf(mpMenuGlobalUsuario.getMpStatus()));
    			
    			if (mpMenuGlobalUsuario.getLogin().toLowerCase().equals("master"))
    				mpUsuario.setMpGrupos(mpGrupoList);
    			//    			
    			mpUsuario = mpCadastroUsuarioService.salvar(mpUsuario);
    		}
    	}
	}
		
	public void enviaRegistro() {
		//
		// Trata Esqueci Senha ...
		// -----------------------
		if (this.indEsqueciSenha) {
			this.emailSenha = this.emailRegistro;
			
			this.enviaSenha();
			//
			return ;
		}
		//
		String mensagem = "";
		if (this.usuarioRegistro.isEmpty()) mensagem = mensagem + "(Informar Usuário!)";
		else if (this.usuarioRegistro.length() < 5)
			mensagem = mensagem + "(Usuário c/tam.Inválido! Min.5)";
		else if (this.usuarioRegistro.length() > 15)
			mensagem = mensagem + "(Usuário c/tam.Inválido! Max.15)";
		
		if (this.nomeRegistro.isEmpty()) mensagem = mensagem + "(Informar Nome!)";
		else if (this.nomeRegistro.length() < 5)
			mensagem = mensagem + "\n(Nome c/tam.Inválido! Min.5)";
		else if (this.nomeRegistro.length() > 100)
			mensagem = mensagem + "\n(Nome c/tam.Inválido! Max.100)";
		
		if (this.emailRegistro.isEmpty()) mensagem = mensagem + "\n(Informar Email!)";
		
		if (this.senhaRegistro.isEmpty()) mensagem = mensagem + "\n(Informar Senha!)";
		else if (this.senhaRegistro.length() < 5)
			mensagem = mensagem + "\n(Senha c/tam.Inválido! Min.5)";
		else if (this.senhaRegistro.length() > 20)
			mensagem = mensagem + "\n(Senha c/tam.Inválido! Max.20)";
		
		if (this.senhaConfirmaRegistro.isEmpty()) mensagem = mensagem + 
																"\n(Informar Senha Confirmação!)";
		
		if (!mensagem.isEmpty()) {
			MpFacesUtil.addInfoMessage(mensagem);
			return;
		}
		if (!this.senhaRegistro.equals(this.senhaConfirmaRegistro)) {
			MpFacesUtil.addInfoMessage("Senhas não conferem !");
			return;
		}
		//
		this.mpUsuario = this.mpCadastroUsuarioService.porLogin(usuarioRegistro);
		if (null == this.mpUsuario)
			this.mpUsuario = new MpUsuario();
		else {
			MpFacesUtil.addInfoMessage(
					"Usuário... já se encontra cadastrado na nossa base de dados!");
			return;
		}
		//
		this.mpUsuario = this.mpCadastroUsuarioService.porEmail(emailRegistro);
		if (null == mpUsuario)
			this.mpUsuario = new MpUsuario();
		else {
			MpFacesUtil.addInfoMessage(
					"E-mail... já se encontra cadastrado na nossa base de dados!");
			return;
		}

		// ------------------------------
		this.mpUsuario = new MpUsuario();

		this.mpUsuario.setLogin(this.usuarioRegistro);
		this.mpUsuario.setLoginGrupo("G." + this.usuarioRegistro.toUpperCase());
		this.mpUsuario.setNome(this.nomeRegistro);
		this.mpUsuario.setEmail(this.emailRegistro);
		this.mpUsuario.setSenha(this.senhaRegistro);
		this.mpUsuario.setMpStatus(MpStatus.REGISTRO);
		
//		this.mpUsuario.setMpGrupos(mpGrupoList);
		
		this.mpUsuario = this.mpCadastroUsuarioService.salvar(mpUsuario);		
		//
		MailMessage message = mpMailer.novaMensagem();
		
		try {
			//
			message.to(this.emailRegistro)
				.subject("MPXDS - Confirmação de REGISTRO")
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpUsuarioRegistro.template")))
				.put("mpUsuario", this.mpUsuario)
				.put("locale", new Locale("pt", "BR"))
				.send();
			//
		} catch (Exception e) {
			//
			MpFacesUtil.addInfoMessage("Erro envio do email ! (" + e.toString());
			
			return;
		}
		// 
		String msg = "\n======================";
		msg = msg + "\nUma mensagem foi enviada para o seu e-mail.";

		msg = msg + "\n\nEsta mensagem contém um link, para completar";
		msg = msg + "\no seu registro.";
		msg = msg + "\nSe a mensagem não constar em sua caixa de entrada,";
		msg = msg + "\nverifique as pastas de SPAM e a Lixeira de seu e-mail.";
		msg = msg + "\n\nSe você encontrar dificuldades contate o ";
		msg = msg + "\nadministrador pela nossa página de contato.";
		msg = msg + "\n==========================";
		//
		MpFacesUtil.addInfoMessage(msg);
		// logs info
		logger.info("Usuário REGISTRO! (Login=" + mpUsuario.getLogin());
	}

	public void enviaSenha() {
		System.out.println("MpLoginBean.enviaSenha() - ( Email = " + this.emailSenha);
		//
		if (this.emailSenha.isEmpty()) {
			MpFacesUtil.addInfoMessage("Informar Email... para recuperar Senha!");
			return;
		}
		//
		this.mpUsuario = mpCadastroUsuarioService.porEmail(this.emailSenha);
		if (null == this.mpUsuario) {
			MpFacesUtil.addErrorMessage(
							"E-mail informado... não consta na nossa base de Dados!");
			return;
		}
		//
//		System.out.println("MpLoginBean.enviaSenha() - ( Email = " + this.emailSenha +
//																" ( Usuario = " + this.mpUsuario);
		//
		MailMessage message = mpMailer.novaMensagem();
		
		message.to(this.emailSenha)
				.subject("MPXDS - Solicitação Recuperação Senha")
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
														"/emails/mpRecuperacaoSenha.template")))
				.put("mpUsuario", this.mpUsuario)
				.put("locale", new Locale("pt", "BR"))
				.send();
		//
		MpFacesUtil.addInfoMessage("Recuperação Senha... enviada por e-mail com sucesso!");
		// logs info
		logger.info("Usuário Recuperação Senha! (Login=" + mpUsuario.getLogin() + "/" +
																			mpUsuario.getEmail());
	}

	public void trataEsqueciSenha() {
		//
		if (this.indEsqueciSenha)
			this.indVisivelRegistro = false;
		else
			this.indVisivelRegistro = true;
	}
	
	// ---------------------------

	public String getIdI() { return idI; }
	public void setIdI(String idI) { this.idI = idI; }
	public String getIdU() { return idU; }
	public void setIdU(String idU) { this.idU = idU; }

	// ---
	
	public MpUsuario getMpUsuario() { return mpUsuario; }
	public void setMpUsuario(MpUsuario mpUsuario) { this.mpUsuario = mpUsuario; }

	public List<MpUsuario> getMpUsuarioAtivoList() { return mpUsuarioAtivoList; }	

	public String getEmailSenha() { return emailSenha; }
	public void setEmailSenha(String emailSenha) { this.emailSenha = emailSenha; }
	
	public String getLoginAmbiente() { return loginAmbiente; }
	public void setLoginAmbiente(String loginAmbiente) { this.loginAmbiente = loginAmbiente; }
	
	public String getLoginEmail() {	return loginEmail; }
	public void setLoginEmail(String loginEmail) { this.loginEmail = loginEmail; }
	public String getLoginEmailAnt() {	return loginEmailAnt; }
	public void setLoginEmailAnt(String loginEmailAnt) { this.loginEmailAnt = loginEmailAnt; }
	
	public String getMensagemSistema() { return mensagemSistema; }
	public void setMensagemSistema(String mensagemSistema) {
													this.mensagemSistema = mensagemSistema;	}

	public String getTransacaoSistema() { return transacaoSistema; }
	public void setTransacaoSistema(String transacaoSistema) {
													this.transacaoSistema = transacaoSistema; }
	
	public String getUsuarioRegistro() { return usuarioRegistro; }
	public void setUsuarioRegistro(String usuarioRegistro) {
													this.usuarioRegistro = usuarioRegistro; }

	public String getNomeRegistro() { return nomeRegistro; }
	public void setNomeRegistro(String nomeRegistro) { this.nomeRegistro = nomeRegistro; }

	public String getEmailRegistro() { return emailRegistro; }
	public void setEmailRegistro(String emailRegistro) { this.emailRegistro = emailRegistro; }

	public String getSenhaRegistro() { return senhaRegistro; }
	public void setSenhaRegistro(String senhaRegistro) { this.senhaRegistro = senhaRegistro; }

	public String getSenhaConfirmaRegistro() { return senhaConfirmaRegistro; }
	public void setSenhaConfirmaRegistro(String senhaConfirmaRegistro) { 
										this.senhaConfirmaRegistro = senhaConfirmaRegistro; }
	
    public Boolean getIndEsqueciSenha() { return indEsqueciSenha; }
    public void setIndEsqueciSenha(Boolean indEsqueciSenha) {
    													this.indEsqueciSenha = indEsqueciSenha; }
    
    public Boolean getIndVisivelRegistro() { return indVisivelRegistro; }
    public void setIndVisivelRegistro(Boolean indVisivelRegistro) {
    												this.indVisivelRegistro = indVisivelRegistro; }
    
}