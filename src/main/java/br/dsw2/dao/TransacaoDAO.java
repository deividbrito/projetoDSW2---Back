package br.dsw2.dao;

import br.dsw2.model.Transacao;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class TransacaoDAO {

    private final DataSource ds;

    public TransacaoDAO() throws NamingException {
        InitialContext ctx = new InitialContext();
        ds = (DataSource) ctx.lookup("java:comp/env/jdbc/FinanceiroDS");
    }

    // nova transação
    public int create(Transacao t) throws Exception {
        String sql = "INSERT INTO transacao (descricao, valor, tipo, categoria, data) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, t.getDescricao());
            ps.setBigDecimal(2, t.getValor());
            ps.setString(3, t.getTipo());
            ps.setString(4, t.getCategoria());
            ps.setDate(5, Date.valueOf(t.getData()));

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // retorna o id gerado
            } else {
                throw new SQLException("ID não gerado.");
            }
        }
    }

    // buscar uma transação pelo ID
    public Transacao findById(int id) throws Exception {
        String sql = "SELECT * FROM transacao WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setDescricao(rs.getString("descricao"));
                t.setValor(rs.getBigDecimal("valor"));
                t.setTipo(rs.getString("tipo"));
                t.setCategoria(rs.getString("categoria"));
                t.setData(rs.getDate("data").toLocalDate());
                return t;
            } else {
                return null;
            }
        }
    }
    
    // excluir uma transação pelo ID
    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM transacao WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    
    // atualizar uma transação
    public void update(Transacao t) throws Exception {
        String sql = "UPDATE transacao SET descricao = ?, valor = ?, tipo = ?, categoria = ?, data = ? WHERE id = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getDescricao());
            ps.setBigDecimal(2, t.getValor());
            ps.setString(3, t.getTipo());
            ps.setString(4, t.getCategoria());
            ps.setDate(5, Date.valueOf(t.getData()));
            ps.setInt(6, t.getId());

            ps.executeUpdate();
        }
    }
    
    // listar transacoes
    public List<Transacao> findAll(String tipo, String categoria, Integer mes, Integer ano, int offset, int tamanho) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT * FROM transacao WHERE 1=1");
        
        if (tipo != null) sql.append(" AND tipo = ?");
        if (categoria != null) sql.append(" AND categoria = ?");
        if (mes != null) sql.append(" AND MONTH(data) = ?");
        if (ano != null) sql.append(" AND YEAR(data) = ?");
        
        sql.append(" ORDER BY data DESC LIMIT ? OFFSET ?");

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (tipo != null) ps.setString(index++, tipo);
            if (categoria != null) ps.setString(index++, categoria);
            if (mes != null) ps.setInt(index++, mes);
            if (ano != null) ps.setInt(index++, ano);
            ps.setInt(index++, tamanho);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();
            List<Transacao> lista = new java.util.ArrayList<>();
            while (rs.next()) {
                Transacao t = new Transacao();
                t.setId(rs.getInt("id"));
                t.setDescricao(rs.getString("descricao"));
                t.setValor(rs.getBigDecimal("valor"));
                t.setTipo(rs.getString("tipo"));
                t.setCategoria(rs.getString("categoria"));
                t.setData(rs.getDate("data").toLocalDate());
                lista.add(t);
            }
            return lista;
        }
    }
    
    //funcoes para resumo
    // retorna total por categoria e tipo
    public List<Map<String, Object>> getResumoPorCategoria() throws Exception {
        String sql = """
            SELECT categoria, tipo, SUM(valor) AS total
            FROM transacao
            GROUP BY categoria, tipo
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Map<String, Object>> resultado = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> item = Map.of(
                    "categoria", rs.getString("categoria"),
                    "tipo", rs.getString("tipo"),
                    "total", rs.getBigDecimal("total")
                );
                resultado.add(item);
            }

            return resultado;
        }
    }

    // retorna o saldo atual (receitas - despesas)
    public double getSaldoAtual() throws Exception {
        String sql = """
            SELECT 
                SUM(CASE WHEN tipo = 'receita' THEN valor ELSE -valor END) AS saldo
            FROM transacao
            """;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal("saldo") != null ? rs.getBigDecimal("saldo").doubleValue() : 0.0;
            } else {
                return 0.0;
            }
        }
    }
}
