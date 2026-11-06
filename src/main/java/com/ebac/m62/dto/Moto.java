package com.ebac.m62.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.util.UUID;

@Entity
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMoto;

    private String marca;
    private String modelo;
    private int anio;
    private String numeroSerie;

    @PrePersist
    public void generarNumeroSerie() {
        if (numeroSerie == null) {
            numeroSerie = "MOTO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
    }

    public Long getIdMoto() {return idMoto;}
    public void setIdMoto(Long idMoto) { this.idMoto = idMoto; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
}