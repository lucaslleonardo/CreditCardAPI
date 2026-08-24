package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IContaRepository extends JpaRepository<ContaEntity, Long> {
    Optional<ContaEntity> findByNumeroConta(String numeroConta);
    List<ContaEntity> findClienteId(Long ClienteId);
    Optional<ContaEntity> findByContaYCliente(long contaId, long clienteId);

}
