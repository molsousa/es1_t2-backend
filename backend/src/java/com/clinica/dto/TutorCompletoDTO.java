package com.clinica.dto;

import com.clinica.model.Tutor;
import com.clinica.model.Endereco;
import java.util.List;

public class TutorCompletoDTO {

    private Tutor tutor;
    private Endereco endereco;
    private String numero;
    private String complemento;
    private List<TelefoneDTO> telefones;
    private List<String> emails;
    private String siglaTipoLogradouro;

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public List<TelefoneDTO> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<TelefoneDTO> telefones) {
        this.telefones = telefones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getSiglaTipoLogradouro() {
        return siglaTipoLogradouro;
    }

    public void setSiglaTipoLogradouro(String siglaTipoLogradouro) {
        this.siglaTipoLogradouro = siglaTipoLogradouro;
    }

}
