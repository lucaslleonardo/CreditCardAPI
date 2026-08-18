package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContaRepository extends JpaRepository<ContaEntity, Long> {
}
