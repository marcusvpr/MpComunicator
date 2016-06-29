package com.mpxds.mpComunicator.util.sms;

import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;

import br.com.facilitamovel.bean.MO;
import br.com.facilitamovel.bean.Retorno;
//import br.com.facilitamovel.bean.SmsMultiplo;
//import br.com.facilitamovel.bean.SmsMultiploMessages;
import br.com.facilitamovel.bean.SmsSimples;
import br.com.facilitamovel.service.CheckCredit;
import br.com.facilitamovel.service.ReceiveMessage;
import br.com.facilitamovel.service.SendMessage;

public class MpUtilSMS {
	//
	@Inject
	private static MpMensagemMovimentos mpMensagemMovimentos;
	
	private static String usuario = "?????";
	private static String senha = "?????";
	
	// ---
	
	public static String simple(String celular, String mensagem) throws Exception {
		//
		SmsSimples sms = new SmsSimples();
		//
		sms.setUser(usuario);
		sms.setPassword(senha);
		sms.setDestinatario(celular); // Ex.: 5199430558
		sms.setMessage(mensagem);
		// sms.setAno(2015);
		Retorno retorno = SendMessage.simpleSend(sms);
		//
		System.out.println("MpEnviaSMS.simple() - (Codigo = " + retorno.getCodigo() + 
													" Descricao = " + retorno.getMensagem());
		//
		checkCredits();
		//
		return retorno.getCodigo() + "/" + retorno.getMensagem();
	}

//	public static void simple(String usuario, String senha) throws Exception {
//		// Simple Send
//		SmsSimples sms = new SmsSimples();
//		sms.setUser(usuario);
//		sms.setPassword(senha);
//		sms.setDestinatario("5199430558");
//		sms.setMessage("ABC");
//		//sms.setAno(2015);
//		Retorno retorno = SendMessage.simpleSend(sms);
//		System.out.println("Codigo:" + retorno.getCodigo());
//		System.out.println("Descricao:" + retorno.getMensagem());
//	}

//	/**
//	 * Exemplo de uma mensagem para multiplos destinatarios
//	 * @throws Exception
//	 */
//	public static void multiple(String usuario, String senha) throws Exception {
//		// Multiple Send
//		SmsMultiplo sms = new SmsMultiplo();
//		sms.setUser(usuario);
//		sms.setPassword(senha);
//
//		// Multiplos destinatarios
//		List<String> nmbs = new ArrayList<String>();
//		nmbs.add("5199430558");
//		nmbs.add("5199430558");
//		nmbs.add("5198961100");
//		sms.setDestinatarios(nmbs);
//
//		// Chave Interna do Cliente, nao eh obrigatorio
//		List<String> chaveCliente = new ArrayList<String>();
//		chaveCliente.add("1");
//		chaveCliente.add("2");
//		chaveCliente.add("3");
//		sms.setChaveClientes(chaveCliente);
//
//		sms.setMessage("teste");
//		Retorno retorno = SendMessage.multipleSend(sms);
//
//		System.out.println("Codigo:" + retorno.getCodigo());
//		System.out.println("Descricao:" + retorno.getMensagem());
//	}
//
//	
//	/**
//	 * Exemplo de envio de Multiplas Mensagens para Multiplos Destinatarios
//	 * @throws Exception
//	 */
//	public static void multipleMsgs(String usuario, String senha) throws Exception{
//		// Multiple Send
//		SmsMultiploMessages sms = new SmsMultiploMessages();
//		sms.setUser(usuario);
//		sms.setPassword(senha);
//
//		// Multiplos destinatarios
//		List<String> listNmbs = new ArrayList<String>();
//		listNmbs.add("5199430558");
//		listNmbs.add("5199430558");
//		listNmbs.add("5199430558");
//		sms.setDestinatarios(listNmbs);
//
//		// Chave Interna do Cliente, nao eh obrigatorio
//		List<String> messagesList = new ArrayList<String>();
//		messagesList.add("Mensagem 1");
//		messagesList.add("Mensagem 2");
//		messagesList.add("Mensagem 3");
//		sms.setMessages(messagesList);
//
//		// Chave Interna do Cliente, nao eh obrigatorio
//		List<String> chaveCliente = new ArrayList<String>();
//		chaveCliente.add("1");
//		chaveCliente.add("2");
//		chaveCliente.add("3");
//		sms.setChaveClientes(chaveCliente);
//
//		Retorno retorno = SendMessage.multipleMessagesToMultPhones(sms);
//		System.out.println("Codigo:" + retorno.getCodigo());
//		System.out.println("Descricao:" + retorno.getMensagem());
//	}
//	
//	
	/**
     * Lista os MO nao lidos da plataforma, apos executar este metodo, o mesmo vai marcar como lido os MOs, portanto para
	 * continuar seus testes, va ate o painel de controle da Facilita, Mensagens Recebidas, e Marque todos como NAO LIDOS.
	 * https://www.facilitamovel.com.br/manuais/IntegracaoHTTPS.pdf
	 * @param usuario
	 * @param senha
	 * @return
	 * @throws Exception
	 */
	public static void listaMensagensRecebidas(){
		try {
			List<MO> caixaEntrada = ReceiveMessage.readUnreadMO(usuario, senha);
			if(caixaEntrada != null && caixaEntrada.size() > 0){
				for (MO mo : caixaEntrada) {
					//
					System.out.println("Telefone:" + mo.getTelefone());
					String dataHora = new SimpleDateFormat("dd/MM/yyyy kk:mm").
																		format(mo.getDataHora());
					System.out.println("Data/Hora:" + dataHora);
					System.out.println("Mensagem:" + mo.getMensagem());
					System.out.println("\n\n");
					
					// Tratar mensagens ...
					if (null == dataHora || dataHora.length() < 18) continue;
					//
					MpMensagemMovimento mpMensagemMovimento = mpMensagemMovimentos.porTelefoneData(
																mo.getTelefone(), mo.getDataHora());
					if (null == mpMensagemMovimento)
						System.out.println("MpUtiSMS.listaMensagensRecebidas() - " +
																	"mpMensagemMovimento = NULL"); 
					else {
						mpMensagemMovimento.setRespostaUsuario(mo.getMensagem());
						
						if (mo.getMensagem().toUpperCase().equals("CONFIRMAR"))
							mpMensagemMovimento.setMpStatusMensagemUsuario(MpStatusMensagem.
																						CONFIRMADA);
						else
						if (mo.getMensagem().toUpperCase().equals("ADIAR"))
							mpMensagemMovimento.setMpStatusMensagemUsuario(MpStatusMensagem.ADIADA);
						else
						if (mo.getMensagem().toUpperCase().equals("CANCELAR"))
							mpMensagemMovimento.setMpStatusMensagemUsuario(MpStatusMensagem.
																						CANCELADA);
						else
							mpMensagemMovimento.setMpStatusMensagemUsuario(MpStatusMensagem.
																					OPCAO_INVALIDA);
						//
						mpMensagemMovimentos.guardar(mpMensagemMovimento);
						//
						System.out.println("MpUtiSMS.listaMensagensRecebidas() - (Resposta = " +
																				mo.getMensagem());
					}					
				}
			} else {
				System.out.println("MpUtiSMS.listaMensagensRecebidas() - " + 
											" Nao existem mensagens em sua Caixa de Entrada");
			}
		} catch (Exception e) {
			//Possivelmente LOGIN INVALIDO
			System.out.println("MpUtiSMS.listaMensagensRecebidas() - (Exception = " + e.toString()); 
		}
		
	}
	
	/**
	 * Verifica exatamente quantos creditos possui em sua conta no momento que for feita a requisicao
	 * https://www.facilitamovel.com.br/manuais/IntegracaoHTTPS.pdf
	 */
	public static void checkCredits() throws Exception {
		try {
			System.out.println(CheckCredit.checkRealCredit(usuario, senha));
		} catch (Exception e) {
			//Possivelmente LOGIN INVALIDO
			e.printStackTrace();
		}
	}
		
}
