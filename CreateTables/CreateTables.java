package CreateTables;

import connectionToDatabase.SistemaBancario_Database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTables {
   SistemaBancario_Database sistemaBancarioDatabase = new SistemaBancario_Database();
   Connection connection = sistemaBancarioDatabase.getConnetion();

    public CreateTables() throws SQLException, IOException {
    }


    public void Tables() throws SQLException {
        String createClient = """
                CREATE TABLE IF NOT EXISTS clientes(
                    CPF VARCHAR(50)  NOT NULL,
                    NOME VARCHAR(50) NOT NULL,
                    NASCIMENTO VARCHAR(15) NOT NULL,
                    PRIMARY KEY(CPF)
                )
                """;

        String createAccount = """
                CREATE TABLE IF NOT EXISTS Accounts(
                    ID int UNSIGNED NOT NULL AUTO_INCREMENT,
                    BALANCE DECIMAL(10,2) NOT NULL,
                    ClientsID VARCHAR(15) NOT NULL,
                    PRIMARY KEY(ID),
                    Foreign Key (ClientsID) REFERENCES clientes (CPF)
                )
                """;


        Statement statementClient = connection.createStatement();
        statementClient.execute(createClient);
        statementClient.close();

        Statement statementAccount = connection.createStatement();
        statementAccount.execute(createAccount);
        statementAccount.close();
    }
}
