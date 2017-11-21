package com.algaworks.algamoneyapi.repository.filter;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;

public class LancamentoFilter {

    private String descricao;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimentoDe;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimentoAte;

    public String getDescricao() {
	return descricao;
    }

    public Optional<String> getOptionalDescricao() {
	return Optional.ofNullable(getDescricao());
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public LocalDate getDataVencimentoDe() {
	return dataVencimentoDe;
    }

    public Optional<LocalDate> getOptionalDataVencimentoDe() {
	return Optional.ofNullable(getDataVencimentoDe());
    }

    public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
	this.dataVencimentoDe = dataVencimentoDe;
    }

    public LocalDate getDataVencimentoAte() {
	return dataVencimentoAte;
    }

    public Optional<LocalDate> getOptionalDataVencimentoAte() {
	return Optional.ofNullable(getDataVencimentoAte());
    }

    public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
	this.dataVencimentoAte = dataVencimentoAte;
    }

}
