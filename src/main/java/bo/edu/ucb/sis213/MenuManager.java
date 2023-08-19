package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class MenuManager {
    public static void mostrarMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo(connection);
                    break;
                case 2:
                    realizarDeposito(connection);
                    break;
                case 3:
                    realizarRetiro(connection);
                    break;
                case 4:
                    cambiarPIN(connection);
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void consultarSaldo(Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT saldo FROM usuarios WHERE id = ?");
            preparedStatement.setInt(1, App.usuarioId);  // Usa la variable de usuarioId de la clase App
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double saldo = resultSet.getDouble("saldo");
                System.out.println("Su saldo actual es: $" + saldo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void realizarDeposito(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void realizarRetiro(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();
    
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (cantidad > App.saldo) {
            System.out.println("Saldo insuficiente.");
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese su PIN actual: ");
        int pinIngresado = scanner.nextInt();

        if (pinIngresado == App.pinActual) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement("UPDATE usuarios SET pin = ? WHERE id = ?");
                    preparedStatement.setInt(1, nuevoPin);
                    preparedStatement.setInt(2, App.usuarioId);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        App.pinActual = nuevoPin;
                        System.out.println("PIN actualizado con éxito.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }
}
