package br.com.lucaslleonardo.CreditCardAPI.exception;

public class FaturaNaoAbertaException extends RuntimeException {
    public FaturaNaoAbertaException(String message) {
        super(message);
    }
}
