package com.clinica.col;

import com.clinica.model.Veterinario;

public class VeterinarioCol {

    public static boolean validar(Veterinario vet) {
        if (vet.getNome() == null || vet.getNome().trim().isEmpty()) {
            return false;
        }

        String cpfLimpo = vet.getCpf().replaceAll("[^0-9]", "");

        if (cpfLimpo.length() != 11) {
            return false;
        }

        if (vet.getCrmv() == null || vet.getCrmv().trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
