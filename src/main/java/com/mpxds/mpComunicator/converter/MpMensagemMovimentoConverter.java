package com.mpxds.mpComunicator.converter;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.convert.ClientConverter;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;

@FacesConverter(forClass = MpMensagemMovimento.class)
public class MpMensagemMovimentoConverter implements Converter, ClientConverter {

	@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		MpMensagemMovimento retorno = null;
		
		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			retorno = mpMensagemMovimentos.porId(id);
		}
		
		return retorno;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			if (null == ((MpMensagemMovimento) value).getId())
				return "";
			else
				return ((MpMensagemMovimento) value).getId().toString();
		}
		
		return "";
	}

	@Override
	public Map<String, Object> getMetadata() {
		return null;
	}

	@Override
	public String getConverterId() {
		return "com.mpxds.MpMensagemMovimento";
	}

}