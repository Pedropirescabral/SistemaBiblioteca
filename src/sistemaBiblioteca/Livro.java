package sistemaBiblioteca;

public class Livro {
    private int id;
    private String titulo;
    private String autor;
    private String isbn;
    private int ano;
    private int estoque;

    public Livro(int id, String titulo, String autor, String isbn, int ano, int estoque) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.ano = ano;
        this.estoque = estoque;
    }

    public int getId()         { return id; }
    public String getTitulo()  { return titulo; }
    public String getAutor()   { return autor; }
    public String getIsbn()    { return isbn; }
    public int getAno()        { return ano; }
    public int getEstoque()    { return estoque; }

    @Override
    public String toString() { return titulo; }
}
