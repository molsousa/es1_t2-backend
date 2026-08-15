package com.clinica.col;

import com.clinica.model.Pet;

public class PetCol {

    public static boolean validar(Pet pet) {
        if (pet.getNome() == null || pet.getNome().trim().isEmpty()) {
            return false;
        }

        if (pet.getIdTutor() <= 0) {
            return false;
        }

        if (pet.getDataNascimento() == null) {
            return false;
        }

        return true;
    }
}
