package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.RestauranteOrg;
import sv.edu.ues.reservas.domain.enums.EstadoOrganizacion;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgActualizarRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgCrearRequest;
import sv.edu.ues.reservas.dto.restaurante.RestauranteOrgResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.RestauranteOrgRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteOrgServiceTest {

    @Mock
    private RestauranteOrgRepository restauranteOrgRepository;

    @InjectMocks
    private RestauranteOrgService restauranteOrgService;

    @Test
    void crearGuardaNombreRecortadoYEstadoActiva() {
        UUID idGenerado = UUID.randomUUID();
        when(restauranteOrgRepository.save(any(RestauranteOrg.class)))
                .thenAnswer(invocation -> {
                    RestauranteOrg org = invocation.getArgument(0);
                    org.setIdRestauranteOrg(idGenerado);
                    return org;
                });

        RestauranteOrgResponse response = restauranteOrgService.crear(
                new RestauranteOrgCrearRequest("  Grupo Pupusas  "));

        ArgumentCaptor<RestauranteOrg> guardada = ArgumentCaptor.forClass(RestauranteOrg.class);
        verify(restauranteOrgRepository).save(guardada.capture());
        assertEquals("Grupo Pupusas", guardada.getValue().getNombre());
        assertEquals(EstadoOrganizacion.ACTIVA, guardada.getValue().getEstadoOrganizacion());

        assertEquals(idGenerado, response.idRestauranteOrg());
        assertEquals("Grupo Pupusas", response.nombre());
        assertEquals(EstadoOrganizacion.ACTIVA, response.estadoOrganizacion());
    }

    @Test
    void obtenerPorIdDevuelveOrganizacion() {
        RestauranteOrg org = organizacion("Grupo Pupusas");
        when(restauranteOrgRepository.findById(org.getIdRestauranteOrg()))
                .thenReturn(Optional.of(org));

        RestauranteOrgResponse response =
                restauranteOrgService.obtenerPorId(org.getIdRestauranteOrg());

        assertEquals(org.getIdRestauranteOrg(), response.idRestauranteOrg());
        assertEquals("Grupo Pupusas", response.nombre());
    }

    @Test
    void obtenerPorIdInexistenteLanzaNoEncontrado() {
        UUID id = UUID.randomUUID();
        when(restauranteOrgRepository.findById(id)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteOrgService.obtenerPorId(id));
        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    void obtenerTodasMapeaCadaOrganizacion() {
        when(restauranteOrgRepository.findAll()).thenReturn(List.of(
                organizacion("Grupo A"),
                organizacion("Grupo B")
        ));

        List<RestauranteOrgResponse> response = restauranteOrgService.obtenerTodas();

        assertEquals(2, response.size());
        assertEquals("Grupo A", response.get(0).nombre());
        assertEquals("Grupo B", response.get(1).nombre());
    }

    @Test
    void obtenerTodasSinRegistrosDevuelveListaVacia() {
        when(restauranteOrgRepository.findAll()).thenReturn(List.of());

        assertTrue(restauranteOrgService.obtenerTodas().isEmpty());
    }

    @Test
    void actualizarCambiaNombreRecortado() {
        RestauranteOrg org = organizacion("Nombre viejo");
        when(restauranteOrgRepository.findById(org.getIdRestauranteOrg()))
                .thenReturn(Optional.of(org));
        when(restauranteOrgRepository.save(org)).thenReturn(org);

        RestauranteOrgResponse response = restauranteOrgService.actualizar(
                org.getIdRestauranteOrg(),
                new RestauranteOrgActualizarRequest("  Nombre nuevo "));

        assertEquals("Nombre nuevo", response.nombre());
        verify(restauranteOrgRepository).save(org);
    }

    @Test
    void actualizarSinNombreConservaElActual() {
        RestauranteOrg org = organizacion("Grupo Pupusas");
        when(restauranteOrgRepository.findById(org.getIdRestauranteOrg()))
                .thenReturn(Optional.of(org));
        when(restauranteOrgRepository.save(org)).thenReturn(org);

        RestauranteOrgResponse response = restauranteOrgService.actualizar(
                org.getIdRestauranteOrg(),
                new RestauranteOrgActualizarRequest(null));

        assertEquals("Grupo Pupusas", response.nombre());
        assertEquals(EstadoOrganizacion.ACTIVA, response.estadoOrganizacion());
    }

    @Test
    void actualizarInexistenteNoGuarda() {
        UUID id = UUID.randomUUID();
        when(restauranteOrgRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteOrgService.actualizar(
                        id, new RestauranteOrgActualizarRequest("Otro")));
        verify(restauranteOrgRepository, never()).save(any(RestauranteOrg.class));
    }

    @Test
    void eliminarBorraLaOrganizacionEncontrada() {
        RestauranteOrg org = organizacion("Grupo Pupusas");
        when(restauranteOrgRepository.findById(org.getIdRestauranteOrg()))
                .thenReturn(Optional.of(org));

        restauranteOrgService.eliminar(org.getIdRestauranteOrg());

        verify(restauranteOrgRepository).delete(org);
    }

    @Test
    void eliminarInexistenteNoBorra() {
        UUID id = UUID.randomUUID();
        when(restauranteOrgRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteOrgService.eliminar(id));
        verify(restauranteOrgRepository, never()).delete(any(RestauranteOrg.class));
    }

    private RestauranteOrg organizacion(String nombre) {
        RestauranteOrg org = new RestauranteOrg();
        org.setIdRestauranteOrg(UUID.randomUUID());
        org.setNombre(nombre);
        org.setEstadoOrganizacion(EstadoOrganizacion.ACTIVA);
        return org;
    }
}
