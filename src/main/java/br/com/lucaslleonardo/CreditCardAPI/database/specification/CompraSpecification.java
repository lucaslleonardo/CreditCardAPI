package br.com.lucaslleonardo.CreditCardAPI.database.specification;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompraSpecification {

    public static Specification<CompraEntity> statusCompraSpecification(StatusCompra statusCompra) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status compra"),statusCompra);
    }

    public static Specification<CompraEntity> periodoCompraSpecification(LocalDate dataCompra){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("dataCompra"),dataCompra);
    }

    public static Specification<CompraEntity> compraMaiorQue(BigDecimal valorCompra){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("valorCompra"),valorCompra);
    }

    public static Specification<CompraEntity> compraMenorQue(BigDecimal valorCompra){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("valorCompra"),valorCompra);
    }


}
