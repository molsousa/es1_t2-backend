package com.clinica.controller;

import com.clinica.factory.ConnectionFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/relatorios")
public class RelatorioController extends HttpServlet {

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String inicio = req.getParameter("inicio");
        String fim = req.getParameter("fim");
        if (inicio == null || fim == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parâmetros inicio e fim são obrigatórios.");
            return;
        }

        try {
            JsonObject resultado = new JsonObject();

            // 1. Atendimentos por tipo (espécie)
            Map<String, Integer> tipos = new HashMap<>();
            String sqlTipos = "SELECT e.descricao_especie, COUNT(*) AS total "
                    + "FROM \"Consulta\" c "
                    + "JOIN \"Pet\" p ON c.id_pet = p.id_pet "
                    + "JOIN \"Especie\" e ON p.id_especie = e.id_especie "
                    + "WHERE c.data_consulta BETWEEN ? AND ? "
                    + "GROUP BY e.descricao_especie";
            try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlTipos)) {
                stmt.setDate(1, java.sql.Date.valueOf(inicio));
                stmt.setDate(2, java.sql.Date.valueOf(fim));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    tipos.put(rs.getString("descricao_especie"), rs.getInt("total"));
                }
            }

            // 2. Motivos mais recorrentes (top 5)
            Map<String, Integer> motivos = new HashMap<>();
            String sqlMotivos = "SELECT motivo, COUNT(*) AS total "
                    + "FROM \"Consulta\" "
                    + "WHERE data_consulta BETWEEN ? AND ? AND motivo IS NOT NULL "
                    + "GROUP BY motivo "
                    + "ORDER BY total DESC LIMIT 5";
            try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlMotivos)) {
                stmt.setDate(1, java.sql.Date.valueOf(inicio));
                stmt.setDate(2, java.sql.Date.valueOf(fim));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    motivos.put(rs.getString("motivo"), rs.getInt("total"));
                }
            }

            // 3. Vacinas mais aplicadas no período
            Map<String, Integer> vacinas = new HashMap<>();
            String sqlVacinas = "SELECT v.nome_vacina, COUNT(*) AS total "
                    + "FROM \"VacinaAplicada\" va "
                    + "JOIN \"Vacina\" v ON va.id_vacina = v.id_vacina "
                    + "WHERE va.data_aplicacao BETWEEN ? AND ? "
                    + "GROUP BY v.nome_vacina";
            try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement stmt = conn.prepareStatement(sqlVacinas)) {
                stmt.setDate(1, java.sql.Date.valueOf(inicio));
                stmt.setDate(2, java.sql.Date.valueOf(fim));
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    vacinas.put(rs.getString("nome_vacina"), rs.getInt("total"));
                }
            }

            resultado.add("tipos", gson.toJsonTree(tipos));
            resultado.add("motivos", gson.toJsonTree(motivos));
            resultado.add("vacinas", gson.toJsonTree(vacinas));

            resp.setContentType("application/json");
            resp.getWriter().write(resultado.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
        }
    }
}
