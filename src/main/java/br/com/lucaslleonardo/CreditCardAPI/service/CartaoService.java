package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CartaoPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CartaoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CartaoResponse;
import br.com.lucaslleonardo.CreditCardAPI.exception.CartaoJaExistenteException;
import br.com.lucaslleonardo.CreditCardAPI.exception.CartaoNaoEncontradoException;
import br.com.lucaslleonardo.CreditCardAPI.exception.ClienteNaoEncontradoException;
import br.com.lucaslleonardo.CreditCardAPI.exception.ContaNaoEncontradaException;
import br.com.lucaslleonardo.CreditCardAPI.mappers.CartaoMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IClienteRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IContaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoService {

    private final CartaoMapper cartaoMapper;
    private final ICartaoRepository cartaoRepository;
    private final IClienteRepository clienteRepository;
    private final IContaRepository contaRepository;

    public static Logger log = LoggerFactory.getLogger(CartaoService.class);

    public CartaoResponse save(CartaoPostRequest cartaoPostRequest) {

        log.info("Iniciando cadastro do cartao de numero {}", cartaoPostRequest.getNumeroCartao());
        if (cartaoRepository.findByNumeroCartao(cartaoPostRequest.getNumeroCartao()).isPresent()) {

            log.warn("Cartao de numero {} ja esta cadastrado", cartaoPostRequest.getNumeroCartao());
            throw new CartaoJaExistenteException("Cartao ja cadastrado");
        }

        CartaoEntity cartaoEntity = cartaoMapper.toEntity(cartaoPostRequest);

        log.info("Definindo status do cartao como ATIVO");
        cartaoEntity.setStatusCartao(StatusCartao.ATIVO);

        try {
            CartaoEntity savedCartao = cartaoRepository.save(cartaoEntity);
            log.info("Cartao cadastrado com sucesso. ID: {}", savedCartao.getId());
            return cartaoMapper.toResponse(savedCartao);

        } catch (Exception e) {
            log.error("Erro ao salvar o cartao de numero {}", cartaoPostRequest.getNumeroCartao(), e);
            throw e;
        }
    }


    public CartaoResponse consultaCartao(long id) {

        log.info("Consultando cartao de ID {}", id);
        CartaoEntity cartaoEntity = cartaoRepository.findById(id)
                .orElseThrow(() -> {log.warn("Cartao de ID {} nao encontrado", id);

                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        log.info("Cartao de ID {} encontrado com sucesso", id);
        return cartaoMapper.toResponse(cartaoEntity);
    }


    public List<CartaoResponse> cartoesPorConta(long contaId, long clienteId) {


        log.info("Verificando existencia do cliente de ID {}", clienteId);

        clienteRepository.findById(clienteId)
                .orElseThrow(() -> {log.warn("Cliente de ID {} nao encontrado", clienteId);
                    return new ClienteNaoEncontradoException("Cliente nao encontrado");
                });

        log.info("Verificando se a conta {} pertence ao cliente {}", contaId, clienteId);

        contaRepository.findByIdAndClienteId(contaId, clienteId)
                .orElseThrow(() -> { log.warn("Conta {} nao encontrada para o cliente {}", contaId, clienteId);
                    return new ContaNaoEncontradaException("Conta nao encontrada");
                });

        List<CartaoEntity> cartaoEntities = cartaoRepository.findByContaId(contaId);

        log.info("Foram encontrados {} cartoes para a conta {}", cartaoEntities.size(), contaId);

        return cartaoMapper.toResponseList(cartaoEntities);
    }


    public void update(CartaoPatchRequest cartaoPatchRequest, long id) {

        log.info("Iniciando atualizacao do cartao de ID {}", id);
        CartaoEntity cartaoEntity = cartaoRepository.findById(id)
                .orElseThrow(() -> { log.warn("Cartao de ID {} nao encontrado para atualizacao", id);
                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        log.info("Alterando status do cartao de ID {} para {}", id, cartaoPatchRequest.getStatusCartao());

        cartaoEntity.setStatusCartao(cartaoPatchRequest.getStatusCartao());

        try {
            cartaoRepository.save(cartaoEntity);
            log.info("Cartao de ID {} atualizado com sucesso", id);
        } catch (Exception e) {
            log.error("Erro ao atualizar o cartao de ID {}", id, e);
            throw e;
        }
    }


    public void delete(long id) {

        log.info("Iniciando exclusao do cartao de ID {}", id);
        if (cartaoRepository.findById(id).isEmpty()) {
            log.warn("Tentativa de excluir cartao de ID {} que nao existe", id);
            throw new CartaoNaoEncontradoException("Cartao nao encontrado");
        }

        try {
            cartaoRepository.deleteById(id);
            log.info("Cartao de ID {} excluido com sucesso", id);
        } catch (Exception e) {
            log.error("Erro ao excluir o cartao de ID {}", id, e);
            throw e;
        }
    }


    public BigDecimal consultaLimite(long id) {

        log.info("Consultando limite total do cartao de ID {}", id);
        CartaoEntity cartaoEntity = cartaoRepository.findById(id)
                .orElseThrow(() -> { log.warn("Cartao de ID {} nao encontrado", id);
                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        log.info("Limite total do cartao de ID {}: {}", id, cartaoEntity.getLimite());
        return cartaoEntity.getLimite();
    }


    public BigDecimal consultaLimiteDisponivel(long id) {

        log.info("Consultando limite disponivel do cartao de ID {}", id);
        CartaoEntity cartaoEntity = cartaoRepository.findById(id)
                .orElseThrow(() -> {log.warn("Cartao de ID {} nao encontrado", id);
                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        log.info("Limite disponivel do cartao de ID {}: {}", id, cartaoEntity.getLimiteDisponivel());
        return cartaoEntity.getLimiteDisponivel();
    }

}