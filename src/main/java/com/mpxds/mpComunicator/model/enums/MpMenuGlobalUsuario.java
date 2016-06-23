package com.mpxds.mpComunicator.model.enums;

public enum MpMenuGlobalUsuario {
    U01(1, "marcus", "Marcus Rodrigues", "marcus_vpr@hotmail.com", "teste", "ATIVO",
    																	"MASCULINO", "marcus.jpg"),
    U02(2, "master", "Administrador MASTER", "renato_rjx@hotmail.com", "teste", "ATIVO", 
    																	"MASCULINO", ""),
    U03(3, "teste", "Usuário TESTE", "teste@hotmail.com", "teste", "ATIVO",
    																	"MASCULINO", "");
	
	private Integer id;
	private String login;
	private String nome;
	private String email;
	private String senha;
	private String mpStatus;
	private String mpSexo;
	private String imagem;
	
	// ---
	
	MpMenuGlobalUsuario(Integer id, 
				String login,
				String nome,
				String email,
				String senha,
				String mpStatus,
				String mpSexo,
				String imagem) {
		this.id = id;
		this.login = login;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.mpStatus = mpStatus;
		this.mpSexo = mpSexo;
		this.imagem = imagem;
	}

	public Integer getId() { return id; }	
	public String getLogin() { return this.login; }
	public String getNome() { return this.nome; }
	public String getEmail() { return this.email; }
	public String getSenha() { return this.senha; }
	public String getMpStatus() { return this.mpStatus; }
	public String getMpSexo() { return this.mpSexo; }
	public String getImagem() { return this.imagem; }
	
}