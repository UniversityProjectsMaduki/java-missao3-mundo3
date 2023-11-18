package cadastrobd.model;

public class PessoaFisica extends Pessoa {
    private String cpf;

    // Adicionado o tipoPessoa 'F' ao construtor para indicar Pessoa Física
    public PessoaFisica(Integer id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cpf) {
        super(id, nome, logradouro, cidade, estado, telefone, email, 'F'); // Passa 'F' como argumento para o construtor da superclasse
        this.cpf = cpf;
    }

    // Getters e setters para o campo CPF
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // Método toString sobreposto para incluir CPF na representação em string da PessoaFisica
    @Override
    public String toString() {
        // Chamada do toString() da superclasse e adição do CPF
        return super.toString() + ", CPF: " + cpf;
    }
}
