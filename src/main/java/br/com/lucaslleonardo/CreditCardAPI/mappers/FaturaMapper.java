package br.com.lucaslleonardo.CreditCardAPI.mappers;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FaturaMapper {

    FaturaEntity toEntity(FaturaPostRequest faturaPostRequest);

    FaturaResponse toResponse(FaturaEntity faturaEntity);

    List<FaturaResponse> toResponseList(List<FaturaEntity> faturaEntities);

}
