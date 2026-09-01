package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.CompraEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.FaturaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCompra;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusFatura;
import br.com.lucaslleonardo.CreditCardAPI.database.specification.CompraFilterRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CompraPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CompraResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.CompraMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICompraRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IFaturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompraServiceTest {

    @Mock
    private ICompraRepository compraRepository;

    @Mock
    private ICartaoRepository cartaoRepository;

    @Mock
    private IFaturaRepository faturaRepository;

    @Mock
    private CompraMapper compraMapper;

    @InjectMocks
    private CompraService compraService;

    private CompraEntity compra;
    private CompraPostRequest compraPostRequest;
    private CompraResponse compraResponse;
    private CartaoEntity cartao;
    private ContaEntity conta;
    private FaturaEntity fatura;

    @BeforeEach
    void setUp() {

        conta = ContaEntity.builder()
                .id(1L)
                .numeroConta("123456789")
                .agencia(1234)
                .saldo(new BigDecimal("5000.00"))
                .build();

        cartao = CartaoEntity.builder()
                .id(1L)
                .limiteDisponivel(new BigDecimal("3000.00"))
                .statusCartao(StatusCartao.ATIVO)
                .conta(conta)
                .build();

        fatura = FaturaEntity.builder()
                .id(1L)
                .valor(new BigDecimal("1000.00"))
                .statusFatura(StatusFatura.ABERTA)
                .build();

        compra = CompraEntity.builder()
                .id(1L)
                .valor(new BigDecimal("500.00"))
                .statusCompra(StatusCompra.APROVADA)
                .cartao(cartao)
                .build();

        compraPostRequest = CompraPostRequest.builder()
                .valor(new BigDecimal("500.00"))
                .cartao(cartao)
                .build();

        compraResponse = CompraResponse.builder()
                .id(1L)
                .valor(new BigDecimal("500.00"))
                .statusCompra(StatusCompra.APROVADA)
                .build();
    }

    @Test
    void deveSalvarCompraComSucesso() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.of(fatura));

        when(compraMapper.toEntity(compraPostRequest))
                .thenReturn(compra);

        when(compraRepository.save(compra))
                .thenReturn(compra);

        when(compraMapper.toResponse(compra))
                .thenReturn(compraResponse);

        CompraResponse response = compraService.save(compraPostRequest);

        assertNotNull(response);
        assertEquals(compraResponse, response);
        assertEquals(StatusCompra.APROVADA, compra.getStatusCompra());
        assertEquals(new BigDecimal("1500.00"), fatura.getValor());
        assertEquals(new BigDecimal("2500.00"), cartao.getLimiteDisponivel());

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository).findByCartaoContaId(1L);
        verify(compraMapper).toEntity(compraPostRequest);
        verify(faturaRepository).save(fatura);
        verify(cartaoRepository).save(cartao);
        verify(compraRepository).save(compra);
        verify(compraMapper).toResponse(compra);
    }

    @Test
    void deveSalvarCompraRecusadaPorLimiteInsuficiente() {

        cartao.setLimiteDisponivel(new BigDecimal("100.00"));

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.of(fatura));

        when(compraMapper.toEntity(compraPostRequest))
                .thenReturn(compra);

        when(compraRepository.save(compra))
                .thenReturn(compra);

        when(compraMapper.toResponse(compra))
                .thenReturn(compraResponse);

        CompraResponse response = compraService.save(compraPostRequest);

        assertNotNull(response);
        assertEquals(StatusCompra.RECUSADA, compra.getStatusCompra());

        verify(compraRepository).save(compra);
        verify(faturaRepository, never()).save(any());
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoCartaoNaoForEncontrado() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.save(compraPostRequest)
        );

        assertEquals("Cartao nao encontrado", exception.getMessage());

        verify(cartaoRepository).findById(1L);
        verify(compraMapper, never()).toEntity(any());
        verify(compraRepository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoCartaoNaoEstiverAtivo() {

        cartao.setStatusCartao(StatusCartao.BLOQUEADO);

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.save(compraPostRequest)
        );

        assertEquals("Cartao nao ativo", exception.getMessage());

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository, never()).findByCartaoContaId(any());
        verify(compraRepository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoFaturaNaoForEncontrada() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.save(compraPostRequest)
        );

        assertEquals("Fatura nao encontrada", exception.getMessage());

        verify(cartaoRepository).findById(1L);
        verify(faturaRepository).findByCartaoContaId(1L);
        verify(compraRepository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoFaturaNaoEstiverAberta() {

        fatura.setStatusFatura(StatusFatura.FECHADA);

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.of(fatura));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.save(compraPostRequest)
        );

        assertEquals("Fatura nao esta aberta", exception.getMessage());

        verify(faturaRepository).findByCartaoContaId(1L);
        verify(compraRepository, never()).save(any());
    }

    @Test
    void deveRetornarCompraPorId() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        when(compraMapper.toResponse(compra))
                .thenReturn(compraResponse);

        CompraResponse response = compraService.encontrarCompra(1L);

        assertNotNull(response);
        assertEquals(compraResponse, response);

        verify(compraRepository).findById(1L);
        verify(compraMapper).toResponse(compra);
    }

    @Test
    void deveLancarErroQuandoCompraNaoForEncontrada() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.encontrarCompra(1L)
        );

        assertEquals("Compra nao encontrada", exception.getMessage());

        verify(compraRepository).findById(1L);
        verify(compraMapper, never()).toResponse(any());
    }

    @Test
    void deveRetornarComprasComSucesso() {

        CompraFilterRequest filter = new CompraFilterRequest();

        List<CompraEntity> compras = List.of(compra);
        List<CompraResponse> responses = List.of(compraResponse);

        when(compraMapper.toRequestList(compras))
                .thenReturn(responses);

        List<CompraResponse> resultado = compraService.encontrarCompras(filter);

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(compraMapper).toRequestList(compras);
    }

    @Test
    void deveCancelarCompraComSucesso() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.of(fatura));

        when(compraRepository.save(compra))
                .thenReturn(compra);

        when(compraMapper.toResponse(compra))
                .thenReturn(compraResponse);

        CompraResponse response = compraService.cancelarCompra(1L);

        assertNotNull(response);
        assertEquals(StatusCompra.CANCELADA, compra.getStatusCompra());
        assertEquals(new BigDecimal("500.00"), fatura.getValor());
        assertEquals(new BigDecimal("3500.00"), cartao.getLimiteDisponivel());

        verify(compraRepository).findById(1L);
        verify(faturaRepository).findByCartaoContaId(1L);
        verify(faturaRepository).save(fatura);
        verify(cartaoRepository).save(cartao);
        verify(compraRepository).save(compra);
        verify(compraMapper).toResponse(compra);
    }

    @Test
    void deveLancarErroAoCancelarCompraInexistente() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.cancelarCompra(1L)
        );

        assertEquals("Compra nao encontrada", exception.getMessage());

        verify(compraRepository).findById(1L);
        verify(faturaRepository, never()).findByCartaoContaId(any());
        verify(cartaoRepository, never()).save(any());
    }

    @Test
    void deveLancarErroQuandoFaturaNaoForEncontradaAoCancelar() {

        when(compraRepository.findById(1L))
                .thenReturn(Optional.of(compra));

        when(faturaRepository.findByCartaoContaId(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> compraService.cancelarCompra(1L)
        );

        assertEquals("Fatura nao encontrada", exception.getMessage());

        verify(compraRepository).findById(1L);
        verify(faturaRepository).findByCartaoContaId(1L);
        verify(faturaRepository, never()).save(any());
        verify(cartaoRepository, never()).save(any());
    }
}