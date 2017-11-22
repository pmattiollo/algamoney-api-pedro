package com.algaworks.algamoneyapi.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.algaworks.algamoneyapi.model.Pessoa;
import com.algaworks.algamoneyapi.repository.PessoaRepository;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    public Page<Pessoa> pesquisar(String nome, Pageable pageable) {
	return pessoaRepository.findByNomeContaining(nome, pageable);
    }

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
	Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
	BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
	return pessoaRepository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
	Pessoa pessoaSalva = buscarPessoaPeloCodigo(codigo);
	pessoaSalva.setAtivo(ativo);
	pessoaRepository.save(pessoaSalva);
    }

    private Pessoa buscarPessoaPeloCodigo(Long codigo) {
	Optional<Pessoa> pessoaSalvaOpt = Optional.ofNullable(pessoaRepository.findOne(codigo));
	Pessoa pessoaSalva = pessoaSalvaOpt.orElseThrow(() -> new EmptyResultDataAccessException(1));
	return pessoaSalva;
    }

}
