package br.com.lucaslleonardo.CreditCardAPI.controller;


import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.CompraFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CompraPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CompraResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.CompraService;
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
public class CompraController {

    public static Logger log = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompraResponse createCompra(@Valid @RequestBody CompraPostRequest compraPostRequest) {
        log.info("requisição para criar compra");
        return compraService.save(compraPostRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompraResponse encontrarCompra(@PathVariable Long id) {
        log.info("requisição para encontrar compra específica");
        return compraService.encontrarCompra(id);
    }

    @GetMapping("/listarTodos")
    @ResponseStatus(HttpStatus.OK)
    public List<CompraResponse> listarCompras(CompraFilterRequest compraFilterRequest) {
        log.info("requisição para listar todas compras");
        return compraService.encontrarCompras(compraFilterRequest);
    }

    @PutMapping("/cancelar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CompraResponse cancelarCompra(@PathVariable Long id){
        log.info("requisição para atualizar status da compra");
        return compraService.cancelarCompra(id);
    }


}
