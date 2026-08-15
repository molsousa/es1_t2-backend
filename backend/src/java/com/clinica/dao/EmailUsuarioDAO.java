package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class EmailUsuarioDAO {

    public void inserir(int idUsuario, String email, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"EmailUsuario\" (email_usuario, id_usuario) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setInt(2, idUsuario);
            stmt.executeUpdate();
        }
    }

    public List<String> listarPorUsuario(int idUsuario) throws SQLException {
        List<String> emails = new ArrayList<>();
        String sql = "SELECT email_usuario FROM \"EmailUsuario\" WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                emails.add(rs.getString("email_usuario"));
            }
        }
        return emails;
    }
}
