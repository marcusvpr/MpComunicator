package com.mpxds.mpComunicator.model.enums;

public enum MpMenuGlobalGrupo {
	//
	G01(1, "ADMINISTRADORES", "ADMINISTRADORES"),
	G04(2, "USUARIOS", "USUARIOS");
	
	private Integer id;
	private String nome;
	private String descricao;
	
	// ---
	
	MpMenuGlobalGrupo(Integer id, 
				String nome,
				String descricao) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
	}

	public Integer getId() { return id; }
	
	public String getNome() { return this.nome; }

	public String getDescricao() { return this.descricao; }
	
}