package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAO {

    public int inserir(Pet pet) throws SQLException {
        String sql = "INSERT INTO \"Pet\" (nome_pet, data_nascimento, id_tutor, id_especie, id_raca, id_sexo) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_pet";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setDate(2, Date.valueOf(pet.getDataNascimento()));
            stmt.setInt(3, pet.getTutorId());
            stmt.setInt(4, pet.getIdEspecie());
            stmt.setInt(5, pet.getIdRaca());
            stmt.setInt(6, pet.getIdSexo());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Pet> listarPorTutor(int idTutor) throws SQLException {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT id_pet, nome_pet, data_nascimento, id_especie, id_raca, id_sexo FROM \"Pet\" WHERE id_tutor = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTutor);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Pet p = new Pet();
                p.setId(rs.getInt("id_pet"));
                p.setNome(rs.getString("nome_pet"));
                p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                p.setIdEspecie(rs.getInt("id_especie"));
                p.setIdRaca(rs.getInt("id_raca"));
                p.setIdSexo(rs.getInt("id_sexo"));
                p.setTutorId(idTutor);
                lista.add(p);
            }
        }
        return lista;
    }

    public List<Pet> listarTodos() throws SQLException {
        List<Pet> lista = new ArrayList<>();
        String sql = "SELECT id_pet, nome_pet, data_nascimento, id_tutor, id_especie, id_raca, id_sexo FROM \"Pet\"";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Pet p = new Pet();
                p.setId(rs.getInt("id_pet"));
                p.setNome(rs.getString("nome_pet"));
                p.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                p.setTutorId(rs.getInt("id_tutor"));
                p.setIdEspecie(rs.getInt("id_especie"));
                p.setIdRaca(rs.getInt("id_raca"));
                p.setIdSexo(rs.getInt("id_sexo"));
                lista.add(p);
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // Verificar se há consultas associadas
            String sqlCheckConsulta = "SELECT COUNT(*) FROM \"Consulta\" WHERE id_pet = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckConsulta)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Não é possível excluir: pet possui consultas associadas.");
                }
            }

            // Verificar se há vacinas aplicadas associadas
            String sqlCheckVacina = "SELECT COUNT(*) FROM \"VacinaAplicada\" WHERE id_pet = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckVacina)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Não é possível excluir: pet possui vacinas aplicadas.");
                }
            }

            // Deletar pet
            String sql = "DELETE FROM \"Pet\" WHERE id_pet = ?";
            int rows;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                rows = stmt.executeUpdate();
            }
            if (rows == 0) {
                throw new SQLException("Pet não encontrado.");
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
