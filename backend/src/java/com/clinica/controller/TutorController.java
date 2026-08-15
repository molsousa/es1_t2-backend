package com.clinica.controller;

import com.clinica.dto.TutorCompletoDTO;
import com.clinica.model.Tutor;
import com.clinica.servicos.TutorService;
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

@WebServlet("/api/tutor/*")
public class TutorController extends HttpServlet {

    private TutorService service = new TutorService();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context)
                    -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context)
                    -> LocalDate.parse(json.getAsString()))
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        // Caso 1: Buscar tutor completo /api/tutor/{id}/completo
        if (pathInfo != null && pathInfo.matches("/\\d+/completo")) {
            try {
                // Extrai o ID da URL
                int id = Integer.parseInt(pathInfo.substring(1, pathInfo.indexOf("/", 1)));
                TutorCompletoDTO dto = service.buscarCompletoPorId(id);
                if (dto == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Tutor não encontrado.");
                    return;
                }
                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(dto));
                return;
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
                return;
            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
                return;
            }
        }

        // Caso 2: Listar todos (já existente)
        try {
            List<Tutor> tutores = service.listarTodos();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(tutores));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Mantenha o POST existente para cadastro simples (se usado)
        try {
            Tutor tutor = gson.fromJson(req.getReader(), Tutor.class);
            int id = service.cadastrar(tutor);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"id\":" + id + "}");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
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
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao excluir.");
        }
    }
}
