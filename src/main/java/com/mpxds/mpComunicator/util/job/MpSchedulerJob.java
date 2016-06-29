package com.mpxds.mpComunicator.util.job;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//import javax.faces.context.ExternalContext;
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import org.apache.deltaspike.scheduler.api.Scheduled;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.model.enums.MpTipoContato;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;
import com.mpxds.mpComunicator.repository.MpUsuarios;
import com.mpxds.mpComunicator.repository.filter.MpMensagemMovimentoFilter;

import com.mpxds.mpComunicator.util.MpAppUtil;
import com.mpxds.mpComunicator.util.mail.MpMailer;
import com.mpxds.mpComunicator.util.sms.MpUtilSMS;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Scheduled(cronExpression = "0 0/5 * * * ?")
public class MpSchedulerJob implements Job {
	//
	private Log log = LogFactory.getLog(MpSchedulerJob.class);

	@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;
	
	@Inject
	private MpMailer mpMailer;
	
	@Inject
	private MpUsuarios mpUsuarios;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//	private SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/yyyy");

//	private ExternalContext ectx;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//
		this.log.info("MpSchedulerJob - Entrou 000");
		
		
//		ExternalContext externalContext = 
//                FacesContext.getCurrentInstance().getExternalContext();
//		HttpServletResponse response = 
//                (HttpServletResponse) externalContext.getResponse();
//		FacesContext aFacesContext = FacesContext.getCurrentInstance();
//		ServletContext contextX = 
//               (ServletContext) aFacesContext.getExternalContext().getContext();
//	    String realPath = contextX.getRealPath("/");     	
//	    String serverName = contextX.getServerInfo();
//		
//		System.out.println("MpSchedulerJobForm.execute() - realPath = " + realPath +
//									"/ServeInfo = " + serverName );
//		
//		if (null == FacesContext.getCurrentInstance())
//			System.out.println("MpSchedulerJobForm.execute()/FacesContext.getCurrentInstance()=NULL");
//		else
//			if (null == FacesContext.getCurrentInstance().getExternalContext())
//				System.out.println("MpSchedulerJobForm.execute()/FacesContext." +
//													"getCurrentInstance().getExternalContext()=NULL");
//			else {
//				this.ectx = FacesContext.getCurrentInstance().getExternalContext();

		// ===========================================
		// Trata SMS mensangens de RETORNO não lidas !
		// ===========================================		
		MpUtilSMS.listaMensagensRecebidas();

		// --- 
		Date dataHoraJob = new Date();
		
		this.trataMensagemMovimento(dataHoraJob);
		// ==========
		System.out.println("MpSchedulerJob.inicializar() = MpComunicator = " +
																		sdf.format(dataHoraJob));
//			}

	}
		
	private void trataMensagemMovimento(Date dataJob) {
		//
		MpMensagemMovimentoFilter mpMensagemMovimentoFilter = MpAppUtil.capturaMensagemMovimento(
																						dataJob);
		
		mpMensagemMovimentoFilter.setIndRespostaUsuario(true);
		//
		List<MpMensagemMovimento> mpMensagemMovimentoList = mpMensagemMovimentos.filtrados(
																		mpMensagemMovimentoFilter);
		//
		MpUsuario mpUsuario = new MpUsuario();
		String mpUsuarioAnt = null;
		//
		System.out.println("MpSchedulerJob.trataMensagemMovimento() - Entrou 0000 ( Inds = "  +
				mpMensagemMovimentoFilter.getIndDomingo() + "/" + mpMensagemMovimentoFilter.
																			getIndSegunda() + "/" +
				mpMensagemMovimentoFilter.getIndTerca() + "/" + mpMensagemMovimentoFilter.
																			getIndQuarta() + "/" +
				mpMensagemMovimentoFilter.getIndQuinta() + "/" + mpMensagemMovimentoFilter.
																				getIndSexta() + "/" +
				mpMensagemMovimentoFilter.getIndSabado() + "/ DataJob = " +
										sdf.format(dataJob) + " / MensagemMovimentoList.size = " + 
										mpMensagemMovimentoList.size());
		//
		Calendar calendarI = Calendar.getInstance();
		Calendar calendarF = Calendar.getInstance();
		//
        for (MpMensagemMovimento mpMensagemMovimento : mpMensagemMovimentoList) {
        	//
        	if (null == mpMensagemMovimento.getDataProgramada())
        		continue;
        	// Tratar intervalo(-5min / +5Min) para selecionar o MensagemMovimento x DataHora.Job ...
			calendarI.setTime(mpMensagemMovimento.getDataProgramada());
			calendarI.add(Calendar.MINUTE, -5);
			calendarF.setTime(mpMensagemMovimento.getDataProgramada());
			calendarF.add(Calendar.MINUTE, 5);
        	
			Date dataI = calendarI.getTime();
			Date dataF = calendarF.getTime();
			
//        	if (!this.mpMensagemMovimento.getIndSemanalmente()) {
        		if (mpMensagemMovimento.getDataProgramada().after(dataI)
        		&&  mpMensagemMovimento.getDataProgramada().before(dataF))
        			assert true; // Idem IGNORE ou NOP !
        		else
        			continue;
//        	}
        	//
    		if (null == mpUsuarioAnt
    		||  mpUsuarioAnt != mpMensagemMovimento.getMpAuditoriaObjeto().getUserInc()) {
    			mpUsuario = mpUsuarios.porLoginEmail(mpMensagemMovimento.getMpAuditoriaObjeto().
    																				getUserInc());
    			//
    			mpUsuarioAnt = mpMensagemMovimento.getMpAuditoriaObjeto().getUserInc();
    		}
    		//
			if (null == mpUsuario) {
				System.out.println("MpSchedulerJob.trataMensagemMovimento() ( Email NULL = " +
											mpMensagemMovimento.getMpAuditoriaObjeto().getUserInc());
				return;
			}
			//
			System.out.println("MpSchedulerJob.trataMensagemMovimento() - MensagemMovimento " + 
						sdf.format(mpMensagemMovimento.getDataProgramada()) + " / Status = " + 
						mpMensagemMovimento.getMpStatusMensagem());
			//
			if (mpMensagemMovimento.getMpStatusMensagem().equals(MpStatusMensagem.NOVA)) {
				//
				if (mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.EMAIL))
					this.enviaEmail(mpMensagemMovimento, mpUsuario);
				else
					if (mpMensagemMovimento.getMpTipoContato().equals(MpTipoContato.SMS))
						this.enviaSMS(mpMensagemMovimento, mpUsuario);
				//
			}
        }
	}

	private void enviaEmail(MpMensagemMovimento mpMensagemMovimento, MpUsuario mpUsuario) {
		//
		MailMessage message = mpMailer.novaMensagem();
		//	    		
		String url = "http://localhost:8080/MpComunicator/";
//		String url = "http://www.mpxds.com/MpComunicator/";

//		String url = ectx.getRequestScheme() + "://" + ectx.getRequestServerName()
//		  				+ ":" + ectx.getRequestServerPort()  + ectx.getRequestContextPath();		
		//
		try {
			if (null == mpMensagemMovimento.getIndRespostaUsuario())
				mpMensagemMovimento.setIndRespostaUsuario(false);
			//
			if (mpMensagemMovimento.getIndRespostaUsuario()) {	    	    		
				String urlUsuarioResposta = "<a href=" + "\"" + url +
					"/MpAlertaLogMensagem?idMM=" + mpMensagemMovimento.getId() +
					"\" target=\"_blank\">Clique aqui para confirmar!</a>";
				//
				message.to(mpUsuario.getEmail().trim())
					.subject("MPXDS MpComunicator : " + mpMensagemMovimento.getId())
					.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
											"/emails/mpMensagemMovimentoResposta.template")))
					.put("mpMensagemMovimento", mpMensagemMovimento)
					.put("urlUsuarioResposta", urlUsuarioResposta)
					.put("locale", new Locale("pt", "BR"))
					.send();
			} else
				message.to(mpUsuario.getEmail().trim())
				.subject("MPXDS MpComunicator : " + mpMensagemMovimento.getId())
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream(
													"/emails/mpMensagemMovimento.template")))
				.put("mpMensagemMovimento", mpMensagemMovimento)
				.put("locale", new Locale("pt", "BR"))
				.send();
			//
			mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ENVIADA);
			//
		} catch(Exception e) {
			//
			System.out.println("MpSchedulerJob.trataMensagemMovimento() - Erro envio " + 
							"E-mail... Verificar o Anti-Virus/Firewall (Id = "
							+ mpMensagemMovimento.getId() + " / Exception = " + e.toString());
			//
			mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ERRO_ENVIO);
		}
		//
		this.gravaMensagemMovimento(mpMensagemMovimento);
	}

	private void enviaSMS(MpMensagemMovimento mpMensagemMovimento, MpUsuario mpUsuario) {
		//
		try {
			String celular = mpMensagemMovimento.getMpContato().getCelular();
			String mensagem = mpMensagemMovimento.getMensagem();
			//
			if (null == mpMensagemMovimento.getIndRespostaUsuario())
				mpMensagemMovimento.setIndRespostaUsuario(false);

			if (mpMensagemMovimento.getIndRespostaUsuario())
				mensagem = mensagem + " ( Responda ? : CONFIRMAR ADIAR CANCELAR )";
			//
			String codigoRetorno = MpUtilSMS.simple(celular, mensagem);
			//
			mpMensagemMovimento.setCodigoRetorno(codigoRetorno);

			mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ENVIADA);
			//
		} catch(Exception e) {
			System.out.println("Erro envio do SMS... (ID = " + mpMensagemMovimento.getId() + 
																		" / Exception = " + e);
			//
			mpMensagemMovimento.setMpStatusMensagem(MpStatusMensagem.ERRO_ENVIO);
		}
		//
		this.gravaMensagemMovimento(mpMensagemMovimento);
	}

	private void gravaMensagemMovimento(MpMensagemMovimento mpMensagemMovimento) {
		//
		// this.mpMensagemMovimentos.guardar(mpMensagemMovimento);
		//
		try {
			Class.forName("org.hsqldb.jdbcDriver");

			Connection conn = DriverManager.getConnection(
								"jdbc:hsqldb:file:~/db/mpComunicator/mpComunicatorDB", "sa", "");
			//
			Statement stmt = conn.createStatement();

			if (null == mpMensagemMovimento.getCodigoRetorno())
				mpMensagemMovimento.setCodigoRetorno("");
			//
			String sql = "UPDATE mp_mensagem_movimento SET " + 
					" mpstatus_mensagem = '" + mpMensagemMovimento.getMpStatusMensagem() + "'," +
					" codigo_retorno = '" + mpMensagemMovimento.getCodigoRetorno() + "'" +
					" WHERE id = " + mpMensagemMovimento.getId();
			//
			stmt.execute(sql);
		    //
		    stmt.close();
		    conn.close();
			//
			System.out.println("MpSchedulerJob.gravaMensagemMovimento() - (Id = " + 
																		mpMensagemMovimento.getId());
		    //
		} catch (Exception e) {
			System.out.println("MpSchedulerJob.gravaMensagemMovimento() - Erro (Id = " + 
					+ mpMensagemMovimento.getId() + " / Exception = " + e.toString());
		}
	}

}