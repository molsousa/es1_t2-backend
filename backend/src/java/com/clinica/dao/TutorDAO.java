package com.clinica.dao;

import com.clinica.dto.TelefoneDTO;
import com.clinica.dto.TutorCompletoDTO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;
import com.clinica.model.Tutor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TutorDAO {

    public int inserir(Tutor tutor, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"Usuario\" (nome_usuario, login, senha, crmv, nro_endereco, complemento_endereco, id_tipo_usuario, id_endereco) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tutor.getNome());
            stmt.setString(2, tutor.getCpf());
            stmt.setString(3, tutor.getSenha());
            stmt.setNull(4, Types.VARCHAR);
            if (tutor.getNroEndereco() == 0) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, tutor.getNroEndereco());
            }
            stmt.setString(6, tutor.getComplemento() != null ? tutor.getComplemento() : "");
            stmt.setInt(7, 1);
            stmt.setInt(8, tutor.getIdEndereco());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public List<Tutor> listarTodos() throws SQLException {
        List<Tutor> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nome_usuario, login, senha FROM \"Usuario\" WHERE id_tipo_usuario = 1";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Tutor t = new Tutor();
                t.setId(rs.getInt("id_usuario"));
                t.setNome(rs.getString("nome_usuario"));
                t.setCpf(rs.getString("login"));
                t.setSenha(rs.getString("senha"));
                lista.add(t);
            }
        }
        return lista;
    }

    public Tutor buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, login, senha FROM \"Usuario\" WHERE id_usuario = ? AND id_tipo_usuario = 1";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Tutor t = new Tutor();
                t.setId(rs.getInt("id_usuario"));
                t.setNome(rs.getString("nome_usuario"));
                t.setCpf(rs.getString("login"));
                t.setSenha(rs.getString("senha"));
                return t;
            }
        }
        return null;
    }

    public TutorCompletoDTO buscarCompletoPorId(int id) throws SQLException {
        Tutor tutor = null;
        String sqlTutor = "SELECT id_usuario, nome_usuario, login, senha, nro_endereco, complemento_endereco, id_endereco "
                + "FROM \"Usuario\" WHERE id_usuario = ? AND id_tipo_usuario = 1";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlTutor)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tutor = new Tutor();
                    tutor.setId(rs.getInt("id_usuario"));
                    tutor.setNome(rs.getString("nome_usuario"));
                    tutor.setCpf(rs.getString("login"));
                    tutor.setSenha(rs.getString("senha"));
                    tutor.setNroEndereco(rs.getInt("nro_endereco"));
                    tutor.setComplemento(rs.getString("complemento_endereco"));
                    tutor.setIdEndereco(rs.getInt("id_endereco"));
                } else {
                    return null;
                }
            }
        }
        if (tutor == null) {
            return null;
        }

        Endereco endereco = null;
        if (tutor.getIdEndereco() > 0) {
            EnderecoDAO endDAO = new EnderecoDAO();
            endereco = endDAO.buscarPorIdEndereco(tutor.getIdEndereco());
        }

        FoneUsuarioDAO foneDAO = new FoneUsuarioDAO();
        List<TelefoneDTO> telefones = foneDAO.listarPorUsuario(id);

        EmailUsuarioDAO emailDAO = new EmailUsuarioDAO();
        List<String> emails = emailDAO.listarPorUsuario(id);

        TutorCompletoDTO dto = new TutorCompletoDTO();
        dto.setTutor(tutor);
        dto.setEndereco(endereco);
        dto.setTelefones(telefones);
        dto.setEmails(emails);
        return dto;
    }

    public void excluir(int id) throws SQLException {
        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // Verifica se existem pets associados
            String sqlCheckPet = "SELECT COUNT(*) FROM \"Pet\" WHERE id_tutor = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlCheckPet)) {
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    throw new SQLException("Não é possível excluir: tutor possui pets cadastrados.");
                }
            }

            // Deleta dependências (telefones e emails)
            String sqlFone = "DELETE FROM \"FoneUsuario\" WHERE id_usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlFone)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            String sqlEmail = "DELETE FROM \"EmailUsuario\" WHERE id_usuario = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlEmail)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            // Deleta usuário e verifica se alguma linha foi afetada
            String sqlUser = "DELETE FROM \"Usuario\" WHERE id_usuario = ? AND id_tipo_usuario = 1";
            int rows;
            try (PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
                stmt.setInt(1, id);
                rows = stmt.executeUpdate();
            }
            if (rows == 0) {
                throw new SQLException("Tutor não encontrado ou já foi excluído.");
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
