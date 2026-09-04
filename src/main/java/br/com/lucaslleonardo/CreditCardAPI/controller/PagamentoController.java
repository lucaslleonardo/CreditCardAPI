package br.com.lucaslleonardo.CreditCardAPI.controller;


import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.PagamentoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.PagamentoResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Pagamento", description = "Operações relacionadas ao pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Realiza pagamento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Fatura nao encontrada"),
            @ApiResponse(responseCode = "400", description = "Pagamento invalido")
    })
    public PagamentoResponse realizarPagamento(PagamentoPostRequest pagamentoPostRequest, long cartaoId){
        log.info("Requisição para realizar pagamento");
        return pagamentoService.pagamentoFatura(pagamentoPostRequest, cartaoId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista pagamentos antigos")
    public List<PagamentoResponse> listarPagamentos(long cartaoId){
        log.info("Requisição para listar pagamentos antigos ");
        return pagamentoService.consultaDePagamentos(cartaoId);
    }

}
