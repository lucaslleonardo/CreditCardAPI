package br.com.lucaslleonardo.CreditCardAPI.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}
