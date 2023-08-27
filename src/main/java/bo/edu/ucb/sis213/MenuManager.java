package bo.edu.ucb.sis213;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MenuManager {
    public static void mostrarMenu(Connection connection) {
        String[] opciones = {"Consultar saldo", "Realizar un depósito", "Realizar un retiro", "Cambiar PIN", "Salir"};
        int opcionSeleccionada = JOptionPane.showOptionDialog(null, "Menú Principal", "ATM Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);

        switch (opcionSeleccionada) {
            case 0:
                consultarSaldo(connection);
                break;
            case 1:
                realizarDeposito(connection);
                break;
            case 2:
                realizarRetiro(connection);
                break;
            case 3:
                cambiarPIN(connection);
                break;
            case 4:
                JOptionPane.showMessageDialog(null, "Gracias por usar el cajero. ¡Hasta luego!");
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción no válida. Intente nuevamente.");
        }
    }

    public static void consultarSaldo(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT saldo FROM usuarios WHERE id = ?");
            preparedStatement.setInt(1, App.usuarioId);  // Usa la variable de usuarioId de la clase App
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double saldo = resultSet.getDouble("saldo");
                JOptionPane.showMessageDialog(null, "Su saldo actual es: $" + saldo, "Saldo", JOptionPane.INFORMATION_MESSAGE);

                mostrarMenu(connection);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void realizarDeposito(Connection connection) {
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a depositar: $");
        double cantidad;

        try {
            cantidad = Double.parseDouble(cantidadStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Realizar el depósito en la tabla usuarios
                    PreparedStatement depositStatement = connection.prepareStatement("UPDATE usuarios SET saldo = saldo + ? WHERE id = ?");
                    depositStatement.setDouble(1, cantidad);
                    depositStatement.setInt(2, App.usuarioId);
                    int depositRowsAffected = depositStatement.executeUpdate();

                    if (depositRowsAffected > 0) {
                        App.saldo += cantidad;
                        System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + App.saldo);

                        // Registrar la transacción en la tabla histórica
                        insertTransaccion(connection, "DEPOSITO", cantidad);

                        mostrarMenu(connection);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void realizarRetiro(Connection connection) {
        String cantidadStr = JOptionPane.showInputDialog("Ingrese la cantidad a retirar: $");
        double cantidad;

        try {
            cantidad = Double.parseDouble(cantidadStr);

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(null, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Realizar el retiro en la tabla usuarios
                    PreparedStatement withdrawStatement = connection.prepareStatement("UPDATE usuarios SET saldo = saldo - ? WHERE id = ?");
                    withdrawStatement.setDouble(1, cantidad);
                    withdrawStatement.setInt(2, App.usuarioId);
                    int withdrawRowsAffected = withdrawStatement.executeUpdate();

                    if (withdrawRowsAffected > 0) {
                        App.saldo -= cantidad;
                        System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + App.saldo);

                        // Registrar la transacción en la tabla histórica
                        insertTransaccion(connection, "RETIRO", cantidad);

                        mostrarMenu(connection);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public static void insertTransaccion(Connection connection, String tipo, double cantidad) {
        try {
            PreparedStatement transaccionStatement = connection.prepareStatement("INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)");
            transaccionStatement.setInt(1, App.usuarioId);
            transaccionStatement.setString(2, tipo);
            transaccionStatement.setDouble(3, cantidad);
            int transaccionRowsAffected = transaccionStatement.executeUpdate();
    
            if (transaccionRowsAffected > 0) {
                System.out.println("Transacción registrada en la tabla histórica.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cambiarPIN(Connection connection) {
        String pinActualStr = JOptionPane.showInputDialog("Ingrese su PIN actual: ");
        int pinIngresado;

        try {
            pinIngresado = Integer.parseInt(pinActualStr);

            if (pinIngresado == App.pinActual) {
                // Resto de la lógica del cambio de PIN
            } else {
                JOptionPane.showMessageDialog(null, "PIN incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "PIN inválido. Ingrese un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
