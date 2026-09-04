package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.FaturaFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.FaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Fatura", description = "Operações relacionadas a fatura")
public class FaturaController {

    private final FaturaService faturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra fatura")
    @ApiResponse(responseCode = "409", description = "Fatura ja cadastrada")
    public FaturaResponse cadastrarFatura(@RequestBody @Valid FaturaPostRequest  faturaPostRequest) {
        log.info("Requisição para cadastrar infos da fatura");
        return faturaService.cadastrarInfosFatura(faturaPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta uma fatura")
    @ApiResponse(responseCode = "404", description = "Fatura nao encontrada")
    public FaturaResponse consultarFatura(@PathVariable long cartaoId) {
        log.info("Requisição para consultar uma fatura");
        return faturaService.consultaFatura(cartaoId);
    }

    @GetMapping("/{id}/listarTodas")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista todas as faturas")
    @ApiResponse(responseCode = "404", description = "Cartao nao encontrado")
    public List<FaturaResponse> consultarTodasFaturas(@PathVariable long cartaoId) {
        log.info("Requisição para consultar todas as faturas");
        return faturaService.consultaFaturas(cartaoId);
    }

    @PostMapping("/{id}/fechar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Fecha uma fatura")
    @ApiResponse(responseCode = "404", description = "Fatura nao encontrada")
    public void fecharFatura(@PathVariable long cartaoId) {
        log.info("Requisição para fechar uma fatura");
        faturaService.fecharFatura(cartaoId);
    }


    @GetMapping("/valor")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta o valor da fatura")
    @ApiResponse(responseCode = "404", description = "Fatura nao encontrada")
    public BigDecimal consultarValorFatura(@PathVariable long cartaoId) {
        log.info("Requisição para consultar valor da fatura");
        return faturaService.consultaValorFatura(cartaoId);
    }

    @GetMapping("/Vencimento")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consulta data de vencimento da fatura")
    @ApiResponse(responseCode = "404", description = "Fatura nao encontrada")
    public LocalDate consultarVencimento(@PathVariable long cartaoId) {
       log.info("Requisição para consultar vencimento");
        return faturaService.consultaDataVencimento(cartaoId);
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Consultando faturas por status")
    @ApiResponse(responseCode = "404", description = "Cartao encontrado")
    public List<FaturaResponse> consultarStatusFatura(@RequestBody @Valid FaturaFilterRequest filterRequest, @PathVariable long cartaoId) {
        log.info("Requisição para ver status da fatura");
        return faturaService.consutaPorStatus(filterRequest, cartaoId);
    }

    @GetMapping("/compras")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista de compras na fatura")
    @ApiResponse(responseCode = "404", description = "Fatura nao encontrada")
    public List<CompraEntity> consultarCompras(@PathVariable long cartaoId, @RequestBody @Valid FaturaFilterRequest filterRequest) {
        log.info("Requisição para consultar compras da fatura");
        return faturaService.comprasFaturas(cartaoId, filterRequest);
    }




}
