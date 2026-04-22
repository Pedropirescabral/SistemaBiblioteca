package sistemaBiblioteca;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel painelConteudo;
    private PainelEmprestimos painelEmprestimos;

    public TelaPrincipal() {
        setTitle("Sistema de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Sidebar ──────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(new Color(210, 210, 210));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JLabel lblMenu = new JLabel("Menu");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 14));
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblMenu);
        sidebar.add(Box.createVerticalStrut(10));

        JButton btnLivros = criarBotaoMenu("Livros");
        JButton btnLeitores = criarBotaoMenu("Leitores");
        JButton btnEmprestimos = criarBotaoMenu("Empréstimos");

        sidebar.add(btnLivros);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnLeitores);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnEmprestimos);
        sidebar.add(Box.createVerticalGlue());

        // ── Painel de conteúdo com CardLayout ────────────────────
        cardLayout = new CardLayout();
        painelConteudo = new JPanel(cardLayout);

        PainelLivros painelLivros = new PainelLivros();
        PainelLeitores painelLeitores = new PainelLeitores();
        painelEmprestimos = new PainelEmprestimos();

        painelConteudo.add(painelLivros, "LIVROS");
        painelConteudo.add(painelLeitores, "LEITORES");
        painelConteudo.add(painelEmprestimos, "EMPRESTIMOS");

        // ── Ações dos botões ─────────────────────────────────────
        btnLivros.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LIVROS");
            ativarBotao(btnLivros, btnLeitores, btnEmprestimos);
        });

        btnLeitores.addActionListener(e -> {
            cardLayout.show(painelConteudo, "LEITORES");
            ativarBotao(btnLeitores, btnLivros, btnEmprestimos);
        });

        btnEmprestimos.addActionListener(e -> {
            painelEmprestimos.atualizarCombos();
            cardLayout.show(painelConteudo, "EMPRESTIMOS");
            ativarBotao(btnEmprestimos, btnLivros, btnLeitores);
        });

        // Tela inicial: Livros
        ativarBotao(btnLivros, btnLeitores, btnEmprestimos);

        add(sidebar, BorderLayout.WEST);
        add(painelConteudo, BorderLayout.CENTER);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void ativarBotao(JButton ativo, JButton... outros) {
        ativo.setBackground(new Color(150, 150, 150));
        for (JButton btn : outros) {
            btn.setBackground(Color.WHITE);
        }
    }
}
