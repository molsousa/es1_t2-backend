package com.clinica.dao;

import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;

import java.sql.*;

public class EnderecoDAO {

    public Endereco buscarPorIdEndereco(int idEndereco) throws SQLException {
        String sql = "SELECT e.id_endereco, e.cep, e.id_cidade, e.id_logradouro, e.id_bairro, "
                + "c.nome_cidade, c.sigla_uf, b.nome_bairro, l.nome_logradouro, l.sigla_tipo_logradouro "
                + "FROM \"Endereco\" e "
                + "JOIN \"Cidade\" c ON e.id_cidade = c.id_cidade "
                + "JOIN \"Bairro\" b ON e.id_bairro = b.id_bairro "
                + "JOIN \"Logradouro\" l ON e.id_logradouro = l.id_logradouro "
                + "WHERE e.id_endereco = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEndereco);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Endereco end = new Endereco();
                end.setId(rs.getInt("id_endereco"));
                end.setCep(rs.getString("cep"));
                end.setCidade(rs.getString("nome_cidade"));
                end.setUf(rs.getString("sigla_uf"));
                end.setBairro(rs.getString("nome_bairro"));
                end.setLogradouro(rs.getString("nome_logradouro"));
                end.setSiglaTipoLogradouro(rs.getString("sigla_tipo_logradouro"));
                return end;
            }
        }
        return null;
    }

    public int inserirOuBuscar(Endereco endereco) throws SQLException {
        // Usar a sigla do tipo informada ou "R" como padrão
        String siglaTipo = endereco.getSiglaTipoLogradouro() != null && !endereco.getSiglaTipoLogradouro().isEmpty()
                ? endereco.getSiglaTipoLogradouro()
                : "R";

        int idCidade = buscarOuInserirCidade(endereco.getCidade(), endereco.getUf());
        int idBairro = buscarOuInserirBairro(endereco.getBairro());
        int idLogradouro = buscarOuInserirLogradouro(endereco.getLogradouro(), siglaTipo);

        // Busca endereço existente (evitar duplicação)
        String sqlBusca = "SELECT id_endereco FROM \"Endereco\" WHERE cep = ? AND id_cidade = ? AND id_logradouro = ? AND id_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, endereco.getCep());
            stmt.setInt(2, idCidade);
            stmt.setInt(3, idLogradouro);
            stmt.setInt(4, idBairro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_endereco");
            }
        }

        String sqlInsert = "INSERT INTO \"Endereco\" (cep, id_cidade, id_logradouro, id_bairro) VALUES (?, ?, ?, ?) RETURNING id_endereco";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, endereco.getCep());
            stmt.setInt(2, idCidade);
            stmt.setInt(3, idLogradouro);
            stmt.setInt(4, idBairro);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Erro ao inserir endereço.");
    }

    private int buscarOuInserirCidade(String nome, String uf) throws SQLException {
        String sqlBusca = "SELECT id_cidade FROM \"Cidade\" WHERE nome_cidade = ? AND sigla_uf = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, nome);
            stmt.setString(2, uf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_cidade");
            }
        }
        String sqlInsert = "INSERT INTO \"Cidade\" (nome_cidade, sigla_uf) VALUES (?, ?) RETURNING id_cidade";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            stmt.setString(2, uf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Erro ao inserir cidade.");
    }

    private int buscarOuInserirBairro(String nome) throws SQLException {
        String sqlBusca = "SELECT id_bairro FROM \"Bairro\" WHERE nome_bairro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_bairro");
            }
        }
        String sqlInsert = "INSERT INTO \"Bairro\" (nome_bairro) VALUES (?) RETURNING id_bairro";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Erro ao inserir bairro.");
    }

    private int buscarOuInserirLogradouro(String nome, String siglaTipo) throws SQLException {
        // Verifica se o tipo existe
        String sqlTipo = "SELECT sigla_tipo_logradouro FROM \"TipoLogradouro\" WHERE sigla_tipo_logradouro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlTipo)) {
            stmt.setString(1, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                String insertTipo = "INSERT INTO \"TipoLogradouro\" (sigla_tipo_logradouro, nome_tipo_logradouro) VALUES (?, ?)";
                try (PreparedStatement stmtTipo = conn.prepareStatement(insertTipo)) {
                    stmtTipo.setString(1, siglaTipo);
                    stmtTipo.setString(2, siglaTipo);
                    stmtTipo.executeUpdate();
                }
            }
        }

        String sqlBusca = "SELECT id_logradouro FROM \"Logradouro\" WHERE nome_logradouro = ? AND sigla_tipo_logradouro = ?";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlBusca)) {
            stmt.setString(1, nome);
            stmt.setString(2, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_logradouro");
            }
        }

        String sqlInsert = "INSERT INTO \"Logradouro\" (nome_logradouro, sigla_tipo_logradouro) VALUES (?, ?) RETURNING id_logradouro";
        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
            stmt.setString(1, nome);
            stmt.setString(2, siglaTipo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        throw new SQLException("Erro ao inserir logradouro.");
    }
}
