package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Raca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RacaDAO {

    public List<Raca> listarTodos() throws SQLException {
        List<Raca> lista = new ArrayList<>();
        String sql = "SELECT id_raca, descricao_raca FROM \"Raca\" ORDER BY descricao_raca";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Raca r = new Raca();
                r.setId(rs.getInt("id_raca"));
                r.setDescricao(rs.getString("descricao_raca"));
                lista.add(r);
            }
        }
        return lista;
    }
}
