package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.*;

public class CidadeDAO {

    public String buscarOuInserir(String nomeCidade, String siglaUf) throws SQLException {
        String sqlSelect = "SELECT id_cidade FROM \"Cidade\" WHERE nome_cidade = ? AND sigla_uf = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
            stmt.setString(1, nomeCidade);
            stmt.setString(2, siglaUf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nome_cidade");
            }
        }
        String sqlInsert = "INSERT INTO \"Cidade\" (nome_cidade, sigla_uf) VALUES (?, ?) RETURNING id_cidade";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nomeCidade);
            stmt.setString(2, siglaUf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return "";
    }
}
