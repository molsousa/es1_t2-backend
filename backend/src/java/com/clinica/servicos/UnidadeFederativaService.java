package com.clinica.servicos;

import com.clinica.dao.UnidadeFederativaDAO;
import com.clinica.model.UnidadeFederativa;
import java.sql.SQLException;
import java.util.List;

public class UnidadeFederativaService {

    private UnidadeFederativaDAO dao = new UnidadeFederativaDAO();

    public List<UnidadeFederativa> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
