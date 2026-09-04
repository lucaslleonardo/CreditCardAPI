package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPagamentoRepository extends JpaRepository<PagamentoEntity, Long> {

    List<PagamentoEntity> findByFaturaCartaoIdAndFaturaStatusFatura(long cartaoId, StatusFatura statusFatura);
}
