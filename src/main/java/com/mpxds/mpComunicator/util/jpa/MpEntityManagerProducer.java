package com.mpxds.mpComunicator.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@ApplicationScoped
public class MpEntityManagerProducer {

	private EntityManagerFactory factory;
	
	public MpEntityManagerProducer() {
		factory = Persistence.createEntityManagerFactory("MpComunicatorPU");
	}
	
	@Produces @RequestScoped
	public EntityManager createEntityManager() {
		return factory.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}
	
}