package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.UsuarioMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;


    //response pq é o tipo de retorno que entrega quando chama
    public UsuarioResponse save(UsuarioPostRequest usuarioPostRequest) {
        if(usuarioRepository.findByEmail(usuarioPostRequest.getEmail()).isPresent()){
            throw new RuntimeException("Email ja utilizado");
        }

        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuarioPostRequest);

        try{
            UsuarioEntity savedUsuario =  usuarioRepository.save(usuarioEntity);
            return usuarioMapper.toResponse(savedUsuario);
        }catch(Exception e){
            throw e;
        }
    }

    public void update(UsuarioPatchRequest usuarioPatchRequest, Long id ) {

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        usuario.setEmail(usuarioPatchRequest.getEmail());
        usuario.setNome(usuarioPatchRequest.getNome());
        usuario.setSenha(usuarioPatchRequest.getSenha());

        try{
            usuarioMapper.update(usuarioPatchRequest ,usuario);
            usuarioRepository.save(usuario);
        }catch(Exception e){
            throw e;
        }
    }

    public List<UsuarioResponse> findAll() {
        List<UsuarioEntity> usuarioEntities = usuarioRepository.findAll();
        return usuarioMapper.toResponseList(usuarioEntities);
    }

    public UsuarioResponse findById(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        return usuarioMapper.toResponse(usuario);
    }

    public void delete(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        usuarioRepository.delete(usuario);
    }



}
