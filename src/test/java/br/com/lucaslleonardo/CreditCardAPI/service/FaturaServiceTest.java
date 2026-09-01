package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.FaturaFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.FaturaSpecification;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.FaturaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.FaturaResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.FaturaMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICompraRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private FaturaEntity fatura;
    private CartaoEntity cartao;
    private FaturaResponse faturaResponse;
    private FaturaPostRequest faturaPostRequest;
    private FaturaFilterRequest filter;

    @BeforeEach
    void setUp() {

        cartao = CartaoEntity.builder()
                .id(1L)
                .build();

        fatura = FaturaEntity.builder()
                .id(1L)
                .dataFechamento(LocalDate.now())
                .dataVencimento(LocalDate.now().plusDays(30))
                .valor(BigDecimal.ZERO)
                .statusFatura(StatusFatura.ABERTA)
                .cartao(cartao)
                .build();

        faturaResponse = FaturaResponse.builder()
                .id(1L)
                .dataFechamento(fatura.getDataFechamento())
                .dataVencimento(fatura.getDataVencimento())
                .valor(BigDecimal.ZERO)
                .statusFatura(StatusFatura.ABERTA)
                .build();

        faturaPostRequest = mock(FaturaPostRequest.class);

        filter = mock(FaturaFilterRequest.class);
    }


    @Test
    void deveCadastrarFaturaComSucesso() {

        when(faturaPostRequest.getCartao())
                .thenReturn(cartao);

        when(faturaRepository.findByCartaId(cartao.getId()))
                .thenReturn(Optional.empty());

        when(faturaMapper.toEntity(faturaPostRequest))
                .thenReturn(fatura);

        when(faturaRepository.save(fatura))
                .thenReturn(fatura);

        when(faturaMapper.toResponse(fatura))
                .thenReturn(faturaResponse);

        FaturaResponse response =
                faturaService.cadastrarInfosFatura(faturaPostRequest);

        assertNotNull(response);
        assertEquals(faturaResponse, response);

        assertEquals(StatusFatura.ABERTA, fatura.getStatusFatura());
        assertEquals(LocalDate.now(), fatura.getDataFechamento());
        assertEquals(
                LocalDate.now().plusDays(30),
                fatura.getDataVencimento()
        );

        verify(faturaRepository).findByCartaId(cartao.getId());
        verify(faturaMapper).toEntity(faturaPostRequest);
        verify(faturaRepository).save(fatura);
        verify(faturaMapper).toResponse(fatura);
    }


    @Test
    void deveLancarErroAoCadastrarFaturaJaExistente() {

        when(faturaPostRequest.getCartao())
                .thenReturn(cartao);

        when(faturaRepository.findByCartaId(cartao.getId()))
                .thenReturn(Optional.of(fatura));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.cadastrarInfosFatura(faturaPostRequest)
        );

        assertEquals("Fatura ja cadastrada", exception.getMessage());

        verify(faturaRepository).findByCartaId(cartao.getId());
        verify(faturaMapper, never()).toEntity(any());
        verify(faturaRepository, never()).save(any());
    }


    @Test
    void deveConsultarFaturaComSucesso() {

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        when(faturaMapper.toResponse(fatura))
                .thenReturn(faturaResponse);

        FaturaResponse response =
                faturaService.consultaFatura(1L);

        assertNotNull(response);
        assertEquals(faturaResponse, response);

        verify(faturaRepository).findByCartaId(1L);
        verify(faturaMapper).toResponse(fatura);
    }


    @Test
    void deveLancarErroQuandoFaturaNaoExistirNaConsulta() {

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.consultaFatura(1L)
        );

        assertEquals("Fatura nao encontrada", exception.getMessage());

        verify(faturaRepository).findByCartaId(1L);
        verify(faturaMapper, never()).toResponse(any());
    }


    @Test
    void deveConsultarTodasFaturasComSucesso() {

        List<FaturaEntity> faturas = List.of(fatura);
        List<FaturaResponse> responses = List.of(faturaResponse);

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoId(1L))
                .thenReturn(faturas);

        when(faturaMapper.toResponseList(faturas))
                .thenReturn(responses);

        List<FaturaResponse> resultado =
                faturaService.consultaFaturas(1L);

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository).findByCartaoId(1L);
        verify(faturaMapper).toResponseList(faturas);
    }


    @Test
    void deveLancarErroQuandoCartaoNaoExistirAoConsultarFaturas() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.consultaFaturas(1L)
        );

        assertEquals("Cartao nao encontrado", exception.getMessage());

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository, never()).findByCartaoId(any());
        verify(faturaMapper, never()).toResponseList(any());
    }


    @Test
    void deveFecharFaturaComSucesso() {

        fatura.setDataFechamento(LocalDate.now());

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        faturaService.fecharFatura(1L);

        assertEquals(
                StatusFatura.FECHADA,
                fatura.getStatusFatura()
        );

        verify(faturaRepository).findByCartaId(1L);
        verify(faturaRepository).save(fatura);
    }


    @Test
    void naoDeveFecharFaturaAntesDaDataDeFechamento() {

        fatura.setDataFechamento(
                LocalDate.now().plusDays(1)
        );

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        faturaService.fecharFatura(1L);

        assertEquals(
                StatusFatura.ABERTA,
                fatura.getStatusFatura()
        );

        verify(faturaRepository).findByCartaId(1L);
        verify(faturaRepository, never()).save(any());
    }


    @Test
    void deveLancarErroAoFecharFaturaInexistente() {

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.fecharFatura(1L)
        );

        assertEquals("Fatura inexistente", exception.getMessage());

        verify(faturaRepository).findByCartaId(1L);
        verify(faturaRepository, never()).save(any());
    }


    @Test
    void deveConsultarValorDaFatura() {

        fatura.setValor(new BigDecimal("500.00"));

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        BigDecimal resultado =
                faturaService.consultaValorFatura(1L);

        assertNotNull(resultado);
        assertEquals(
                new BigDecimal("500.00"),
                resultado
        );

        verify(faturaRepository).findByCartaId(1L);
    }


    @Test
    void deveConsultarDataDeVencimento() {

        LocalDate dataVencimento =
                LocalDate.now().plusDays(30);

        fatura.setDataVencimento(dataVencimento);

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        LocalDate resultado =
                faturaService.consultaDataVencimento(1L);

        assertNotNull(resultado);
        assertEquals(dataVencimento, resultado);

        verify(faturaRepository).findByCartaId(1L);
    }


    @Test
    void deveConsultarFaturasPorStatus() {

        fatura.setCartao(cartao);

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(filter.getStatusFatura())
                .thenReturn(StatusFatura.ABERTA);

        when(faturaRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(fatura));

        when(faturaMapper.toResponseList(anyList()))
                .thenReturn(List.of(faturaResponse));

        List<FaturaResponse> resultado =
                faturaService.consutaPorStatus(filter, 1L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository).findAll(any(Specification.class));
        verify(faturaMapper).toResponseList(anyList());
    }


    @Test
    void deveConsultarComprasDaFatura() {

        CompraEntity compra = CompraEntity.builder()
                .id(1L)
                .valor(new BigDecimal("100.00"))
                .fatura(fatura)
                .build();

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        when(filter.getValor())
                .thenReturn(new BigDecimal("50.00"));

        when(compraRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(compra));

        List<CompraEntity> resultado =
                faturaService.comprasFaturas(1L, filter);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(compra, resultado.get(0));

        verify(faturaRepository).findByCartaId(1L);
        verify(compraRepository).findAll(any(Specification.class));
    }


    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremComprasNaFatura() {

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.of(fatura));

        when(filter.getValor())
                .thenReturn(null);

        when(compraRepository.findAll(any(Specification.class)))
                .thenReturn(List.of());

        List<CompraEntity> resultado =
                faturaService.comprasFaturas(1L, filter);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(faturaRepository).findByCartaId(1L);
        verify(compraRepository).findAll(any(Specification.class));
    }


    @Test
    void deveLancarErroQuandoFaturaNaoExistirAoConsultarCompras() {

        when(faturaRepository.findByCartaId(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> faturaService.comprasFaturas(1L, filter)
        );

        assertEquals("Fatura inexistente", exception.getMessage());

        verify(faturaRepository).findByCartaId(1L);
        verify(compraRepository, never())
                .findAll(any(Specification.class));
    }
}

