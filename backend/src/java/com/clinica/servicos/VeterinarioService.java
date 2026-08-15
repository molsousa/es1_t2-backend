package com.clinica.servicos;

import com.clinica.col.VeterinarioCol;
import com.clinica.dao.*;
import com.clinica.dto.TelefoneDTO;
import com.clinica.dto.VeterinarioCompletoDTO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;
import com.clinica.model.Veterinario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VeterinarioService {

    private VeterinarioDAO vetDAO = new VeterinarioDAO();

    public int cadastrarCompleto(VeterinarioCompletoDTO dto) throws SQLException {
        if (!VeterinarioCol.validar(dto.getVeterinario())) {
            throw new IllegalArgumentException("Dados do veterinário inválidos.");
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            Endereco endereco = dto.getEndereco();
            String siglaTipo = (endereco.getSiglaTipoLogradouro() != null && !endereco.getSiglaTipoLogradouro().isEmpty())
                    ? endereco.getSiglaTipoLogradouro() : "R";

            Endereco enderecoCompleto = new Endereco();
            enderecoCompleto.setCep(endereco.getCep() != null ? endereco.getCep() : "");
            enderecoCompleto.setLogradouro(endereco.getLogradouro() != null ? endereco.getLogradouro() : "");
            enderecoCompleto.setBairro(endereco.getBairro() != null ? endereco.getBairro() : "");
            enderecoCompleto.setCidade(endereco.getCidade());
            enderecoCompleto.setUf(endereco.getUf());
            enderecoCompleto.setNumero(dto.getNumero() != null ? dto.getNumero() : "0");
            enderecoCompleto.setComplemento(dto.getComplemento() != null ? dto.getComplemento() : "");
            enderecoCompleto.setSiglaTipoLogradouro(siglaTipo);

            EnderecoDAO endDAO = new EnderecoDAO();
            int idEndereco = endDAO.inserirOuBuscar(enderecoCompleto);
            if (idEndereco <= 0) {
                throw new SQLException("Falha ao inserir/buscar endereço.");
            }

            Veterinario vet = dto.getVeterinario();
            vet.setIdEndereco(idEndereco);
            vet.setNroEndereco(dto.getNumero());
            vet.setComplemento(dto.getComplemento());
            vet.setSenha(vet.getSenha() != null ? vet.getSenha() : "123456");

            int idUsuario = vetDAO.inserir(vet, conn);
            if (idUsuario <= 0) {
                throw new SQLException("Falha ao inserir veterinário.");
            }

            FoneUsuarioDAO foneDAO = new FoneUsuarioDAO();
            for (TelefoneDTO tel : dto.getTelefones()) {
                if (tel != null && tel.getNumero() != null && !tel.getNumero().trim().isEmpty()) {
                    foneDAO.inserir(idUsuario, tel.getDdi(), tel.getDdd(), tel.getNumero().trim(), conn);
                }
            }

            EmailUsuarioDAO emailDAO = new EmailUsuarioDAO();
            for (String email : dto.getEmails()) {
                if (email != null && !email.trim().isEmpty()) {
                    emailDAO.inserir(idUsuario, email.trim(), conn);
                }
            }

            conn.commit();
            return idUsuario;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Veterinario> listarTodos() throws SQLException {
        return vetDAO.listarTodos();
    }

    public Veterinario buscarPorId(int id) throws SQLException {
        return vetDAO.buscarPorId(id);
    }

    public VeterinarioCompletoDTO buscarCompletoPorId(int id) throws SQLException {
        return vetDAO.buscarCompletoPorId(id);
    }

    public void excluir(int id) throws SQLException {
        vetDAO.excluir(id);
    }
}
