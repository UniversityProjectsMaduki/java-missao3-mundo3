package cadastrobd.model;

public class PessoaJuridica extends Pessoa {
    private String cnpj;

    // Construtor de PessoaJuridica
    public PessoaJuridica(Integer id, String nome, String logradouro, String cidade, String estado, String telefone, String email, String cnpj) {
        super(id, nome, logradouro, cidade, estado, telefone, email, 'J'); // Chama o construtor da superclasse com 'J' como tipoPessoa
        this.cnpj = cnpj;
    }

    // Getter e setter
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    // Método toString para incluir CNPJ
    @Override
    public String toString() {
        return super.toString() + ", CNPJ: " + cnpj;
    }
}
