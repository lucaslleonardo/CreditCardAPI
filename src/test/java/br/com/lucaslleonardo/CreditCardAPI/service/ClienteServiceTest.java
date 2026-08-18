package br.com.lucaslleonardo.CreditCardAPI.service;
import br.com.lucaslleonardo.CreditCardAPI.database.entity.ClienteEntity;
import br.com.lucaslleonardo.CreditCardAPI.database.enums.StatusCliente;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.ClientePatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.ClientePostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.ClienteResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.ClienteMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IClienteRepository;
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

class ClienteServiceTest {

    @Mock
    private IClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteEntity cliente;
    private ClientePostRequest clientePostRequest;
    private ClientePatchRequest clientePatchRequest;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {

        cliente = ClienteEntity.builder()
                .id(1L)
                .nome("Lucas")
                .email("lucas@email.com")
                .status(StatusCliente.ATIVO)
                .build();

        clientePostRequest = ClientePostRequest.builder()
                .nome("Lucas")
                .email("lucas@email.com")
                .build();

        clientePatchRequest = ClientePatchRequest.builder()
                .nome("Lucas Alterado")
                .email("lucasalterado@email.com")
                .status(StatusCliente.ATIVO)
                .build();

        clienteResponse = ClienteResponse.builder()
                .id(1L)
                .nome("Lucas")
                .email("lucas@email.com")
                .status(StatusCliente.ATIVO)
                .build();
    }

    @Test
    void deveSalvarClienteComSucesso() {

        when(clienteRepository.findByEmail(clientePostRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(clienteMapper.toEntity(clientePostRequest))
                .thenReturn(cliente);

        when(clienteRepository.save(cliente))
                .thenReturn(cliente);

        when(clienteMapper.toResponse(cliente))
                .thenReturn(clienteResponse);

        ClienteResponse response = clienteService.save(clientePostRequest);

        assertNotNull(response);
        assertEquals(clienteResponse, response);

        verify(clienteRepository).findByEmail(clientePostRequest.getEmail());
        verify(clienteMapper).toEntity(clientePostRequest);
        verify(clienteRepository).save(cliente);
        verify(clienteMapper).toResponse(cliente);
    }

    @Test
    void deveLancarErroAoSalvarClienteComEmailJaExistente() {

        when(clienteRepository.findByEmail(clientePostRequest.getEmail()))
                .thenReturn(Optional.of(cliente));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.save(clientePostRequest)
        );

        assertEquals(
                "Cliente ja cadastrado com esse email",
                exception.getMessage()
        );

        verify(clienteRepository)
                .findByEmail(clientePostRequest.getEmail());

        verify(clienteMapper, never())
                .toEntity(any());

        verify(clienteRepository, never())
                .save(any());
    }

    @Test
    void deveLancarErroQuandoRepositoryFalharAoSalvar() {

        when(clienteRepository.findByEmail(clientePostRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(clienteMapper.toEntity(clientePostRequest))
                .thenReturn(cliente);

        when(clienteRepository.save(cliente))
                .thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.save(clientePostRequest)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveAtualizarClienteComSucesso() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.update(clientePatchRequest, 1L);

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).save(cliente);

        assertEquals(
                clientePatchRequest.getNome(),
                cliente.getNome()
        );

        assertEquals(
                clientePatchRequest.getEmail(),
                cliente.getEmail()
        );

        assertEquals(
                clientePatchRequest.getStatus(),
                cliente.getStatus()
        );
    }

    @Test
    void deveLancarErroAoAtualizarClienteInexistente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.update(clientePatchRequest, 1L)
        );

        assertEquals(
                "Cliente nao encontrado",
                exception.getMessage()
        );

        verify(clienteRepository).findById(1L);

        verify(clienteRepository, never())
                .save(any());
    }

    @Test
    void deveLancarErroQuandoRepositoryFalharAoAtualizar() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        doThrow(new RuntimeException("Erro no banco"))
                .when(clienteRepository)
                .save(cliente);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.update(clientePatchRequest, 1L)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveRetornarTodosClientes() {

        List<ClienteEntity> clientes = List.of(cliente);

        List<ClienteResponse> responses = List.of(clienteResponse);

        when(clienteRepository.findAll())
                .thenReturn(clientes);

        when(clienteMapper.toResponseList(clientes))
                .thenReturn(responses);

        List<ClienteResponse> resultado = clienteService.findAll();

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(clienteRepository).findAll();
        verify(clienteMapper).toResponseList(clientes);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremClientes() {

        List<ClienteEntity> clientes = List.of();
        List<ClienteResponse> responses = List.of();

        when(clienteRepository.findAll())
                .thenReturn(clientes);

        when(clienteMapper.toResponseList(clientes))
                .thenReturn(responses);

        List<ClienteResponse> resultado = clienteService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(clienteRepository).findAll();
        verify(clienteMapper).toResponseList(clientes);
    }

    @Test
    void deveRetornarClientePorId() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        when(clienteMapper.toResponse(cliente))
                .thenReturn(clienteResponse);

        ClienteResponse response = clienteService.findById(1L);

        assertNotNull(response);
        assertEquals(clienteResponse, response);

        verify(clienteRepository).findById(1L);
        verify(clienteMapper).toResponse(cliente);
    }

    @Test
    void deveLancarErroQuandoClienteNaoForEncontradoPorId() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.findById(1L)
        );

        assertEquals(
                "Cliente nao encontrado",
                exception.getMessage()
        );

        verify(clienteRepository).findById(1L);

        verify(clienteMapper, never())
                .toResponse(any());
    }

    @Test
    void deveDeletarClienteComSucesso() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.delete(1L);

        verify(clienteRepository).findById(1L);
        verify(clienteRepository).delete(cliente);
    }

    @Test
    void deveLancarErroAoDeletarClienteInexistente() {

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> clienteService.delete(1L)
        );

        assertEquals(
                "Cliente nao encontrado",
                exception.getMessage()
        );

        verify(clienteRepository).findById(1L);

        verify(clienteRepository, never())
                .delete(any());
    }

}