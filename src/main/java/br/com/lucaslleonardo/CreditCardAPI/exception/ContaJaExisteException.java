package br.com.lucaslleonardo.CreditCardAPI.exception;

public class ContaJaExisteException extends RuntimeException {
    public ContaJaExisteException(String message) {
        super(message);
    }
}
