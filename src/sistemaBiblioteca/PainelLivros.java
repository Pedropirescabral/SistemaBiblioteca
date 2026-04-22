package sistemaBiblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PainelLivros extends JPanel {

    private JTextField txtTitulo, txtAutor, txtIsbn, txtAno, txtEstoque;
    private DefaultTableModel tableModel;

    public PainelLivros() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Cadastro de Livros");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        wrapper.add(lblTitulo);
        wrapper.add(new JSeparator());
        wrapper.add(Box.createVerticalStrut(15));

        wrapper.add(criarLabel("Título"));
        txtTitulo = criarCampo();
        wrapper.add(txtTitulo);
        wrapper.add(Box.createVerticalStrut(10));

        wrapper.add(criarLabel("Autor"));
        txtAutor = criarCampo();
        wrapper.add(txtAutor);
        wrapper.add(Box.createVerticalStrut(10));

        // ISBN + Ano
        JPanel linhaIsbnAno = new JPanel(new GridLayout(1, 2, 20, 0));
        linhaIsbnAno.setBackground(Color.WHITE);
        linhaIsbnAno.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel pIsbn = new JPanel(new BorderLayout(0, 4));
        pIsbn.setBackground(Color.WHITE);
        pIsbn.add(criarLabel("ISBN"), BorderLayout.NORTH);
        txtIsbn = new JTextField();
        txtIsbn.setFont(new Font("Arial", Font.PLAIN, 13));
        pIsbn.add(txtIsbn, BorderLayout.CENTER);

        JPanel pAno = new JPanel(new BorderLayout(0, 4));
        pAno.setBackground(Color.WHITE);
        pAno.add(criarLabel("Ano de Publicação"), BorderLayout.NORTH);
        txtAno = new JTextField();
        txtAno.setFont(new Font("Arial", Font.PLAIN, 13));
        pAno.add(txtAno, BorderLayout.CENTER);

        linhaIsbnAno.add(pIsbn);
        linhaIsbnAno.add(pAno);
        wrapper.add(linhaIsbnAno);
        wrapper.add(Box.createVerticalStrut(10));

        // Estoque
        wrapper.add(criarLabel("Estoque"));
        txtEstoque = new JTextField();
        txtEstoque.setFont(new Font("Arial", Font.PLAIN, 13));
        txtEstoque.setMaximumSize(new Dimension(200, 35));
        JPanel pEst = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pEst.setBackground(Color.WHITE);
        pEst.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pEst.add(txtEstoque);
        wrapper.add(pEst);
        wrapper.add(Box.createVerticalStrut(15));

        // Botões
        JPanel pBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotoes.setBackground(Color.WHITE);
        pBotoes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setPreferredSize(new Dimension(120, 38));
        btnSalvar.setBackground(new Color(80, 80, 80));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setPreferredSize(new Dimension(120, 38));
        btnLimpar.setBackground(new Color(180, 180, 180));
        btnLimpar.setFocusPainted(false);
        btnLimpar.setFont(new Font("Arial", Font.PLAIN, 13));

        pBotoes.add(btnSalvar);
        pBotoes.add(Box.createHorizontalStrut(10));
        pBotoes.add(btnLimpar);
        wrapper.add(pBotoes);
        wrapper.add(Box.createVerticalStrut(20));

        // Tabela
        JLabel lblTabela = new JLabel("Livros cadastrados");
        lblTabela.setFont(new Font("Arial", Font.BOLD, 13));
        wrapper.add(lblTabela);
        wrapper.add(Box.createVerticalStrut(5));

        String[] colunas = {"ID", "Título", "Autor", "Estoque"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabela = new JTable(tableModel);
        tabela.setRowHeight(28);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.getTableHeader().setBackground(new Color(200, 200, 200));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 180));
        wrapper.add(scroll);

        add(wrapper, BorderLayout.NORTH);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());

        carregarTabela();
    }

    private void salvar() {
        String titulo   = txtTitulo.getText().trim();
        String autor    = txtAutor.getText().trim();
        String isbn     = txtIsbn.getText().trim();
        String anoStr   = txtAno.getText().trim();
        String estStr   = txtEstoque.getText().trim();

        if (titulo.isEmpty() || autor.isEmpty() || estStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios: Título, Autor e Estoque.");
            return;
        }

        try {
            int estoque = Integer.parseInt(estStr);
            int ano     = anoStr.isEmpty() ? 0 : Integer.parseInt(anoStr);

            String sql = "INSERT INTO livros (titulo, autor, isbn, ano, estoque) VALUES (?, ?, ?, ?, ?)";
            try (Connection con = ConexaoDB.conectar();
                 PreparedStatement ps = con.prepareStatement(sql)) {
                if (con == null) return;
                ps.setString(1, titulo);
                ps.setString(2, autor);
                ps.setString(3, isbn);
                ps.setInt(4, ano);
                ps.setInt(5, estoque);
                ps.executeUpdate();
            }

            carregarTabela();
            limpar();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ano e Estoque devem ser valores numéricos.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, titulo, autor, estoque FROM livros ORDER BY id";
        try (Connection con = ConexaoDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (con == null) return;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("titulo"),
                    rs.getString("autor"),
                    rs.getInt("estoque")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + ex.getMessage());
        }
    }

    private void limpar() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtAno.setText("");
        txtEstoque.setText("");
        txtTitulo.requestFocus();
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    private JTextField criarCampo() {
        JTextField f = new JTextField();
        f.setFont(new Font("Arial", Font.PLAIN, 13));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        f.setPreferredSize(new Dimension(Integer.MAX_VALUE, 35));
        return f;
    }
}
