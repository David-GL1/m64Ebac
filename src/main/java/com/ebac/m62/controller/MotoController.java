package com.ebac.m62.controller;

import com.ebac.m62.dto.Moto;
import com.ebac.m62.service.MotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class MotoController {

    @Autowired
    MotoService motoService;

    @GetMapping("/motos")
    public List<Moto> obtenerMotos() {
        return motoService.obtenerMotos();
    }

    @GetMapping("/motos/{id}")
    public ResponseEntity<Moto> obtenerMotoPorId(@PathVariable Long id) {
        Optional<Moto> motoOptional = motoService.obtenerMotoPorId(id);
        return motoOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/motos")
    public ResponseEntity<Moto> crearMoto(@RequestBody Moto moto) throws URISyntaxException {
        Moto motoCreada = motoService.crearMoto(moto);
        return ResponseEntity.created(new URI("http://localhost:8080/motos/" + motoCreada.getIdMoto()))
                .body(motoCreada);
    }

    @PutMapping("/motos/{id}")
    public ResponseEntity<Moto> actualizarMoto(@PathVariable Long id, @RequestBody Moto motoActualizada) {
        Optional<Moto> motoOptional = motoService.obtenerMotoPorId(id);

        if (motoOptional.isPresent()) {
            motoActualizada.setIdMoto(id);
            motoActualizada.setNumeroSerie(motoOptional.get().getNumeroSerie());
            return ResponseEntity.ok(motoService.actualizarMoto(motoActualizada));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/motos/{id}")
    public ResponseEntity<Void> eliminarMoto(@PathVariable Long id) {
        motoService.eliminarMoto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/motos/marca/{marca}")
    public List<Moto> buscarPorMarca(@PathVariable String marca) {
        return motoService.buscarPorMarca(marca);
    }
}