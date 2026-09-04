package br.com.lucaslleonardo.CreditCardAPI.exception;

public class CartaoJaExistenteException extends RuntimeException {
    public CartaoJaExistenteException(String message) {
        super(message);
    }
}
