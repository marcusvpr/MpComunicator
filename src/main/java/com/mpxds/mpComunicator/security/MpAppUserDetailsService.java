package com.mpxds.mpComunicator.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mpxds.mpComunicator.model.MpGrupo;
import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.repository.MpUsuarios;
import com.mpxds.mpComunicator.util.cdi.MpCDIServiceLocator;
import com.mpxds.mpComunicator.security.MpUsuarioSistema;

public class MpAppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String loginEmail) throws UsernameNotFoundException {
		MpUsuarios mpUsuarios = MpCDIServiceLocator.getBean(MpUsuarios.class);
		MpUsuario mpUsuario = mpUsuarios.porLoginEmail(loginEmail);
		
		MpUsuarioSistema mpUser = null;
		
		if (mpUsuario != null) {
			mpUser = new MpUsuarioSistema(mpUsuario, getMpGrupos(mpUsuario));
		}
		
		return mpUser;
	}

	private Collection<? extends GrantedAuthority> getMpGrupos(MpUsuario mpUsuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		for (MpGrupo mpGrupo : mpUsuario.getMpGrupos()) {
			authorities.add(new SimpleGrantedAuthority(mpGrupo.getNome().toUpperCase()));
		}
		
		return authorities;
	}

}
