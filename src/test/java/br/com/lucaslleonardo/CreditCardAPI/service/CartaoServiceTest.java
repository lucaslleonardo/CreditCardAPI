package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.CartaoEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCartao;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.CartaoPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.CartaoPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.CartaoResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.CartaoMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.ICartaoRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IClienteRepository;
import br.com.lucaslleonardo.CreditCardAPI.repository.IContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Testes da classe CartaoService")
class CartaoServiceTest {

    @Mock
    private ICartaoRepository cartaoRepository;

    @Mock
    private IClienteRepository clienteRepository;

    @Mock
    private IContaRepository contaRepository;

    @Mock
    private CartaoMapper cartaoMapper;

    @InjectMocks
    private CartaoService cartaoService;

    private CartaoEntity cartao;
    private CartaoPostRequest cartaoPostRequest;
    private CartaoPatchRequest cartaoPatchRequest;
    private CartaoResponse cartaoResponse;
    private ClienteEntity cliente;
    private ContaEntity conta;

    @BeforeEach
    void setUp() {

        cliente = ClienteEntity.builder()
                .id(1L)
                .nome("Lucas")
                .email("lucas@email.com")
                .build();

        conta = ContaEntity.builder()
                .id(1L)
                .numeroConta("123456")
                .build();

        cartao = CartaoEntity.builder()
                .id(1L)
                .numeroCartao("1234567890123456")
                .statusCartao(StatusCartao.ATIVO)
                .limite(new BigDecimal("5000.00"))
                .limiteDisponivel(new BigDecimal("5000.00"))
                .conta(conta)
                .build();

        cartaoPostRequest = CartaoPostRequest.builder()
                .numeroCartao("1234567890123456")
                .limite(new BigDecimal("5000.00"))
                .build();

        cartaoPatchRequest = CartaoPatchRequest.builder()
                .statusCartao(StatusCartao.BLOQUEADO)
                .build();

        cartaoResponse = CartaoResponse.builder()
                .id(1L)
                .numeroCartao("1234567890123456")
                .statusCartao(StatusCartao.ATIVO)
                .limite(new BigDecimal("5000.00"))
                .limiteDisponivel(new BigDecimal("5000.00"))
                .build();
    }

    @Test
    @DisplayName("Deve salvar um cartao com sucesso quando o número não existe")
    void deveSalvarCartaoComSucesso() {

        when(cartaoRepository.findByNumeroCartao(cartaoPostRequest.getNumeroCartao()))
                .thenReturn(Optional.empty());

        when(cartaoMapper.toEntity(cartaoPostRequest))
                .thenReturn(cartao);

        when(cartaoRepository.save(cartao))
                .thenReturn(cartao);

        when(cartaoMapper.toResponse(cartao))
                .thenReturn(cartaoResponse);

        CartaoResponse response = cartaoService.save(cartaoPostRequest);

        assertNotNull(response);
        assertEquals(cartaoResponse, response);

        verify(cartaoRepository).findByNumeroCartao(cartaoPostRequest.getNumeroCartao());
        verify(cartaoMapper).toEntity(cartaoPostRequest);
        verify(cartaoRepository).save(cartao);
        verify(cartaoMapper).toResponse(cartao);
    }

    @Test
    @DisplayName("Deve definir status ATIVO ao criar um cartao")
    void deveSalvarCartaoComStatusAtivo() {

        when(cartaoRepository.findByNumeroCartao(cartaoPostRequest.getNumeroCartao()))
                .thenReturn(Optional.empty());

        when(cartaoMapper.toEntity(cartaoPostRequest))
                .thenReturn(cartao);

        when(cartaoRepository.save(cartao))
                .thenReturn(cartao);

        when(cartaoMapper.toResponse(cartao))
                .thenReturn(cartaoResponse);

        cartaoService.save(cartaoPostRequest);

        assertEquals(StatusCartao.ATIVO, cartao.getStatusCartao());

        verify(cartaoRepository).save(cartao);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cartao já existe")
    void deveLancarErroAoSalvarCartaoComNumeroJaExistente() {

        when(cartaoRepository.findByNumeroCartao(cartaoPostRequest.getNumeroCartao()))
                .thenReturn(Optional.of(cartao));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.save(cartaoPostRequest)
        );

        assertEquals(
                "Cartao ja cadastrado",
                exception.getMessage()
        );

        verify(cartaoRepository)
                .findByNumeroCartao(cartaoPostRequest.getNumeroCartao());

        verify(cartaoMapper, never())
                .toEntity(any());

        verify(cartaoRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre erro ao salvar")
    void deveLancarErroQuandoRepositoryFalharAoSalvar() {

        when(cartaoRepository.findByNumeroCartao(cartaoPostRequest.getNumeroCartao()))
                .thenReturn(Optional.empty());

        when(cartaoMapper.toEntity(cartaoPostRequest))
                .thenReturn(cartao);

        when(cartaoRepository.save(cartao))
                .thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.save(cartaoPostRequest)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(cartaoRepository).save(cartao);
    }

    @Test
    @DisplayName("Deve retornar um cartao quando o ID existe")
    void deveRetornarCartaoPorId() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        when(cartaoMapper.toResponse(cartao))
                .thenReturn(cartaoResponse);

        CartaoResponse response = cartaoService.consultaCartao( 1L);

        assertNotNull(response);
        assertEquals(cartaoResponse, response);

        verify(cartaoRepository).findById(1L);
        verify(cartaoMapper).toResponse(cartao);
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cartao não existe")
    void deveLancarErroQuandoCartaoNaoForEncontradoPorId() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.consultaCartao( 1L)
        );

        assertEquals(
                "Cartao nao encontrado",
                exception.getMessage()
        );

        verify(cartaoRepository).findById(1L);

        verify(cartaoMapper, never())
                .toResponse(any());
    }

    @Test
    @DisplayName("Deve retornar lista de cartoes por conta")
    void deveRetornarCartoesPorConta() {

        List<CartaoEntity> cartoes = List.of(cartao);

        List<CartaoResponse> responses = List.of(cartaoResponse);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(contaRepository.findByContaYCliente(1L, 1L))
                .thenReturn(Optional.of(conta));

        when(cartaoRepository.findByContaId(1L))
                .thenReturn(cartoes);

        when(cartaoMapper.toResponseList(cartoes))
                .thenReturn(responses);

        List<CartaoResponse> resultado = cartaoService.cartoesPorConta(1L, 1L);

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(clienteRepository).findById(1L);
        verify(contaRepository).findByContaYCliente(1L, 1L);
        verify(cartaoRepository).findByContaId(1L);
        verify(cartaoMapper).toResponseList(cartoes);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não existe")
    void deveLancarErroQuandoClienteNaoExiste() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.cartoesPorConta(1L, 1L)
        );

        assertEquals(
                "Cliente nao encontrado",
                exception.getMessage()
        );

        verify(clienteRepository).findById(1L);

        verify(contaRepository, never())
                .findByContaYCliente(any(Long.class), any(Long.class));

        verify(cartaoRepository, never())
                .findByContaId(any(Long.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando conta não pertence ao cliente")
    void deveLancarErroQuandoContaNaoPertenceAoCliente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(contaRepository.findByContaYCliente(1L, 1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.cartoesPorConta(1L, 1L)
        );

        assertEquals(
                "Conta nao encontrada",
                exception.getMessage()
        );

        verify(clienteRepository).findById(1L);
        verify(contaRepository).findByContaYCliente(1L, 1L);

        verify(cartaoRepository, never())
                .findByContaId(any(Long.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando conta não tem cartoes")
    void deveRetornarListaVaziaQuandoContaNaoTemCartoes() {

        List<CartaoEntity> cartoes = List.of();
        List<CartaoResponse> responses = List.of();

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(contaRepository.findByContaYCliente(1L, 1L))
                .thenReturn(Optional.of(conta));

        when(cartaoRepository.findByContaId(1L))
                .thenReturn(cartoes);

        when(cartaoMapper.toResponseList(cartoes))
                .thenReturn(responses);

        List<CartaoResponse> resultado = cartaoService.cartoesPorConta(1L, 1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(cartaoRepository).findByContaId(1L);
        verify(cartaoMapper).toResponseList(cartoes);
    }

    @Test
    @DisplayName("Deve retornar múltiplos cartoes por conta")
    void deveRetornarMultiplosCartoesPorConta() {

        CartaoEntity cartao2 = CartaoEntity.builder()
                .id(2L)
                .numeroCartao("9876543210123456")
                .statusCartao(StatusCartao.ATIVO)
                .limite(new BigDecimal("3000.00"))
                .limiteDisponivel(new BigDecimal("3000.00"))
                .conta(conta)
                .build();

        CartaoResponse cartaoResponse2 = CartaoResponse.builder()
                .id(2L)
                .numeroCartao("9876543210123456")
                .statusCartao(StatusCartao.ATIVO)
                .limite(new BigDecimal("3000.00"))
                .limiteDisponivel(new BigDecimal("3000.00"))
                .build();

        List<CartaoEntity> cartoes = List.of(cartao, cartao2);
        List<CartaoResponse> responses = List.of(cartaoResponse, cartaoResponse2);

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(contaRepository.findByContaYCliente(1L, 1L))
                .thenReturn(Optional.of(conta));

        when(cartaoRepository.findByContaId(1L))
                .thenReturn(cartoes);

        when(cartaoMapper.toResponseList(cartoes))
                .thenReturn(responses);

        List<CartaoResponse> resultado = cartaoService.cartoesPorConta(1L, 1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(cartaoRepository).findByContaId(1L);
        verify(cartaoMapper).toResponseList(cartoes);
    }

    @Test
    @DisplayName("Deve atualizar o status de um cartao com sucesso")
    void deveAtualizarCartaoComSucesso() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        cartaoService.update(cartaoPatchRequest, 1L);

        verify(cartaoRepository).findById(1L);
        verify(cartaoRepository).save(cartao);

        assertEquals(
                cartaoPatchRequest.getStatusCartao(),
                cartao.getStatusCartao()
        );
    }

    @Test
    @DisplayName("Deve atualizar de ATIVO para INATIVO")
    void deveAtualizarMudanciaDeStatus() {

        cartao.setStatusCartao(StatusCartao.ATIVO);

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        cartaoService.update(cartaoPatchRequest, 1L);

        assertEquals(StatusCartao.BLOQUEADO, cartao.getStatusCartao());

        verify(cartaoRepository).save(cartao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar cartao inexistente")
    void deveLancarErroAoAtualizarCartaoInexistente() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.update(cartaoPatchRequest, 1L)
        );

        assertEquals(
                "Cartao nao encontrado",
                exception.getMessage()
        );

        verify(cartaoRepository).findById(1L);

        verify(cartaoRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre erro ao atualizar")
    void deveLancarErroQuandoRepositoryFalharAoAtualizar() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        doThrow(new RuntimeException("Erro no banco"))
                .when(cartaoRepository)
                .save(cartao);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.update(cartaoPatchRequest, 1L)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(cartaoRepository).save(cartao);
    }

    @Test
    @DisplayName("Deve deletar um cartao com sucesso")
    void deveDeletarCartaoComSucesso() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        cartaoService.delete(1L);

        verify(cartaoRepository).findById(1L);
        verify(cartaoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar cartao inexistente")
    void deveLancarErroAoDeletarCartaoInexistente() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.delete(1L)
        );

        assertEquals(
                "Cartao nao encontrado",
                exception.getMessage()
        );

        verify(cartaoRepository).findById(1L);

        verify(cartaoRepository, never())
                .deleteById(any());
    }

    @Test
    @DisplayName("Deve deletar o cartao correto")
    void deveDeletarCartaoCorreto() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        cartaoService.delete(1L);

        verify(cartaoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar o limite total do cartao")
    void deveRetornarLimiteTotalCartao() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        BigDecimal resultado = cartaoService.consultaLimite(1L);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("5000.00"), resultado);

        verify(cartaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cartao não existe ao consultar limite")
    void deveLancarErroAoConsultarLimiteCartaoInexistente() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.consultaLimite(1L)
        );

        assertEquals(
                "Cartao nao encontrado",
                exception.getMessage()
        );

        verify(cartaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar o limite disponível do cartao")
    void deveRetornarLimiteDisponivel() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        BigDecimal resultado = cartaoService.consultaLimiteDisponivel(1L);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("5000.00"), resultado);

        verify(cartaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cartao não existe ao consultar limite disponível")
    void deveLancarErroAoConsultarLimiteDisponiveCartaoInexistente() {

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> cartaoService.consultaLimiteDisponivel(1L)
        );

        assertEquals(
                "Cartao nao encontrado",
                exception.getMessage()
        );

        verify(cartaoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar limite disponível diferente do limite total")
    void deveRetornarLimiteDisponibleMenorQueLimiteTotal() {

        cartao.setLimiteDisponivel(new BigDecimal("2500.00"));

        when(cartaoRepository.findById(1L))
                .thenReturn(Optional.of(cartao));

        BigDecimal resultado = cartaoService.consultaLimiteDisponivel(1L);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("2500.00"), resultado);
        assertNotEquals(cartao.getLimite(), resultado);

        verify(cartaoRepository).findById(1L);
    }

}