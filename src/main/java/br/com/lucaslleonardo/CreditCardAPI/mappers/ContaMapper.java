package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ContaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ContaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    ContaEntity toEntity(ContaPostRequest contaPostRequest);

    ContaResponse toResponse(ContaEntity contaEntity);

    List<ContaResponse> toResponseList(List<ContaEntity> contaEntity);

    void update(ContaPatchRequest contaPatchRequest, @MappingTarget ContaEntity contaEntity);

}
