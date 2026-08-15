package com.clinica.servicos;

import com.clinica.dao.TipoLogradouroDAO;
import com.clinica.model.TipoLogradouro;
import java.sql.SQLException;
import java.util.List;

public class TipoLogradouroService {

    private TipoLogradouroDAO dao = new TipoLogradouroDAO();

    public List<TipoLogradouro> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
