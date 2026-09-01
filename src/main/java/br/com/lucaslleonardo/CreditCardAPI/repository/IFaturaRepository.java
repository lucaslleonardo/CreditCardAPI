package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IFaturaRepository extends JpaRepository<FaturaEntity, Long >, JpaSpecificationExecutor<FaturaEntity> {

    Optional <FaturaEntity> findByCartaoContaId(Long contaId);
    List<FaturaEntity> findByCartaoId (Long cartaoId);
    Optional<FaturaEntity> findByCartaoIdAndStatusFatura(Long cartaoId, StatusFatura statusFatura);


}
