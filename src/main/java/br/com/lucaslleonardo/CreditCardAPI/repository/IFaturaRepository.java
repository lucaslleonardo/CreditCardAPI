package br.com.lucaslleonardo.CreditCardAPI.repository;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IFaturaRepository extends JpaRepository<FaturaEntity, Long >,
        JpaSpecificationExecutor<FaturaEntity> {
}
