package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.ContaEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusConta;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ContaPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ContaPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ContaResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.ContaMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes da classe ContaService")
class ContaServiceTest {

    @Mock
    private IContaRepository contaRepository;

    @Mock
    private ContaMapper contaMapper;

    @InjectMocks
    private ContaService contaService;

    private ContaEntity conta;
    private ContaPostRequest contaPostRequest;
    private ContaPatchRequest contaPatchRequest;
    private ContaResponse contaResponse;

    @BeforeEach
    void setUp() {

        conta = ContaEntity.builder()
                .id(1L)
                .numeroConta("123456")
                .statusConta(StatusConta.ATIVA)
                .build();

        contaPostRequest = ContaPostRequest.builder()
                .numeroConta("123456")
                .build();

        contaPatchRequest = ContaPatchRequest.builder()
                .status(StatusConta.INATIVA)
                .build();

        contaResponse = ContaResponse.builder()
                .id(1L)
                .numeroConta("123456")
                .statusConta(StatusConta.ATIVA)
                .build();
    }

    @Test
    @DisplayName("Deve salvar uma conta com sucesso quando o número não existe")
    void deveSalvarContaComSucesso() {

        when(contaRepository.findByNumeroConta(contaPostRequest.getNumeroConta()))
                .thenReturn(Optional.empty());

        when(contaMapper.toEntity(contaPostRequest))
                .thenReturn(conta);

        when(contaRepository.save(conta))
                .thenReturn(conta);

        when(contaMapper.toResponse(conta))
                .thenReturn(contaResponse);

        ContaResponse response = contaService.save(contaPostRequest);

        assertNotNull(response);
        assertEquals(contaResponse, response);

        verify(contaRepository).findByNumeroConta(contaPostRequest.getNumeroConta());
        verify(contaMapper).toEntity(contaPostRequest);
        verify(contaRepository).save(conta);
        verify(contaMapper).toResponse(conta);
    }

    @Test
    @DisplayName("Deve definir status ATIVA ao criar uma conta")
    void deveSalvarContaComStatusAtiva() {

        when(contaRepository.findByNumeroConta(contaPostRequest.getNumeroConta()))
                .thenReturn(Optional.empty());

        when(contaMapper.toEntity(contaPostRequest))
                .thenReturn(conta);

        when(contaRepository.save(conta))
                .thenReturn(conta);

        when(contaMapper.toResponse(conta))
                .thenReturn(contaResponse);

        contaService.save(contaPostRequest);

        assertEquals(StatusConta.ATIVA, conta.getStatusConta());

        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a conta já existe")
    void deveLancarErroAoSalvarContaComNumeroJaExistente() {

        when(contaRepository.findByNumeroConta(contaPostRequest.getNumeroConta()))
                .thenReturn(Optional.of(conta));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.save(contaPostRequest)
        );

        assertEquals(
                "Conta ja cadastrada com esse numero",
                exception.getMessage()
        );

        verify(contaRepository)
                .findByNumeroConta(contaPostRequest.getNumeroConta());

        verify(contaMapper, never())
                .toEntity(any());

        verify(contaRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre erro ao salvar")
    void deveLancarErroQuandoRepositoryFalharAoSalvar() {

        when(contaRepository.findByNumeroConta(contaPostRequest.getNumeroConta()))
                .thenReturn(Optional.empty());

        when(contaMapper.toEntity(contaPostRequest))
                .thenReturn(conta);

        when(contaRepository.save(conta))
                .thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.save(contaPostRequest)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve retornar uma conta quando o ID existe")
    void deveRetornarContaPorId() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        when(contaMapper.toResponse(conta))
                .thenReturn(contaResponse);

        ContaResponse response = contaService.verUmaConta(1L);

        assertNotNull(response);
        assertEquals(contaResponse, response);

        verify(contaRepository).findById(1L);
        verify(contaMapper).toResponse(conta);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a conta não existe")
    void deveLancarErroQuandoContaNaoForEncontradaPorId() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.verUmaConta(1L)
        );

        assertEquals(
                "Conta nao encontrada",
                exception.getMessage()
        );

        verify(contaRepository).findById(1L);

        verify(contaMapper, never())
                .toResponse(any());
    }

    @Test
    @DisplayName("Deve retornar lista de contas de um cliente")
    void deveRetornarContasDeCliente() {

        List<ContaEntity> contas = List.of(conta);

        List<ContaResponse> responses = List.of(contaResponse);

        when(contaRepository.findClienteId(1L))
                .thenReturn(contas);

        when(contaMapper.toResponseList(contas))
                .thenReturn(responses);

        List<ContaResponse> resultado = contaService.verContasDeCliente(1L);

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(contaRepository).findClienteId(1L);
        verify(contaMapper).toResponseList(contas);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando cliente não tem contas")
    void deveRetornarListaVaziaQuandoClienteNaoTemContas() {

        List<ContaEntity> contas = List.of();
        List<ContaResponse> responses = List.of();

        when(contaRepository.findClienteId(1L))
                .thenReturn(contas);

        when(contaMapper.toResponseList(contas))
                .thenReturn(responses);

        List<ContaResponse> resultado = contaService.verContasDeCliente(1L);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(contaRepository).findClienteId(1L);
        verify(contaMapper).toResponseList(contas);
    }

    @Test
    @DisplayName("Deve retornar múltiplas contas de um cliente")
    void deveRetornarMultiplasContasDeCliente() {

        ContaEntity conta2 = ContaEntity.builder()
                .id(2L)
                .numeroConta("789012")
                .statusConta(StatusConta.ATIVA)
                .build();

        ContaResponse contaResponse2 = ContaResponse.builder()
                .id(2L)
                .numeroConta("789012")
                .statusConta(StatusConta.ATIVA)
                .build();

        List<ContaEntity> contas = List.of(conta, conta2);
        List<ContaResponse> responses = List.of(contaResponse, contaResponse2);

        when(contaRepository.findClienteId(1L))
                .thenReturn(contas);

        when(contaMapper.toResponseList(contas))
                .thenReturn(responses);

        List<ContaResponse> resultado = contaService.verContasDeCliente(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(contaRepository).findClienteId(1L);
        verify(contaMapper).toResponseList(contas);
    }

    @Test
    @DisplayName("Deve atualizar o status de uma conta com sucesso")
    void deveAtualizarContaComSucesso() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        contaService.update(1L, contaPatchRequest);

        verify(contaRepository).findById(1L);
        verify(contaRepository).save(conta);

        assertEquals(
                contaPatchRequest.getStatus(),
                conta.getStatusConta()
        );
    }

    @Test
    @DisplayName("Deve atualizar de ATIVA para INATIVA")
    void deveAtualizarMudanciaDeStatus() {

        conta.setStatusConta(StatusConta.ATIVA);

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        contaService.update(1L, contaPatchRequest);

        assertEquals(StatusConta.INATIVA, conta.getStatusConta());

        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar conta inexistente")
    void deveLancarErroAoAtualizarContaInexistente() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.update(1L, contaPatchRequest)
        );

        assertEquals(
                "Conta nao encontrada",
                exception.getMessage()
        );

        verify(contaRepository).findById(1L);

        verify(contaRepository, never())
                .save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre erro ao atualizar")
    void deveLancarErroQuandoRepositoryFalharAoAtualizar() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        doThrow(new RuntimeException("Erro no banco"))
                .when(contaRepository)
                .save(conta);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.update(1L, contaPatchRequest)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(contaRepository).save(conta);
    }

    @Test
    @DisplayName("Deve deletar uma conta com sucesso")
    void deveDeletarContaComSucesso() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        contaService.delete(1L);

        verify(contaRepository).findById(1L);
        verify(contaRepository).delete(conta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar conta inexistente")
    void deveLancarErroAoDeletarContaInexistente() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> contaService.delete(1L)
        );

        assertEquals(
                "Conta nao encontrada",
                exception.getMessage()
        );

        verify(contaRepository).findById(1L);

        verify(contaRepository, never())
                .delete(any());
    }

    @Test
    @DisplayName("Deve deletar a conta correta")
    void deveDeletarContaCorreta() {

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        contaService.delete(1L);

        verify(contaRepository).delete(conta);
    }

}