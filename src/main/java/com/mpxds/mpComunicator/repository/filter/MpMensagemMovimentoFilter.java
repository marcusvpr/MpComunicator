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

	private Date dataProgramada;
	private Boolean indRespostaUsuario;
	
	private Boolean indDomingo;
	private Boolean indSegunda;
	private Boolean indTerca;
	private Boolean indQuarta;
	private Boolean indQuinta;
	private Boolean indSexta;
	private Boolean indSabado;
	private Boolean indSemanalmente;
	private Boolean indAtivo;
	
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
	
	public Date getDataProgramada() { return dataProgramada; }
	public void setDataProgramada(Date dataProgramada) { this.dataProgramada = dataProgramada; }
	
	public Boolean getIndRespostaUsuario() { return indRespostaUsuario; }
	public void setIndRespostaUsuario(Boolean indRespostaUsuario) { 
													this.indRespostaUsuario = indRespostaUsuario; }

	// ---

	public Boolean getIndDomingo() { return indDomingo; }
	public void setIndDomingo(Boolean indDomingo) { this.indDomingo = indDomingo; }

	public Boolean getIndSegunda() { return indSegunda; }
	public void setIndSegunda(Boolean indSegunda) { this.indSegunda = indSegunda; }

	public Boolean getIndTerca() { return indTerca; }
	public void setIndTerca(Boolean indTerca) { this.indTerca = indTerca; }

	public Boolean getIndQuarta() { return indQuarta; }
	public void setIndQuarta(Boolean indQuarta) { this.indQuarta = indQuarta; }

	public Boolean getIndQuinta() { return indQuinta; }
	public void setIndQuinta(Boolean indQuinta) { this.indQuinta = indQuinta; }

	public Boolean getIndSexta() { return indSexta; }
	public void setIndSexta(Boolean indSexta) { this.indSexta = indSexta; }

	public Boolean getIndSabado() { return indSabado; }
	public void setIndSabado(Boolean indSabado) { this.indSabado = indSabado; }

	public Boolean getIndSemanalmente() { return indSemanalmente; }
	public void setIndSemanalmente(Boolean indSemanalmente) {
														this.indSemanalmente = indSemanalmente;	}

	public Boolean getIndAtivo() { return indAtivo; }
	public void setIndAtivo(Boolean indAtivo) { this.indAtivo = indAtivo; }
	
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