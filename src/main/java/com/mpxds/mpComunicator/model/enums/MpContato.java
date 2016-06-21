package com.mpxds.mpComunicator.model.enums;

public enum MpContato {

	MARCUS(1, "Marcus Rodrigues", "021 9 97435-5450", "marcus_vpr@hotmail.com",
											"marcus_vpr@hotmail.com", "", "Rio de Janeiro"),
	ESTEVAO(2, "Estevão Ramos", "031 9 9572-4394", "stevaoramoss@hotmail.com",
											"estevao ramos", "", "?"),
	GABRIEL(3, "Gabriel Gonçalves", "031 9 9444-8724", "gabriel.ggfreitas@gmail.com",
											"gabrielgoncalvesfreitas@hotmail.com", "", "?"),
	DANIEL(4, "Daniel Cardoso de Faria", "061 X 9501-7999", "kelvinsilva1010@gmail.com",
											"kelvinsk810@hotmail.com", "", "?"),
	KELVIN(5, "Kelvin Silva", "062 9 8400-6647", "danielcfariia@outlook.com",
											"danielcfariia", "", "?"),
	RICK(6, "Rick Deu Hangalo", "244 9 1637-2177", "hangaloandre@gmail.com",
											"andre.hangalo1", "", "??? - Angola"),
	MAIKO(7, "Maiko Cunha", "044 X 9948-3814", "mkacunha@gmail.com",
											"maikocunha", "", "Araruna"),
	SIDNEI(8, "Sidnei Ferreira", "014 9 9713-2075", "tecnico.sidnei@hotmail.com)",
											"sidnei.ferreira37", "", "?"),
	PEDRO(9, "Pedro Henrique Andalecio Gontijo", "075 9 9242-6357", "pedroandalecio@hotmail.com",
											"pedroandalecio@hotmail.com", "", "?");
	
	private Integer id;
	private String nome;
	private String celular;
	private String email;
	private String skype;
	private String telegram;
	private String localizacao;
	
	// ---

	MpContato(Integer id,
			  String nome,
			  String celular,
			  String email,
			  String skype,
			  String telegram,
		  	  String localizacao) { 
		this.id = id;
		this.nome = nome;
		this.celular = celular;
		this.email = email;
		this.skype = skype;
		this.telegram = telegram;
		this.localizacao = localizacao;
	}
	
	public Integer getId() { return id; }
	public String getNome() { return nome; }
	public String getCelular() { return celular; }
	public String getEmail() { return email; }
	public String getSkype() { return skype; }
	public String getTelegram() { return telegram; }
	public String getCidade() { return localizacao; }

}