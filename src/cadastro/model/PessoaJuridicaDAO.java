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
                rs.getInt("idPessoa"),
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
        final String sql = "SELECT P.*, PJ.cnpj FROM Pessoa P INNER JOIN PessoaJuridica PJ ON P.idPessoa = PJ.idPessoaJuridica WHERE PJ.idPessoaJuridica = ?";
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
        final String sql = "SELECT P.*, PJ.cnpj FROM Pessoa P INNER JOIN PessoaJuridica PJ ON P.idPessoa = PJ.idPessoaJuridica ORDER BY P.nome";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(extrairPessoaJuridica(rs));
            }
        }
        return list;
    }

    public void incluir(PessoaJuridica pessoa) throws SQLException {
        final String sqlPessoa = "INSERT INTO Pessoa (nome, logradouro, cidade, estado, telefone, email, tipoPessoa) VALUES (?, ?, ?, ?, ?, ?, 'J')";
        final String sqlPessoaJuridica = "INSERT INTO PessoaJuridica (idPessoaJuridica, cnpj) VALUES (?, ?)";

        try {
            conn.setAutoCommit(false);

            int pessoaId = 0;
            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmtPessoa.setString(1, pessoa.getNome());
                stmtPessoa.setString(2, pessoa.getLogradouro());
                stmtPessoa.setString(3, pessoa.getCidade());
                stmtPessoa.setString(4, pessoa.getEstado());
                stmtPessoa.setString(5, pessoa.getTelefone());
                stmtPessoa.setString(6, pessoa.getEmail());
                stmtPessoa.executeUpdate();

                try (ResultSet generatedKeys = stmtPessoa.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pessoaId = generatedKeys.getInt(1);
                    }
                }
            }

            if (pessoaId == 0) {
                throw new SQLException("Falha ao inserir pessoa, nenhum ID foi gerado.");
            }

            try (PreparedStatement stmtPessoaJuridica = conn.prepareStatement(sqlPessoaJuridica)) {
                stmtPessoaJuridica.setInt(1, pessoaId);
                stmtPessoaJuridica.setString(2, pessoa.getCnpj());
                stmtPessoaJuridica.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
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
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public void excluir(int id) throws SQLException {
        String sqlPessoaJuridica = "DELETE FROM PessoaJuridica WHERE idPessoaJuridica = ?";
        String sqlPessoa = "DELETE FROM Pessoa WHERE idPessoa = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtPessoaJuridica = conn.prepareStatement(sqlPessoaJuridica)) {
                stmtPessoaJuridica.setInt(1, id);
                stmtPessoaJuridica.executeUpdate();
            }
            try (PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa)) {
                stmtPessoa.setInt(1, id);
                stmtPessoa.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

}
