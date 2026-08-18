package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteEntity toEntity(ClientePostRequest clientePostRequest);

    ClienteResponse toResponse(ClienteEntity clienteEntity);

    List<ClienteResponse> toResponseList(List<ClienteEntity> clienteEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE )
    void update(UsuarioPatchRequest usuarioPatchRequest, @MappingTarget UsuarioEntity usuarioEntity);

}
