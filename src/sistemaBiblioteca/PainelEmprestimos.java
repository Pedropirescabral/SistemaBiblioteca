package sistemaBiblioteca;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PainelEmprestimos extends JPanel {

    private JComboBox<Livro>   cmbLivro;
    private JComboBox<Leitor>  cmbLeitor;
    private JTextField         txtDataEmprestimo, txtDataDevolucao;
    private DefaultTableModel  tableModel;
    private JTable             tabela;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PainelEmprestimos() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.WHITE);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Empréstimos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        wrapper.add(lblTitulo);
        wrapper.add(new JSeparator());
        wrapper.add(Box.createVerticalStrut(15));

        // ComboBox Livro
        wrapper.add(criarLabel("Livro"));
        cmbLivro = new JComboBox<>();
        cmbLivro.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbLivro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbLivro.setBackground(Color.WHITE);
        wrapper.add(cmbLivro);
        wrapper.add(Box.createVerticalStrut(10));

        // ComboBox Leitor
        wrapper.add(criarLabel("Leitor"));
        cmbLeitor = new JComboBox<>();
        cmbLeitor.setFont(new Font("Arial", Font.PLAIN, 13));
        cmbLeitor.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cmbLeitor.setBackground(Color.WHITE);
        wrapper.add(cmbLeitor);
        wrapper.add(Box.createVerticalStrut(10));

        // Datas
        JPanel linhaDatas = new JPanel(new GridLayout(1, 2, 20, 0));
        linhaDatas.setBackground(Color.WHITE);
        linhaDatas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel pEmp = new JPanel(new BorderLayout(0, 4));
        pEmp.setBackground(Color.WHITE);
        pEmp.add(criarLabel("Data do Empréstimo"), BorderLayout.NORTH);
        txtDataEmprestimo = new JTextField(LocalDate.now().format(FMT));
        txtDataEmprestimo.setFont(new Font("Arial", Font.PLAIN, 13));
        pEmp.add(txtDataEmprestimo, BorderLayout.CENTER);

        JPanel pDev = new JPanel(new BorderLayout(0, 4));
        pDev.setBackground(Color.WHITE);
        pDev.add(criarLabel("Data de Devolução Prevista"), BorderLayout.NORTH);
        txtDataDevolucao = new JTextField(LocalDate.now().plusDays(14).format(FMT));
        txtDataDevolucao.setFont(new Font("Arial", Font.PLAIN, 13));
        pDev.add(txtDataDevolucao, BorderLayout.CENTER);

        linhaDatas.add(pEmp);
        linhaDatas.add(pDev);
        wrapper.add(linhaDatas);
        wrapper.add(Box.createVerticalStrut(15));

        // Botão Registrar
        JPanel pBotao = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pBotao.setBackground(Color.WHITE);
        pBotao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setPreferredSize(new Dimension(160, 38));
        btnRegistrar.setBackground(new Color(80, 80, 80));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setFont(new Font("Arial", Font.PLAIN, 13));
        pBotao.add(btnRegistrar);
        wrapper.add(pBotao);
        wrapper.add(Box.createVerticalStrut(20));

        // Tabela
        JLabel lblTabela = new JLabel("Empréstimos ativos");
        lblTabela.setFont(new Font("Arial", Font.BOLD, 13));
        wrapper.add(lblTabela);
        wrapper.add(Box.createVerticalStrut(5));

        String[] colunas = {"ID", "Livro", "Leitor", "Devolução", "Ação"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; }
            @Override public Class<?> getColumnClass(int c) { return c == 4 ? JButton.class : Object.class; }
        };

        tabela = new JTable(tableModel);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Arial", Font.PLAIN, 12));
        tabela.getTableHeader().setBackground(new Color(200, 200, 200));
        tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Destacar linhas atrasadas
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, value, sel, foc, row, col);
                String dataDev = (String) tableModel.getValueAt(row, 3);
                boolean atrasado = isAtrasado(dataDev);
                if (sel) {
                    c.setBackground(new Color(220, 230, 255));
                    c.setForeground(Color.BLACK);
                } else if (atrasado) {
                    c.setBackground(new Color(255, 255, 200));
                    c.setForeground(col == 3 ? Color.RED : Color.BLACK);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        tabela.getColumn("Ação").setCellRenderer(new BotaoRenderer());
        tabela.getColumn("Ação").setCellEditor(new BotaoEditor(new JCheckBox(), this));
        tabela.getColumn("Ação").setMaxWidth(110);
        tabela.getColumn("ID").setMaxWidth(50);

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        wrapper.add(scroll);

        add(wrapper, BorderLayout.NORTH);

        btnRegistrar.addActionListener(e -> registrar());
    }

    // Chamado ao navegar para esta tela
    public void atualizarCombos() {
        cmbLivro.removeAllItems();
        String sqlL = "SELECT id, titulo, autor, isbn, ano, estoque FROM livros ORDER BY titulo";
        try (Connection con = ConexaoDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlL)) {
            if (con == null) return;
            while (rs.next()) {
                cmbLivro.addItem(new Livro(rs.getInt("id"), rs.getString("titulo"),
                        rs.getString("autor"), rs.getString("isbn"),
                        rs.getInt("ano"), rs.getInt("estoque")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar livros: " + ex.getMessage());
        }

        cmbLeitor.removeAllItems();
        String sqlR = "SELECT id, nome, cpf, telefone, email FROM leitores ORDER BY nome";
        try (Connection con = ConexaoDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sqlR)) {
            if (con == null) return;
            while (rs.next()) {
                cmbLeitor.addItem(new Leitor(rs.getInt("id"), rs.getString("nome"),
                        rs.getString("cpf"), rs.getString("telefone"), rs.getString("email")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar leitores: " + ex.getMessage());
        }

        carregarTabela();
    }

    private void registrar() {
        Livro  livro  = (Livro)  cmbLivro.getSelectedItem();
        Leitor leitor = (Leitor) cmbLeitor.getSelectedItem();
        String dataEmp = txtDataEmprestimo.getText().trim();
        String dataDev = txtDataDevolucao.getText().trim();

        if (livro == null || leitor == null) {
            JOptionPane.showMessageDialog(this, "Selecione um livro e um leitor.");
            return;
        }
        if (dataEmp.isEmpty() || dataDev.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha as datas.");
            return;
        }

        String sql = "INSERT INTO emprestimos (livro_id, leitor_id, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) return;
            ps.setInt(1, livro.getId());
            ps.setInt(2, leitor.getId());
            ps.setString(3, dataEmp);
            ps.setString(4, dataDev);
            ps.executeUpdate();
            carregarTabela();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar: " + ex.getMessage());
        }
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        String sql = "SELECT e.id, l.titulo, r.nome, e.data_devolucao " +
                     "FROM emprestimos e " +
                     "JOIN livros  l ON e.livro_id  = l.id " +
                     "JOIN leitores r ON e.leitor_id = r.id " +
                     "ORDER BY e.id";
        try (Connection con = ConexaoDB.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (con == null) return;
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("e.id"),
                    rs.getString("l.titulo"),
                    rs.getString("r.nome"),
                    rs.getString("e.data_devolucao"),
                    "Devolver"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage());
        }
    }

    public void devolverLinha(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) return;
        int id = (int) tableModel.getValueAt(row, 0);
        String sql = "DELETE FROM emprestimos WHERE id = ?";
        try (Connection con = ConexaoDB.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (con == null) return;
            ps.setInt(1, id);
            ps.executeUpdate();
            tableModel.removeRow(row);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao devolver: " + ex.getMessage());
        }
    }

    private boolean isAtrasado(String dataDev) {
        try {
            return LocalDate.parse(dataDev, FMT).isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private JLabel criarLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Arial", Font.PLAIN, 12));
        return l;
    }

    // ── Renderer botão Devolver ──────────────────────────────
    static class BotaoRenderer extends JButton implements TableCellRenderer {
        public BotaoRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int row, int col) {
            setText("Devolver");
            setBackground(new Color(100, 100, 100));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 11));
            return this;
        }
    }

    // ── Editor botão Devolver ────────────────────────────────
    static class BotaoEditor extends DefaultCellEditor {
        private final JButton botao;
        private final PainelEmprestimos painel;
        private int linhaAtual;

        public BotaoEditor(JCheckBox cb, PainelEmprestimos painel) {
            super(cb);
            this.painel = painel;
            botao = new JButton("Devolver");
            botao.setOpaque(true);
            botao.setBackground(new Color(100, 100, 100));
            botao.setForeground(Color.WHITE);
            botao.setFont(new Font("Arial", Font.PLAIN, 11));
            botao.addActionListener(e -> {
                fireEditingStopped();
                painel.devolverLinha(linhaAtual);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v,
                boolean sel, int row, int col) {
            linhaAtual = row;
            return botao;
        }

        @Override
        public Object getCellEditorValue() { return "Devolver"; }
    }
}
