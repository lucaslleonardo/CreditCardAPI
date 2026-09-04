package br.com.lucaslleonardo.CreditCardAPI.controller;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import br.com.lucaslleonardo.CreditCardAPI.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario", description = "Operações relacionadas ao usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public static Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra o usuario")
    @ApiResponse(responseCode = "409", description = "Usuario ja cadastrado no sistema")
    public UsuarioResponse save(@Valid @RequestBody UsuarioPostRequest usuarioPostRequest) {
        log.info("requisição para criar usuario");
        return usuarioService.save(usuarioPostRequest);
    }

    @GetMapping("/TodosUsuarios")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Lista todos os usuarios")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    public List<UsuarioResponse> findAll() {
        log.info("requisição para listar todos usuarios");
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Busca um usuario")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    public UsuarioResponse findById(@PathVariable Long id) {
        log.info("requisição para encontrar cliente específico");
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}/atualizar")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Atualiza dados do usuario")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    public void atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioPatchRequest usuarioPatchRequest) {
        log.info("requisição para atualizar informações do usuario");
        usuarioService.update(usuarioPatchRequest, id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "")
    @ApiResponse(responseCode = "404", description = "Usuario nao encontrado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("requisição para deletar usuario");
        usuarioService.delete(id);
    }

}
