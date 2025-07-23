package br.dsw2.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NotFoundCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setContentType("application/json");
        resp.getWriter().write("{\"erro\":\"Rota não encontrada\"}");
    }
}
