package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.*;
import com.clinica.model.*;
import java.util.*;

public class UnidadeFederativaDAO {
    
    public List<UnidadeFederativa> listarTodos() throws SQLException {
        List<UnidadeFederativa> lista = new ArrayList<>();
        String sql = "SELECT sigla_uf, nome_uf FROM \"UnidadeFederativa\" ORDER BY nome_uf";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                UnidadeFederativa uf = new UnidadeFederativa();
                uf.setSigla(rs.getString("sigla_uf"));
                uf.setNome(rs.getString("nome_uf"));
                lista.add(uf);
            }
        }
        return lista;
    }
}
