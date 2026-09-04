package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ClientePatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.ClienteService;
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

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/cliente")
@Tag(name = "Clientes", description = "Operações relacionadas ao cliente")
public class ClienteController {

    public static Logger log = LoggerFactory.getLogger(ClienteController.class);

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra o cliente")
    @ApiResponse(responseCode = "409", description = "Cliente ja cadastrado")
    public ClienteResponse save(@RequestBody @Valid ClientePostRequest clientePostRequest) {
        log.info("requisição para criar cliente");
        return clienteService.save(clientePostRequest);
    }

    @GetMapping("/listaClientes")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "retorna todos clientes")
    public List<ClienteResponse> findAll() {
        log.info("requisição para listar todos clientes");
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "retorna cliente")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado")
    public ClienteResponse findById(@RequestParam @Valid Long id) {
        log.info("requisição para encontrar cliente específico");
        return clienteService.findById(id);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "atualiza dados cliente")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado")
    public void update(@RequestBody @Valid ClientePatchRequest clientePatchRequest, @PathVariable Long id) {
        log.info("requisição para atualizar dados do cliente");
        clienteService.update(clientePatchRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deleta cliente")
    @ApiResponse(responseCode = "404", description = "Cliente nao encontrado")
    public void deleteById(@PathVariable Long id) {
        log.info("requisição para deletar cliente");
        clienteService.delete(id);
    }
}
