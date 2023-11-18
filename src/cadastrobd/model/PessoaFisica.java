package cadastrobd.model;

public class PessoaFisica extends Pessoa {
    private String cpf;

    // Construtor de PessoaFisica
    public PessoaFisica(Integer id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cpf) {
        super(id, nome, logradouro, cidade, estado, telefone, email, 'F'); // Chama o construtor da superclasse com 'F' como tipoPessoa
        this.cpf = cpf;
    }

    // Getter e setter
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // Método toString para incluir CPF
    @Override
    public String toString() {
        return super.toString() + ", CPF: " + cpf;
    }
}
