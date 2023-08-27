package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static int usuarioId;
    public static double saldo;
    public static int pinActual;

    public static void main(String[] args) {
        final Connection[] connection = {null}; // Array para almacenar la conexión

        try {
            connection[0] = DatabaseManager.getConnection();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se puede conectar a la Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

        // Diseño de la ventana de login
        JTextField aliasField = new JTextField(20);
        JPasswordField pinField = new JPasswordField(4);
        JButton ingresarButton = new JButton("Ingresar");

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.add(new JLabel("Alias:"));
        loginPanel.add(aliasField);
        loginPanel.add(new JLabel("PIN:"));
        loginPanel.add(pinField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(ingresarButton);

        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.getContentPane().add(loginPanel);
        loginFrame.pack();
        loginFrame.setVisible(true);

        ingresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alias = aliasField.getText();
                char[] pinChars = pinField.getPassword();
                String pinString = new String(pinChars);
                int pinIngresado = Integer.parseInt(pinString);

                if (DatabaseManager.validarCredenciales(connection[0], alias, pinIngresado)) {
                    loginFrame.dispose(); // Cerrar la ventana de login después de un inicio de sesión exitoso
                    MenuManager.mostrarMenu(connection[0]); // Mostrar el panel del menú principal
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
