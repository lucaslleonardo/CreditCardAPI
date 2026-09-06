package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.repository.specification.FaturaFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.FaturaMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICompraRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FaturaServiceTest {

    @Mock
    private ICartaoRepository cartaoRepository;

    @Mock
    private IFaturaRepository faturaRepository;

    @Mock
    private ICompraRepository compraRepository;

    @Mock
    private FaturaMapper faturaMapper;

    @InjectMocks
    private FaturaService faturaService;

    private CartaoEntity cartao;
    private FaturaEntity fatura;
    private FaturaResponse faturaResponse;
    private FaturaPostRequest faturaPostRequest;

    @BeforeEach
    void setUp() {

        cartao = CartaoEntity.builder()
                .id(1L)
                .build();

        fatura = FaturaEntity.builder()
                .id(1L)
                .valor(BigDecimal.ZERO)
                .dataFechamento(LocalDate.now())
                .dataVencimento(LocalDate.now().plusDays(7))
                .statusFatura(StatusFatura.ABERTA)
                .cartao(cartao)
                .build();

        faturaResponse = FaturaResponse.builder()
                .id(1L)
                .valor(BigDecimal.ZERO)
                .dataFechamento(LocalDate.now())
                .dataVencimento(LocalDate.now().plusDays(7))
                .statusFatura(StatusFatura.ABERTA)
                .build();

        faturaPostRequest = FaturaPostRequest.builder()
                .cartao(cartao)
                .dataFechamento(LocalDate.now().plusDays(10))
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar fatura com sucesso")
    void deveCadastrarFaturaComSucesso() {

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.empty());

        when(faturaMapper.toEntity(faturaPostRequest))
                .thenReturn(fatura);

        when(faturaRepository.save(fatura))
                .thenReturn(fatura);

        when(faturaMapper.toResponse(fatura))
                .thenReturn(faturaResponse);

        FaturaResponse resultado =
                faturaService.cadastrarInfosFatura(faturaPostRequest);

        assertNotNull(resultado);
        assertEquals(faturaResponse, resultado);

        assertEquals(faturaPostRequest.getDataFechamento(),
                fatura.getDataFechamento());

        assertEquals(
                faturaPostRequest.getDataFechamento().plusDays(7),
                fatura.getDataVencimento()
        );

        assertEquals(StatusFatura.ABERTA, fatura.getStatusFatura());

        verify(faturaRepository).findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        );

        verify(faturaRepository).save(fatura);
        verify(faturaMapper).toResponse(fatura);
    }

    @Test
    @DisplayName("Deve lançar exceção ao cadastrar fatura quando já existir uma fatura aberta")
    void deveLancarExcecaoQuandoJaExistirFaturaAberta() {

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.cadastrarInfosFatura(faturaPostRequest)
        );

        assertEquals("Fatura ja cadastrada", exception.getMessage());

        verify(faturaRepository, never()).save(any(FaturaEntity.class));
        verify(faturaMapper, never()).toEntity(any(FaturaPostRequest.class));
    }

    @Test
    @DisplayName("Deve consultar a fatura aberta do cartão")
    void deveConsultarFaturaAberta() {

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        when(faturaMapper.toResponse(fatura))
                .thenReturn(faturaResponse);

        FaturaResponse resultado =
                faturaService.consultaFatura(cartao.getId());

        assertNotNull(resultado);
        assertEquals(faturaResponse, resultado);

        verify(faturaRepository).findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        );

        verify(faturaMapper).toResponse(fatura);
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar fatura inexistente")
    void deveLancarExcecaoAoConsultarFaturaInexistente() {

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.consultaFatura(cartao.getId())
        );

        assertEquals("Fatura nao encontrada", exception.getMessage());

        verify(faturaMapper, never()).toResponse(any(FaturaEntity.class));
    }

    @Test
    @DisplayName("Deve consultar todas as faturas do cartão")
    void deveConsultarTodasAsFaturas() {

        FaturaEntity fatura2 = FaturaEntity.builder()
                .id(2L)
                .cartao(cartao)
                .valor(BigDecimal.valueOf(200))
                .statusFatura(StatusFatura.FECHADA)
                .build();

        List<FaturaEntity> faturas = List.of(fatura, fatura2);

        List<FaturaResponse> responses = List.of(faturaResponse);

        when(cartaoRepository.findById(cartao.getId()))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoId(cartao.getId()))
                .thenReturn(faturas);

        when(faturaMapper.toResponseList(faturas))
                .thenReturn(responses);

        List<FaturaResponse> resultado =
                faturaService.consultaFaturas(cartao.getId());

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(cartaoRepository).findById(cartao.getId());
        verify(faturaRepository).findByCartaoId(cartao.getId());
        verify(faturaMapper).toResponseList(faturas);
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar todas as faturas quando cartão não existir")
    void deveLancarExcecaoQuandoCartaoNaoExistir() {

        when(cartaoRepository.findById(cartao.getId()))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.consultaFaturas(cartao.getId())
        );

        assertEquals("Cartao nao encontrado", exception.getMessage());

        verify(faturaRepository, never()).findByCartaoId(anyLong());
        verify(faturaMapper, never()).toResponseList(anyList());
    }

    @Test
    @DisplayName("Deve fechar a fatura quando atingir a data de fechamento")
    void deveFecharFaturaQuandoAtingirDataDeFechamento() {

        fatura.setDataFechamento(LocalDate.now());
        fatura.setStatusFatura(StatusFatura.ABERTA);

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        faturaService.fecharFatura(cartao.getId());

        assertEquals(StatusFatura.FECHADA, fatura.getStatusFatura());

        verify(faturaRepository).save(fatura);
    }

    @Test
    @DisplayName("Não deve fechar a fatura quando ainda não atingir a data de fechamento")
    void naoDeveFecharFaturaAntesDaData() {

        fatura.setDataFechamento(LocalDate.now().plusDays(1));
        fatura.setStatusFatura(StatusFatura.ABERTA);

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        faturaService.fecharFatura(cartao.getId());

        assertEquals(StatusFatura.ABERTA, fatura.getStatusFatura());

        verify(faturaRepository, never()).save(any(FaturaEntity.class));
    }

    @Test
    @DisplayName("Deve consultar o valor da fatura aberta")
    void deveConsultarValorFatura() {

        fatura.setValor(BigDecimal.valueOf(500));

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        BigDecimal resultado =
                faturaService.consultaValorFatura(cartao.getId());

        assertEquals(BigDecimal.valueOf(500), resultado);

        verify(faturaRepository).findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        );
    }

    @Test
    @DisplayName("Deve consultar a data de vencimento da fatura aberta")
    void deveConsultarDataVencimento() {

        LocalDate vencimento = LocalDate.now().plusDays(7);
        fatura.setDataVencimento(vencimento);

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        LocalDate resultado =
                faturaService.consultaDataVencimento(cartao.getId());

        assertEquals(vencimento, resultado);

        verify(faturaRepository).findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        );
    }

    @Test
    @DisplayName("Deve consultar faturas filtrando por status")
    void deveConsultarFaturasPorStatus() {

        FaturaFilterRequest filter = mock(FaturaFilterRequest.class);

        when(filter.getStatusFatura())
                .thenReturn(StatusFatura.FECHADA);

        when(cartaoRepository.findById(cartao.getId()))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(fatura));

        when(faturaMapper.toResponseList(anyList()))
                .thenReturn(List.of(faturaResponse));

        List<FaturaResponse> resultado =
                faturaService.consutaPorStatus(filter, cartao.getId());

        assertNotNull(resultado);

        verify(cartaoRepository).findById(cartao.getId());
        verify(faturaRepository).findAll(any(Specification.class));
        verify(faturaMapper).toResponseList(anyList());
    }

    @Test
    @DisplayName("Deve consultar todas as compras da fatura")
    void deveConsultarComprasDaFatura() {

        FaturaFilterRequest filter = mock(FaturaFilterRequest.class);

        when(filter.getValor())
                .thenReturn(null);

        CompraEntity compra = CompraEntity.builder()
                .id(1L)
                .fatura(fatura)
                .valor(BigDecimal.valueOf(100))
                .build();

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        when(compraRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(compra));

        List<CompraEntity> resultado =
                faturaService.comprasFaturas(cartao.getId(), filter);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compra, resultado.get(0));

        verify(compraRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("Deve consultar compras da fatura filtrando por valor")
    void deveConsultarComprasPorValor() {

        FaturaFilterRequest filter = mock(FaturaFilterRequest.class);

        when(filter.getValor())
                .thenReturn(BigDecimal.valueOf(100));

        CompraEntity compra = CompraEntity.builder()
                .id(1L)
                .fatura(fatura)
                .valor(BigDecimal.valueOf(200))
                .build();

        when(faturaRepository.findByCartaoIdAndStatusFatura(
                cartao.getId(),
                StatusFatura.ABERTA
        )).thenReturn(Optional.of(fatura));

        when(compraRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(compra));

        List<CompraEntity> resultado =
                faturaService.comprasFaturas(cartao.getId(), filter);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(compraRepository).findAll(any(Specification.class));
    }
}