package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPagamentoRepository extends JpaRepository<PagamentoEntity, Long> {
}
