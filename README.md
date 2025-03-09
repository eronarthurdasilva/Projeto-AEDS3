
# Projeto AEDs III - Implementação do CRUD de Transações

## Descrição do Projeto

Este projeto implementa um sistema CRUD (Create, Read, Update, Delete) para gerenciar transações financeiras. O sistema lê dados de um arquivo CSV, armazena-os em um arquivo binário e permite operações de criação, leitura, atualização e exclusão de registros.

## Estrutura do Projeto

## Arquivos

### `src/Transaction.java`

A classe `Transaction` representa uma transação financeira e contém os seguintes atributos:

- `transactionID`: ID da transação
- `userID`: ID do usuário
- `transactionAmount`: Valor da transação
- `transactionType`: Tipo da transação
- `timestamp`: Data e hora da transação
- `accountBalance`: Saldo da conta
- `deviceType`: Tipo de dispositivo
- `location`: Localização da transação
- `merchantCategory`: Categoria do comerciante
- `ipAddressFlag`: Flag do endereço de IP
- `previousFraudulentActivity`: Atividade fraudulenta anterior
- `dailyTransactionCount`: Contagem de transações diárias
- `avgTransactionAmount7d`: Média do valor da transação em 7 dias
- `failedTransactionCount7d`: Contagem de transações falhadas em 7 dias
- `cardType`: Tipo de cartão
- `cardAge`: Idade do cartão
- `transactionDistance`: Distância da transação
- `authenticationMethod`: Método de autenticação
- `riskScore`: Pontuação de risco
- `isWeekend`: Indica se é fim de semana
- `fraudLabel`: Etiqueta de fraude

#### Construtores

- `Transaction(int, String, float, String, String, float, String, String, String, boolean, int, int, float, int, String, int, float, String, float, boolean, boolean)`: Inicializa todos os atributos da transação.
- `Transaction()`: Inicializa os atributos com valores padrão.

#### Métodos

- `toString()`: Retorna uma representação em string dos dados da transação.
- `toByteArray()`: Converte o objeto `Transaction` em um array de bytes.
- `fromByteArray(byte[])`: Reconstrói um objeto `Transaction` a partir de um array de bytes.

### `src/Main.java`

A classe `Main` contém o menu principal e os métodos para realizar operações CRUD no arquivo binário.

#### Métodos

- `main(String[])`: Exibe o menu e chama os métodos correspondentes às operações CRUD.
- `carregarCSV(String)`: Carrega dados de um arquivo CSV e os armazena no arquivo binário.
- `create()`: Cria um novo registro de transação no arquivo binário.
- `read()`: Lê um registro de transação do arquivo binário com base no ID.
- `update()`: Atualiza um registro de transação no arquivo binário com base no ID.
- `delete()`: Marca um registro de transação como deletado no arquivo binário com base no ID.

## Estratégia

1. **Leitura do CSV**: O método `carregarCSV` lê os dados do arquivo CSV e cria objetos `Transaction` para cada linha. Esses objetos são então serializados e armazenados no arquivo binário.
2. **Criação de Transações**: O método `create` solicita ao usuário os dados da transação, cria um objeto `Transaction`, serializa-o e o armazena no arquivo binário.
3. **Leitura de Transações**: O método `read` solicita ao usuário o ID da transação, percorre o arquivo binário para encontrar o registro correspondente, desserializa-o e exibe os dados.
4. **Atualização de Transações**: O método `update` solicita ao usuário o ID da transação e os novos dados, encontra o registro correspondente no arquivo binário, atualiza os dados, serializa o objeto atualizado e o armazena no arquivo binário.
5. **Exclusão de Transações**: O método `delete` solicita ao usuário o ID da transação, encontra o registro correspondente no arquivo binário e marca o registro como deletado.

## Conclusão

Este projeto demonstra a implementação de um sistema CRUD para gerenciar transações financeiras, utilizando leitura e escrita em arquivos binários. A estrutura modular do código facilita a manutenção e a expansão futura do sistema.