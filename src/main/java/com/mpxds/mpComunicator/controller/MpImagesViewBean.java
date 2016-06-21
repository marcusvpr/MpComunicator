package com.mpxds.mpComunicator.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import com.mpxds.mpComunicator.model.enums.MpTipoContato;
 
@ManagedBean
public class MpImagesViewBean {
     
    private List<String> images;

    // ---
    
    @PostConstruct
    public void init() {
    	//
    	List<MpTipoContato> mpTipoContatoList = Arrays.asList(MpTipoContato.values());

		this.images = new ArrayList<String>();
        //
		for (MpTipoContato mpTipoContato : mpTipoContatoList) {
			//
			this.images.add(mpTipoContato.getImagem());
		}
    }
 
    public List<String> getImages() { return images; }
    
}