package com.mpxds.mpComunicator.model.enums;

public enum MpContato {

	MARCUS("Marcus Rodrigues", "021 9 97435-5450", "marcus_vpr@hotmail.com",
											"marcus_vpr@hotmail.com", "", "Rio de Janeiro"),
	ESTEVAO("Estevão Ramos", "031 9 9572-4394", "stevaoramoss@hotmail.com",
											"estevao ramos", "", "?"),
	GABRIEL("Gabriel Gonçalves", "031 9 9444-8724", "gabriel.ggfreitas@gmail.com",
											"gabrielgoncalvesfreitas@hotmail.com", "", "?"),
	DANIEL("Daniel Cardoso de Faria", "061 X 9501-7999", "kelvinsilva1010@gmail.com",
											"kelvinsk810@hotmail.com", "", "?"),
	KELVIN("Kelvin Silva", "062 9 8400-6647", "danielcfariia@outlook.com",
											"danielcfariia", "", "?"),
	RICK("Rick Deu Hangalo", "244 9 1637-2177", "hangaloandre@gmail.com",
											"andre.hangalo1", "", "??? - Angola"),
	MAIKO("Maiko Cunha", " 044 X 9948-3814", "mkacunha@gmail.com",
											"maikocunha", "", "Araruna");
	
	private String nome;
	private String celular;
	private String email;
	private String skype;
	private String telegram;
	private String localizacao;
	
	// ---

	MpContato(String nome,
			  String celular,
			  String email,
			  String skype,
			  String telegram,
		  	  String localizacao) { 
		this.nome = nome;
		this.celular = celular;
		this.email = email;
		this.skype = skype;
		this.telegram = telegram;
		this.localizacao = localizacao;
	}
	
	public String getNome() { return nome; }
	public String getCelular() { return celular; }
	public String getEmail() { return email; }
	public String getSkype() { return skype; }
	public String getTelegram() { return telegram; }
	public String getCidade() { return localizacao; }

}