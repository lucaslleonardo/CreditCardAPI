package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.UsuarioMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;


    public static Logger log = LoggerFactory.getLogger(UsuarioService.class);

    //response pq é o tipo de retorno que entrega quando chama
    public UsuarioResponse save(UsuarioPostRequest usuarioPostRequest) {

       log.info("Verifica se o email ja foi usado em outra conta");
        if(usuarioRepository.findByEmail(usuarioPostRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email ja utilizado");
        }

        log.info("Cadastrando o usuario com email {}",usuarioPostRequest.getEmail());
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioPostRequest);

        usuarioEntity.setSenha(passwordEncoder.encode(usuarioPostRequest.getSenha()));

        try{
            UsuarioEntity savedUsuario =  usuarioRepository.save(usuarioEntity);
            return usuarioMapper.toResponse(savedUsuario);
        }catch(Exception e){
            log.error("Erro ao cadstrar usuario");
            throw e;
        }
    }

    public void update(UsuarioPatchRequest usuarioPatchRequest, Long id ) {

        log.info("Procura o usuario no banco");
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> { log.warn("Erro ao procurar o usuario no banco");
                    return new RuntimeException("Usuario nao encontrado");
                });

        log.info("Pega as novas informações");
        usuario.setEmail(usuarioPatchRequest.getEmail());
        usuario.setNome(usuarioPatchRequest.getNome());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        try{
            usuarioMapper.update(usuarioPatchRequest ,usuario);
            usuarioRepository.save(usuario);
        }catch(Exception e){
            log.error("Erro ao atualizar usuario");
            throw e;
        }
    }

    public List<UsuarioResponse> findAll() {
        log.info("Pega todos os usuarios registrados atravez do email");
        List<UsuarioEntity> usuarioEntities = usuarioRepository.findAll();
        return usuarioMapper.toResponseList(usuarioEntities);
    }

    public UsuarioResponse findById(Long id) {
        log.info("Verifica se o usario esta cadastrado no banco");
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->  { log.warn("Usuario nao encontrado");
                    return new RuntimeException("Usuario nao encontrado");
                });

        log.info("retorna o usuario {}",usuario);
        return usuarioMapper.toResponse(usuario);
    }

    public void delete(Long id) {
        log.info("Verifica se o usario esta cadastrado no banco");
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> { log.warn("Usuario nao encontrado");
                    return new RuntimeException("Usuario nao encontrado");
                });

        log.info("Deleta o usuario {}",usuario);
        usuarioRepository.delete(usuario);
    }



}
