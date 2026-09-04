package br.com.lucaslleonardo.CreditCardAPI.exception;

public class FaturaNaoEncontradaException extends RuntimeException {
    public FaturaNaoEncontradaException(String message) {
        super(message);
    }
}
