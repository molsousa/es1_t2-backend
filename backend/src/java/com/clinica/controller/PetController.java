package com.clinica.controller;

import com.clinica.model.Pet;
import com.clinica.servicos.PetService;
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

@WebServlet("/api/pet/*")
public class PetController extends HttpServlet {

    private PetService service = new PetService();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                    -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context)
                    -> LocalDate.parse(json.getAsString()))
            .create();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Pet pet = gson.fromJson(req.getReader(), Pet.class);
            int id = service.cadastrar(pet);
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
        // / ou /all -> lista todos
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/all")) {
            try {
                List<Pet> pets = service.listarTodos();
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(pets));
                return;
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao listar pets.");
                return;
            }
        }
        // ?tutorId=X
        String tutorIdParam = req.getParameter("tutorId");
        if (tutorIdParam != null) {
            try {
                int idTutor = Integer.parseInt(tutorIdParam);
                List<Pet> pets = service.listarPorTutor(idTutor);
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(pets));
                return;
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "tutorId inválido.");
                return;
            } catch (SQLException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
                return;
            }
        }
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Use /all ou parâmetro tutorId.");
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
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir.");
        }
    }
}
