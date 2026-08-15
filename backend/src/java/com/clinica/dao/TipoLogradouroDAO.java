package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.TipoLogradouro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoLogradouroDAO {

    public List<TipoLogradouro> listarTodos() throws SQLException {
        List<TipoLogradouro> lista = new ArrayList<>();
        String sql = "SELECT sigla_tipo_logradouro, nome_tipo_logradouro FROM \"TipoLogradouro\" ORDER BY nome_tipo_logradouro";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                TipoLogradouro t = new TipoLogradouro();
                t.setSigla(rs.getString("sigla_tipo_logradouro"));
                t.setNome(rs.getString("nome_tipo_logradouro"));
                lista.add(t);
            }
        }
        return lista;
    }
}
