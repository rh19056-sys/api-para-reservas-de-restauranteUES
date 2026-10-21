package sv.edu.ues.reservas.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.ues.reservas.domain.entity.Menu;
import sv.edu.ues.reservas.domain.entity.MenuPlato;
import sv.edu.ues.reservas.domain.entity.Plato;
import sv.edu.ues.reservas.domain.entity.RestauranteSucursal;
import sv.edu.ues.reservas.dto.catalogo.MenuPlatoRequest;
import sv.edu.ues.reservas.repository.BebidaRepository;
import sv.edu.ues.reservas.repository.MenuBebidaRepository;
import sv.edu.ues.reservas.repository.MenuPlatoRepository;
import sv.edu.ues.reservas.repository.MenuRepository;
import sv.edu.ues.reservas.repository.PlatoRepository;
import sv.edu.ues.reservas.repository.RestauranteSucursalRepository;
import sv.edu.ues.reservas.repository.VinoRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private PlatoRepository platoRepository;
    @Mock
    private BebidaRepository bebidaRepository;
    @Mock
    private VinoRepository vinoRepository;
    @Mock
    private MenuPlatoRepository menuPlatoRepository;
    @Mock
    private MenuBebidaRepository menuBebidaRepository;
    @Mock
    private RestauranteSucursalRepository sucursalRepository;

    @InjectMocks
    private CatalogoService catalogoService;

    @Test
    void asignarPlatoRechazaProductoDeOtraSucursal() {
        UUID idMenu = UUID.randomUUID();
        UUID idPlato = UUID.randomUUID();
        RestauranteSucursal sucursalMenu = sucursal(UUID.randomUUID());
        RestauranteSucursal sucursalPlato = sucursal(UUID.randomUUID());
        Menu menu = new Menu();
        menu.setIdMenu(idMenu);
        menu.setSucursal(sucursalMenu);
        Plato plato = new Plato();
        plato.setIdPlato(idPlato);
        plato.setSucursal(sucursalPlato);

        when(menuRepository.findById(idMenu)).thenReturn(Optional.of(menu));
        when(platoRepository.findById(idPlato)).thenReturn(Optional.of(plato));

        assertThrows(IllegalArgumentException.class,
                () -> catalogoService.asignarPlato(idMenu, new MenuPlatoRequest(idPlato, 1, 1)));
        verify(menuPlatoRepository, never()).save(any(MenuPlato.class));
    }

    @Test
    void asignarPlatoGuardaRelacionDeLaMismaSucursal() {
        UUID idSucursal = UUID.randomUUID();
        UUID idMenu = UUID.randomUUID();
        UUID idPlato = UUID.randomUUID();
        RestauranteSucursal sucursal = sucursal(idSucursal);
        Menu menu = new Menu();
        menu.setIdMenu(idMenu);
        menu.setSucursal(sucursal);
        Plato plato = new Plato();
        plato.setIdPlato(idPlato);
        plato.setNombre("Sopa");
        plato.setSucursal(sucursal);

        when(menuRepository.findById(idMenu)).thenReturn(Optional.of(menu));
        when(platoRepository.findById(idPlato)).thenReturn(Optional.of(plato));
        when(menuPlatoRepository.existsById(any())).thenReturn(false);
        when(menuPlatoRepository.save(any(MenuPlato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = catalogoService.asignarPlato(idMenu,
                new MenuPlatoRequest(idPlato, 2, 1));

        assertEquals(idPlato, response.idPlato());
        assertEquals("Sopa", response.nombrePlato());
        assertEquals(2, response.cantidad());
        verify(menuPlatoRepository).save(any(MenuPlato.class));
    }

    private RestauranteSucursal sucursal(UUID id) {
        RestauranteSucursal sucursal = new RestauranteSucursal();
        sucursal.setIdSucursal(id);
        return sucursal;
    }
}
