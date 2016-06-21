package com.mpxds.mpComunicator.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.mpxds.mpComunicator.model.MpUsuario;

public class MpUsuarioSistema extends User {

	private static final long serialVersionUID = 1L;
	
	private MpUsuario mpMpUsuario;
	
	public MpUsuarioSistema(MpUsuario mpMpUsuario, Collection<? extends GrantedAuthority> authorities) {
		super(mpMpUsuario.getEmail(), mpMpUsuario.getSenha(), authorities);
		this.mpMpUsuario = mpMpUsuario;
	}

	public MpUsuario getMpUsuario() {
		return mpMpUsuario;
	}

}
