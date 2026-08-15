package com.clinica.col;

import com.clinica.model.Consulta;

public class ConsultaCol {

    public static boolean validar(Consulta consulta) {
        if (consulta.getDataConsulta() == null) {
            return false;
        }

        if (consulta.getHoraConsulta() == null) {
            return false;
        }

        if (consulta.getIdPet() <= 0) {
            return false;
        }

        if (consulta.getIdVeterinario() <= 0) {
            return false;
        }

        return true;
    }
}
