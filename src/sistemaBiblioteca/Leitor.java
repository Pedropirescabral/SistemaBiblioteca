package sistemaBiblioteca;

public class Leitor {
    private int id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    public Leitor(int id, String nome, String cpf, String telefone, String email) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }

    public int getId()           { return id; }
    public String getNome()      { return nome; }
    public String getCpf()       { return cpf; }
    public String getTelefone()  { return telefone; }
    public String getEmail()     { return email; }

    @Override
    public String toString() { return nome; }
}
