package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ContaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ContaResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.ContaMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IContaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {

    public static Logger log = LoggerFactory.getLogger(ContaService.class);

    private final ContaMapper contaMapper;
    private final IContaRepository contaRepository;

    public ContaResponse save(ContaPostRequest contaPostRequest) {
        log.info("procura se a conta ja foi criada com o numero {}",contaPostRequest.getNumeroConta());
        if(contaRepository.findByNumeroConta(contaPostRequest.getNumeroConta()).isPresent()) {
            throw new RuntimeException("Conta ja cadastrada com esse numero");
        }

        ContaEntity contaEntity = contaMapper.toEntity(contaPostRequest);
        log.info("add o status ativa");
        contaEntity.setStatusConta(StatusConta.ATIVA);

        try{
            ContaEntity savedContaEntity = contaRepository.save(contaEntity);
            return contaMapper.toResponse(savedContaEntity);
        }catch(Exception e){
            log.error("Erro ao tentar criar conta do Cliente", e);
            throw e;
        }
    }

    public ContaResponse verUmaConta(long id) {
        log.info("procura a conta de id {}",id);
        ContaEntity contaEntity = contaRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Conta nao encontrada"));

        log.info("Retorna conta de id {}",id);
        return contaMapper.toResponse(contaEntity);
    }


    public List<ContaResponse> verContasDeCliente(long clienteId) {

        log.info("retorna todas contas de um cliente {}",clienteId);
        List<ContaEntity> contas = contaRepository.findByClienteId(clienteId);

        return contaMapper.toResponseList(contas);
    }


    public void update(long id, ContaPatchRequest contaPatchRequest) {

        log.info("procura a conta de id {}",id);
        ContaEntity conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada"));

        log.info("atualiza status de conta do id {}",id);
        conta.setStatusConta(contaPatchRequest.getStatus());

        try{
            contaRepository.save(conta);
        }catch(Exception e){
            log.error("Erro ao tentar atualizar conta", e);
            throw e;
        }

    }

    public void delete(long id) {
        log.info("procura a conta de id {}",id);
        ContaEntity conta = contaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta nao encontrada"));
        log.info("remova conta do id {}",id);
        contaRepository.delete(conta);

    }

}
