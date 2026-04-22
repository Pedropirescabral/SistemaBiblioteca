package sistemaBiblioteca;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexaoDB {

    // ── Altere SENHA conforme sua instalação do MySQL ────────
    private static final String URL     = "jdbc:mysql://localhost:3306/biblioteca?useSSL=false&serverTimezone=America/Sao_Paulo";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";        // ex: "root" ou "1234"

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "Driver MySQL não encontrado.\nAdicione o mysql-connector-java.jar em Libraries.",
                "Erro de Driver", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Erro ao conectar ao banco de dados:\n" + e.getMessage() +
                "\n\nVerifique se o MySQL está rodando e se a senha está correta em ConexaoDB.java",
                "Erro de Conexão", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
