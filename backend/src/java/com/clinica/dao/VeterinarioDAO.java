package com.clinica.dao;

import com.clinica.dto.TelefoneDTO;
import com.clinica.dto.VeterinarioCompletoDTO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;
import com.clinica.model.Veterinario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO {

    public int inserir(Veterinario vet, Connection conn) throws SQLException {
        String sql = "INSERT INTO \"Usuario\" (nome_usuario, login, senha, crmv, nro_endereco, complemento_endereco, id_tipo_usuario, id_endereco) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vet.getNome());
            stmt.setString(2, vet.getCpf());
            stmt.setString(3, vet.getSenha() != null ? vet.getSenha() : "123456");
            stmt.setString(4, vet.getCrmv());

            Integer nroEndereco = null;
            String nroStr = vet.getNroEndereco();
            if (nroStr != null && !nroStr.isEmpty()) {
                try {
                    nroEndereco = Integer.parseInt(nroStr);
                } catch (NumberFormatException e) {
                    nroEndereco = 0;
                }
            }
            if (nroEndereco == null) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, nroEndereco);
            }

            stmt.setString(6, vet.getComplemento() != null ? vet.getComplemento() : "");
            stmt.setInt(7, 2);
            stmt.setInt(8, vet.getIdEndereco());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Veterinario> listarTodos() throws SQLException {
        List<Veterinario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nome_usuario, login, senha, crmv, nro_endereco, complemento_endereco, id_endereco "
                + "FROM \"Usuario\" WHERE id_tipo_usuario = 2";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Veterinario v = new Veterinario();
                v.setId(rs.getInt("id_usuario"));
                v.setNome(rs.getString("nome_usuario"));
                v.setCpf(rs.getString("login"));
                v.setSenha(rs.getString("senha"));
                v.setCrmv(rs.getString("crmv"));
                v.setNroEndereco(String.valueOf(rs.getInt("nro_endereco")));
                v.setComplemento(rs.getString("complemento_endereco"));
                v.setIdEndereco(rs.getInt("id_endereco"));
                lista.add(v);
            }
        }
        return lista;
    }

    public Veterinario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, login, senha, crmv, nro_endereco, complemento_endereco, id_endereco "
                + "FROM \"Usuario\" WHERE id_usuario = ? AND id_tipo_usuario = 2";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Veterinario v = new Veterinario();
                v.setId(rs.getInt("id_usuario"));
                v.setNome(rs.getString("nome_usuario"));
                v.setCpf(rs.getString("login"));
                v.setSenha(rs.getString("senha"));
                v.setCrmv(rs.getString("crmv"));
                v.setNroEndereco(String.valueOf(rs.getInt("nro_endereco")));
                v.setComplemento(rs.getString("complemento_endereco"));
                v.setIdEndereco(rs.getInt("id_endereco"));
                return v;
            }
        }
        return null;
    }

    public VeterinarioCompletoDTO buscarCompletoPorId(int id) throws SQLException {
        Veterinario vet = buscarPorId(id);
        if (vet == null) {
            return null;
        }

        Endereco endereco = null;
        if (vet.getIdEndereco() > 0) {
            EnderecoDAO endDAO = new EnderecoDAO();
            endereco = endDAO.buscarPorIdEndereco(vet.getIdEndereco());
        }

        FoneUsuarioDAO foneDAO = new FoneUsuarioDAO();
        List<TelefoneDTO> telefones = foneDAO.listarPorUsuario(id);

        EmailUsuarioDAO emailDAO = new EmailUsuarioDAO();
        List<String> emails = emailDAO.listarPorUsuario(id);

        VeterinarioCompletoDTO dto = new VeterinarioCompletoDTO();
        dto.setVeterinario(vet);
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

            String sqlUser = "DELETE FROM \"Usuario\" WHERE id_usuario = ? AND id_tipo_usuario = 2";
            int rows;
            try (PreparedStatement stmt = conn.prepareStatement(sqlUser)) {
                stmt.setInt(1, id);
                rows = stmt.executeUpdate();
            }
            if (rows == 0) {
                throw new SQLException("Veterinário não encontrado ou já foi excluído.");
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
