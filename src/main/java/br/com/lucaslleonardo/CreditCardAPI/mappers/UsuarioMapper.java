package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioEntity toEntity(UsuarioPostRequest usuarioPostRequest);

    UsuarioResponse toResponse(UsuarioEntity usuarioEntity);

    List<UsuarioResponse> toResponseList(List<UsuarioEntity> usuarioEntityList);

    //aqui ele vai substituir o valor do entity com oq foi enviado no patch
    //caso tiver algo vazio ele releva
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(UsuarioPatchRequest usuarioPatchRequest, @MappingTarget UsuarioEntity usuarioEntity);



}
