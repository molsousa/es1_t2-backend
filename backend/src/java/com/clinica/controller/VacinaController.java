package com.clinica.controller;

import com.clinica.model.Vacina;
import com.clinica.servicos.VacinaService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/api/vacinas/*")
public class VacinaController extends HttpServlet {

    private VacinaService service = new VacinaService();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                    -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context)
                    -> LocalDate.parse(json.getAsString()))
            .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Vacina vacina = gson.fromJson(req.getReader(), Vacina.class);
            int id = service.registrar(vacina);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + id + "}");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/pendentes")) {
            try {
                List<Vacina> pendentes = service.listarPendentes();
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(pendentes));
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar vacinas pendentes.");
                return;
            }
        }
        if (pathInfo != null && pathInfo.equals("/aplicadas")) {
            try {
                List<Vacina> todas = service.listarTodasAplicacoes();
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(todas));
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar vacinas aplicadas.");
                return;
            }
        }
        try {
            List<Vacina> catalogo = service.listarTodas();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(catalogo));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar catálogo de vacinas.");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID não informado.");
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            service.excluir(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir vacina: " + e.getMessage());
        }
    }
}
