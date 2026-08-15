package com.clinica.servicos;

import com.clinica.dao.RacaDAO;
import com.clinica.model.Raca;
import java.sql.SQLException;
import java.util.List;

public class RacaService {

    private RacaDAO dao = new RacaDAO();

    public List<Raca> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
