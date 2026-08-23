package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ContaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ContaResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.ContaService;
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
public class ContaController {

    public static Logger log = LoggerFactory.getLogger(ContaController.class);

    private final ContaService contaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ContaResponse save(@RequestBody @Valid ContaPostRequest contaPostRequest){
        log.info("requisicao para criar conta");
        return contaService.save(contaPostRequest);
    }

    @GetMapping("/{clienteId}/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ContaResponse verConta(@PathVariable long id){
        log.info("requisicao para ver conta do Cliente");
        return contaService.verUmaConta(id);
    }

    @GetMapping("/{clienteId}/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ContaResponse> verContasDeCliente(@PathVariable long id){
        log.info("requisicao para ver contas do Cliente");
        return contaService.verContasDeCliente(id);
    }


    @PutMapping("/{clienteId/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable long id,@RequestBody @Valid ContaPatchRequest contaPatchRequest){
       log.info("requisicao para atualizar conta do Cliente");
       contaService.update(id,contaPatchRequest);
    }

    @DeleteMapping("/{clienteId/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id){
        log.info("requisicao para deletar conta do Cliente");
        contaService.delete(id);
    }
}
