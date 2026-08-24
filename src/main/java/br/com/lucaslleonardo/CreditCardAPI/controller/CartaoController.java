package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CartaoPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CartaoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CartaoResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.CartaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/cartao")
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartaoResponse save (CartaoPostRequest cartaoPostRequest) {
        return cartaoService.save(cartaoPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CartaoResponse consultarCartao(@PathVariable Long id) {
        return cartaoService.consultaCartao(id);
    }

    @GetMapping("/{contaId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CartaoResponse> consultarCartaoPorContaId(@PathVariable Long contaId, @PathVariable Long clienteId) {
        return cartaoService.cartoesPorConta(contaId,clienteId);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public void update (@PathVariable long id, @RequestBody @Valid CartaoPatchRequest cartaoPatchRequest) {
         cartaoService.update(cartaoPatchRequest,id);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        cartaoService.delete(id);
    }
    
    @GetMapping("/{id}/limite")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getLimite(@PathVariable long id) {
        return cartaoService.consultaLimite(id);
    }

    @GetMapping("/{id}/limiteDisponivel")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getLimiteDisponivel(@PathVariable long id) {
        return cartaoService.consultaLimiteDisponivel(id);
    }


}
