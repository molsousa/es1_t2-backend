/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinica.model;

import java.time.LocalDate;

/**
 *
 * @author molsousa
 */
public class Vacina {

    private int id;
    private String nomeVacina;
    private LocalDate dataAplicacao;
    private String dose;
    private String observacao;
    private int idPet;
    private int idVeterinario;
    private int petId;
    private int periodicidadeMeses;
    private LocalDate dataProxima;

    public LocalDate getDataProxima() {
        return dataProxima;
    }

    public void setDataProxima(LocalDate dataProxima) {
        this.dataProxima = dataProxima;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getPeriodicidadeMeses() {
        return periodicidadeMeses;
    }

    public void setPeriodicidadeMeses(int periodicidadeMeses) {
        this.periodicidadeMeses = periodicidadeMeses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeVacina() {
        return nomeVacina;
    }

    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    public LocalDate getDataAplicacao() {
        return dataAplicacao;
    }

    public void setDataAplicacao(LocalDate dataAplicacao) {
        this.dataAplicacao = dataAplicacao;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(int idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

}
