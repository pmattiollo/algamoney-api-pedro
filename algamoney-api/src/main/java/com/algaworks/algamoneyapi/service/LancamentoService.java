package com.algaworks.algamoneyapi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algaworks.algamoneyapi.model.Lancamento;
import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.repository.LancamentoRepository;
import com.algaworks.algamoneyapi.repository.PessoaRepository;
import com.algaworks.algamoneyapi.repository.filter.LancamentoFilter;
import com.algaworks.algamoneyapi.repository.projection.ResumoLancamento;
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
	return lancamentoRepository.findOne(codigo);
    }

    public Lancamento salvar(Lancamento lancamento) {
	Optional<Pessoa> pessoaSalvaOpt = Optional.of(pessoaRepository.findOne(lancamento.getCodigoDaPessoa()));
	pessoaSalvaOpt.filter(Pessoa::getAtivo).orElseThrow(() -> new PessoaInexistenteOuInativaException());
	return lancamentoRepository.save(lancamento);
    }

    public void remover(Long codigo) {
	lancamentoRepository.delete(codigo);
    }

}
