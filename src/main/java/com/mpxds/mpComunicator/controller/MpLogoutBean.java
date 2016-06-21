package com.mpxds.mpComunicator.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
//import javax.inject.Inject;
import javax.inject.Named;

import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpLogoutBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	// --------------
	
	public MpLogoutBean() {
//		System.out.println("MpLogoutBean() - 000");
	}
	
	public void inicializar() {
		//
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext extContext = context.getExternalContext();
		String url = extContext.encodeActionURL(extContext.getRequestContextPath() +
																	"/MpLogin.xhtml");
//																	"/j_spring_security_logout");
		try {
			extContext.redirect(url);
			//
		} catch (IOException e) {
			MpFacesUtil.addErrorMessage(e.getMessage());
		}
	}

	public void inicializarErro() {
		//
	}
		
}