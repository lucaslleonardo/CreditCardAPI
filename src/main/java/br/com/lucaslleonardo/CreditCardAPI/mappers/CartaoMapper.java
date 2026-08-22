package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CartaoPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CartaoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CartaoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartaoMapper {

    CartaoEntity toEntity(CartaoPostRequest cartaoPostRequest);
    CartaoResponse toResponse(CartaoEntity cartaoEntity);
    List<CartaoResponse> toResponseList(List<CartaoEntity> cartaoEntities);

    void update(CartaoPatchRequest cartaoPatchRequest, @MappingTarget CartaoEntity cartaoEntity);



}
