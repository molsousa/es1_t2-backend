package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Especie;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspecieDAO {

    public List<Especie> listarTodos() throws SQLException {
        List<Especie> lista = new ArrayList<>();
        String sql = "SELECT id_especie, descricao_especie FROM \"Especie\" ORDER BY descricao_especie";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Especie e = new Especie();
                e.setId(rs.getInt("id_especie"));
                e.setDescricao(rs.getString("descricao_especie"));
                lista.add(e);
            }
        }
        return lista;
    }
}
