package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.PagamentoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.PagamentoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    PagamentoEntity toEntity(PagamentoPostRequest pagamentoPostRequest);

    PagamentoResponse toResponse(PagamentoEntity pagamentoEntity);

    List<PagamentoResponse> toResponseList(List<PagamentoEntity> pagamentoEntityList);
}
