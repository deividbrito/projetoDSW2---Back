package br.dsw2.command;

import br.dsw2.dao.TransacaoDAO;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ResumoFinanceiroCommand implements Command {

    private final Gson gson = new Gson();

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        TransacaoDAO dao = new TransacaoDAO();

        List<Map<String, Object>> resumo = dao.getResumoPorCategoria();
        double saldo = dao.getSaldoAtual();

        var resposta = Map.of(
            "saldo", saldo,
            "resumo", resumo
        );

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print(gson.toJson(resposta));
        out.flush();
    }
}
