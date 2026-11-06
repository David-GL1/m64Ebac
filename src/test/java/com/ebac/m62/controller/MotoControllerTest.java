package com.ebac.m62.controller;

import com.ebac.m62.dto.Moto;
import com.ebac.m62.service.MotoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MotoControllerTest {

    @Mock
    MotoService motoService;

    @InjectMocks
    MotoController motoController;

    private Moto crearMotoDePrueba(Long id, String marca, String modelo, int anio) {
        Moto moto = new Moto();
        moto.setIdMoto(id);
        moto.setMarca(marca);
        moto.setModelo(modelo);
        moto.setAnio(anio);
        moto.setNumeroSerie("MOTO-TEST" + id);
        return moto;
    }

    // ---------- obtenerMotos ----------

    @Test
    void obtenerMotos() {
        List<Moto> motos = List.of(
                crearMotoDePrueba(1L, "Yamaha", "FZ 150", 2024),
                crearMotoDePrueba(2L, "Honda", "CB 190R", 2023));
        when(motoService.obtenerMotos()).thenReturn(motos);

        List<Moto> resultado = motoController.obtenerMotos();

        assertEquals(2, resultado.size());
        assertEquals("Yamaha", resultado.get(0).getMarca());
        verify(motoService, times(1)).obtenerMotos();
    }

    @Test
    void obtenerMotosListaVacia() {
        when(motoService.obtenerMotos()).thenReturn(List.of());

        List<Moto> resultado = motoController.obtenerMotos();

        assertTrue(resultado.isEmpty());
    }

    // ---------- obtenerMotoPorId ----------

    @Test
    void obtenerMotoPorId() {
        Moto moto = crearMotoDePrueba(1L, "Yamaha", "FZ 150", 2024);
        when(motoService.obtenerMotoPorId(1L)).thenReturn(Optional.of(moto));

        ResponseEntity<Moto> respuesta = motoController.obtenerMotoPorId(1L);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals("FZ 150", respuesta.getBody().getModelo());
    }

    @Test
    void obtenerMotoPorIdNoExiste() {
        when(motoService.obtenerMotoPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Moto> respuesta = motoController.obtenerMotoPorId(99L);

        assertEquals(404, respuesta.getStatusCode().value());
        assertNull(respuesta.getBody());
    }

    // ---------- crearMoto ----------

    @Test
    void crearMoto() throws Exception {
        Moto entrada = new Moto();
        entrada.setMarca("Suzuki");
        Moto guardada = crearMotoDePrueba(5L, "Suzuki", "Gixxer 250", 2025);
        when(motoService.crearMoto(any(Moto.class))).thenReturn(guardada);

        ResponseEntity<Moto> respuesta = motoController.crearMoto(entrada);

        assertEquals(201, respuesta.getStatusCode().value());
        assertEquals(5L, respuesta.getBody().getIdMoto());
        assertTrue(respuesta.getHeaders().getLocation().toString().endsWith("/motos/5"));
        verify(motoService, times(1)).crearMoto(entrada);
    }

    // ---------- actualizarMoto ----------

    @Test
    void actualizarMoto() {
        Moto existente = crearMotoDePrueba(2L, "Honda", "CB 190R", 2023);
        Moto cambios = new Moto();
        cambios.setMarca("Honda");
        cambios.setModelo("CB 300R");
        cambios.setAnio(2025);

        when(motoService.obtenerMotoPorId(2L)).thenReturn(Optional.of(existente));
        when(motoService.actualizarMoto(any(Moto.class))).thenAnswer(inv -> inv.getArgument(0));

        ResponseEntity<Moto> respuesta = motoController.actualizarMoto(2L, cambios);

        assertEquals(200, respuesta.getStatusCode().value());
        assertEquals(2L, respuesta.getBody().getIdMoto());
        assertEquals("CB 300R", respuesta.getBody().getModelo());
        assertEquals("MOTO-TEST2", respuesta.getBody().getNumeroSerie());
        verify(motoService, times(1)).actualizarMoto(any(Moto.class));
    }

    @Test
    void actualizarMotoNoExiste() {
        when(motoService.obtenerMotoPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Moto> respuesta = motoController.actualizarMoto(99L, new Moto());

        assertEquals(404, respuesta.getStatusCode().value());
        verify(motoService, never()).actualizarMoto(any(Moto.class));
    }

    // ---------- eliminarMoto ----------

    @Test
    void eliminarMoto() {
        doNothing().when(motoService).eliminarMoto(4L);

        ResponseEntity<Void> respuesta = motoController.eliminarMoto(4L);

        assertEquals(204, respuesta.getStatusCode().value());
        verify(motoService, times(1)).eliminarMoto(4L);
    }

    // ---------- buscarPorMarca ----------

    @Test
    void buscarPorMarca() {
        List<Moto> yamahas = List.of(crearMotoDePrueba(1L, "Yamaha", "FZ 150", 2024));
        when(motoService.buscarPorMarca("Yamaha")).thenReturn(yamahas);

        List<Moto> resultado = motoController.buscarPorMarca("Yamaha");

        assertEquals(1, resultado.size());
        assertEquals("Yamaha", resultado.get(0).getMarca());
        verify(motoService, times(1)).buscarPorMarca("Yamaha");
    }

    @Test
    void buscarPorMarcaSinResultados() {
        when(motoService.buscarPorMarca("Ducati")).thenReturn(List.of());

        List<Moto> resultado = motoController.buscarPorMarca("Ducati");

        assertTrue(resultado.isEmpty());
    }
}