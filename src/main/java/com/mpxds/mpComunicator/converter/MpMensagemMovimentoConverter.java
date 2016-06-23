package com.mpxds.mpComunicator.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;
import com.mpxds.mpComunicator.util.cdi.MpCDIServiceLocator;

@FacesConverter(forClass=MpMensagemMovimento.class)
public class MpMensagemMovimentoConverter implements Converter {

	//@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;
	
	public MpMensagemMovimentoConverter() {
		this.mpMensagemMovimentos = (MpMensagemMovimentos) MpCDIServiceLocator.getBean(MpMensagemMovimentos.class);
	}
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		MpMensagemMovimento retorno = null;

		if (value != null) {
			retorno = this.mpMensagemMovimentos.porId(new Long(value));
		}

		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			return ((MpMensagemMovimento) value).getId().toString();
		}
		return "";
	}

}