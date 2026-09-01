package br.com.lucaslleonardo.CreditCardAPI.database.specification;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class FaturaSpecification {

    public static Specification<FaturaEntity> statusFatura(StatusFatura statusFatura){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("statusFatura"),statusFatura);
    }



}
