package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CartaoPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CartaoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CartaoResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cartão", description = "Operações relacionadas ao cartao")
public class CartaoController {

    private final CartaoService cartaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adiciona um cartão")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409",description = "Cartão ja cadastrado"),
            @ApiResponse(responseCode = "404",description = "Cartão não encontrado"),
            @ApiResponse(responseCode = "404",description = "Conta não encontrada"),
    })
    public CartaoResponse save (CartaoPostRequest cartaoPostRequest) {
        return cartaoService.save(cartaoPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta de cartao")
    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    public CartaoResponse consultarCartao(@PathVariable Long id) {
        return cartaoService.consultaCartao(id);
    }

    @GetMapping("/{contaId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta de cartao")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "404", description = "Cartão não encontrado")})
    public List<CartaoResponse> consultarCartaoPorContaId(@PathVariable Long contaId, @PathVariable Long clienteId) {
        return cartaoService.cartoesPorConta(contaId,clienteId);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza dados do cartao")
    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    public void update (@PathVariable long id, @RequestBody @Valid CartaoPatchRequest cartaoPatchRequest) {
         cartaoService.update(cartaoPatchRequest,id);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta o cartão")
    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
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
