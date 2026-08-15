package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.*;

public class LogradouroDAO {

    public String buscarOuInserir(String nome, String siglaTipo) throws SQLException {
        String sqlTipo = "SELECT sigla_tipo_logradouro FROM \"TipoLogradouro\" WHERE sigla_tipo_logradouro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlTipo)) {
            stmt.setString(1, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                String insertTipo = "INSERT INTO \"TipoLogradouro\" (sigla_tipo_logradouro, nome_tipo_logradouro) VALUES (?, ?)";
                try (PreparedStatement stmtTipo = conn.prepareStatement(insertTipo)) {
                    stmtTipo.setString(1, siglaTipo);
                    stmtTipo.setString(2, siglaTipo);
                    stmtTipo.executeUpdate();
                }
            }
        }
        String sqlSelect = "SELECT id_logradouro FROM \"Logradouro\" WHERE nome_logradouro = ? AND sigla_tipo_logradouro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
            stmt.setString(1, nome);
            stmt.setString(2, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nome_logradouro");
            }
        }
        String sqlInsert = "INSERT INTO \"Logradouro\" (nome_logradouro, sigla_tipo_logradouro) VALUES (?, ?) RETURNING id_logradouro";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            stmt.setString(2, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return "";
    }
}
