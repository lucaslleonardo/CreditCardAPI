package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.FaturaFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.FaturaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fatura")
@RequiredArgsConstructor
@Validated
@Slf4j
public class FaturaController {

    private final FaturaService faturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FaturaResponse cadastrarFatura(@RequestBody @Valid FaturaPostRequest  faturaPostRequest) {
        log.info("Requisição para cadastrar infos da fatura");
        return faturaService.cadastrarInfosFatura(faturaPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FaturaResponse consultarFatura(@PathVariable long cartaoId) {
        log.info("Requisição para consultar uma fatura");
        return faturaService.consultaFatura(cartaoId);
    }

    @GetMapping("/{id}/listarTodas")
    @ResponseStatus(HttpStatus.OK)
    public List<FaturaResponse> consultarTodasFaturas(@PathVariable long cartaoId) {
        log.info("Requisição para consultar todas as faturas");
        return faturaService.consultaFaturas(cartaoId);
    }

    @PostMapping("/{id}/fechar")
    @ResponseStatus(HttpStatus.OK)
    public void fecharFatura(@PathVariable long cartaoId) {
        log.info("Requisição para fechar uma fatura");
        faturaService.fecharFatura(cartaoId);
    }


    @GetMapping("/valor")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal consultarValorFatura(@PathVariable long cartaoId) {
        log.info("Requisição para consultar valor da fatura");
        return faturaService.consultaValorFatura(cartaoId);
    }

    @GetMapping("/Vencimento")
    @ResponseStatus(HttpStatus.OK)
    public LocalDate consultarVencimento(@PathVariable long cartaoId) {
       log.info("Requisição para consultar vencimento");
        return faturaService.consultaDataVencimento(cartaoId);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<FaturaResponse> consultarStatusFatura(@RequestBody @Valid FaturaFilterRequest filterRequest, @PathVariable long cartaoId) {
        log.info("Requisição para ver status da fatura");
        return faturaService.consutaPorStatus(filterRequest, cartaoId);
    }

    @GetMapping("/compras")
    @ResponseStatus(HttpStatus.OK)
    public List<CompraEntity> consultarCompras(@PathVariable long cartaoId, @RequestBody @Valid FaturaFilterRequest filterRequest) {
        log.info("Requisição para consultar compras da fatura");
        return faturaService.comprasFaturas(cartaoId, filterRequest);
    }




}
