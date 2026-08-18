package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByEmail(String email);

}
