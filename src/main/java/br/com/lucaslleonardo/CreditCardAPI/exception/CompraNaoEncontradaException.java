package br.com.lucaslleonardo.CreditCardAPI.exception;

public class CompraNaoEncontradaException extends RuntimeException {
    public CompraNaoEncontradaException(String message) {
        super(message);
    }
}
