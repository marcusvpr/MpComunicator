package com.mpxds.mpComunicator.model.enums;

public enum MpContato {

	MARCUS(1, "Marcus Rodrigues", "21974355450", "marcus_vpr@hotmail.com",
									"marcus_vpr@hotmail.com", "", "Rio de Janeiro", "marcus.jpg"),
	ESTEVAO(2, "Estevão Ramos", "031 9 9572-4394", "stevaoramoss@hotmail.com",
									"estevao ramos", "", "?", "estevao.jpg"),
	GABRIEL(3, "Gabriel Gonçalves", "031 9 9444-8724", "gabriel.ggfreitas@gmail.com",
									"gabrielgoncalvesfreitas@hotmail.com", "", "?", "blank.jpg"),
	DANIEL(4, "Daniel Cardoso de Faria", "061 X 9501-7999", "kelvinsilva1010@gmail.com",
									"kelvinsk810@hotmail.com", "", "?", "blank.jpg"),
	KELVIN(5, "Kelvin Silva", "062 9 8400-6647", "danielcfariia@outlook.com",
									"danielcfariia", "", "?", "blank.jpg"),
	RICK(6, "Rick Deu Hangalo", "+244 9 1637-2177", "hangaloandre@gmail.com",
									"andre.hangalo1", "", "??? - Angola", "rick.jpg"),
	MAIKO(7, "Maiko Cunha", "044 X 9948-3814", "mkacunha@gmail.com",
									"maikocunha", "", "Araruna", "blank.jpg"),
	SIDNEI(8, "Sidnei Ferreira", "014 9 9713-2075", "tecnico.sidnei@hotmail.com)",
									"sidnei.ferreira37", "", "?", "blank.jpg"),
	PEDRO(9, "Pedro Henrique Andalecio Gontijo", "075 9 9242-6357", "pedroandalecio@hotmail.com",
									"pedroandalecio@hotmail.com", "", "?", "pedro.jpg"),
	MIMOSO(10, "José Luiz Mimoso", "075 9 9242-6357", "jlamimoso@yahoo.com.br",
									"jlamimoso@yahoo.com.br", "", "Rio de Janeiro", "mimoso.jpg"),
	PRISCO(11, "Prisco Brito", "021 9 8242-6173", " prisco.brito@gmail.com",
									" prisco.brito@gmail.com", "", "Rio de Janeiro", "prisco.jpg"),
	RUBENS(12, "Rubens Sales", "021 9 8731-8328", "rubenssales@gmail.com",
									"rubenssales@gmail.com", "", "Rio de Janeiro", "rubens.jpg"),
	VALDEMAR(13, "Valdemar Carlos Adao", "+244 9 3062-0153", "valdemarc.adao@hotmail.com",
									"valdemaradao", "", "Angola", "blank.jpg"),
	DOUGLAS(14, "Douglas Dallagnol", "054 X 9949-4019", "douglas_dallagnol@hotmail.com",
									"douglas_dallagnol", "", "?", "blank.jpg"),
	JOAO(15, "João Vitor Feitosa", "079 9 9959-7351", "???@mail.com",
									"???", "", "?", "joao.jpg"),
	ALEX(16, "Alex Sandro", "019 9 8128-0600", "a.sandro.delgado@gmail.com",
									"lequinhopucc", "", "?", "blank.jpg");
	
	private Integer id;
	private String nome;
	private String celular;
	private String email;
	private String skype;
	private String telegram;
	private String localizacao;
	private String imagem;
	
	// ---

	MpContato(Integer id,
			  	String nome,
			  	String celular,
			  	String email,
			  	String skype,
			  	String telegram,
			  	String localizacao,
				String imagem) { 
		this.id = id;
		this.nome = nome;
		this.celular = celular;
		this.email = email;
		this.skype = skype;
		this.telegram = telegram;
		this.localizacao = localizacao;
		this.imagem = imagem;
	}
	
	public Integer getId() { return id; }
	public String getNome() { return nome; }
	public String getCelular() { return celular; }
	public String getEmail() { return email; }
	public String getSkype() { return skype; }
	public String getTelegram() { return telegram; }
	public String getLocalizacao() { return localizacao; }
	public String getImagem() { return imagem; }

	public String getNomeLocalizacao() { return nome + " ( " + localizacao + " )"; }
	
}