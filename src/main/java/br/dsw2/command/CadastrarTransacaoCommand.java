package br.dsw2.command;

import br.dsw2.dao.TransacaoDAO;
import br.dsw2.model.Transacao;
import br.dsw2.adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.time.LocalDate;

public class CadastrarTransacaoCommand implements Command {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        // lê o JSON enviado no corpo da requisição
        BufferedReader reader = req.getReader();
        Transacao nova = gson.fromJson(reader, Transacao.class);

        // validação básica
        if (nova.getDescricao() == null || nova.getValor() == null || nova.getTipo() == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"erro\":\"Descrição, valor e tipo são obrigatórios.\"}");
            return;
        }

        // insere no banco
        TransacaoDAO dao = new TransacaoDAO();
        int idGerado = dao.create(nova);
        nova.setId(idGerado);

        // retorna a transação criada em JSON
        resp.setContentType("application/json");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(gson.toJson(nova));
    }
}
