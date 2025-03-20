# **Projeto AEDs III - Sistema CRUD**

## **📌 Descrição do Projeto**

Este projeto implementa um sistema CRUD (**Create, Read, Update, Delete**) aplicadas em um arquivo CSV, a que utilizei foi dados de transações bancarias, armazenando as informações em um **arquivo binário** e permite operações eficientes de criação, leitura, atualização e exclusão de registros.

## **📂 Estrutura do Projeto**

O projeto é composto por duas classes principais:

- **`Transaction`**: Representa uma transação financeira e seus atributos.
- **`Main`**: Gerencia o menu principal e as operações CRUD.

## **📜 Arquivos e Funcionalidades**

### **`src/Transaction.java`**

A classe `Transaction` encapsula os dados de uma transação financeira e fornece métodos para manipulação e serialização dos dados.

#### **📌 Atributos Principais**

- **Identificação e Dados Básicos**:
  - `transactionID`: Identificador único da transação.
  - `userID`: Identificador do usuário.
  - `transactionAmount`: Valor da transação.
  - `transactionType`: Tipo da transação (ex.: débito, crédito).
  - `timestamp`: Data e hora da transação.

- **Informações Adicionais**:
  - `accountBalance`: Saldo da conta após a transação.
  - `deviceType`: Tipo de dispositivo utilizado.
  - `location`: Localização da transação.
  - `merchantCategory`: Categoria do comerciante.

- **Indicadores de Risco**:
  - `ipAddressFlag`: Indica se o endereço IP é suspeito.
  - `previousFraudulentActivity`: Histórico de atividades fraudulentas.
  - `dailyTransactionCount`: Número de transações realizadas no dia.
  - `avgTransactionAmount7d`: Média do valor das transações nos últimos 7 dias.
  - `failedTransactionCount7d`: Número de transações falhadas nos últimos 7 dias.
  - `riskScore`: Pontuação de risco da transação.

- **Outros Dados**:
  - `cardType`: Tipo de cartão utilizado.
  - `cardAge`: Tempo de uso do cartão.
  - `transactionDistance`: Distância entre o local da transação e o endereço registrado.
  - `authenticationMethod`: Método de autenticação utilizado.
  - `isWeekend`: Indica se a transação ocorreu em um fim de semana.
  - `fraudLabel`: Indica se a transação foi classificada como fraudulenta.

#### **📌 Métodos**

- **Construtores**:
  - `Transaction(...)`: Inicializa todos os atributos da transação.
  - `Transaction()`: Inicializa os atributos com valores padrão.

- **Manipulação de Dados**:
  - `toString()`: Retorna uma representação textual dos dados da transação.
  - `toByteArray()`: Serializa o objeto `Transaction` em um array de bytes.
  - `fromByteArray(byte[])`: Reconstrói um objeto `Transaction` a partir de um array de bytes.

### **`src/Main.java`**

A classe `Main` gerencia o fluxo do programa e implementa as operações CRUD.

#### **📌 Funcionalidades**

- **Menu Principal**:
  - Exibe as opções disponíveis para o usuário.
  - Chama os métodos correspondentes às operações CRUD.

- **Operações CRUD**:
  - `carregarCSV(String)`: Lê dados de um arquivo CSV e os armazena no arquivo binário.
  - `create()`: Cria um novo registro de transação.
  - `read()`: Lê um registro de transação com base no ID.
  - `update()`: Atualiza um registro existente.
  - `delete()`: Marca um registro como excluído.

## **🛠️ Estratégia de Implementação**

1. **Leitura do CSV**:
     - O método `carregarCSV` processa o arquivo CSV, cria objetos `Transaction` e os armazena no arquivo binário.

2. **Criação de Transações**:
     - O método `create` solicita os dados ao usuário, cria um objeto `Transaction` e o serializa no arquivo binário.

3. **Leitura de Transações**:
     - O método `read` busca um registro pelo ID, desserializa os dados e os exibe ao usuário.

4. **Atualização de Transações**:
     - O método `update` permite modificar os dados de um registro existente e atualizá-lo no arquivo binário.

5. **Exclusão de Transações**:
     - O método `delete` marca um registro como excluído, sem removê-lo fisicamente do arquivo.

## **📈 Melhorias Futuras**

- 📌 **Implementar uma Interface Gráfica (GUI)** para tornar a interação mais amigável.
- 📌 **Adição de Validações Mais Robustas** para entrada de dados.
- 📌 **Integração com um Banco de Dados Relacional (ex.: PostgreSQL, MySQL)** para maior escalabilidade.
- 📌 **Incorporar Algoritmos de Detecção de Fraudes** para análise em tempo real.

## **📌 Conclusão**

Este projeto demonstra a implementação de um **CRUD robusto**, além de aprender o funcionamento de um banco de dados, para gerenciamento de uma base de dados utilizada, utilizando **arquivos binários** para armazenamento persistente. A modularidade do código facilita a **manutenção e expansão futura**, permitindo a adição de novas funcionalidades conforme necessário.

