package com.mpxds.mpComunicator.repository.filter;

import java.io.Serializable;

public class MpGrupoFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String nome;
	private String descricao;
	private String status;

	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;	
	
	// ---
	
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getDescricao() { return descricao; }
	public void setDescricao(String descricao) { this.descricao = descricao; }

	public String getStatus() {	return status; }
	public void setStatus(String status) { this.status = status; }

	// --------------
	
	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}
	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}
	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getPropriedadeOrdenacao() {
		return propriedadeOrdenacao;
	}
	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
		this.propriedadeOrdenacao = propriedadeOrdenacao;
	}

	public boolean isAscendente() {
		return ascendente;
	}
	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}

}