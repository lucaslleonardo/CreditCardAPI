package br.com.lucaslleonardo.CreditCardAPI.exception;

public class ClienteJaCadastradoException extends RuntimeException {
    public ClienteJaCadastradoException(String message) {
        super(message);
    }
}
