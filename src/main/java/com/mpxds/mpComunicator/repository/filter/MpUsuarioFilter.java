package com.mpxds.mpComunicator.repository.filter;

import java.io.Serializable;

public class MpUsuarioFilter implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private String login;
	private String nome;
	private String email;
	private String status;
	private String tenant;

	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;	
	
	// --------
	
	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }
	
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }

	public String getTenant() { return tenant; }
	public void setTenant(String tenant) { this.tenant = tenant; }

	// -------
	
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