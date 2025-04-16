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
### **📌 Manipulação Indexada da Base de Dados**

Nesta segunda parte do projeto, o objetivo é implementar uma abordagem de **indexação** para a base de dados criada na Parte I, utilizando estruturas eficientes para acesso rápido aos registros. Todas as operações CRUD (**Create, Read, Update, Delete**) serão otimizadas por meio de um índice, eliminando a necessidade de percorrer todo o arquivo.

### **📂 Estrutura de Indexação com Árvore B+**

#### **Por que usar Árvore B+?**
A **Árvore B+** foi escolhida como estrutura de indexação devido às suas vantagens em operações de disco e buscas sequenciais. Suas características incluem:
- Todas as chaves estão nas folhas, permitindo buscas rápidas e eficientes.
- As folhas são encadeadas, facilitando operações como **range queries**.
- É amplamente utilizada em sistemas de banco de dados para indexação.

#### **Como funciona a Árvore B+ no projeto?**
1. **Estrutura Geral**:
  - Cada nó pode ter até `ordem` filhos (parametrizável).
  - As folhas armazenam pares `(id, posiçãoNoArquivo)`.

2. **Operações CRUD com Índice**:
  - **Inserção**:
    - Escreve o registro no arquivo de dados.
    - Insere o par `(id, posição)` na Árvore B+.
    - Salva a Árvore B+ no arquivo de índices.
  - **Busca**:
    - Consulta o índice pelo `id` na Árvore B+.
    - Obtém a posição no arquivo e lê o registro correspondente.
  - **Atualização**:
    - Atualiza o registro no arquivo de dados.
    - Atualiza o índice, se necessário.
  - **Remoção**:
    - Marca o registro como excluído no arquivo de dados.
    - Remove o par `(id, posição)` da Árvore B+.

#### **📜 Classes Sugeridas**
Para implementar a indexação, as seguintes classes serão criadas:

- **`BPlusTree.java`**: Implementa a lógica da Árvore B+.
- **`BPlusNode.java`**: Representa os nós da Árvore B+.
- **`IndexEntry.java`**: Armazena pares `(id, posiçãoNoArquivo)`.
- **`IndexManager.java`**: Gerencia a persistência do índice em arquivo.

#### **📈 Benefícios**
- **Acesso Rápido**: As operações CRUD serão significativamente mais rápidas, mesmo com grandes volumes de dados.
- **Escalabilidade**: A estrutura é adequada para bases de dados maiores e mais complexas.
- **Manutenção Simplificada**: A modularidade do índice facilita a manutenção e futuras expansões.

### **📌 Tipos de Indexação Apresentados na Aula**

A aula aborda principalmente os seguintes tipos de indexação:

#### **Índice Simples**
- Um índice para todo o arquivo, geralmente ordenado pela chave de busca.
- Pode ser **denso** (uma entrada para cada registro) ou **esparso** (uma entrada para cada bloco).

#### **Índice Composto**
- Utiliza mais de um campo como chave de indexação.

#### **Índice Multinível**
- Um índice sobre outro índice, formando uma hierarquia (ex.: árvore B ou B+).

#### **Índice Invertido**
- Usado quando há busca por múltiplos campos não ordenados.

### **📌 Qual é o Melhor Tipo de Indexação?**

Não existe um único tipo de indexação que seja "o melhor" para todas as situações. A escolha depende do contexto de uso, volume de dados, tipo de consultas e operações realizadas.

| Tipo de Índice   | Vantagens                                   | Desvantagens                              | Quando Usar                                   |
|-------------------|--------------------------------------------|-------------------------------------------|----------------------------------------------|
| **Simples**       | Fácil de implementar, rápido para buscas sequenciais | Pode ser lento para grandes volumes, atualização trabalhosa | Arquivos pequenos ou buscas simples          |
| **Composto**      | Permite buscas por múltiplos campos        | Mais complexo, ocupa mais espaço          | Quando buscas envolvem mais de um campo      |
| **Multinível**    | Muito eficiente para grandes volumes, escalável | Mais complexo, manutenção de índices      | Grandes bases de dados, buscas rápidas       |
| **Invertido**     | Excelente para buscas por múltiplos campos não ordenados | Mais espaço, manutenção complexa          | Sistemas de busca textual, consultas flexíveis |

### **📌 Resumo da Aula**

A aula destaca que índices multiníveis (como árvores B ou B+) são os mais eficientes para grandes volumes de dados, pois permitem buscas, inserções e remoções rápidas, mesmo em arquivos muito grandes. Já os índices simples são mais indicados para arquivos pequenos ou quando a simplicidade é mais importante que a performance.

### **📌 Hashing Estendido — Estrutura e Justificativa**

#### **Justificativa**
- **Campo indexado**: `id` (Transaction_ID), pois é único e usado para busca direta.
- **Função hash**: Usaremos `id % 2^d` (onde `d` é a profundidade global).
- **Tamanho do bucket**: Por exemplo, 4 registros por bucket (parametrizável).
- **Motivo**: O Hashing Estendido permite crescimento dinâmico e acesso direto eficiente.

#### **Como funciona o Hashing Estendido?**
1. **Estrutura Geral**:
   - Utiliza uma tabela de diretórios que aponta para buckets.
   - Cada bucket armazena registros com o mesmo valor de hash.

2. **Operações CRUD**:
   - **Inserção**:
     - Calcula o hash do `id` e insere no bucket correspondente.
     - Se o bucket estiver cheio, ocorre um **split** (divisão do bucket).
   - **Busca**:
     - Calcula o hash do `id` e acessa diretamente o bucket correspondente.
   - **Atualização**:
     - Localiza o registro pelo hash e atualiza os dados.
   - **Remoção**:
     - Localiza o registro pelo hash e o remove do bucket.

#### **📈 Benefícios do Hashing Estendido**
- **Acesso Direto**: Operações de busca e inserção são extremamente rápidas.
- **Crescimento Dinâmico**: A estrutura se adapta ao aumento de dados sem necessidade de reorganização completa.
- **Eficiência**: Ideal para buscas diretas em grandes volumes de dados.

Combinando a Árvore B+ para indexação sequencial e o Hashing Estendido para acesso direto, o sistema se tornará robusto, eficiente e escalável, atendendo a diferentes tipos de consultas e operações.

