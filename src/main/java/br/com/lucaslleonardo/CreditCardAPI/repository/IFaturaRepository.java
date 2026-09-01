package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IFaturaRepository extends JpaRepository<FaturaEntity, Long >, JpaSpecificationExecutor<FaturaEntity> {

    Optional<FaturaEntity> findById(long id);
    Optional <FaturaEntity> findByContaId(Long ContaId);
    List<FaturaEntity> findByCartaoId (Long cartaoId);
    Optional<FaturaEntity> findByCartaId (Long cartaoId);

}
