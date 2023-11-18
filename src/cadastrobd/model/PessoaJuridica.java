package cadastrobd.model;

public class PessoaJuridica extends Pessoa {
    private String cnpj;

    // Adicionado o tipoPessoa 'J' ao construtor para indicar Pessoa Jurídica
    public PessoaJuridica(int id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cnpj) {
        super(id, nome, logradouro, cidade, estado, telefone, email, 'J'); // Passa 'J' como argumento para o construtor da superclasse
        this.cnpj = cnpj;
    }

    // Getters e setters para o campo CNPJ
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    // Método toString sobreposto para incluir CNPJ na representação em string da PessoaJuridica
    @Override
    public String toString() {
        // Chamada do toString() da superclasse e adição do CNPJ
        return super.toString() + ", CNPJ: " + cnpj;
    }
}
