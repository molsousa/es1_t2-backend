package com.clinica.col;

import com.clinica.model.Tutor;

public class TutorCol {

    public static boolean validar(Tutor tutor) {
        if (tutor.getNome() == null || tutor.getNome().trim().isEmpty()) {
            return false;
        }

        if (tutor.getCpf() == null || tutor.getCpf().length() != 11) {
            return false;
        }
        return true;
    }
}
