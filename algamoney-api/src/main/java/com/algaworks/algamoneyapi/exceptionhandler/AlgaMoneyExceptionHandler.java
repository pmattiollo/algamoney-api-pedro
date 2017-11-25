package com.algaworks.algamoneyapi.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.algaworks.algamoneyapi.service.exception.EntidadeNaoEncontradaException;
import com.algaworks.algamoneyapi.service.exception.PessoaInexistenteOuInativaException;

@ControllerAdvice
public class AlgaMoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {
	String mensagemUsuario = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
	String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
	List<Erro> listaDeErros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	return handleExceptionInternal(ex, listaDeErros, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	    HttpHeaders headers, HttpStatus status, WebRequest request) {
	List<Erro> listaDeErros = criarListaDeErros(ex.getBindingResult());
	return handleExceptionInternal(ex, listaDeErros, headers, status, request);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class })
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex,
	    WebRequest request) {
	String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null,
		LocaleContextHolder.getLocale());
	String mensagemDesenvolvedor = ex.toString();
	List<Erro> listaDeErros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	return handleExceptionInternal(ex, listaDeErros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
	    WebRequest request) {
	String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null,
		LocaleContextHolder.getLocale());
	String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
	List<Erro> listaDeErros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	return handleExceptionInternal(ex, listaDeErros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ PessoaInexistenteOuInativaException.class })
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
	String mensagemUsuario = messageSource.getMessage("pessoa.inexistente-ou-inativa", null,
		LocaleContextHolder.getLocale());
	String mensagemDesenvolvedor = ex.toString();
	List<Erro> listaDeErros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	return ResponseEntity.badRequest().body(listaDeErros);
    }

    @ExceptionHandler({ EntidadeNaoEncontradaException.class })
    public ResponseEntity<Object> handleCodigoNaoLocalizadoException(EntidadeNaoEncontradaException ex) {
	String[] args = { ex.getCampo(), ex.getValor().toString() };
	String mensagemUsuario = messageSource.getMessage("entidade.nao-encotrada", args,
		LocaleContextHolder.getLocale());
	String mensagemDesenvolvedor = ex.toString();
	List<Erro> listaDeErros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	return ResponseEntity.badRequest().body(listaDeErros);
    }

    private List<Erro> criarListaDeErros(BindingResult bindingResult) {
	List<Erro> erros = new ArrayList<>();

	for (FieldError fieldError : bindingResult.getFieldErrors()) {
	    String mensagemUsuario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
	    String mensagemDesenvolvedor = fieldError.toString();
	    erros.add(new Erro(mensagemUsuario, mensagemDesenvolvedor));
	}
	return erros;
    }

    public static class Erro {

	private String mensagemUsuario;
	private String mensagemDesenvolvedor;

	public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
	    this.mensagemUsuario = mensagemUsuario;
	    this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

	public String getMensagemUsuario() {
	    return mensagemUsuario;
	}

	public void setMensagemUsuario(String mensagemUsuario) {
	    this.mensagemUsuario = mensagemUsuario;
	}

	public String getMensagemDesenvolvedor() {
	    return mensagemDesenvolvedor;
	}

	public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
	    this.mensagemDesenvolvedor = mensagemDesenvolvedor;
	}

    }

}
