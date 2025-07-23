package br.dsw2.factory;

import br.dsw2.command.*;
import jakarta.servlet.http.HttpServletRequest;

public class CommandFactory {

    public Command getCommand(HttpServletRequest req) {
        String path = req.getPathInfo() == null ? "" : req.getPathInfo();
        String method = req.getMethod();

        // POST /transacoes
        if ("/transacoes".equals(path) && "POST".equalsIgnoreCase(method)) {
            return new CadastrarTransacaoCommand();
        }

        // GET /transacoes/{id}
        if (path.matches("^/transacoes/\\d+$") && "GET".equalsIgnoreCase(method)) {
            return new BuscarTransacaoCommand();
        }

        // PUT /transacoes/{id}
        if (path.matches("^/transacoes/\\d+$") && "PUT".equalsIgnoreCase(method)) {
            return new AtualizarTransacaoCommand();
        }
        
        // DELETE /transacoes/{id}
        if (path.matches("^/transacoes/\\d+$") && "DELETE".equalsIgnoreCase(method)) {
            return new ExcluirTransacaoCommand();
        }

        // GET /transacoes
        if ("/transacoes".equals(path) && "GET".equalsIgnoreCase(method)) {
            return new ListarTransacoesCommand();
        }
        
        // GET /resumo
        if ("/resumo".equals(path) && "GET".equalsIgnoreCase(method)) {
            return new ResumoFinanceiroCommand();
        }


        // se nada bater, comando de erro
        return new NotFoundCommand();
    }
}
