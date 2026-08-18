package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICartaoRepository extends JpaRepository<CartaoEntity, Long> {
}
