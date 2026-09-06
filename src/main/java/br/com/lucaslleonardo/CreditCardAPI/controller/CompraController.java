package br.com.lucaslleonardo.CreditCardAPI.controller;


import br.com.lucaslleonardo.CreditCardAPI.repository.specification.CompraFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CompraPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CompraResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.CompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/compras")
@Validated
@RequiredArgsConstructor
@Tag(name="Compras", description = "Operações relacionadas as compras")
public class CompraController {

    public static Logger log = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Adiciona uma compra")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "Cartao não encontrado"),
            @ApiResponse(responseCode = "403",description = "Cartao não esta ativo"),
            @ApiResponse(responseCode = "404", description = "Fatura nao encontrada"),
            @ApiResponse(responseCode = "403", description = "Fatura nao esta aberta")
    })
    public CompraResponse createCompra(@Valid @RequestBody CompraPostRequest compraPostRequest) {
        log.info("requisição para criar compra");
        return compraService.save(compraPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna compra")
    @ApiResponse(responseCode = "404", description = "Compra não encontrada")
    public CompraResponse encontrarCompra(@PathVariable Long id) {
        log.info("requisição para encontrar compra específica");
        return compraService.encontrarCompra(id);
    }

    @GetMapping("/listarTodos")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Retorna lista de compras")
    public List<CompraResponse> listarCompras(CompraFilterRequest compraFilterRequest) {
        log.info("requisição para listar todas compras");
        return compraService.encontrarCompras(compraFilterRequest);
    }

    @PutMapping("/cancelar/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "cancela compra")
    @ApiResponse(responseCode = "404", description = "Compra nao encontrada")
    public CompraResponse cancelarCompra(@PathVariable Long id){
        log.info("requisição para atualizar status da compra");
        return compraService.cancelarCompra(id);
    }


}
