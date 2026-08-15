package com.clinica.controller;

import com.clinica.model.Sexo;
import com.clinica.servicos.SexoService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/sexos")
public class SexoController extends HttpServlet {

    private SexoService service = new SexoService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Sexo> lista = service.listarTodos();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(lista));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar sexos.");
        }
    }
}
