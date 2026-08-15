package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.clinica.dto.TelefoneDTO;
import java.util.*;

public class FoneUsuarioDAO {

    public void inserir(int idUsuario, int ddi, int ddd, String numero, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"FoneUsuario\" (nro_telefone_usuario, nro_ddi, nro_ddd, id_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numero);
            stmt.setInt(2, ddi);
            stmt.setInt(3, ddd);
            stmt.setInt(4, idUsuario);
            stmt.executeUpdate();
        }
    }

    public List<TelefoneDTO> listarPorUsuario(int idUsuario) throws SQLException {
        List<TelefoneDTO> lista = new ArrayList<>();
        String sql = "SELECT nro_telefone_usuario, nro_ddi, nro_ddd FROM \"FoneUsuario\" WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TelefoneDTO tel = new TelefoneDTO();
                tel.setNumero(rs.getString("nro_telefone_usuario"));
                tel.setDdi(rs.getInt("nro_ddi"));
                tel.setDdd(rs.getInt("nro_ddd"));
                lista.add(tel);
            }
        }
        return lista;
    }
}
