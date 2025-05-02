# **Projeto AEDs III - Sistema CRUD**

## Parte I
### **📌 Descrição do Projeto**

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

## Parte II
### 📌 Manipulação Indexada da Base de Dados

Nesta segunda parte do projeto, implementei uma abordagem de **indexação** para a base de dados criada na Parte I, utilizando duas estruturas eficientes para acesso rápido aos registros: **Árvore B+** e **Hashing Estendido**. Agora, todas as operações CRUD (Create, Read, Update, Delete) são otimizadas por meio de um índice, eliminando a necessidade de percorrer todo o arquivo sequencialmente.

---

### 📂 Estrutura de Indexação com Árvore B+

#### Por que escolhi a Árvore B+?
Optei pela **Árvore B+** como índice principal porque ela é amplamente utilizada em bancos de dados reais devido à sua eficiência em operações de disco e buscas sequenciais. Suas principais vantagens são:
- Todas as chaves ficam nas folhas, o que permite buscas rápidas e eficientes.
- As folhas são encadeadas, facilitando buscas por intervalos (range queries).
- A ordem da árvore é parametrizável, permitindo ajustar o desempenho conforme o volume de dados.

#### Como funciona a Árvore B+ no projeto?
- **Estrutura:** Cada nó pode ter até `ordem` filhos (parametrizável). As folhas armazenam pares `(id, posiçãoNoArquivo)`, onde `id` é o identificador único da transação e `posiçãoNoArquivo` é o offset do registro no arquivo binário.
- **Operações CRUD:**
  - **Inserção:** Ao criar um registro, ele é escrito no arquivo binário e o par `(id, posição)` é inserido na Árvore B+.
  - **Busca:** O índice é consultado pelo `id`, retornando a posição do registro para leitura direta.
  - **Atualização:** O registro é atualizado no arquivo e o índice é mantido sincronizado.
  - **Remoção:** O registro é marcado como excluído (lápide) e removido do índice.
- **Impressão:** A árvore pode ser impressa no console, mostrando os IDs presentes em cada nó por nível, facilitando a visualização da estrutura.

---

### 📂 Estrutura de Indexação com Hashing Estendido

#### Por que implementei o Hashing Estendido?
Implementei o **Hashing Estendido** como alternativa para acesso direto e eficiente, especialmente útil para buscas exatas. Ele permite que o sistema cresça dinamicamente conforme o volume de dados aumenta, sem a necessidade de reorganização completa.

#### Como funciona o Hashing Estendido no projeto?
- **Estrutura:**
  - **Diretório:** Um array de ponteiros para buckets, cujo tamanho é sempre uma potência de 2 (2^d, onde d é a profundidade global).
  - **Buckets:** Cada bucket armazena até X pares `(id, posiçãoNoArquivo)`. O valor de X (por exemplo, 4) é parametrizável.
- **Função Hash:** Utilizo `id % 2^d` para determinar o bucket de cada registro.
- **Operações CRUD:**
  - **Inserção:** O hash do `id` determina o bucket. Se o bucket estiver cheio, ocorre um split (divisão) e, se necessário, o diretório é expandido.
  - **Busca:** O hash do `id` leva diretamente ao bucket correto, onde a busca é feita.
  - **Atualização:** O registro é localizado pelo hash e atualizado.
  - **Remoção:** O registro é localizado pelo hash e removido do bucket.
- **Impressão:** O hash pode ser impresso no console, mostrando cada bucket, sua profundidade local e os pares `(id, posição)` armazenados. Exemplo:
Bucket 0 (profundidade 2): (1, 0) (5, 128) Bucket 1 (profundidade 2): (2, 32)

---

### 📌 Qual índice foi utilizado e por quê?

- **Campo indexado:** O campo escolhido para indexação foi o `id` (Transaction_ID), pois é único e garante eficiência nas operações de busca, inserção e remoção.
- **Árvore B+:** Escolhida como índice principal por sua eficiência em buscas sequenciais, escalabilidade e uso consolidado em bancos de dados.
- **Hashing Estendido:** Implementado para comparação e para oferecer acesso direto eficiente, especialmente útil para buscas exatas e para demonstrar domínio de diferentes técnicas de indexação.

---

### 📈 Como é feita a impressão das estruturas?

- **Árvore B+:**  
- Impressa por nível, mostrando os IDs presentes em cada nó.  
- Exemplo de saída:
  ```
  [10, 20]
  [5, 8] [12, 15, 18] [22, 25]
  ```
- **Hashing Estendido:**  
- Impressão mostra cada bucket, sua profundidade local e os pares armazenados.
- Exemplo de saída:
  ```
  Bucket 0 (profundidade 2): (1, 0) (5, 128)
  Bucket 1 (profundidade 2): (2, 32)
  ```

---

### 📌 Conclusão

Com a implementação da Árvore B+ e do Hashing Estendido, o sistema ficou robusto, eficiente e escalável, atendendo a diferentes tipos de consultas e operações. O usuário pode alternar entre os índices pelo menu, e todas as operações CRUD são otimizadas pelo índice selecionado, garantindo acesso rápido e seguro aos dados.