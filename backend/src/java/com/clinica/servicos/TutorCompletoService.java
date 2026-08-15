package com.clinica.servicos;

import com.clinica.col.TutorCol;
import com.clinica.dao.*;
import com.clinica.dto.TelefoneDTO;
import com.clinica.dto.TutorCompletoDTO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;
import com.clinica.model.Tutor;

import java.sql.Connection;
import java.sql.SQLException;

public class TutorCompletoService {

    public int cadastrarTutorCompleto(TutorCompletoDTO dto) throws SQLException, IllegalArgumentException {
        if (dto.getTutor() == null) {
            throw new IllegalArgumentException("Dados do tutor não informados.");
        }

        if (!TutorCol.validar(dto.getTutor())) {
            throw new IllegalArgumentException("Dados do tutor inválidos. Verifique nome e CPF (11 dígitos).");
        }

        if (dto.getEndereco() == null || dto.getEndereco().getCidade() == null || dto.getEndereco().getCidade().isEmpty()) {
            throw new IllegalArgumentException("Endereço incompleto. Informe pelo menos cidade e UF.");
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            // Endereço
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

            // Tutor
            Tutor tutor = dto.getTutor();
            tutor.setIdEndereco(idEndereco);
            try {
                tutor.setNroEndereco((dto.getNumero() != null && !dto.getNumero().isEmpty()) ? Integer.parseInt(dto.getNumero()) : 0);
            } catch (NumberFormatException e) {
                tutor.setNroEndereco(0);
            }
            tutor.setComplemento(dto.getComplemento() != null ? dto.getComplemento() : "");
            tutor.setSenha((tutor.getSenha() != null && !tutor.getSenha().isEmpty()) ? tutor.getSenha() : "123456");

            TutorDAO tutorDAO = new TutorDAO();
            int idUsuario = tutorDAO.inserir(tutor, conn);
            if (idUsuario <= 0) {
                throw new SQLException("Falha ao inserir usuário. CPF já pode estar cadastrado.");
            }

            // Telefones
            FoneUsuarioDAO foneDAO = new FoneUsuarioDAO();
            for (TelefoneDTO tel : dto.getTelefones()) {
                if (tel != null && tel.getNumero() != null && !tel.getNumero().trim().isEmpty()) {
                    foneDAO.inserir(idUsuario, tel.getDdi(), tel.getDdd(), tel.getNumero().trim(), conn);
                }
            }

            // Emails
            EmailUsuarioDAO emailDAO = new EmailUsuarioDAO();
            for (String email : dto.getEmails()) {
                if (email != null && !email.trim().isEmpty()) {
                    emailDAO.inserir(idUsuario, email.trim(), conn);
                }
            }

            conn.commit();
            return idUsuario;

        } catch (SQLException | IllegalArgumentException e) {
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
}
