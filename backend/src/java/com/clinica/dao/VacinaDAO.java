package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Vacina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VacinaDAO {

    public int buscarIdVacinaPorNome(String nome) throws SQLException {
        String sql = "SELECT id_vacina FROM \"Vacina\" WHERE nome_vacina = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public int inserir(Vacina vacina, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"VacinaAplicada\" (data_aplicacao, dose, observacao, id_pet, id_vacina, id_veterinario) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_vacina_aplicada";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(vacina.getDataAplicacao()));
            stmt.setString(2, vacina.getDose() != null ? vacina.getDose() : "1");
            stmt.setString(3, vacina.getObservacao() != null ? vacina.getObservacao() : "");
            stmt.setInt(4, vacina.getPetId());
            stmt.setInt(5, buscarIdVacinaPorNome(vacina.getNomeVacina()));
            stmt.setInt(6, vacina.getIdVeterinario() > 0 ? vacina.getIdVeterinario() : 1);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Vacina> listarPendentes() throws SQLException {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT va.id_vacina_aplicada, va.data_aplicacao, va.dose, va.observacao, va.id_pet, va.id_vacina, va.id_veterinario, v.nome_vacina "
                + "FROM \"VacinaAplicada\" va JOIN \"Vacina\" v ON va.id_vacina = v.id_vacina";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vacina v = new Vacina();
                v.setId(rs.getInt("id_vacina_aplicada"));
                v.setDataAplicacao(rs.getDate("data_aplicacao").toLocalDate());
                v.setDose(rs.getString("dose"));
                v.setObservacao(rs.getString("observacao"));
                v.setPetId(rs.getInt("id_pet"));
                v.setIdVeterinario(rs.getInt("id_veterinario"));
                v.setNomeVacina(rs.getString("nome_vacina"));
                lista.add(v);
            }
        }
        return lista;
    }

    public List<Vacina> listarTodas() throws SQLException {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT id_vacina, nome_vacina, descricao_vacina FROM \"Vacina\" ORDER BY nome_vacina";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vacina v = new Vacina();
                v.setId(rs.getInt("id_vacina"));
                v.setNomeVacina(rs.getString("nome_vacina"));
                v.setObservacao(rs.getString("descricao_vacina"));
                lista.add(v);
            }
        }
        return lista;
    }

    public List<Vacina> listarTodasAplicacoes() throws SQLException {
        List<Vacina> lista = new ArrayList<>();
        String sql = "SELECT va.id_vacina_aplicada, va.data_aplicacao, va.dose, va.observacao, va.id_pet, va.id_vacina, va.id_veterinario, v.nome_vacina "
                + "FROM \"VacinaAplicada\" va JOIN \"Vacina\" v ON va.id_vacina = v.id_vacina";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Vacina v = new Vacina();
                v.setId(rs.getInt("id_vacina_aplicada"));
                v.setDataAplicacao(rs.getDate("data_aplicacao").toLocalDate());
                v.setDose(rs.getString("dose"));
                v.setObservacao(rs.getString("observacao"));
                v.setPetId(rs.getInt("id_pet"));
                v.setIdVeterinario(rs.getInt("id_veterinario"));
                v.setNomeVacina(rs.getString("nome_vacina"));
                lista.add(v);
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM \"VacinaAplicada\" WHERE id_vacina_aplicada = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Vacina não encontrada.");
            }
        }
    }
}
