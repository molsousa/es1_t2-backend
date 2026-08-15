package com.clinica.controller;

import com.clinica.dao.DDDAO;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/ddd")
public class DDDController extends HttpServlet {

    private DDDAO dao = new DDDAO();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Integer> dddList = dao.listarDDDs();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(dddList));
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar DDDs.");
        }
    }
}
