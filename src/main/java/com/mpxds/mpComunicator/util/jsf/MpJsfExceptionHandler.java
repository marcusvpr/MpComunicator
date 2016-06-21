package com.mpxds.mpComunicator.util.jsf;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mpxds.mpComunicator.exception.MpNegocioException;

public class MpJsfExceptionHandler extends ExceptionHandlerWrapper {

	private static Log log = LogFactory.getLog(MpJsfExceptionHandler.class);
	
	private ExceptionHandler wrapped;
	
	public MpJsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}
	
	@Override
	public void handle() throws FacesException {
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
		 
		while (events.hasNext()) {
			ExceptionQueuedEvent event = events.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			
			Throwable exception = context.getException();
			MpNegocioException negocioException = getNegocioException(exception);
			
			boolean handled = false;
			
			try {
				if (exception instanceof ViewExpiredException) {
					handled = true;
					redirect("/");
				} else if (negocioException != null) {
					handled = true;
					MpFacesUtil.addErrorMessage(negocioException.getMessage());
				} else {
					handled = true;
					log.error("Erro de sistema: " + exception.getMessage(), exception);
					redirect("/MpErro.xhtml");
				}
			} finally {
				if (handled) {
					events.remove();
				}
			}
		}
		
		getWrapped().handle();
	}
	
	private MpNegocioException getNegocioException(Throwable exception) {
		if (exception instanceof MpNegocioException) {
			return (MpNegocioException) exception;
		} else if (exception.getCause() != null) {
			return getNegocioException(exception.getCause());
		}
		
		return null;
	}
	
	private void redirect(String page) {
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			ExternalContext externalContext = facesContext.getExternalContext();
			String contextPath = externalContext.getRequestContextPath();
	
			externalContext.redirect(contextPath + page);
			facesContext.responseComplete();
		} catch (IOException e) {
			throw new FacesException(e);
		}
	}

}