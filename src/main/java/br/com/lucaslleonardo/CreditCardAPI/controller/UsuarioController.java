package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse save(@Valid @RequestBody UsuarioPostRequest usuarioPostRequest) {
        return usuarioService.save(usuarioPostRequest);
    }

    @GetMapping("/TodosUsuarios")
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioResponse> findAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResponse findById(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    public void atualizar(@PathVariable Long id, @RequestBody UsuarioPatchRequest usuarioPatchRequest) {
        usuarioService.update(usuarioPatchRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        usuarioService.delete(id);
    }

}
