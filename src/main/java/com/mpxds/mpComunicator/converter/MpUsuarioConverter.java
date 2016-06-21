package com.mpxds.mpComunicator.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.repository.MpUsuarios;
import com.mpxds.mpComunicator.util.cdi.MpCDIServiceLocator;

@FacesConverter(forClass=MpUsuario.class)
public class MpUsuarioConverter implements Converter {

	//@Inject
	private MpUsuarios mpUsuarios;
	
	public MpUsuarioConverter() {
		this.mpUsuarios = (MpUsuarios) MpCDIServiceLocator.getBean(MpUsuarios.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		MpUsuario retorno = null;

		if (value != null) {
			retorno = this.mpUsuarios.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return ((MpUsuario) value).getId().toString();
		}
		return "";
	}

}