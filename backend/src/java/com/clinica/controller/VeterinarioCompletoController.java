package com.clinica.controller;

import com.clinica.dto.VeterinarioCompletoDTO;
import com.clinica.servicos.VeterinarioService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/api/veterinario/completo")
public class VeterinarioCompletoController extends HttpServlet {

    private VeterinarioService service = new VeterinarioService();
    private Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            VeterinarioCompletoDTO dto = gson.fromJson(req.getReader(), VeterinarioCompletoDTO.class);
            int id = service.cadastrarCompleto(dto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"id\":" + id + "}");
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro no banco de dados: " + e.getMessage());
        }
    }
}
