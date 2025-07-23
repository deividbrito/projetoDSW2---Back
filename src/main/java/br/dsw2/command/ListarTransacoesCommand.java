package br.dsw2.command;

import br.dsw2.dao.TransacaoDAO;
import br.dsw2.model.Transacao;
import br.dsw2.adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.util.List;

public class ListarTransacoesCommand implements Command {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // parâmetros de filtro
        String tipo = req.getParameter("tipo");           // receita, despesa
        String categoria = req.getParameter("categoria"); // alimentação, transporte, etc.
        String mesStr = req.getParameter("mes");
        String anoStr = req.getParameter("ano");

        // parâmetros de paginação
        int pagina = parseOrDefault(req.getParameter("pagina"), 1);
        int tamanho = parseOrDefault(req.getParameter("tamanho"), 10);
        int offset = (pagina - 1) * tamanho;

        Integer mes = mesStr != null ? Integer.parseInt(mesStr) : null;
        Integer ano = anoStr != null ? Integer.parseInt(anoStr) : null;

        TransacaoDAO dao = new TransacaoDAO();
        List<Transacao> transacoes = dao.findAll(tipo, categoria, mes, ano, offset, tamanho);

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(transacoes));
    }

    private int parseOrDefault(String param, int defaultValue) {
        try {
            return param != null ? Integer.parseInt(param) : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
