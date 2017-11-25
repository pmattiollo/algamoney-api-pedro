package com.algaworks.algamoneyapi.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.model.Lancamento_;
import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.repository.LancamentoRepository;
import com.algaworks.algamoneyapi.repository.PessoaRepository;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projection.ResumoLancamento;
import com.algaworks.algamoneyapi.service.exception.EntidadeNaoEncontradaException;
import com.algaworks.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
	return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
	return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    public Lancamento buscarLancamentoPeloCodigo(Long codigo) {
	return buscarLancamentoExistente(codigo);
    }

    public Lancamento salvar(Lancamento lancamento) {
	validarPessoa(lancamento);
	return lancamentoRepository.save(lancamento);
    }

    public void remover(Long codigo) {
	lancamentoRepository.delete(codigo);
    }

    public Lancamento atualizar(Long codigo, Lancamento lancamento) {
	Lancamento lancamentoSalvo = buscarLancamentoPeloCodigo(codigo);

	if (!lancamentoSalvo.getPessoa().equals(lancamento.getPessoa())) {
	    validarPessoa(lancamento);
	}

	BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
	return salvar(lancamentoSalvo);
    }

    private Lancamento buscarLancamentoExistente(Long codigo) {
	Optional<Lancamento> pessoaSalvaOpt = Optional.ofNullable(lancamentoRepository.findOne(codigo));
	return pessoaSalvaOpt
		.orElseThrow(() -> new EntidadeNaoEncontradaException(Lancamento_.codigo.getName(), codigo));
    }

    private void validarPessoa(Lancamento lancamento) {
	Optional<Pessoa> pessoaSalvaOpt = Optional.ofNullable(pessoaRepository.findOne(lancamento.getCodigoDaPessoa()));

	if (!pessoaSalvaOpt.filter(Pessoa::getAtivo).isPresent()) {
	    throw new PessoaInexistenteOuInativaException();
	}
    }

}
