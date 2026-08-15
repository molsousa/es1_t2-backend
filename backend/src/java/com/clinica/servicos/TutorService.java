package com.clinica.servicos;

import com.clinica.col.TutorCol;
import com.clinica.dao.*;
import com.clinica.dto.TutorCompletoDTO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Endereco;
import com.clinica.model.Tutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TutorService {

    private TutorDAO dao = new TutorDAO();

    public int cadastrar(Tutor tutor) throws SQLException, IllegalArgumentException {
        if (!TutorCol.validar(tutor)) {
            throw new IllegalArgumentException("Dados do tutor inválidos.");
        }
        // Para cadastro simples, sem endereço completo
        return 0;
    }

    public List<Tutor> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public Tutor buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    /**
     * Busca todos os dados do tutor (incluindo endereço, telefones, emails).
     */
    public TutorCompletoDTO buscarCompletoPorId(int id) throws SQLException {
        // Usar o método do DAO que já retorna o DTO completo
        return dao.buscarCompletoPorId(id);
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}
