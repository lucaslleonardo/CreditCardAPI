package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ContaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ContaResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("Conta")
@Tag(name= "Conta", description = "Operações relacionadas as contas")
public class ContaController {

    public static Logger log = LoggerFactory.getLogger(ContaController.class);

    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria conta")
    @ApiResponse(responseCode = "409", description = "Conta ja cadastrada")
    public ContaResponse save(@RequestBody @Valid ContaPostRequest contaPostRequest){
        log.info("requisicao para criar conta");
        return contaService.save(contaPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna conta")
    @ApiResponse(responseCode = "404", description = "Conta nao encontrada")
    public ContaResponse verConta(@PathVariable long id){
        log.info("requisicao para ver conta do Cliente");
        return contaService.verUmaConta(id);
    }

    @GetMapping("/{clienteId}/all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna contas de um cliente")
    public List<ContaResponse> verContasDeCliente(@PathVariable long id){
        log.info("requisicao para ver contas do Cliente");
        return contaService.verContasDeCliente(id);
    }


    @PutMapping("/{clienteId}/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza dados da conta")
    @ApiResponse(responseCode = "404", description = "Conta nao encontrada")
    public void update(@PathVariable long id,@RequestBody @Valid ContaPatchRequest contaPatchRequest){
       log.info("requisicao para atualizar conta do Cliente");
       contaService.update(id,contaPatchRequest);
    }

    @DeleteMapping("/{clienteId}/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta conta")
    @ApiResponse(responseCode = "404", description = "Conta nao encontrada")
    public void delete(@PathVariable long id){
        log.info("requisicao para deletar conta do Cliente");
        contaService.delete(id);
    }
}
