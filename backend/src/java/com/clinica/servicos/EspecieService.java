package com.clinica.servicos;

import com.clinica.dao.EspecieDAO;
import com.clinica.model.Especie;
import java.sql.SQLException;
import java.util.List;

public class EspecieService {

    private EspecieDAO dao = new EspecieDAO();

    public List<Especie> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
