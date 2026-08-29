package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CompraPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CompraPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CompraResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompraMapper {

    CompraEntity toEntity(CompraPostRequest compraPostRequest);

    CompraResponse toResponse(CompraEntity compraEntity);

    List<CompraResponse> toRequestList(List<CompraEntity> compraEntities);

    void update (CompraPatchRequest compraPatchRequest, @MappingTarget CompraEntity compraEntity);

}
