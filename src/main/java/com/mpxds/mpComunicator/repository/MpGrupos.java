package com.mpxds.mpComunicator.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mpxds.mpComunicator.model.MpGrupo;
import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.repository.filter.MpGrupoFilter;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpGrupos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	private Criteria criarCriteriaParaFiltro(MpGrupoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(MpGrupo.class);
		
		if (StringUtils.isNotBlank(filtro.getNome()))
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getStatus()))
			criteria.add(Restrictions.ilike("mpStatus", filtro.getStatus(), MatchMode.ANYWHERE));
		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<MpGrupo> filtrados(MpGrupoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setFirstResult(filtro.getPrimeiroRegistro());
		criteria.setMaxResults(filtro.getQuantidadeRegistros());
		
		if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null)
			criteria.addOrder(Order.asc(filtro.getPropriedadeOrdenacao()));
		else if (filtro.getPropriedadeOrdenacao() != null)
			criteria.addOrder(Order.desc(filtro.getPropriedadeOrdenacao()));
		//		
		return criteria.list();
	}
	
	public int quantidadeFiltrados(MpGrupoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
	
	
	public MpGrupo guardar(MpGrupo mpGrupo) {
		try {
			return manager.merge(mpGrupo);
		} catch (OptimisticLockException e) {
			throw new MpNegocioException(
						"Erro de concorrência. Esse GRUPO... já foi alterado anteriormente!");
		}
	}
	
	@MpTransactional
	public void remover(MpGrupo mpGrupo) throws MpNegocioException {
		try {
			mpGrupo = porId(mpGrupo.getId());
			manager.remove(mpGrupo);
			manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("Grupo... não pode ser excluído.");
		}
	}

	public MpGrupo porNome(String nome) {
		try {
			return manager.createQuery("from MpGrupo where upper(nome) = :nome", MpGrupo.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public MpGrupo porId(Long id) {
		return manager.find(MpGrupo.class, id);
	}
	
	public List<MpGrupo> mpGrupoList() {
		return manager.createQuery("from MpGrupo ORDER By nome", 
														MpGrupo.class).getResultList();
	}
	
	public MpGrupo porNavegacao(String acao, String nome) {
		//
//		System.out.println("MpGrupos.MpGrupo ( " + acao + " / " + nome);
		//
		try {
			//
			if (acao.equals("mpEnd") || acao.equals("mpPrev")) 
				return manager.createQuery(
					"from MpGrupo where upper(nome) < :nome ORDER BY nome DESC",
					MpGrupo.class)
					.setParameter("nome", nome.toUpperCase())
					.setMaxResults(1)
					.getSingleResult();
			else 
			if (acao.equals("mpFirst") || acao.equals("mpNext")) 
				return manager.createQuery(
					"from MpGrupo where upper(nome) > :nome ORDER BY nome ASC",
					MpGrupo.class)
					.setParameter("nome", nome.toUpperCase())
					.setMaxResults(1)
					.getSingleResult();
			else
				return null;
						
		} catch (NoResultException e) {
			return null;
		}
	}
	
}