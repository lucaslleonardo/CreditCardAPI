package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.repository.specification.CompraSpecification;
import br.com.lucaslleonardo.CreditCardAPI.repository.specification.FaturaFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.repository.specification.FaturaSpecification;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import br.com.lucaslleonardo.CreditCardAPI.exception.CartaoNaoEncontradoException;
import br.com.lucaslleonardo.CreditCardAPI.exception.FaturaJaCadastradaException;
import br.com.lucaslleonardo.CreditCardAPI.exception.FaturaNaoEncontradaException;
import br.com.lucaslleonardo.CreditCardAPI.mappers.FaturaMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICompraRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class FaturaService {

    private final ICartaoRepository cartaoRepository;
    private final IFaturaRepository faturaRepository;
    private final ICompraRepository compraRepository;
    private final FaturaMapper faturaMapper;

    public FaturaResponse cadastrarInfosFatura(FaturaPostRequest faturaPostRequest) {

        Long cartaoId = faturaPostRequest.getCartao().getId();

        log.info("Iniciando cadastro da fatura para o cartao de ID {}", cartaoId);

        if (faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA).isPresent()) {
            log.warn("Ja existe uma fatura para o cartao de ID {}", cartaoId);
            throw new FaturaJaCadastradaException("Fatura ja cadastrada");
        }

        FaturaEntity faturaEntity = faturaMapper.toEntity(faturaPostRequest);

        LocalDate dataFechamento = faturaPostRequest.getDataFechamento();
        LocalDate dataVencimento = dataFechamento.plusDays(7);

        faturaEntity.setDataFechamento(dataFechamento);
        faturaEntity.setDataVencimento(dataVencimento);
        faturaEntity.setStatusFatura(StatusFatura.ABERTA);

        try {
            FaturaEntity savedFatura = faturaRepository.save(faturaEntity);
            log.info( "Fatura cadastrada com sucesso. ID: {}", savedFatura.getId());

            return faturaMapper.toResponse(savedFatura);
        } catch (Exception e) {

            log.error("Erro ao cadastrar fatura para o cartao de ID {}", cartaoId, e);
            throw e;
        }
    }

    public FaturaResponse consultaFatura(long cartaoId) {

        log.info("Consultando fatura do cartao de ID {}", cartaoId);

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA)
                .orElseThrow(() -> { log.warn("Fatura nao encontrada para o cartao de ID {}", cartaoId);

                    return new FaturaNaoEncontradaException("Fatura nao encontrada");
                });

        log.info("Fatura de ID {} encontrada com sucesso", faturaEntity.getId());

        return faturaMapper.toResponse(faturaEntity);
    }

    public List<FaturaResponse> consultaFaturas(long cartaoId) {

        log.info("Consultando todas as faturas do cartao de ID {}", cartaoId);

        cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> {log.warn("Cartao de ID {} nao encontrado", cartaoId);

                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        List<FaturaEntity> faturas = faturaRepository.findByCartaoId(cartaoId);

        log.info("Foram encontradas {} faturas para o cartao de ID {}", faturas.size(), cartaoId);

        return faturaMapper.toResponseList(faturas);
    }

    public void fecharFatura(long cartaoId) {

        log.info("Iniciando fechamento da fatura do cartao de ID {}", cartaoId);

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA)
                .orElseThrow(() -> {
                    log.warn("Fatura nao encontrada para o cartao de ID {}", cartaoId);

                    return new FaturaNaoEncontradaException("Fatura inexistente");
                });

        if (faturaEntity.getDataFechamento().isEqual(LocalDate.now())) {

            log.info("Data de fechamento atingida para a fatura de ID {}", faturaEntity.getId());

            faturaEntity.setStatusFatura(StatusFatura.FECHADA);

            faturaRepository.save(faturaEntity);

            log.info("Fatura de ID {} fechada com sucesso", faturaEntity.getId());

        } else {

            log.info("Fatura de ID {} ainda nao atingiu a data de fechamento", faturaEntity.getId());
        }
    }

    public BigDecimal consultaValorFatura(long cartaoId) {

        log.info("Consultando valor da fatura do cartao de ID {}", cartaoId);

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA)
                .orElseThrow(() -> {
                    log.warn("Fatura nao encontrada para o cartao de ID {}", cartaoId);

                    return new FaturaNaoEncontradaException("Fatura inexistente");
                });

        log.info("Valor da fatura de ID {}: {}", faturaEntity.getId(), faturaEntity.getValor());

        return faturaEntity.getValor();
    }

    public LocalDate consultaDataVencimento(long cartaoId) {

        log.info("Consultando data de vencimento da fatura do cartao de ID {}", cartaoId
        );

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA)
                .orElseThrow(() -> {
                    log.warn("Fatura nao encontrada para o cartao de ID {}", cartaoId);

                    return new FaturaNaoEncontradaException("Fatura inexistente");
                });

        log.info("Data de vencimento da fatura de ID {}: {}", faturaEntity.getId(), faturaEntity.getDataVencimento());

        return faturaEntity.getDataVencimento();
    }

    public List<FaturaResponse> consutaPorStatus(FaturaFilterRequest filter, long cartaoId
    ) {

        log.info("Consultando faturas por status para o cartao de ID {}", cartaoId
        );

        cartaoRepository.findById(cartaoId)
                .orElseThrow(() -> {log.warn("Cartao de ID {} nao encontrado", cartaoId);
                    return new CartaoNaoEncontradoException("Cartao nao encontrado");
                });

        Specification<FaturaEntity> specification = Specification.allOf();

        if (filter.getStatusFatura() != null) {

            log.info("Aplicando filtro de status {}", filter.getStatusFatura());
            specification = specification.and(FaturaSpecification.statusFatura(filter.getStatusFatura()));
        }

        List<FaturaEntity> faturas = faturaRepository.findAll(specification)
                .stream()
                .filter(fatura -> fatura.getCartao().getId().equals(cartaoId))
                .toList();

        log.info("Foram encontradas {} faturas para o cartao de ID {}", faturas.size(), cartaoId);

        return faturaMapper.toResponseList(faturas);
    }

    public List<CompraEntity> comprasFaturas(long cartaoId, FaturaFilterRequest filter) {

        log.info("Consultando compras da fatura do cartao de ID {}", cartaoId
        );

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFatura(cartaoId,StatusFatura.ABERTA)
                .orElseThrow(() -> {
                    log.warn("Fatura nao encontrada para o cartao de ID {}", cartaoId);

                    return new FaturaNaoEncontradaException("Fatura inexistente");
                });

        Specification<CompraEntity> specification = Specification.allOf();

        if (filter.getValor() != null) {

            log.info("Aplicando filtro de compras com valor maior que {}", filter.getValor());

            specification = specification.and(CompraSpecification.compraMaiorQue(filter.getValor()));
        }

        List<CompraEntity> compras = compraRepository.findAll(specification)
                .stream()
                .filter(compra -> compra.getFatura().getId().equals(faturaEntity.getId()))
                .toList();

        log.info("Foram encontradas {} compras na fatura de ID {}", compras.size(), faturaEntity.getId());

        return compras;
    }
}

