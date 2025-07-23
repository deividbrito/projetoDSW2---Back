package br.dsw2.command;

import br.dsw2.dao.TransacaoDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcluirTransacaoCommand implements Command {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String path = req.getPathInfo(); // ex: /transacoes/5
        int id = Integer.parseInt(path.substring("/transacoes/".length()));

        TransacaoDAO dao = new TransacaoDAO();
        boolean sucesso = dao.delete(id);

        if (sucesso) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND); // 404
            resp.setContentType("application/json");
            resp.getWriter().write("{\"erro\":\"Transação não encontrada.\"}");
        }
    }
}
