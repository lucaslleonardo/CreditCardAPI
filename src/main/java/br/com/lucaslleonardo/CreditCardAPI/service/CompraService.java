package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.CompraFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.CompraSpecification;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CompraPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CompraResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.CompraMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICompraRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompraService {

    private final ICompraRepository compraRepository;
    private final ICartaoRepository cartaoRepository;
    private final IFaturaRepository faturaRepository;
    private final CompraMapper compraMapper;

    @Transactional
    public CompraResponse save(CompraPostRequest compraPostRequest) {

        Long cartaoId = compraPostRequest.getCartao().getId();

        log.info("Iniciando cadastro de compra para o cartao de ID {}", cartaoId);

        CartaoEntity cartaoEntity = cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> { log.warn("Cartao de ID {} nao encontrado", cartaoId);
                    return new RuntimeException("Cartao nao encontrado");
                });

        log.info("Cartao de ID {} encontrado", cartaoId);

        if (cartaoEntity.getStatusCartao() != StatusCartao.ATIVO) {
            log.warn("Compra recusada: cartao de ID {} nao esta ativo", cartaoId);
            throw new RuntimeException("Cartao nao ativo");
        }

        log.info("Cartao de ID {} esta ativo", cartaoId);

        ContaEntity conta = cartaoEntity.getConta();

        log.info("Consultando fatura da conta de ID {}", conta.getId());

        FaturaEntity faturaEntity = faturaRepository.findByCartaoContaId(conta.getId())
                .orElseThrow(() -> { log.warn("Fatura nao encontrada para a conta de ID {}", conta.getId());
                    return new RuntimeException("Fatura nao encontrada");
                });

        if (faturaEntity.getStatusFatura() != StatusFatura.ABERTA) {
            log.warn("Compra recusada: fatura da conta de ID {} nao esta aberta", conta.getId());
            throw new RuntimeException("Fatura nao esta aberta");
        }

        log.info("Fatura da conta de ID {} esta aberta", conta.getId());

        CompraEntity compraEntity = compraMapper.toEntity(compraPostRequest);

        log.info("Verificando limite disponivel do cartao de ID {}", cartaoId);

        if (cartaoEntity.getLimiteDisponivel().compareTo(compraPostRequest.getValor()) < 0) {

            log.warn("Compra recusada por limite insuficiente. Cartao: {}, Limite disponivel: {}, Valor da compra: {}",
                    cartaoId,
                    cartaoEntity.getLimiteDisponivel(),
                    compraPostRequest.getValor());

            compraEntity.setStatusCompra(StatusCompra.RECUSADA);

        } else {

            compraEntity.setStatusCompra(StatusCompra.APROVADA);
            faturaEntity.setValor(faturaEntity.getValor().add(compraEntity.getValor()));
            cartaoEntity.setLimiteDisponivel(cartaoEntity.getLimiteDisponivel().subtract(compraEntity.getValor()));

            log.info("Compra aprovada. Cartao: {}, Valor: {}, Novo valor da fatura: {}, Novo limite disponivel: {}",
                    cartaoId,
                    compraEntity.getValor(),
                    faturaEntity.getValor(),
                    cartaoEntity.getLimiteDisponivel());

            faturaRepository.save(faturaEntity);
            cartaoRepository.save(cartaoEntity);
        }

        CompraEntity savedCompra = compraRepository.save(compraEntity);

        log.info("Compra de ID {} cadastrada com status {}",
                savedCompra.getId(),
                savedCompra.getStatusCompra());
        return compraMapper.toResponse(savedCompra);
    }

    public CompraResponse encontrarCompra(Long id) {

        log.info("Consultando compra de ID {}", id);

        CompraEntity compraEntity = compraRepository.findById(id)
                .orElseThrow(() -> {log.warn("Compra de ID {} nao encontrada", id);
                    return new RuntimeException("Compra nao encontrada");
                });

        log.info("Compra de ID {} encontrada com sucesso", id);
        return compraMapper.toResponse(compraEntity);
    }

    public List<CompraResponse> encontrarCompras(CompraFilterRequest filter) {

        log.info("Iniciando consulta de compras com filtros");

        Specification<CompraEntity> specification = Specification.allOf();

        if (filter.getDataCompra() != null) {log.info("Aplicando filtro de data da compra");

            specification = specification.and(
                    CompraSpecification.periodoCompraSpecification(filter.getDataCompra()));
        }

        if (filter.getStatusCompra() != null) {
            log.info("Aplicando filtro de status da compra: {}", filter.getStatusCompra());

            specification = specification.and(
                    CompraSpecification.statusCompraSpecification(filter.getStatusCompra()));
        }

        if (filter.getValor() != null) {
            log.info("Aplicando filtro de valor da compra: {}", filter.getValor());

            specification = specification.and(
                    CompraSpecification.compraMaiorQue(filter.getValor()));
        }

        if (filter.getValor() != null) {
            log.info("Aplicando filtro de valor menor que: {}", filter.getValor());

            specification = specification.and(
                    CompraSpecification.compraMenorQue(filter.getValor()));
        }

        List<CompraEntity> compras = compraRepository.findAll(specification);

        log.info("Foram encontradas {} compras", compras.size());
        return compraMapper.toRequestList(compras);
    }

    @Transactional
    public CompraResponse cancelarCompra(Long id) {

        log.info("Iniciando cancelamento da compra de ID {}", id);

        CompraEntity compraEntity = compraRepository.findById(id)
                .orElseThrow(() -> {log.warn("Compra de ID {} nao encontrada", id);
                    return new RuntimeException("Compra nao encontrada");
                });

        CartaoEntity cartaoEntity = compraEntity.getCartao();

        log.info("Cartao da compra de ID {} encontrado", id);

        ContaEntity contaEntity = cartaoEntity.getConta();

        log.info("Consultando fatura da conta de ID {}", contaEntity.getId());

        FaturaEntity faturaEntity = faturaRepository.findByCartaoContaId(contaEntity.getId())
                .orElseThrow(() -> {log.warn("Fatura nao encontrada para a conta de ID {}", contaEntity.getId());
                    return new RuntimeException("Fatura nao encontrada");
                });

        faturaEntity.setValor(faturaEntity.getValor().subtract(compraEntity.getValor()));

        cartaoEntity.setLimiteDisponivel(cartaoEntity.getLimiteDisponivel().add(compraEntity.getValor()));

        compraEntity.setStatusCompra(StatusCompra.CANCELADA);

        log.info("Compra de ID {} cancelada. Valor: {}, Novo valor da fatura: {}, Novo limite disponivel: {}",
                id,
                compraEntity.getValor(),
                faturaEntity.getValor(),
                cartaoEntity.getLimiteDisponivel());

        faturaRepository.save(faturaEntity);
        cartaoRepository.save(cartaoEntity);
        compraRepository.save(compraEntity);

        log.info("Cancelamento da compra de ID {} concluido com sucesso", id);
        return compraMapper.toResponse(compraEntity);
    }
}