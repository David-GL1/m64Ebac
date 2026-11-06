package com.ebac.m62.service;

import com.ebac.m62.dto.Moto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MotoService {

    @Autowired
    MotoRepository motoRepository;

    public Moto crearMoto(Moto moto) {
        return motoRepository.save(moto);
    }

    public Optional<Moto> obtenerMotoPorId(Long idMoto) {
        return motoRepository.findById(idMoto);
    }

    public List<Moto> obtenerMotos() {
        return motoRepository.findAll();
    }

    public Moto actualizarMoto(Moto moto) {
        return motoRepository.save(moto);
    }

    public void eliminarMoto(Long idMoto) {
        motoRepository.deleteById(idMoto);
    }

    public List<Moto> buscarPorMarca(String marca) {
        return motoRepository.findByMarca(marca);
    }
}