package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.*;

public class BairroDAO {

    public String buscarOuInserir(String nome) throws SQLException {
        String sqlSelect = "SELECT id_bairro FROM \"Bairro\" WHERE nome_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("nome_bairro");
            }
        }
        String sqlInsert = "INSERT INTO \"Bairro\" (nome_bairro) VALUES (?) RETURNING id_bairro";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        return "";
    }
}
