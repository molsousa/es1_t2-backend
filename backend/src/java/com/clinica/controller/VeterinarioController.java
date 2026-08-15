package com.clinica.controller;

import com.clinica.dto.VeterinarioCompletoDTO;
import com.clinica.model.Veterinario;
import com.clinica.servicos.VeterinarioService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/veterinario/*")
public class VeterinarioController extends HttpServlet {

    private VeterinarioService service = new VeterinarioService();
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        // /api/veterinario/{id}/completo
        if (pathInfo != null && pathInfo.matches("/\\d+/completo")) {
            try {
                int id = Integer.parseInt(pathInfo.substring(1, pathInfo.indexOf("/", 1)));
                VeterinarioCompletoDTO dto = service.buscarCompletoPorId(id);
                if (dto == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Veterinário não encontrado.");
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

        // Listar todos
        try {
            List<Veterinario> lista = service.listarTodos();
            resp.setContentType("application/json");
            resp.getWriter().write(gson.toJson(lista));
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Mantido para cadastro simples (se usado)
        try {
            Veterinario vet = gson.fromJson(req.getReader(), Veterinario.class);
            // não implementado
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
