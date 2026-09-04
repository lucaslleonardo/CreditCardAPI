package br.com.lucaslleonardo.CreditCardAPI.exception;

public class CartaoNaoEncontradoException extends RuntimeException {
    public CartaoNaoEncontradoException(String message) {
        super(message);
    }
}
