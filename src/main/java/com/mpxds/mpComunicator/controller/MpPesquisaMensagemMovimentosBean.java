package com.mpxds.mpComunicator.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.mpxds.mpComunicator.model.MpMensagemMovimento;
import com.mpxds.mpComunicator.repository.MpMensagemMovimentos;
import com.mpxds.mpComunicator.repository.filter.MpMensagemMovimentoFilter;
import com.mpxds.mpComunicator.util.jsf.MpFacesUtil;

@Named
@ViewScoped
public class MpPesquisaMensagemMovimentosBean implements Serializable {
	//
	private static final long serialVersionUID = 1L;
	
	@Inject
	private MpMensagemMovimentos mpMensagemMovimentos;
	
	private MpMensagemMovimentoFilter mpFiltro;
	private List<MpMensagemMovimento> mpMensagemMovimentosFiltrados;
	
	private MpMensagemMovimento mpMensagemMovimentoSelecionado;
	
	private LazyDataModel<MpMensagemMovimento> model;
	
	// ---
	
	public MpPesquisaMensagemMovimentosBean() {
		//
		mpFiltro = new MpMensagemMovimentoFilter();
		
		model = new LazyDataModel<MpMensagemMovimento>() {
			//
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<MpMensagemMovimento> load(int first, int pageSize, String sortField,
												SortOrder sortOrder, Map<String, Object> filters) {
				mpFiltro.setPrimeiroRegistro(first);
				mpFiltro.setQuantidadeRegistros(pageSize);
				mpFiltro.setPropriedadeOrdenacao(sortField);
				mpFiltro.setAscendente(SortOrder.ASCENDING.equals(sortOrder));
				
				setRowCount(mpMensagemMovimentos.quantidadeFiltrados(mpFiltro));
				
				return mpMensagemMovimentos.filtrados(mpFiltro);
			}
			
		};
	}

	public void pesquisar() {
		//
		mpMensagemMovimentosFiltrados = mpMensagemMovimentos.filtrados(mpFiltro);

		System.out.println("MpPesquisaMensagemMovientosBean().pesquisar() - 000 ( " +
															mpMensagemMovimentosFiltrados.size());
	}

	public void removerMpMensagemMovimento() {
		//
		this.mpMensagemMovimentos.remover(mpMensagemMovimentoSelecionado);
			
		MpFacesUtil.addInfoMessage("Mensagem Movimento " + this.mpMensagemMovimentoSelecionado.getId()
																	+ " excluído com sucesso.");
	}			
	
	// ---
	
	public List<MpMensagemMovimento> getMpMensagemMovimentosFiltrados() {
															return mpMensagemMovimentosFiltrados; }

	public MpMensagemMovimentoFilter getFiltro() { return mpFiltro; }

	public LazyDataModel<MpMensagemMovimento> getModel() { return model; }
	
	public MpMensagemMovimento getMpMensagemMovimentoSelecionado() { 
															return mpMensagemMovimentoSelecionado; }
	public void setMpMensagemMovimentoSelecionado(MpMensagemMovimento 
															mpMensagemMovimentoSelecionado) {
							this.mpMensagemMovimentoSelecionado = mpMensagemMovimentoSelecionado; }
	
}