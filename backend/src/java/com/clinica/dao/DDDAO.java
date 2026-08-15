package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DDDAO {

    public List<Integer> listarDDDs() throws SQLException {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT nro_ddd FROM \"DDD\" ORDER BY nro_ddd";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(rs.getInt("nro_ddd"));
            }
        }
        return lista;
    }
}
