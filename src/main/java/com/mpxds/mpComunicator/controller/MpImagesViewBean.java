package com.mpxds.mpComunicator.controller;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
 
@ManagedBean
public class MpImagesViewBean {
     
    private List<String> images;
     
    @PostConstruct
    public void init() {
        images = new ArrayList<String>();
        //
        images.add("email.jpg");
        images.add("sms.png");
        images.add("android.jpg");
        images.add("telegram.jpg");
        images.add("googlePush.png");
    }
 
    public List<String> getImages() {
        return images;
    }
    
}