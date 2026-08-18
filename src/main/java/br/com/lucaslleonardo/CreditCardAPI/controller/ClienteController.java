package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ClientePatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse save(@RequestBody @Valid ClientePostRequest clientePostRequest) {
        return clienteService.save(clientePostRequest);
    }

    @GetMapping("/listaClientes")
    @ResponseStatus(HttpStatus.OK)
    public List<ClienteResponse> findAll() {
        return clienteService.findAll();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClienteResponse findById(@RequestParam Long id) {
        return clienteService.findById(id);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid ClientePatchRequest clientePatchRequest, @PathVariable Long id) {
        clienteService.update(clientePatchRequest, id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@RequestParam Long id) {
        clienteService.delete(id);
    }
}
