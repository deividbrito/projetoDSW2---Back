package br.dsw2.command;

import br.dsw2.adapter.LocalDateAdapter;
import br.dsw2.dao.TransacaoDAO;
import br.dsw2.model.Transacao;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;

public class AtualizarTransacaoCommand implements Command {

    private final Gson gson;

    public AtualizarTransacaoCommand() {
        gson = new GsonBuilder()
                .registerTypeAdapter(java.time.LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // obtém o ID da transação pela URL
        String path = req.getPathInfo(); // /transacoes/5
        int id = Integer.parseInt(path.substring("/transacoes/".length()));

        // lê o corpo da requisição
        BufferedReader reader = req.getReader();
        Transacao transacaoAtualizada = gson.fromJson(reader, Transacao.class);
        transacaoAtualizada.setId(id);

        // verifica se a transação existe
        TransacaoDAO dao = new TransacaoDAO();
        Transacao existente = dao.findById(id);

        if (existente == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"erro\":\"Transação não encontrada\"}");
            return;
        }

        // atualiza no banco
        dao.update(transacaoAtualizada);

        // retorna a transação atualizada
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(gson.toJson(transacaoAtualizada));
    }
}
