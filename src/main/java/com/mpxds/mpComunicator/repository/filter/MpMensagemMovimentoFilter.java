package com.mpxds.mpComunicator.repository.filter;

import java.io.Serializable;
import java.util.Date;

public class MpMensagemMovimentoFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private Date dataCriacaoDe;
	private Date dataCriacaoAte;
	private String usuario;
	private String contato;
	private String mensagem;
	private String tipoContato;
	private String status;

	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;	
	
	// ---
	
	public Date getDataCriacaoDe() { return dataCriacaoDe; }
	public void setDataCriacaoDe(Date dataCriacaoDe) { this.dataCriacaoDe = dataCriacaoDe; }
	
	public Date getDataCriacaoAte() { return dataCriacaoAte; }
	public void setDataCriacaoAte(Date dataCriacaoAte) { this.dataCriacaoAte = dataCriacaoAte; }
	
	public String getUsuario() { return usuario; }
	public void setUsuario(String usuario) { this.usuario = usuario; }
	
	public String getContato() { return contato; }
	public void setContato(String contato) { this.contato = contato; }

	public String getMensagem() { return mensagem; }
	public void setMensagem(String mensagem) { this.mensagem = mensagem; }
	
	public String getTipoContato() { return tipoContato; }
	public void setTipoContato(String tipoContato) { this.tipoContato = tipoContato; }

	public String getStatus() {	return status; }
	public void setStatus(String status) { this.status = status; }

	// --------------
	
	public int getPrimeiroRegistro() { return primeiroRegistro; }
	public void setPrimeiroRegistro(int primeiroRegistro) { 
														this.primeiroRegistro = primeiroRegistro; }

	public int getQuantidadeRegistros() { return quantidadeRegistros; }
	public void setQuantidadeRegistros(int quantidadeRegistros) {
												this.quantidadeRegistros = quantidadeRegistros;	}

	public String getPropriedadeOrdenacao() { return propriedadeOrdenacao; }
	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
												this.propriedadeOrdenacao = propriedadeOrdenacao; }

	public boolean isAscendente() { return ascendente; }
	public void setAscendente(boolean ascendente) { this.ascendente = ascendente; }

}