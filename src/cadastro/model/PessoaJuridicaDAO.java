package cadastro.model;

import cadastrobd.model.PessoaJuridica;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaDAO {
    private Connection conn;

    public PessoaJuridicaDAO(Connection conn) {
        this.conn = conn;
    }

    private PessoaJuridica extrairPessoaJuridica(ResultSet rs) throws SQLException {
        return new PessoaJuridica(
                rs.getInt("idPessoaJuridica"),
                rs.getString("nome"),
                rs.getString("logradouro"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("cnpj")
        );
    }

    public PessoaJuridica getPessoa(int id) throws SQLException {
        final String sql = "SELECT P.*, PJ.cnpj FROM Pessoa P " +
                "INNER JOIN PessoaJuridica PJ ON P.idPessoa = PJ.idPessoaJuridica " +
                "WHERE PJ.idPessoaJuridica = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairPessoaJuridica(rs);
                }
            }
        }
        return null;
    }

    public List<PessoaJuridica> getPessoasJuridicas() throws SQLException {
        List<PessoaJuridica> list = new ArrayList<>();
        String sql = "SELECT P.*, PJ.cnpj FROM Pessoa P " +
                "INNER JOIN PessoaJuridica PJ ON P.idPessoa = PJ.idPessoaJuridica " +
                "ORDER BY P.nome";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extrairPessoaJuridica(rs));
            }
        }
        return list;
    }

    private boolean idExiste(int id) throws SQLException {
        if (idExisteNaTabela("PessoaJuridica", "idPessoaJuridica", id) || idExisteNaTabela("Pessoa", "idPessoa", id)) {
            return true;
        }
        return false;
    }

    private boolean idExisteNaTabela(String tabela, String colunaId, int id) throws SQLException {
        final String sql = "SELECT COUNT(*) FROM " + tabela + " WHERE " + colunaId + " = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void incluir(PessoaJuridica pessoa) throws SQLException {
        // Verifica se o ID já existe em alguma das duas tabelas
        if (idExiste(pessoa.getId())) {
            System.out.println("Erro: ID já existe!");
            return; // Interrompe a execução se o ID já existir
        }

        final String sqlPessoa = "INSERT INTO Pessoa (idPessoa, nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        final String sqlPessoaJuridica = "INSERT INTO PessoaJuridica (idPessoaJuridica, cnpj) VALUES (?, ?)";
        try {
            conn.setAutoCommit(false);

            // Insere na tabela Pessoa
            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setInt(1, pessoa.getId());
                stmtPessoa.setString(2, pessoa.getNome());
                stmtPessoa.setString(3, pessoa.getLogradouro());
                stmtPessoa.setString(4, pessoa.getCidade());
                stmtPessoa.setString(5, pessoa.getEstado());
                stmtPessoa.setString(6, pessoa.getTelefone());
                stmtPessoa.setString(7, pessoa.getEmail());
                stmtPessoa.executeUpdate();
            }

            // Insere na tabela PessoaJuridica
            try (PreparedStatement stmtPessoaJuridica = conn.prepareStatement(sqlPessoaJuridica)) {
                stmtPessoaJuridica.setInt(1, pessoa.getId());
                stmtPessoaJuridica.setString(2, pessoa.getCnpj());
                stmtPessoaJuridica.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    public void alterar(PessoaJuridica pessoa) throws SQLException {
        final String sqlPessoa = "UPDATE Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
        final String sqlPessoaJuridica = "UPDATE PessoaJuridica SET cnpj = ? WHERE idPessoaJuridica = ?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setString(1, pessoa.getNome());
                stmtPessoa.setString(2, pessoa.getLogradouro());
                stmtPessoa.setString(3, pessoa.getCidade());
                stmtPessoa.setString(4, pessoa.getEstado());
                stmtPessoa.setString(5, pessoa.getTelefone());
                stmtPessoa.setString(6, pessoa.getEmail());
                stmtPessoa.setInt(7, pessoa.getId());
                stmtPessoa.executeUpdate();
            }
            try (PreparedStatement stmtPessoaJuridica = conn.prepareStatement(sqlPessoaJuridica)) {
                stmtPessoaJuridica.setString(1, pessoa.getCnpj());
                stmtPessoaJuridica.setInt(2, pessoa.getId());
                stmtPessoaJuridica.executeUpdate();
            }
            conn.commit();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void excluir(int id) throws SQLException {
        final String sql = "DELETE FROM PessoaJuridica WHERE idPessoaJuridica = ?; " +
                "DELETE FROM Pessoa WHERE idPessoa = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.setInt(2, id);
            stmt.executeUpdate();
            conn.commit();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
