package sistemaBiblioteca;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PainelLeitores extends JPanel {

    private JTextField txtNome, txtCpf, txtTelefone, txtEmail;
    private DefaultTableModel tableModel;

    public PainelLeitores() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Cadastro de Leitores");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        wrapper.add(lblTitulo);
        wrapper.add(new JSeparator());
        wrapper.add(Box.createVerticalStrut(15));

        wrapper.add(criarLabel("Nome"));
        txtNome = criarCampo();
        wrapper.add(txtNome);
        wrapper.add(Box.createVerticalStrut(10));

        // CPF + Telefone
        JPanel linhaCpfTel = new JPanel(new GridLayout(1, 2, 20, 0));
        linhaCpfTel.setBackground(Color.WHITE);
        linhaCpfTel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel pCpf = new JPanel(new BorderLayout(0, 4));
        pCpf.setBackground(Color.WHITE);
        pCpf.add(criarLabel("CPF"), BorderLayout.NORTH);
        txtCpf = new JTextField();
        txtCpf.setFont(new Font("Arial", Font.PLAIN, 13));
        pCpf.add(txtCpf, BorderLayout.CENTER);

        JPanel pTel = new JPanel(new BorderLayout(0, 4));
        pTel.setBackground(Color.WHITE);
        pTel.add(criarLabel("Telefone"), BorderLayout.NORTH);
        txtTelefone = new JTextField();
        txtTelefone.setFont(new Font("Arial", Font.PLAIN, 13));
        pTel.add(txtTelefone, BorderLayout.CENTER);

        linhaCpfTel.add(pCpf);
        linhaCpfTel.add(pTel);
        wrapper.add(linhaCpfTel);
        wrapper.add(Box.createVerticalStrut(10));

        wrapper.add(criarLabel("E-mail"));
        txtEmail = criarCampo();
        wrapper.add(txtEmail);
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
        JLabel lblTabela = new JLabel("Leitores cadastrados");
        lblTabela.setFont(new Font("Arial", Font.BOLD, 13));
        wrapper.add(lblTabela);
        wrapper.add(Box.createVerticalStrut(5));

        String[] colunas = {"ID", "Nome", "CPF", "Telefone"};
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
        String nome      = txtNome.getText().trim();
        String cpf       = txtCpf.getText().trim();
        String telefone  = txtTelefone.getText().trim();
        String email     = txtEmail.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha os campos obrigatórios: Nome e CPF.");
            return;
        }

        String sql = "INSERT INTO leitores (nome, cpf, telefone, email) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) return;
            ps.setString(1, nome);
            ps.setString(2, cpf);
            ps.setString(3, telefone);
            ps.setString(4, email);
            ps.executeUpdate();
            carregarTabela();
            limpar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, nome, cpf, telefone FROM leitores ORDER BY id";
        try (Connection con = ConexaoDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (con == null) return;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("telefone")
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar leitores: " + ex.getMessage());
        }
    }

    private void limpar() {
        txtNome.setText("");
        txtCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtNome.requestFocus();
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
