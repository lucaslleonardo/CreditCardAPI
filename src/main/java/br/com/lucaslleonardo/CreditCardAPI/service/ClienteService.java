package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ClientePatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import br.com.lucaslleonardo.CreditCardAPI.exception.CartaoNaoEncontradoException;
import br.com.lucaslleonardo.CreditCardAPI.exception.ClienteJaCadastradoException;
import br.com.lucaslleonardo.CreditCardAPI.exception.ClienteNaoEncontradoException;
import br.com.lucaslleonardo.CreditCardAPI.mappers.ClienteMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteMapper clienteMapper;
    private final IClienteRepository clienteRepository;

    public static Logger log = LoggerFactory.getLogger(ClienteService.class);

    public ClienteResponse save(ClientePostRequest clientePostRequest) {

        log.info("procura se o usuario de email {} ja esta registrado", clientePostRequest.getEmail());
        if (clienteRepository.findByEmail(clientePostRequest.getEmail()).isPresent()) {
            throw new ClienteJaCadastradoException("Cliente ja cadastrado com esse email");
        }

        ClienteEntity clienteEntity = clienteMapper.toEntity(clientePostRequest);
        log.info("Adiciona o status 'ATIVO' no cliente");
        clienteEntity.setStatus(StatusCliente.ATIVO);

        try{
            ClienteEntity savedCliente = clienteRepository.save(clienteEntity);
            return clienteMapper.toResponse(savedCliente);
        }catch (Exception e){
            log.error("Erro ao tentar cadastrar Cliente", e);
            throw e;
        }
    }

    public void update(ClientePatchRequest clientePatchRequest, Long id) {

        log.info("Atualizar informações sobre usuario de id: {}",id);
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> { log.warn("Nao encontrou cliente no sistema");
                    return new ClienteNaoEncontradoException("Cliente nao encontrado");
                });

        log.info("Pega as novas informações do cliente");
        cliente.setEmail(clientePatchRequest.getEmail());
        cliente.setNome(clientePatchRequest.getNome());
        cliente.setStatus(clientePatchRequest.getStatus());

        try {
            clienteRepository.save(cliente);
        } catch (Exception e) {
            log.error("Erro ao tentar atualizar Cliente", e);
            throw e;
        }
    }

    public List<ClienteResponse> findAll() {
        log.info("Busca todos os clientes");
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();

        log.info("retorna todos os clientes em uma lista. Lista tem {} clientes", clienteEntities.size());
        return  clienteMapper.toResponseList(clienteEntities);
    }

    public ClienteResponse findById(Long id) {
        log.info("Busca cliente por id: {} no sistema",id );
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> {log.error("Nao encontrou cliente no sistema");
                    return new ClienteNaoEncontradoException("Cliente nao encontrado");
                });

        return clienteMapper.toResponse(cliente);
    }

    public void delete(Long id) {
        log.info("Removendo cliente por id: {} no sistema",id);
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> { log.warn("Nao encontrou cliente no sistema");
                    return new ClienteNaoEncontradoException("Cliente nao encontrado");
                });
        clienteRepository.delete(cliente);
    }
}
