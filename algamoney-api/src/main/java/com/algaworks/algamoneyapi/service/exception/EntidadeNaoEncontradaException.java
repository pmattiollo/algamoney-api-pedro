package com.algaworks.algamoneyapi.service.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String campo;
    private Object valor;

    public EntidadeNaoEncontradaException(String campo, Object valor) {
	super();

	this.campo = campo;
	this.valor = valor;
    }

    public String getCampo() {
	return campo;
    }

    public Object getValor() {
	return valor;
    }

}
