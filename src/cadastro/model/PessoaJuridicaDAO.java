package cadastro.model;

import cadastrobd.model.PessoaJuridica;
import cadastro.model.util.ConectorBD;

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
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("logradouro"),
                rs.getString("cidade"),
                rs.getString("estado"),
                rs.getString("telefone"),
                rs.getString("email"),
                rs.getString("cnpj")
        );
    }

    public PessoaJuridica getPessoa(int id) {
        final String sql = "SELECT * FROM PessoaJuridica WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairPessoaJuridica(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<PessoaJuridica> getPessoas() {
        final String sql = "SELECT * FROM PessoaJuridica ORDER BY nome";
        List<PessoaJuridica> pessoas = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                pessoas.add(extrairPessoaJuridica(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pessoas;
    }

    public void incluir(PessoaJuridica pessoa) {
        final String sql = "INSERT INTO PessoaJuridica (nome, logradouro, cidade, estado, telefone, email, cnpj) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getLogradouro());
            stmt.setString(3, pessoa.getCidade());
            stmt.setString(4, pessoa.getEstado());
            stmt.setString(5, pessoa.getTelefone());
            stmt.setString(6, pessoa.getEmail());
            stmt.setString(7, pessoa.getCnpj());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void alterar(PessoaJuridica pessoa) {
        final String sql = "UPDATE PessoaJuridica SET nome=?, logradouro=?, cidade=?, estado=?, telefone=?, email=?, cnpj=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getLogradouro());
            stmt.setString(3, pessoa.getCidade());
            stmt.setString(4, pessoa.getEstado());
            stmt.setString(5, pessoa.getTelefone());
            stmt.setString(6, pessoa.getEmail());
            stmt.setString(7, pessoa.getCnpj());
            stmt.setInt(8, pessoa.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(int id) {
        final String sql = "DELETE FROM PessoaJuridica WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
