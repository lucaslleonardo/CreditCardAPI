package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ICartaoRepository extends JpaRepository<CartaoEntity, Long> {
    Optional<CartaoEntity> findByNumeroCartao(String numeroCartao);
    List<CartaoEntity> findByContaId(Long contaId);
}
