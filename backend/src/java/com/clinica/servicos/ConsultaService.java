package com.clinica.servicos;

import com.clinica.col.ConsultaCol;
import com.clinica.dao.ConsultaDAO;
import com.clinica.model.Consulta;

import java.sql.SQLException;
import java.util.List;

public class ConsultaService {

    private ConsultaDAO dao = new ConsultaDAO();

    public int agendar(Consulta consulta) throws SQLException, IllegalArgumentException {
        if (!ConsultaCol.validar(consulta)) {
            throw new IllegalArgumentException("Dados da consulta inválidos.");
        }
        return dao.inserir(consulta);
    }

    public List<Consulta> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}
