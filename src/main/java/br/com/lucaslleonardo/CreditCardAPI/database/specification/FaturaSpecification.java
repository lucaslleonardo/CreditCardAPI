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


    public static Specification<FaturaEntity> valorMaiorQue(BigDecimal valor){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("valor"), valor);
    }

    public static Specification<FaturaEntity> valorMenorQue(BigDecimal valor){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("valor"), valor);
    }


}
