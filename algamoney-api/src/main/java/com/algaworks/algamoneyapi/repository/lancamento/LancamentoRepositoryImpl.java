package com.algaworks.algamoneyapi.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.algaworks.algamoneyapi.model.Categoria_;
import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.model.Lancamento_;
import com.algaworks.algamoneyapi.model.Pessoa_;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Lancamento> criteriaQuery = builder.createQuery(Lancamento.class);

	Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
	Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
	criteriaQuery.where(predicates);

	TypedQuery<Lancamento> typedQuery = entityManager.createQuery(criteriaQuery);
	adiconarRestricoesDePaginacao(typedQuery, pageable);
	return new PageImpl<Lancamento>(typedQuery.getResultList(), pageable, getTotal(lancamentoFilter));
    }

    @Override
    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	CriteriaQuery<ResumoLancamento> criteriaQuery = builder.createQuery(ResumoLancamento.class);

	Root<Lancamento> root = criteriaQuery.from(Lancamento.class);
	criteriaQuery.select(builder.construct(ResumoLancamento.class, root.get(Lancamento_.codigo),
		root.get(Lancamento_.descricao), root.get(Lancamento_.dataVencimento),
		root.get(Lancamento_.dataPagamengo), root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
		root.get(Lancamento_.categoria).get(Categoria_.nome), root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

	Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
	criteriaQuery.where(predicates);

	TypedQuery<ResumoLancamento> typedQuery = entityManager.createQuery(criteriaQuery);
	adiconarRestricoesDePaginacao(typedQuery, pageable);
	return new PageImpl<ResumoLancamento>(typedQuery.getResultList(), pageable, getTotal(lancamentoFilter));
    }

    private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
	    Root<Lancamento> root) {
	List<Predicate> predicates = new ArrayList<>();

	lancamentoFilter.getOptionalDescricao().ifPresent(descricao -> predicates.add(
		builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + descricao.toLowerCase() + "%")));
	lancamentoFilter.getOptionalDataVencimentoDe().ifPresent(dataVencimentoDe -> predicates
		.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataVencimentoDe)));
	lancamentoFilter.getOptionalDataVencimentoAte().ifPresent(dataVencimentoAte -> predicates
		.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), dataVencimentoAte)));

	return predicates.toArray(new Predicate[predicates.size()]);
    }

    private void adiconarRestricoesDePaginacao(TypedQuery<?> typedQuery, Pageable pageable) {
	int paginaAtual = pageable.getPageNumber();
	int totalDeRegistrosPorPagina = pageable.getPageSize();
	int primeiroregistroDaPagina = paginaAtual * totalDeRegistrosPorPagina;

	typedQuery.setFirstResult(primeiroregistroDaPagina);
	typedQuery.setMaxResults(0);
    }

    private Long getTotal(LancamentoFilter lancamentoFilter) {
	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
	CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
	Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

	Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
	criteriaQuery.where(predicates);
	criteriaQuery.select(builder.count(root));

	return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
