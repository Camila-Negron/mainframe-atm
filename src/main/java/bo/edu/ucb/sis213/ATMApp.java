package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMApp {
    private static JFrame loginFrame;
    private static JFrame menuFrame;
    private static JTextField userField;
    private static JPasswordField pinField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createLoginScreen();
            createMenuScreen();
        });
    }

    private static void createLoginScreen() {
        loginFrame = new JFrame("ATM Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        JLabel userLabel = new JLabel("Usuario:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(userLabel, constraints);

        userField = new JTextField(15);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(userField, constraints);

        JLabel pinLabel = new JLabel("PIN:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(pinLabel, constraints);

        pinField = new JPasswordField(15);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(pinField, constraints);

        JButton loginButton = new JButton("Ingresar");
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(loginButton, constraints);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateLogin(userField.getText(), new String(pinField.getPassword()))) {
                    loginFrame.setVisible(false);
                    menuFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Usuario o PIN incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(panel);
        loginFrame.setVisible(true);
    }

    private static void createMenuScreen() {
        menuFrame = new JFrame("Menu Principal");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));

        JButton consultarSaldoButton = new JButton("Consultar Saldo");
        JButton realizarDepositoButton = new JButton("Realizar Depósito");
        JButton realizarRetiroButton = new JButton("Realizar Retiro");
        JButton cambiarPINButton = new JButton("Cambiar PIN");
        JButton salirButton = new JButton("Salir");

        panel.add(consultarSaldoButton);
        panel.add(realizarDepositoButton);
        panel.add(realizarRetiroButton);
        panel.add(cambiarPINButton);
        panel.add(salirButton);

        consultarSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Consultar Saldo action
            }
        });

        realizarDepositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Realizar Depósito action
            }
        });

        realizarRetiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Realizar Retiro action
            }
        });

        cambiarPINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle Cambiar PIN action
            }
        });

        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuFrame.add(panel);
        menuFrame.setVisible(false);
    }

    private static boolean validateLogin(String username, String pin) {
        // Perform validation logic here
        // You might want to compare the username and PIN against a database
        // For now, let's assume a simple check
        return "user123".equals(username) && "1234".equals(pin);
    }
}
