package cadastro.model;

import cadastrobd.model.PessoaFisica;
import cadastro.model.util.ConectorBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;


public class PessoaFisicaDAO {

    private Connection conn;

    public PessoaFisicaDAO(Connection conexao) {
        this.conn = conexao;
    }

    public void inserirPessoaFisica(PessoaFisica pf) {
        String sqlPessoa = "INSERT INTO Pessoa (nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlPessoaFisica = "INSERT INTO PessoaFisica (idPessoaFisica, cpf) VALUES (?, ?)";

        try (PreparedStatement stPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
            stPessoa.setString(1, pf.getNome());
            stPessoa.setString(2, pf.getLogradouro());
            stPessoa.setString(3, pf.getCidade());
            stPessoa.setString(4, pf.getEstado());
            stPessoa.setString(5, pf.getTelefone());
            stPessoa.setString(6, pf.getEmail());
            stPessoa.executeUpdate();

            try (ResultSet rs = stPessoa.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1); // Ou rs.getInt("idPessoa") se for o nome da coluna de ID.
                    try (PreparedStatement stPessoaFisica = conn.prepareStatement(sqlPessoaFisica)) {
                        stPessoaFisica.setInt(1, idGerado); // Aqui você deve inserir o id na coluna correta
                        stPessoaFisica.setString(2, pf.getCpf());
                        stPessoaFisica.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void alterar(PessoaFisica pf) {
        String sqlPessoa = "UPDATE Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
        String sqlPessoaFisica = "UPDATE PessoaFisica SET cpf = ? WHERE idPessoa = ?";

        try (PreparedStatement stPessoa = conn.prepareStatement(sqlPessoa)) {
            stPessoa.setString(1, pf.getNome());
            stPessoa.setString(2, pf.getLogradouro());
            stPessoa.setString(3, pf.getCidade());
            stPessoa.setString(4, pf.getEstado());
            stPessoa.setString(5, pf.getTelefone());
            stPessoa.setString(6, pf.getEmail());
            stPessoa.setInt(7, pf.getId());
            stPessoa.executeUpdate();

            try (PreparedStatement stPessoaFisica = conn.prepareStatement(sqlPessoaFisica)) {
                stPessoaFisica.setString(1, pf.getCpf());
                stPessoaFisica.setInt(2, pf.getId());
                stPessoaFisica.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void excluir(Integer id) {
        String sql = "DELETE FROM Pessoa WHERE idPessoa = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PessoaFisica getPessoa(Integer id) {
        String sql = "SELECT Pessoa.*, PessoaFisica.cpf AS cpf FROM Pessoa INNER JOIN PessoaFisica ON Pessoa.idPessoa = PessoaFisica.idPessoa WHERE Pessoa.idPessoa = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new PessoaFisica(
                            rs.getInt("idPessoa"),
                            rs.getString("nome"),
                            rs.getString("logradouro"),
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getString("cpf")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public List<PessoaFisica> getPessoas() {
        List<PessoaFisica> list = new ArrayList<>();
        String sql = "SELECT p.*, pf.cpf FROM Pessoa AS p INNER JOIN PessoaFisica AS pf ON p.idPessoa = pf.idPessoaFisica ORDER BY p.nome";

        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(new PessoaFisica(
                        rs.getInt("idPessoa"),
                        rs.getString("nome"),
                        rs.getString("logradouro"),
                        rs.getString("cidade"),
                        rs.getString("estado"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getString("cpf")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
