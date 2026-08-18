package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Consulta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public int inserir(Consulta consulta, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"Consulta\" (data_consulta, hora_consulta, motivo, id_pet, id_veterinario, id_atendente) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_consulta";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(consulta.getDataConsulta()));
            stmt.setTime(2, Time.valueOf(consulta.getHoraConsulta()));
            stmt.setString(3, consulta.getMotivo());
            stmt.setInt(4, consulta.getIdPet());
            stmt.setInt(5, consulta.getIdVeterinario());
            stmt.setInt(6, consulta.getIdAtendente());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Consulta> listarTodos() throws SQLException {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT id_consulta, data_consulta, hora_consulta, motivo, id_pet, id_veterinario, id_atendente FROM \"Consulta\"";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Consulta c = new Consulta();
                c.setId(rs.getInt("id_consulta"));
                c.setDataConsulta(rs.getDate("data_consulta").toLocalDate());
                c.setHoraConsulta(rs.getTime("hora_consulta").toLocalTime());
                c.setMotivo(rs.getString("motivo"));
                c.setIdPet(rs.getInt("id_pet"));
                c.setIdVeterinario(rs.getInt("id_veterinario"));
                c.setIdAtendente(rs.getInt("id_atendente"));
                lista.add(c);
            }
        }
        return lista;
    }
}