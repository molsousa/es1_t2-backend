package com.clinica.servicos;

import com.clinica.col.ConsultaCol;
import com.clinica.dao.ConsultaDAO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Consulta;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ConsultaService {

    private ConsultaDAO dao = new ConsultaDAO();

    public int agendar(Consulta consulta) throws SQLException, IllegalArgumentException {
        if (!ConsultaCol.validar(consulta)) {
            throw new IllegalArgumentException("Dados da consulta inválidos.");
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            int id = dao.inserir(consulta, conn);

            conn.commit();
            return id;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Consulta> listarTodos() throws SQLException {
        return dao.listarTodos();
    }
}