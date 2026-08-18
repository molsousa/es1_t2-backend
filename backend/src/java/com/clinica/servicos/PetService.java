package com.clinica.servicos;

import com.clinica.col.PetCol;
import com.clinica.dao.PetDAO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Pet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PetService {

    private PetDAO dao = new PetDAO();

    public int cadastrar(Pet pet) throws SQLException, IllegalArgumentException {
        pet.setIdTutor(pet.getTutorId());

        if (!PetCol.validar(pet)) {
            throw new IllegalArgumentException("Dados do pet inválidos.");
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            int id = dao.inserir(pet, conn);

            conn.commit();
            return id;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Pet> listarPorTutor(int idTutor) throws SQLException {
        return dao.listarPorTutor(idTutor);
    }

    public List<Pet> listarTodos() throws SQLException {
        return dao.listarTodos();
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}