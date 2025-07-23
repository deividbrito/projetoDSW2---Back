package br.dsw2.controller;

import br.dsw2.command.Command;
import br.dsw2.factory.CommandFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/*")
public class FrontControllerServlet extends HttpServlet {

    private final CommandFactory factory = new CommandFactory();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Command command = factory.getCommand(req);
            command.execute(req, resp);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            resp.getWriter().write("{\"erro\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
    }
}
