package com.mpxds.mpComunicator.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import java.io.Serializable;

@MappedSuperclass
public abstract class MpBaseEntity implements Serializable, Cloneable {
	//
	private static final long serialVersionUID = 1L;

    private Long id;
	private Long version = 0L;
	
	private MpAuditoriaObjeto mpAuditoriaObjeto;
	
	// ---
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { return this.id; }
	public void setId(Long id) { this.id = id; }
    
	@Embedded
    public MpAuditoriaObjeto getMpAuditoriaObjeto() { return this.mpAuditoriaObjeto ; }
    public void setMpAuditoriaObjeto(MpAuditoriaObjeto newMpAuditoriaObjeto) {
  	  											this.mpAuditoriaObjeto = newMpAuditoriaObjeto; }

	@Version
	@Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    public Long getVersion() { return this.version; }
    public void setVersion(Long newVersion) { this.version = newVersion; }
		
	@Override
	public String toString() {
		String glue = "='";
		String glue2 = "' ";
		StringBuilder buffer = new StringBuilder();

		buffer.append(this.getClass().getName()).append("@").append(Integer.toHexString(
																	this.hashCode())).append(" [");
		buffer.append("id").append(glue).append(this.getId()).append(glue2);
		buffer.append("version").append(glue).append(this.getVersion()).append(glue2);
		buffer.append("]");

		return buffer.toString();
	}
    
	public Object clone() throws CloneNotSupportedException { 
		return super.clone();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		MpBaseEntity  other = (MpBaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		
		return true;
	}

}