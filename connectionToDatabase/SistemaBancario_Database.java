package connectionToDatabase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SistemaBancario_Database {
    public Connection getConnetion() throws SQLException, IOException {
        Properties properties = getProperties();
        String URL = properties.getProperty("banco.url");
        String User = properties.getProperty("banco.usuario");
        String Password = properties.getProperty("banco.senha");

        try {
            return DriverManager.getConnection(URL, User, Password);
        } catch (SQLException e) {
            System.out.println("DEU RUIM");
            throw new RuntimeException(e);
        }
    }

    private static Properties getProperties() throws IOException {
        Properties properties = new Properties();
        String Path = "connection.properties";
        properties.load(SistemaBancario_Database.class.getResourceAsStream(Path));
        return properties;
    }

}
