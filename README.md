# **Projeto AEDs III - Sistema CRUD**

## Parte I
### **📌 Descrição do Projeto**

Este projeto implementa um sistema CRUD (**Create, Read, Update, Delete**) aplicadas em um arquivo CSV, utilizando dados de transações bancárias, armazenando as informações em um **arquivo binário** e permitindo operações eficientes de criação, leitura, atualização e exclusão de registros.

#### **Tipos de Dados Atendidos**
- **ID do registro**: campo `transactionID` (int), gerenciado automaticamente e salvo no cabeçalho do arquivo.
- **String de tamanho variável**: vários campos, como `userID`, `transactionType`, etc.
- **Data**: campo `timestamp` (String, formato "yyyy-MM-dd HH:mm:ss").
- **Lista de valores String com indicador de quantidade**: campo `tags` (array de String) e `numTags` (int), preenchidos pelo usuário.
- **Tipo numérico**: diversos campos float/int, como `transactionAmount`, `accountBalance`, etc.

#### **Estrutura do Arquivo**
- **Cabeçalho**: int que armazena o último valor de ID utilizado.
- **Registros**:
  - **Lápide**: byte que indica se o registro é válido ou excluído.
  - **Indicador de tamanho**: int que indica o tamanho do vetor de bytes.
  - **Vetor de bytes**: bytes que descrevem o objeto, incluindo todos os campos e a lista de tags.

#### **Funcionalidades**
- **Carga da base de dados**: importação de arquivo CSV.
- **CRUD**:
  - **Create**: criação de registro, incluindo preenchimento de lista de tags.
  - **Read**: leitura de registro por ID.
  - **Read conjunto**: leitura de múltiplos registros por IDs informados.
  - **Update**: atualização de registro.
  - **Delete**: marcação de registro como excluído (lápide).

#### **Exemplo de uso da lista de tags**
Ao criar ou atualizar um registro, o usuário informa quantas tags deseja adicionar e digita cada uma delas.  
Essas tags são armazenadas junto ao registro e exibidas na leitura.

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

## **📈 Melhorias Futuras**

- 📌 **Implementar uma Interface Gráfica (GUI)** para tornar a interação mais amigável.
- 📌 **Adição de Validações Mais Robustas** para entrada de dados.
- 📌 **Integração com um Banco de Dados Relacional (ex.: PostgreSQL, MySQL)** para maior escalabilidade.
- 📌 **Incorporar Algoritmos de Detecção de Fraudes** para análise em tempo real.

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

## Parte III
### 📌 Compressão de Dados na Base de Dados

Nesta terceira parte, o sistema foi expandido para suportar **compressão e descompressão** da base de dados utilizando dois algoritmos clássicos: **Huffman** e **LZW**. O objetivo é permitir ao usuário reduzir o espaço ocupado pelo arquivo de dados e comparar o desempenho dos algoritmos.

---

### 📂 Mudanças e Atualizações no Projeto

- **Novas opções no menu principal:**
  - `11 - Comprimir base de dados`
  - `12 - Descomprimir base de dados`
- **Implementação dos algoritmos de compressão:**
  - **Huffman:** Algoritmo de compressão baseado em árvore binária e frequência dos bytes.
  - **LZW:** Algoritmo de compressão baseado em dicionário dinâmico.
- **Geração de arquivos comprimidos:**
  - Após a compressão, são criados arquivos com o nome `transactions.dbHuffmanX` ou `transactions.dbLZWX`, onde `X` é a versão da compressão.
- **Comparação automática dos algoritmos:**
  - O sistema mostra ao usuário a porcentagem de compressão e o tempo de execução de cada algoritmo, indicando qual foi mais eficiente para o arquivo atual.
- **Descompressão:**
  - O usuário pode escolher qual versão e algoritmo deseja descomprimir. O arquivo original é substituído pelo descomprimido.
- **Compressão e descompressão abrangem todo o arquivo:**  
  Incluindo cabeçalho, tamanhos de strings e todos os campos binários.

---

### 🛠️ Como funciona a compressão e descompressão

- **Compressão:**
  1. O usuário escolhe a opção de compressão no menu.
  2. O sistema lê todo o arquivo de dados (`transactions.db`).
  3. Aplica os algoritmos de Huffman e LZW separadamente.
  4. Salva os arquivos comprimidos e exibe o tempo e a taxa de compressão de cada um.
  5. Informa qual algoritmo foi mais eficiente.

- **Descompressão:**
  1. O usuário escolhe a opção de descompressão no menu.
  2. Informa o algoritmo e a versão desejada.
  3. O sistema descomprime o arquivo escolhido e substitui o arquivo de dados original.
  4. Exibe o tempo de descompressão e qual algoritmo foi mais eficiente.

---

### 📋 Exemplo de uso no menu

```
11 - Comprimir base de dados
12 - Descomprimir base de dados
```

Ao escolher **11**, a saída será semelhante a:
```
Huffman: 45.32% de compressão, tempo: 120 ms
LZW: 38.10% de compressão, tempo: 80 ms
LZW foi mais eficiente nesta compressão.
```

Ao escolher **12**, o sistema solicitará o algoritmo e a versão, e mostrará o tempo de descompressão.

---

### 📦 Arquivos adicionados

- `src/Huffman.java` — Implementação do algoritmo de compressão e descompressão Huffman.
- `src/LZW.java` — Implementação do algoritmo de compressão e descompressão LZW.
- Atualizações em `src/Main.java` para incluir as opções de compressão/descompressão e integração com os algoritmos.

---

### 📈 Como testar

Utilize dentro de Docs um arquivo chamado Test.txt, onde tera todas as informação e e até mesmos exemplos de como testar o código.

---

### 🚩 Observações

- Sempre que o arquivo de dados for comprimido ou descomprimido, os índices devem ser reconstruídos para garantir a consistência.
- O dicionário inicial do LZW pode ser ajustado conforme a necessidade do projeto.
- O sistema está preparado para ser expandido com novos algoritmos de compressão no futuro.

---
## Parte 4 - FINAL


### Autor 
Desenvolvido por:
**Eron Arthur da silva**

### Consideração final 

Este projeto foi desenvolvido como parte da disciplina de Algoritmos e Estruturas de Dados III, sob orientação do Prof, testes e documentação são de minha autoria, salvo indicações em comentários ou referências bibliográficas, e mais alguns prompts usados para entendimento e aplicação no debug, correção de erros e atualização do código.
