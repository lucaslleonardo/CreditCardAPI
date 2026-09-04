package br.com.lucaslleonardo.CreditCardAPI.exception;

public class CartaoNaoAtivoException extends RuntimeException {
    public CartaoNaoAtivoException(String message) {
        super(message);
    }
}
