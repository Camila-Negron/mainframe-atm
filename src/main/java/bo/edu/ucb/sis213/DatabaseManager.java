package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseManager {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3307;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "atm";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public static boolean validarCredenciales(Connection connection, String alias, int pin) {
        try {
            String query = "SELECT * FROM usuarios WHERE alias = ? AND pin = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, alias);
            preparedStatement.setInt(2, pin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int usuarioId = resultSet.getInt("id");
                double saldo = resultSet.getDouble("saldo");

                // Almacenar el usuarioId y saldo en variables de la clase App para uso posterior
                App.usuarioId = usuarioId;
                App.saldo = saldo;

                return true;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false; // En caso de error, asumimos que las credenciales no son válidas.
        }
        return false;
    }


    // Otros métodos de acceso a la base de datos
}
