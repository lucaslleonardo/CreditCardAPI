package br.com.lucaslleonardo.CreditCardAPI.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
