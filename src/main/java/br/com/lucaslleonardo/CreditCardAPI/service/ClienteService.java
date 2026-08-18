package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ClientePatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.ClienteMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteMapper clienteMapper;
    private final IClienteRepository clienteRepository;

    public ClienteResponse save(ClientePostRequest clientePostRequest) {
        if (clienteRepository.findByEmail(clientePostRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Cliente ja cadastrado com esse email");
        }

        ClienteEntity clienteEntity = clienteMapper.toEntity(clientePostRequest);
        clienteEntity.setStatus(StatusCliente.ATIVO);

        try{
            ClienteEntity savedCliente = clienteRepository.save(clienteEntity);
            return clienteMapper.toResponse(savedCliente);
        }catch (Exception e){
            throw e;
        }
    }

    public void update(ClientePatchRequest clientePatchRequest, Long id) {

        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        cliente.setEmail(clientePatchRequest.getEmail());
        cliente.setNome(clientePatchRequest.getNome());
        cliente.setStatus(clientePatchRequest.getStatus());

        try {
            clienteRepository.save(cliente);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<ClienteResponse> findAll() {
        List<ClienteEntity> clienteEntities = clienteRepository.findAll();
        return  clienteMapper.toResponseList(clienteEntities);
    }

    public ClienteResponse findById(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        return clienteMapper.toResponse(cliente);
    }

    public void delete(Long id) {
        ClienteEntity cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));
        clienteRepository.delete(cliente);
    }
}
