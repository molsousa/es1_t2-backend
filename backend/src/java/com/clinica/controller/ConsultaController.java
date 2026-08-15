package com.clinica.controller;

import com.clinica.model.Consulta;
import com.clinica.servicos.ConsultaService;
import com.google.gson.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@WebServlet("/api/consulta/*")
public class ConsultaController extends HttpServlet {

    private ConsultaService service = new ConsultaService();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                    -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context)
                    -> LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context)
                    -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, typeOfT, context)
                    -> LocalTime.parse(json.getAsString()))
            .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Consulta consulta = gson.fromJson(req.getReader(), Consulta.class);
            int id = service.agendar(consulta);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + id + "}");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/all")) {
            try {
                List<Consulta> consultas = service.listarTodos();
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(consultas));
                return;
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar consultas.");
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Use /all para listar todas as consultas.");
    }
}
