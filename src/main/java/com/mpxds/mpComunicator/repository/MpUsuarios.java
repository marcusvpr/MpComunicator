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

import com.mpxds.mpComunicator.model.MpUsuario;
import com.mpxds.mpComunicator.model.enums.MpStatus;
import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.repository.filter.MpUsuarioFilter;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpUsuarios implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	private Criteria criarCriteriaParaFiltro(MpUsuarioFilter filtro) {
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(MpUsuario.class);
		
		if (StringUtils.isNotBlank(filtro.getLogin()))
			criteria.add(Restrictions.ilike("login", filtro.getLogin(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getNome()))
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getEmail()))
			criteria.add(Restrictions.ilike("email", filtro.getEmail(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getStatus()))
			criteria.add(Restrictions.eq("mpStatus", MpStatus.valueOf(filtro.getStatus())));
		//		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<MpUsuario> filtrados(MpUsuarioFilter filtro) {
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
	
	public int quantidadeFiltrados(MpUsuarioFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
		
	public MpUsuario guardar(MpUsuario mpUsuario) {
		try {
			return manager.merge(mpUsuario);
		} catch (OptimisticLockException e) {
			throw new MpNegocioException(
						"Erro de concorrência. Esse USUÁRIO... já foi alterado anteriormente!");
		}
	}
	
	@MpTransactional
	public void remover(MpUsuario mpUsuario) throws MpNegocioException {
		try {
			mpUsuario = porId(mpUsuario.getId());
			manager.remove(mpUsuario);
			manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("USUÁRIO... não pode ser excluída.");
		}
	}

	public MpUsuario porNome(String nome) {
		try {
			return manager.createQuery("from MpUsuario where upper(nome) = :nome", MpUsuario.class)
				.setParameter("nome", nome.toUpperCase())
				.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public MpUsuario porId(Long id) {
		return manager.find(MpUsuario.class, id);
	}
		
	public List<MpUsuario> mpVendedores() {
		// TODO filtrar apenas vendedores (por um grupo específico)
		return this.manager.createQuery("from MpUsuario ORDER BY nome", 
																MpUsuario.class).getResultList();
	}
	
	public List<MpUsuario> mpUsuarioAtivos() {
		//
		return this.manager.createQuery("from MpUsuario where mpStatus = 'ATIVO' ORDER BY nome",
															MpUsuario.class).getResultList();
	}
	
	public List<MpUsuario> mpUsuarioBloqueados() {
		//
		return this.manager.createQuery("from MpUsuario where mpStatus = 'BLOQUEADO' ORDER BY nome",
															MpUsuario.class).getResultList();
	}

	public MpUsuario porLoginEmail(String loginEmail) {
		if (null == loginEmail || loginEmail.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		
		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(login) = :loginEmail" + 
																  " or lower(email) = :loginEmail" ,
			MpUsuario.class).setParameter("loginEmail", loginEmail.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porLoginEmailAmbiente(String loginEmail, String ambiente) {
		if (null == loginEmail || loginEmail.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		
		try {
			if (ambiente.isEmpty())
				mpUsuario = this.manager.createQuery(
						"from MpUsuario where lower(login) = :loginEmail" + 
						" or lower(email) = :loginEmail", MpUsuario.class)
						.setParameter("loginEmail", loginEmail.toLowerCase())
						.getSingleResult();
			else
				mpUsuario = this.manager.createQuery(
						"from MpUsuario where lower(login) = :loginEmail" + 
						" or lower(email) = :loginEmail and tenantId = :ambiente", MpUsuario.class)
						.setParameter("loginEmail", loginEmail.toLowerCase())
						.setParameter("tenantId", ambiente)
						.getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login OU e-mail informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porLogin(String login) {
		if (null == login || login.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		
		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(login) = :login",
																					MpUsuario.class)
						.setParameter("login", login.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o login informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porEmail(String email) {
		if (null == email || email.isEmpty()) 
			return null;
		//	
		MpUsuario mpUsuario = null;
		//
		try {
			mpUsuario = this.manager.createQuery("from MpUsuario where lower(email) = :email",
																					MpUsuario.class)
						.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException e) {
			// nenhum usuário encontrado com o e-mail informado
		}
		//
		return mpUsuario;
	}

	public MpUsuario porNavegacao(String acao, String nome) {
		//
//		System.out.println("MpUsuarios.MpUsuario ( " + acao + " / " + nome);
		//
		try {
			//
			if (acao.equals("mpEnd") || acao.equals("mpPrev")) 
				return manager.createQuery(
					"from MpUsuario where upper(nome) < :nome ORDER BY nome DESC",
					MpUsuario.class)
					.setParameter("nome", nome.toUpperCase())
					.setMaxResults(1)
					.getSingleResult();
			else 
			if (acao.equals("mpFirst") || acao.equals("mpNext")) 
				return manager.createQuery(
					"from MpUsuario where upper(nome) > :nome ORDER BY nome ASC",
					MpUsuario.class)
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