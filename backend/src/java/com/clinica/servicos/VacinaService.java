package com.clinica.servicos;

import com.clinica.dao.VacinaDAO;
import com.clinica.factory.ConnectionFactory;
import com.clinica.model.Vacina;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VacinaService {

    private VacinaDAO dao = new VacinaDAO();

    public int registrar(Vacina vacina) throws SQLException {
        if (vacina.getPetId() <= 0 || vacina.getNomeVacina() == null || vacina.getDataAplicacao() == null) {
            throw new IllegalArgumentException("Dados da vacina incompletos.");
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            int id = dao.inserir(vacina, conn);

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

    public List<Vacina> listarPendentes() throws SQLException {
        List<Vacina> todas = dao.listarPendentes();
        LocalDate hoje = LocalDate.now();
        return todas.stream()
                .filter(v -> {
                    LocalDate proxima = v.getDataProxima();
                    return !proxima.isBefore(hoje) && proxima.isBefore(hoje.plusDays(30));
                })
                .collect(Collectors.toList());
    }
    
    public List<Vacina> listarTodas() throws SQLException {
        return dao.listarTodas();
    }
    
    public List<Vacina> listarTodasAplicacoes() throws SQLException {
        return dao.listarTodasAplicacoes();
    }

    public void excluir(int id) throws SQLException {
        dao.excluir(id);
    }
}