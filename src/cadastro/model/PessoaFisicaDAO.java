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
        String sqlPessoa = "INSERT INTO Pessoa (nome, logradouro, cidade, estado, telefone, email, tipoPessoa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlPessoaFisica = "INSERT INTO PessoaFisica (idPessoaFisica, cpf) VALUES (?, ?)";

        try (PreparedStatement stPessoa = conn.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
            stPessoa.setString(1, pf.getNome());
            stPessoa.setString(2, pf.getLogradouro());
            stPessoa.setString(3, pf.getCidade());
            stPessoa.setString(4, pf.getEstado());
            stPessoa.setString(5, pf.getTelefone());
            stPessoa.setString(6, pf.getEmail());
            stPessoa.setString(7, "F"); // Definindo o tipo como 'F' para Pessoa Física
            stPessoa.executeUpdate();

            try (ResultSet rs = stPessoa.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    try (PreparedStatement stPessoaFisica = conn.prepareStatement(sqlPessoaFisica)) {
                        stPessoaFisica.setInt(1, idGerado); // Usando o ID gerado
                        stPessoaFisica.setString(2, pf.getCpf());
                        stPessoaFisica.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    public void alterar(PessoaFisica pf) throws SQLException {
        String sqlPessoa = "UPDATE Pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
        String sqlPessoaFisica = "UPDATE PessoaFisica SET cpf = ? WHERE idPessoaFisica = ?";

        try {
            conn.setAutoCommit(false);

            // Atualiza a tabela Pessoa
            try (PreparedStatement stPessoa = conn.prepareStatement(sqlPessoa)) {
                stPessoa.setString(1, pf.getNome());
                stPessoa.setString(2, pf.getLogradouro());
                stPessoa.setString(3, pf.getCidade());
                stPessoa.setString(4, pf.getEstado());
                stPessoa.setString(5, pf.getTelefone());
                stPessoa.setString(6, pf.getEmail());
                stPessoa.setInt(7, pf.getId());
                stPessoa.executeUpdate();
            }

            // Atualiza a tabela PessoaFisica
            try (PreparedStatement stPessoaFisica = conn.prepareStatement(sqlPessoaFisica)) {
                stPessoaFisica.setString(1, pf.getCpf());
                stPessoaFisica.setInt(2, pf.getId());
                stPessoaFisica.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
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
        String sql = "SELECT Pessoa.idPessoa, Pessoa.nome, Pessoa.logradouro, Pessoa.cidade, Pessoa.estado, Pessoa.telefone, Pessoa.email, PessoaFisica.cpf " +
                "FROM Pessoa INNER JOIN PessoaFisica ON Pessoa.idPessoa = PessoaFisica.idPessoaFisica " +
                "WHERE Pessoa.idPessoa = ?"; // Confirme se o nome da coluna é este
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new PessoaFisica(
                            rs.getInt("idPessoa"), // Confirme se o nome da coluna é este
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
