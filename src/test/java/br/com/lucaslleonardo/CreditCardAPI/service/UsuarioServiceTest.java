package br.com.lucaslleonardo.CreditCardAPI.service;

import br.com.lucaslleonardo.CreditCardAPI.database.entity.UsuarioEntity;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPatch.UsuarioPatchRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoRequest.dtoPost.UsuarioPostRequest;
import br.com.lucaslleonardo.CreditCardAPI.dto.dtoResponse.UsuarioResponse;
import br.com.lucaslleonardo.CreditCardAPI.mappers.UsuarioMapper;
import br.com.lucaslleonardo.CreditCardAPI.repository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
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
class UsuarioServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioEntity usuario;
    private UsuarioPostRequest usuarioPostRequest;
    private UsuarioPatchRequest usuarioPatchRequest;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {

        usuario = UsuarioEntity.builder()
                .id(1L)
                .nome("Lucas")
                .email("lucas@email.com")
                .senha("123456")
                .build();

        usuarioPostRequest = UsuarioPostRequest.builder()
                .nome("Lucas")
                .email("lucas@email.com")
                .senha("123456")
                .build();

        usuarioPatchRequest = UsuarioPatchRequest.builder()
                .nome("Lucas Alterado")
                .email("lucasalterado@email.com")
                .senha("654321")
                .build();

        usuarioResponse = UsuarioResponse.builder()
                .id(1L)
                .nome("Lucas")
                .email("lucas@email.com")
                .build();
    }


    @Test
    void deveSalvarUsuarioComSucesso() {

        when(usuarioRepository.findByEmail(usuarioPostRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(usuarioMapper.toEntity(usuarioPostRequest))
                .thenReturn(usuario);

        when(usuarioRepository.save(usuario))
                .thenReturn(usuario);

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(usuarioResponse);

        UsuarioResponse response = usuarioService.save(usuarioPostRequest);

        assertNotNull(response);
        assertEquals(usuarioResponse, response);

        verify(usuarioRepository).findByEmail(usuarioPostRequest.getEmail());
        verify(usuarioMapper).toEntity(usuarioPostRequest);
        verify(usuarioRepository).save(usuario);
        verify(usuarioMapper).toResponse(usuario);
    }


    @Test
    void deveLancarErroAoSalvarUsuarioComEmailJaExistente() {

        when(usuarioRepository.findByEmail(usuarioPostRequest.getEmail()))
                .thenReturn(Optional.of(usuario));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.save(usuarioPostRequest)
        );

        assertEquals("Email ja utilizado", exception.getMessage());

        verify(usuarioRepository).findByEmail(usuarioPostRequest.getEmail());

        verify(usuarioMapper, never()).toEntity(any());
        verify(usuarioRepository, never()).save(any());
    }


    @Test
    void deveLancarErroQuandoRepositoryFalharAoSalvar() {

        when(usuarioRepository.findByEmail(usuarioPostRequest.getEmail()))
                .thenReturn(Optional.empty());

        when(usuarioMapper.toEntity(usuarioPostRequest))
                .thenReturn(usuario);

        when(usuarioRepository.save(usuario))
                .thenThrow(new RuntimeException("Erro no banco"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.save(usuarioPostRequest)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(usuarioRepository).save(usuario);
    }


    @Test
    void deveAtualizarUsuarioComSucesso() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.update(usuarioPatchRequest, 1L);

        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper).update(usuarioPatchRequest, usuario);
        verify(usuarioRepository).save(usuario);
    }


    @Test
    void deveLancarErroAoAtualizarUsuarioInexistente() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.update(usuarioPatchRequest, 1L)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(1L);

        verify(usuarioMapper, never())
                .update(any(), any());

        verify(usuarioRepository, never())
                .save(any());
    }


    @Test
    void deveLancarErroQuandoRepositoryFalharAoAtualizar() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        doThrow(new RuntimeException("Erro no banco"))
                .when(usuarioRepository)
                .save(usuario);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.update(usuarioPatchRequest, 1L)
        );

        assertEquals("Erro no banco", exception.getMessage());

        verify(usuarioRepository).save(usuario);
    }


    @Test
    void deveRetornarTodosUsuarios() {

        List<UsuarioEntity> usuarios = List.of(usuario);

        List<UsuarioResponse> responses = List.of(usuarioResponse);

        when(usuarioRepository.findAll())
                .thenReturn(usuarios);

        when(usuarioMapper.toResponseList(usuarios))
                .thenReturn(responses);

        List<UsuarioResponse> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertEquals(responses, resultado);

        verify(usuarioRepository).findAll();
        verify(usuarioMapper).toResponseList(usuarios);
    }


    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremUsuarios() {

        List<UsuarioEntity> usuarios = List.of();
        List<UsuarioResponse> responses = List.of();

        when(usuarioRepository.findAll())
                .thenReturn(usuarios);

        when(usuarioMapper.toResponseList(usuarios))
                .thenReturn(responses);

        List<UsuarioResponse> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioRepository).findAll();
        verify(usuarioMapper).toResponseList(usuarios);
    }


    @Test
    void deveRetornarUsuarioPorId() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioMapper.toResponse(usuario))
                .thenReturn(usuarioResponse);

        UsuarioResponse response = usuarioService.findById(1L);

        assertNotNull(response);
        assertEquals(usuarioResponse, response);

        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper).toResponse(usuario);
    }


    @Test
    void deveLancarErroQuandoUsuarioNaoForEncontradoPorId() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.findById(1L)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(1L);
        verify(usuarioMapper, never()).toResponse(any());
    }


    @Test
    void deveDeletarUsuarioComSucesso() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.delete(1L);

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository).delete(usuario);
    }


    @Test
    void deveLancarErroAoDeletarUsuarioInexistente() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.delete(1L)
        );

        assertEquals("Usuario nao encontrado", exception.getMessage());

        verify(usuarioRepository).findById(1L);
        verify(usuarioRepository, never()).delete(any());
    }
}