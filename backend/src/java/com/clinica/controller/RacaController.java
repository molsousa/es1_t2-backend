package com.clinica.controller;

import com.clinica.model.Raca;
import com.clinica.servicos.RacaService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/racas")
public class RacaController extends HttpServlet {

    private RacaService service = new RacaService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Raca> lista = service.listarTodos();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(lista));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar raças.");
        }
    }
}
