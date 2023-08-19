package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class App {
    // Declara las variables estáticas para usuarioId y saldo
    public static int usuarioId;
    public static double saldo;
    public static int pinActual;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (DatabaseManager.validarPIN(connection, pinIngresado)) {
                MenuManager.mostrarMenu(connection);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }
}
