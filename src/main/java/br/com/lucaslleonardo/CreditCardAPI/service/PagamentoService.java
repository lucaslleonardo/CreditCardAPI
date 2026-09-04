package br.com.lucaslleonardo.CreditCardAPI.service;


import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.PagamentoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.PagamentoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.PagamentoResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.PagamentoMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IPagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PagamentoService {

    private final PagamentoMapper pagamentoMapper;
    private final IPagamentoRepository pagamentoRepository;
    private final IFaturaRepository faturaRepository;
    private final ICartaoRepository cartaoRepository;

    public PagamentoResponse pagamentoFatura(PagamentoPostRequest pagamentoPostRequest, long cartaoId) {

        log.info("Iniciando pagamento da fatura do cartao de ID {}", cartaoId);

        FaturaEntity faturaEntity = faturaRepository.findByCartaoIdAndStatusFaturaIn(cartaoId, List.of(StatusFatura.ABERTA, StatusFatura.ATRASADA))
                .orElseThrow(() -> {
                    log.warn("Nao foi encontrada fatura para pagamento no cartao de ID {}", cartaoId);
                    return new RuntimeException("Não foi encontrada fatura para pagamento");
                });

        log.info("Fatura de ID {} encontrada com status {}", faturaEntity.getId(), faturaEntity.getStatusFatura());


        if (LocalDate.now().isAfter(faturaEntity.getDataVencimento()) && !faturaEntity.isJurosAplicado()) {
            log.info("Fatura de ID {} esta vencida. Aplicando juros de 15%", faturaEntity.getId());

            BigDecimal juros = faturaEntity.getValor()
                    .multiply(BigDecimal.valueOf(0.15));
            faturaEntity.setValor(faturaEntity.getValor().add(juros));
            faturaEntity.setStatusFatura(StatusFatura.ATRASADA);
            faturaEntity.setJurosAplicado(true);

            log.info("Juros aplicados. Novo valor da fatura de ID {}: {}", faturaEntity.getId(), faturaEntity.getValor());
        }

        BigDecimal valorFatura = faturaEntity.getValor();

        log.info("Valor atual da fatura de ID {}: {}", faturaEntity.getId(), valorFatura);

        PagamentoEntity pagamentoEntity = pagamentoMapper.toEntity(pagamentoPostRequest);

        log.info("Valor do pagamento: {}", pagamentoEntity.getValor());

        if (valorFatura.compareTo(pagamentoEntity.getValor()) < 0) {
            log.warn("Pagamento recusado. Valor do pagamento {} e maior que o valor da fatura {}", pagamentoEntity.getValor(), valorFatura);
            throw new RuntimeException("Pagamento de valor maior que fatura");
        }

        pagamentoEntity.setFatura(faturaEntity);

        BigDecimal valorRestante = valorFatura.subtract(pagamentoEntity.getValor());

        log.info("Valor restante da fatura de ID {} apos pagamento: {}", faturaEntity.getId(), valorRestante);

        faturaEntity.setValor(valorRestante);

        if (valorRestante.compareTo(BigDecimal.ZERO) == 0) {
            log.info("Fatura de ID {} foi totalmente paga", faturaEntity.getId());

            faturaEntity.setStatusFatura(StatusFatura.PAGA);
            faturaRepository.save(faturaEntity);
        }

        CartaoEntity cartaoEntity = faturaEntity.getCartao();

        log.info("Liberando limite do cartao de ID {} no valor de {}", cartaoId, pagamentoEntity.getValor());

        cartaoEntity.setLimiteDisponivel(cartaoEntity.getLimiteDisponivel().add(pagamentoEntity.getValor()));

        log.info("Novo limite disponivel do cartao de ID {}: {}", cartaoId, cartaoEntity.getLimiteDisponivel());

        faturaRepository.save(faturaEntity);
        cartaoRepository.save(cartaoEntity);

        PagamentoEntity savedPagamento = pagamentoRepository.save(pagamentoEntity);

        log.info("Pagamento de ID {} realizado com sucesso para a fatura de ID {}", savedPagamento.getId(), faturaEntity.getId());

        return pagamentoMapper.toResponse(savedPagamento);

    }


    public List<PagamentoResponse> consultaDePagamentos(long cartaoId) {

        log.info("Consultando pagamentos do cartao de ID {}", cartaoId);

        List<PagamentoEntity> listaPagamentos = pagamentoRepository.findByFaturaCartaoIdAndFaturaStatusFatura(cartaoId, StatusFatura.PAGA);

        log.info("Foram encontrados {} pagamentos para o cartao de ID {}", listaPagamentos.size(), cartaoId);

        return pagamentoMapper.toResponseList(listaPagamentos);

    }
}


