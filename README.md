🏦 Sistema Bancário em Java

Projeto desenvolvido para praticar conceitos de Java Backend com integração a banco de dados utilizando JDBC e MySQL.

⚙️ Tecnologias Utilizadas

- Java 17 (ou a sua versão)
- MySQL
- JDBC
- IntelliJ IDEA
- Git / GitHub

🧠 Funcionalidades

O sistema simula operações básicas de um banco:

- Criar conta
- Encerrar conta
- Acesso à Conta
   * Sacar
   * Depositar
- Visualizar saldo
- Atualizar dados

🚀 Como Rodar o Projeto
1. Clonar o repositório
git clone https://github.com/HenryqueVitoriano/SistemaBancario.git

2. Criar um banco de dados no MySQL Workbench
3. Configurar conexão

  - Abra o arquivo:

      Connection.example.properties


  - Edite para suas credenciais:

      banco.url=jdbc:mysql://localhost/NOMEDOBANCO
      banco.usuario=SEU_USUARIO
      banco.senha=SUA_SENHA

4. Ajustar o Path

  - Na classe:

      SistemaBancario_Database

  - Trocar:

      connection.properties
    
  - por

    connection.example.properties

5. Driver MySQL

Se aparecer erro de driver:

 - Clique com botão direito no arquivo mysql-connector
 - Clique em Add Library

🖥 Exemplo de Execução:

MENU PRINCIPAL

1 - Criar Conta

2 - Acessar Conta

3 - Atualizar Dados

4 - Encerrar Conta

5 - Sair

📚 Aprendizados

Durante o desenvolvimento foram praticados:

- PreparedStatement ->  Para proteger de SQL Injection
- Transactions -> Para garantir que conta e cliente sejam deletados 
- Estruturas de repetição -> Garantir que não ocorra um StackOverFlow
- Lógica de programação
- Organização de Código

👨‍💻 Autor

Henryque Vitoriano

GitHub:
https://github.com/HenryqueVitoriano

📄 Licença

Projeto para fins educacionais.

