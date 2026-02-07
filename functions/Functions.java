package functions;
import connectionToDatabase.SistemaBancario_Database;

import javax.swing.plaf.PanelUI;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.SequencedCollection;

public class Functions {
    private Connection connection;

    public Connection getConnection() throws SQLException, IOException {
        try{
            if (connection != null && !connection.isClosed()){
                return connection;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        SistemaBancario_Database database = new SistemaBancario_Database();
        connection = database.getConnetion();
        return connection;
    }

    //FUNÇOES
    public void options() throws SQLException, IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\u001B[31m╔════════════════════╗\u001B[0m");
        System.out.println("\u001B[31m║   MENU PRINCIPAL   ║\u001B[0m");
        System.out.println("\u001B[31m╚════════════════════╝\u001B[0m");

        System.out.println("BEM VINDO AO BANCO SANTO ANDRÉ! AQUELE VERMELINHO" +
                "\n O QUE DESEJA FAZER HOJE: " +
                "\n 1 - |CRIAR UMA CONTA| " +
                "\n 2 - |ACESSAR SUA CONTA|" +
                "\n 3 - |ATUALIZAR DADOSs CADASTRADOS|" +
                "\n 4 - |ENCERRAR CONTA :(|"  +
                "\n 5 - |SAIR....|"
        );

        System.out.println("DIGITE: ");
        String opcao = scanner.next();
        String cpf;
        int confirm;

        switch (opcao){
            case "1":
                criarConta();
                break;
            case "2":
                System.out.println("INFORME SEU CPF: ");
                cpf = scanner.next();
                confirm = acessarConta(cpf);
                if (confirm < 0){
                    options();
                }
                break;
            case "3":
                System.out.println("INFORME SEU CPF: ");
                cpf = scanner.next();
                confirm = alterarDadosCadastrados(cpf);

                if (confirm == -1){
                    options();
                }else{
                    System.out.println("OPÇÃO INVÁLIDA! TENTE NOVAMENTE");
                    alterarDadosCadastrados(cpf);
                }
                break;
            case "5":
                System.out.println("OBRIGADO POR USAR NOSSOS SERVIÇOS");
                return;
            default:
                System.out.println("OPÇÃO INVÁLIDA");
        }
    }

    public void criarConta() throws SQLException, IOException, ParseException {
        System.out.println("OTIMO! PARA ABRIR UMA CONTA PRECISAREMOS DE ALGUMAS INFORMAÇÕES....");
        Scanner scanner = new Scanner(System.in);
        System.out.println("INFORME SEU CPF: ");
        String cpf = scanner.nextLine();

        System.out.println("INFORME SEU NOME:");
        String nome = scanner.nextLine();

        System.out.println("INFORME SUA DATA DE NASCIMENTO DD/MM/YYYY : ");
        String nascimento = scanner.nextLine();

        String insertClient = "INSERT INTO CLIENTES(CPF, NOME, NASCIMENTO) VALUES (?, ?, ?)";

        PreparedStatement smtm = getConnection().prepareStatement(insertClient);
        smtm.setString(1, cpf);
        smtm.setString(2, nome);
        smtm.setString(3, nascimento);

        smtm.execute();
        smtm.close();

        String insertAccount = "INSERT INTO ACCOUNTS (BALANCE, CLIENTSID) VALUES (?, ?)";

        smtm = getConnection().prepareStatement(insertAccount);

        BigDecimal saldo = new BigDecimal(0);
        smtm.setBigDecimal(1, saldo);
        smtm.setString(2, cpf);

        smtm.execute();
        smtm.close();


        System.out.println("USUÁRIO CADASTRADO E CONTA CRIADA COM SUCESSO");
        options();
    }

    public int acessarConta(String cpf) throws SQLException, IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        String searchAccount = "SELECT * FROM ACCOUNTS WHERE CLIENTSID = ?";
        PreparedStatement smtm = getConnection().prepareStatement(searchAccount);

        smtm.setString(1, cpf);

        ResultSet resultSet = smtm.executeQuery();

        if (resultSet.next()){
            int id = resultSet.getInt("ID");
            BigDecimal saldo = resultSet.getBigDecimal("balance");
            String clientCpf = resultSet.getString("ClientsID");

            System.out.println("\u001B[31m╔════════════════════╗\u001B[0m");
            System.out.println("\u001B[31m║  DADOS DA CONTA:   ║\u001B[0m");
            System.out.println("\u001B[31m╚════════════════════╝\u001B[0m");

            System.out.println(
                    "SALDO ATUAL: " + saldo +
                    "\nID DA CONTA: " + id +
                    "\nID DO CLIENTE: " + clientCpf
            );
        }else {
            System.out.println("CONTA NÃO ENCONTRADA!");
            return -1;
        }



        System.out.println("O QUE DESEJA FAZER NA SUA CONTA: " +
                "\n 1 - SACAR" +
                "\n 2 - DEPOSITAR " +
                "\n 3 - SAIR DA CONTA"
        );

        System.out.println("DIGITE: ");

        String option = scanner.nextLine();

        switch (option){
            case "1" -> sacar(cpf);
            case "2" -> depositar(cpf);
            case "3" -> options();
        }

        smtm.execute();
        smtm.close();
        return 0;
    }

    //TODO
    public void sacar(String cpf) throws SQLException, IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("INFORME QUAL O VALOR DESEJADO PARA SAQUE: ");
        System.out.println("DIGITE: ");
        BigDecimal valorSacado = scanner.nextBigDecimal();

        String select = "SELECT ACCOUNTS.BALANCE FROM ACCOUNTS WHERE CLIENTSID = ?";

        PreparedStatement smtm = getConnection().prepareStatement(select);
        smtm.setString(1, cpf);
        ResultSet resultSet = smtm.executeQuery();

        if (resultSet.next()){
            BigDecimal saldoAtual = resultSet.getBigDecimal("balance");
            BigDecimal novoSaldo = saldoAtual.subtract(valorSacado);



            String update = "UPDATE ACCOUNTS SET BALANCE = ? WHERE CLIENTSID = ?";
            smtm = smtm.getConnection().prepareStatement(update);
            smtm.setBigDecimal(1, novoSaldo);
            smtm.setString(2, cpf);

            int linhasAlteradas = smtm.executeUpdate();
            if (linhasAlteradas > 0){
                System.out.println("VALOR FOI SACADO DE SUA CONTA");
                System.out.println("SALDO ATUAL: " + novoSaldo);
                smtm.close();
                acessarConta(cpf);
            }
        }

    }
    //TODO
    public void depositar(String cpf) throws SQLException, IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("INFORME QUAL O VALOR DESEJADO PARA DEPÓSITO: ");
        System.out.println("DIGITE: ");
        BigDecimal valorSacado = scanner.nextBigDecimal();

        String select = "SELECT ACCOUNTS.BALANCE FROM ACCOUNTS WHERE CLIENTSID = ?";

        PreparedStatement smtm = getConnection().prepareStatement(select);
        smtm.setString(1, cpf);
        ResultSet resultSet = smtm.executeQuery();

        if (resultSet.next()) {
            BigDecimal saldoAtual = resultSet.getBigDecimal("balance");
            BigDecimal novoSaldo = saldoAtual.add(valorSacado);

            String update = "UPDATE ACCOUNTS SET BALANCE = ? WHERE CLIENTSID = ?";
            smtm = smtm.getConnection().prepareStatement(update);

            smtm.setBigDecimal(1, novoSaldo);
            smtm.setString(2, cpf);
            int linhasAlteradas = smtm.executeUpdate();
            if (linhasAlteradas > 0) {
                System.out.println("VALOR FOI DEPOSITADO NA SUA CONTA");
                System.out.println("SALDO ATUAL: " + novoSaldo);
                smtm.close();
                acessarConta(cpf);
            }

        }
    }

    public int alterarDadosCadastrados(String cpf) throws SQLException, IOException {
        String searchAccount = "SELECT * FROM CLIENTES WHERE CPF = ?";
        PreparedStatement smtm = getConnection().prepareStatement(searchAccount);

        smtm.setString(1, cpf);

        ResultSet resultSet = smtm.executeQuery();
        if (resultSet.next()){
            int id = resultSet.getInt("CPF");
            String nome = resultSet.getString("NOME");
            String nascimento = resultSet.getString("NASCIMENTO");

            System.out.println("\u001B[31m╔════════════════════╗\u001B[0m");
            System.out.println("\u001B[31m║DADOS DO CLIENTE:   ║\u001B[0m");
            System.out.println("\u001B[31m╚════════════════════╝\u001B[0m");

            System.out.println(
                    "NOME CADASTRADO: " + nome +
                            "\nCPF DA CONTA: " + id +
                            "\nDATA DE NASCIMENTO CADASTRADA: " + nascimento
            );
        }else {
            System.out.println("CONTA NÃO ENCONTRADA!");
            return -1;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("O QUE DESEJA ATUALIZAR: " +
                "\n1 - NOME CADASTRADO" +
                "\n2 - DATA DE NASCIMENTO CADASTRADA" +
                "\n3 - NÃO DESEJO ATUALIZAR"
        );
        System.out.print("DIGITE: ");
        String option = scanner.nextLine();

        String updateInformations = "";

        switch (option){
            case "1" -> updateInformations = "UPDATE CLIENTES SET NOME = ? WHERE CPF = ?";
            case "2" -> updateInformations = "UPDATE CLIENTES SET NASCIMENTO = ? WHERE CPF = ?";
            case "3" -> {
                return -1;
            }
            default -> {
                return 2;
            }

        }

        System.out.print("INFORME A INFORMAÇÃO CORRETA: ");
        String novaInformacao = scanner.nextLine();

         smtm = smtm.getConnection().prepareStatement(updateInformations);
         smtm.setString(1, novaInformacao);
         smtm.setString(2, cpf);
         int linhasAfetadas = smtm.executeUpdate();

         if (linhasAfetadas > 0){
             System.out.println("DADOS CADASTRADOS ATUALIZADOS");
             alterarDadosCadastrados(cpf);
         }else {
             return -1;
         }

         smtm.close();
        return 0;
    }

}
