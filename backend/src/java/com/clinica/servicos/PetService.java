package com.clinica.servicos;

import com.clinica.col.PetCol;
import com.clinica.dao.PetDAO;
import com.clinica.model.Pet;

import java.sql.SQLException;
import java.util.List;

public class PetService {

    private PetDAO dao = new PetDAO();

    public int cadastrar(Pet pet) throws SQLException, IllegalArgumentException {
        // Copia tutorId para idTutor (usado na validação)
        pet.setIdTutor(pet.getTutorId());

        if (!PetCol.validar(pet)) {
            throw new IllegalArgumentException("Dados do pet inválidos.");
        }
        // Não sobrescrever IDs enviados pelo front
        return dao.inserir(pet);
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
