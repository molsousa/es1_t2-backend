package com.clinica.servicos;

import com.clinica.dao.SexoDAO;
import com.clinica.model.Sexo;
import java.sql.SQLException;
import java.util.List;

public class SexoService {

    private SexoDAO dao = new SexoDAO();

    public List<Sexo> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
