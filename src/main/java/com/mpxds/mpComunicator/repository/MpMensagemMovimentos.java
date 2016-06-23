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

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.model.enums.MpContato;
import com.mpxds.mpComunicator.model.enums.MpTipoContato;
import com.mpxds.mpComunicator.model.enums.MpStatusMensagem;
import com.mpxds.mpComunicator.exception.MpNegocioException;
import com.mpxds.mpComunicator.repository.filter.MpMensagemMovimentoFilter;
import com.mpxds.mpComunicator.util.jpa.MpTransactional;

public class MpMensagemMovimentos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	private Criteria criarCriteriaParaFiltro(MpMensagemMovimentoFilter filtro) {
		Session session = this.manager.unwrap(Session.class);
		
		Criteria criteria = session.createCriteria(MpMensagemMovimento.class);
		
		if (filtro.getDataCriacaoDe() != null)
			criteria.add(Restrictions.ge("dataMovimento", filtro.getDataCriacaoDe()));
		if (filtro.getDataCriacaoAte() != null)
			criteria.add(Restrictions.le("dataMovimento", filtro.getDataCriacaoAte()));	

		if (StringUtils.isNotBlank(filtro.getUsuario()))
			criteria.add(Restrictions.ilike("mpUsuario.nome", filtro.getUsuario(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getContato()))
			criteria.add(Restrictions.eq("mpContato", MpContato.valueOf(filtro.getContato())));
		if (StringUtils.isNotBlank(filtro.getMensagem()))
			criteria.add(Restrictions.ilike("mensagem", filtro.getMensagem(), MatchMode.ANYWHERE));
		if (StringUtils.isNotBlank(filtro.getTipoContato()))
			criteria.add(Restrictions.eq("mpTipoContato", 
													MpTipoContato.valueOf(filtro.getTipoContato())));
		if (StringUtils.isNotBlank(filtro.getStatus()))
			criteria.add(Restrictions.eq("mpStatusMensagem",
													MpStatusMensagem.valueOf(filtro.getStatus())));
		//		
		return criteria;
	}
	
	@SuppressWarnings("unchecked")
	public List<MpMensagemMovimento> filtrados(MpMensagemMovimentoFilter filtro) {
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
	
	public int quantidadeFiltrados(MpMensagemMovimentoFilter filtro) {
		Criteria criteria = criarCriteriaParaFiltro(filtro);
		
		criteria.setProjection(Projections.rowCount());
		
		return ((Number) criteria.uniqueResult()).intValue();
	}
		
	public MpMensagemMovimento guardar(MpMensagemMovimento mpMensagemMovimento) {
		try {
			return manager.merge(mpMensagemMovimento);
		} catch (OptimisticLockException e) {
			throw new MpNegocioException(
					"Erro de concorrência. Essa Mensagem Movimento... já foi alterado anteriormente!");
		}
	}
	
	@MpTransactional
	public void remover(MpMensagemMovimento mpMensagemMovimento) throws MpNegocioException {
		try {
			mpMensagemMovimento = porId(mpMensagemMovimento.getId());
			manager.remove(mpMensagemMovimento);
			manager.flush();
		} catch (PersistenceException e) {
			throw new MpNegocioException("Mensagem Movimento... não pode ser excluída.");
		}
	}
	
	public MpMensagemMovimento porId(Long id) {
		return manager.find(MpMensagemMovimento.class, id);
	}
				
	public MpMensagemMovimento porNavegacao(String acao, String nome) {
		//
		try {
			//
			if (acao.equals("mpEnd") || acao.equals("mpPrev")) 
				return manager.createQuery(
					"from MpMensagemMovimento where upper(nome) < :nome ORDER BY nome DESC",
					MpMensagemMovimento.class)
					.setParameter("nome", nome.toUpperCase())
					.setMaxResults(1)
					.getSingleResult();
			else 
			if (acao.equals("mpFirst") || acao.equals("mpNext")) 
				return manager.createQuery(
					"from MpMensagemMovimento where upper(nome) > :nome ORDER BY nome ASC",
					MpMensagemMovimento.class)
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