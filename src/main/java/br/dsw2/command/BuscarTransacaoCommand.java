package br.dsw2.command;

import br.dsw2.dao.TransacaoDAO;
import br.dsw2.model.Transacao;
import br.dsw2.adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

public class BuscarTransacaoCommand implements Command {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // extrai o ID da URL
        String path = req.getPathInfo(); // ex: /transacoes/5
        int id = Integer.parseInt(path.substring("/transacoes/".length()));

        TransacaoDAO dao = new TransacaoDAO();
        Transacao transacao = dao.findById(id);

        resp.setContentType("application/json");

        if (transacao == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"erro\":\"Transação não encontrada\"}");
        } else {
            resp.getWriter().write(gson.toJson(transacao));
        }
    }
}
