package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByEmail(String email);
}
