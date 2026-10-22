package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.RestauranteOrg;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.domain.enums.EstadoSucursal;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalActualizarRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalCrearRequest;
import sv.edu.ues.reservas.dto.restaurantesucursal.RestauranteSucursalResponse;
import sv.edu.ues.reservas.exception.RecursoNoEncontradoException;
import sv.edu.ues.reservas.repository.RestauranteOrgRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Nota: crear() aún no asigna estadoSucursal (la BD exige NOT NULL);
// por eso no se verifica el estado en la creación. Ver PR del issue #36.
@ExtendWith(MockitoExtension.class)
class RestauranteSucursalServiceTest {

    @Mock
    private RestauranteSucursalRepository restauranteSucursalRepository;
    @Mock
    private RestauranteOrgRepository restauranteOrgRepository;

    @InjectMocks
    private RestauranteSucursalService restauranteSucursalService;

    @Test
    void crearAsociaOrganizacionYMapeaRespuesta() {
        RestauranteOrg org = organizacion();
        UUID idGenerado = UUID.randomUUID();
        when(restauranteOrgRepository.findById(org.getIdRestauranteOrg()))
                .thenReturn(Optional.of(org));
        when(restauranteSucursalRepository.save(any(RestauranteSucursal.class)))
                .thenAnswer(invocation -> {
                    RestauranteSucursal sucursal = invocation.getArgument(0);
                    sucursal.setIdSucursal(idGenerado);
                    return sucursal;
                });

        RestauranteSucursalResponse response = restauranteSucursalService.crear(
                new RestauranteSucursalCrearRequest(
                        org.getIdRestauranteOrg(), "  Calle Arce #10  ", 40, "Salvadoreña"));

        ArgumentCaptor<RestauranteSucursal> guardada =
                ArgumentCaptor.forClass(RestauranteSucursal.class);
        verify(restauranteSucursalRepository).save(guardada.capture());
        assertSame(org, guardada.getValue().getRestauranteOrg());
        assertEquals("Calle Arce #10", guardada.getValue().getDireccion());

        assertEquals(idGenerado, response.idSucursal());
        assertEquals(org.getIdRestauranteOrg(), response.idRestauranteOrg());
        assertEquals("Calle Arce #10", response.direccion());
        assertEquals(40, response.capacidad());
        assertEquals("Salvadoreña", response.tipoCocina());
    }

    @Test
    void crearConOrganizacionInexistenteNoGuarda() {
        UUID idOrg = UUID.randomUUID();
        when(restauranteOrgRepository.findById(idOrg)).thenReturn(Optional.empty());

        RecursoNoEncontradoException ex = assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteSucursalService.crear(
                        new RestauranteSucursalCrearRequest(idOrg, "Calle Arce", 40, null)));
        assertTrue(ex.getMessage().contains(idOrg.toString()));
        verify(restauranteSucursalRepository, never()).save(any(RestauranteSucursal.class));
    }

    @Test
    void obtenerPorIdDevuelveSucursal() {
        RestauranteSucursal sucursal = sucursal(organizacion());
        when(restauranteSucursalRepository.findById(sucursal.getIdSucursal()))
                .thenReturn(Optional.of(sucursal));

        RestauranteSucursalResponse response =
                restauranteSucursalService.obtenerPorId(sucursal.getIdSucursal());

        assertEquals(sucursal.getIdSucursal(), response.idSucursal());
        assertEquals("Calle Arce #10", response.direccion());
        assertEquals(EstadoSucursal.ACTIVA, response.estadoSucursal());
    }

    @Test
    void obtenerPorIdInexistenteLanzaNoEncontrado() {
        UUID id = UUID.randomUUID();
        when(restauranteSucursalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteSucursalService.obtenerPorId(id));
    }

    @Test
    void obtenerTodasMapeaCadaSucursal() {
        RestauranteOrg org = organizacion();
        when(restauranteSucursalRepository.findAll())
                .thenReturn(List.of(sucursal(org), sucursal(org)));

        assertEquals(2, restauranteSucursalService.obtenerTodas().size());
    }

    @Test
    void obtenerPorRestauranteOrgDevuelveSusSucursales() {
        RestauranteOrg org = organizacion();
        when(restauranteOrgRepository.existsById(org.getIdRestauranteOrg())).thenReturn(true);
        when(restauranteSucursalRepository
                .findByRestauranteOrg_idRestauranteOrg(org.getIdRestauranteOrg()))
                .thenReturn(List.of(sucursal(org)));

        List<RestauranteSucursalResponse> response =
                restauranteSucursalService.obtenerPorRestauranteOrg(org.getIdRestauranteOrg());

        assertEquals(1, response.size());
        assertEquals(org.getIdRestauranteOrg(), response.get(0).idRestauranteOrg());
    }

    @Test
    void obtenerPorRestauranteOrgInexistenteNoConsultaSucursales() {
        UUID idOrg = UUID.randomUUID();
        when(restauranteOrgRepository.existsById(idOrg)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteSucursalService.obtenerPorRestauranteOrg(idOrg));
        verify(restauranteSucursalRepository, never())
                .findByRestauranteOrg_idRestauranteOrg(any());
    }

    @Test
    void actualizarSoloModificaCamposEnviados() {
        RestauranteSucursal sucursal = sucursal(organizacion());
        when(restauranteSucursalRepository.findById(sucursal.getIdSucursal()))
                .thenReturn(Optional.of(sucursal));
        when(restauranteSucursalRepository.save(sucursal)).thenReturn(sucursal);

        RestauranteSucursalResponse response = restauranteSucursalService.actualizar(
                sucursal.getIdSucursal(),
                new RestauranteSucursalActualizarRequest(null, 80, " Mariscos "));

        assertEquals("Calle Arce #10", response.direccion());
        assertEquals(80, response.capacidad());
        assertEquals("Mariscos", response.tipoCocina());
        verify(restauranteSucursalRepository).save(sucursal);
    }

    @Test
    void actualizarInexistenteNoGuarda() {
        UUID id = UUID.randomUUID();
        when(restauranteSucursalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteSucursalService.actualizar(
                        id, new RestauranteSucursalActualizarRequest("Otra", null, null)));
        verify(restauranteSucursalRepository, never()).save(any(RestauranteSucursal.class));
    }

    @Test
    void eliminarBorraLaSucursalEncontrada() {
        RestauranteSucursal sucursal = sucursal(organizacion());
        when(restauranteSucursalRepository.findById(sucursal.getIdSucursal()))
                .thenReturn(Optional.of(sucursal));

        restauranteSucursalService.eliminar(sucursal.getIdSucursal());

        verify(restauranteSucursalRepository).delete(sucursal);
    }

    @Test
    void eliminarInexistenteNoBorra() {
        UUID id = UUID.randomUUID();
        when(restauranteSucursalRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> restauranteSucursalService.eliminar(id));
        verify(restauranteSucursalRepository, never()).delete(any(RestauranteSucursal.class));
    }

    private RestauranteOrg organizacion() {
        RestauranteOrg org = new RestauranteOrg();
        org.setIdRestauranteOrg(UUID.randomUUID());
        org.setNombre("Grupo Pupusas");
        return org;
    }

    private RestauranteSucursal sucursal(RestauranteOrg org) {
        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(UUID.randomUUID());
        sucursal.setRestauranteOrg(org);
        sucursal.setDireccion("Calle Arce #10");
        sucursal.setCapacidad(40);
        sucursal.setTipoCocina("Salvadoreña");
        sucursal.setEstadoSucursal(EstadoSucursal.ACTIVA);
        return sucursal;
    }
}
