package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Sexo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SexoDAO {

    public List<Sexo> listarTodos() throws SQLException {
        List<Sexo> lista = new ArrayList<>();
        String sql = "SELECT id_sexo, descricao_sexo FROM \"Sexo\" ORDER BY descricao_sexo";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Sexo s = new Sexo();
                s.setId(rs.getInt("id_sexo"));
                s.setDescricao(rs.getString("descricao_sexo"));
                lista.add(s);
            }
        }
        return lista;
    }
}
