package com.mpxds.mpComunicator.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class MpAuditoriaObjeto implements Serializable {
	//
	private static final long serialVersionUID = 1L;

	private Date dtHrAlt;
	private Date dtHrInc;

	private String userAlt;
	private String userInc;

	// --------------
	
	public MpAuditoriaObjeto() {
		super();
	}

	public MpAuditoriaObjeto(Date dtHrAlt,
							 Date dtHrInc,
							 String userAlt,
							 String userInc) {
		super();
		this.dtHrAlt = dtHrAlt;
		this.dtHrInc = dtHrInc;
		this.userAlt = userAlt;
		this.userInc = userInc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dthr_alt", nullable = true)
	public Date getDtHrAlt() {
		return this.dtHrAlt;
	}
	public void setDtHrAlt(Date newDthrAlt) {
		this.dtHrAlt = newDthrAlt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dthr_inc", nullable = true)
	public Date getDtHrInc() {
		return this.dtHrInc;
	}
	public void setDtHrInc(Date newDtHrInc) {
		this.dtHrInc = newDtHrInc;
	}

	@Column(name = "user_alt", nullable = true, length = 100)
	public String getUserAlt() {
		return this.userAlt;
	}
	public void setUserAlt(String newUserAlt) {
		this.userAlt = newUserAlt;
	}

	@Column(name = "user_inc", nullable = true, length = 100)
	public String getUserInc() {
		return this.userInc;
	}
	public void setUserInc(String newUserInc) {
		this.userInc = newUserInc;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
