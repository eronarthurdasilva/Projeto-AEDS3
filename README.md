# **Projeto AEDs III - Sistema CRUD**
### Descrição principal
Este projeto foi desenvolvido com o objetivo de aplicar os conceitos teóricos aprendidos na disciplina de Algoritmos e Estruturas de Dados III. Ele foi dividido em quatro partes, cada uma abordando tópicos discutidos em sala de aula. O sistema foi implementado inteiramente em Java, uma linguagem na qual possuo grande domínio e experiência. Espero que este projeto seja útil e interessante para quem o explorar.

## Parte I
### **📌 Descrição do Projeto**
A aplicação da Parte I consiste em um sistema CRUD (**Create, Read, Update, Delete**) projetado para manipular registros armazenados em um arquivo binário, que serve como base de dados principal. Além disso, o sistema oferece suporte à importação de dados a partir de arquivos CSV, permitindo uma carga inicial eficiente e prática. Essa funcionalidade é especialmente útil para integrar dados existentes ou realizar migrações de outras fontes. O sistema foi desenvolvido com foco na flexibilidade e na robustez, garantindo que os registros possam ser criados, lidos, atualizados e excluídos de forma eficiente, enquanto mantém a integridade dos dados.

#### **Tipos de Dados Atendidos**
- **ID do registro**: Campo `transactionID` (int), gerenciado automaticamente e salvo no cabeçalho do arquivo.
- **Strings de tamanho variável**: Campos como `userID` e `transactionType`.
- **Data**: Campo `timestamp` armazenado como String no formato "yyyy-MM-dd HH:mm:ss".
- **Lista de tags**: Tags adicionadas pelo usuário ao criar ou atualizar registros.
- **Campos numéricos**: Valores como `transactionAmount` e `accountBalance`.

#### **Estrutura do Arquivo**
- **Cabeçalho**: Contém o último valor de ID utilizado.
- **Registros**:
  - **Lápide**: Indica se o registro está ativo ou excluído.
  - **Tamanho**: Define o tamanho do vetor de bytes do registro.
  - **Vetor de bytes**: Armazena os campos serializados, incluindo as tags.

#### **Funcionalidades**
- **Carga da base de dados**: Importação de arquivo CSV para popular a base.
- **CRUD**:
  - **Create**: Criação de registros com suporte a tags.
  - **Read**: Leitura de registros por ID.
  - **Update**: Atualização de registros.
  - **Delete**: Marcação de registros como excluídos (lápide).

#### **Exemplo de uso da lista de tags**
O usuário pode adicionar múltiplas tags ao criar ou atualizar um registro. Essas tags são armazenadas junto ao registro e exibidas na leitura.

---

## **📂 Estrutura do Projeto**

O projeto é composto pelas classes `Transaction` (representa uma transação financeira) e `Main` (gerencia o menu principal e as operações CRUD).

### **📜 Arquivos e Funcionalidades**

#### **`src/Transaction.java`**
Classe que encapsula os dados de uma transação financeira, com métodos para serialização e desserialização.

#### **`src/Main.java`**
Gerencia o fluxo do programa, implementando o menu interativo e as operações CRUD.

---

## Parte II
### **📌 Manipulação Indexada da Base de Dados**
Nesta etapa, implementei indexação na base de dados utilizando **Árvore B+** e **Hashing Estendido**, com o objetivo de otimizar as operações CRUD e melhorar a eficiência nas buscas. A escolha desses métodos foi baseada em suas características específicas:

### **Árvore B+**
A Árvore B+ foi utilizada como índice principal devido à sua eficiência em buscas sequenciais e operações de intervalo. As folhas da árvore armazenam pares `(id, posiçãoNoArquivo)`, permitindo acesso rápido aos registros. A ordem da árvore é parametrizável, garantindo flexibilidade para diferentes volumes de dados.

### **Hashing Estendido**
O Hashing Estendido foi implementado para buscas exatas, utilizando um diretório dinâmico que aponta para buckets. Esses buckets crescem conforme necessário, garantindo que o sistema se adapte ao aumento do número de registros sem comprometer a performance.

### **Campo Indexado**
O campo `transactionID` foi escolhido para indexação por ser único e essencial para identificar os registros. Essa decisão garante eficiência nas operações de busca, inserção e remoção.

Essas técnicas de indexação foram integradas ao sistema para proporcionar maior desempenho e escalabilidade, atendendo às necessidades de manipulação de grandes volumes de dados.

---

## Parte III
### **📌 Compressão de Dados na Base de Dados**
Nesta etapa do projeto, implementei funcionalidades completas de compressão e descompressão da base de dados utilizando os algoritmos **Huffman** e **LZW**, conforme solicitado no trabalho. O objetivo foi otimizar o armazenamento e a transferência dos dados, além de permitir comparar o desempenho dos algoritmos em diferentes cenários.

#### **Funcionalidades Implementadas**
- **Menu Interativo**: O sistema apresenta opções no menu principal para que eu possa escolher entre compressão e descompressão da base de dados.
- **Compressão**: Ao selecionar a compressão, a base de dados é processada integralmente (incluindo cabeçalho, registros, tamanhos de strings, etc.) por ambos os algoritmos. São gerados dois arquivos, seguindo o padrão:  
  `nomeArquivoNomeAlgoritmoCompressaoX`  
  onde `nomeArquivo` é o nome original, `NomeAlgoritmo` é "Huffman" ou "LZW", e `X` representa a versão da compressão.
- **Exibição de Resultados**: Após a compressão, o sistema exibe:
  - Porcentagem de ganho ou perda de espaço para cada algoritmo.
  - Tempo de execução de cada compressão.
  - Comparação direta, indicando qual algoritmo foi mais eficiente para aquele caso.
- **Descompressão**: Posso escolher descompactar qualquer versão gerada, informando o número da versão desejada. O arquivo descomprimido substitui a base de dados original.
- **Comparação na Descompressão**: O sistema também mostra o tempo de execução de cada algoritmo na descompressão e indica qual foi mais eficiente.
- **Dicionário LZW**: Defini o dicionário inicial do LZW conforme as características dos dados do projeto, visando melhor desempenho para os padrões presentes nos registros.

#### **Decisões de Implementação**
- A compressão é aplicada a todo o conteúdo do arquivo binário, garantindo integridade e eficiência.
- Os algoritmos foram integrados de modo transparente ao fluxo do sistema, permitindo fácil uso.
- O sistema foi projetado para facilitar a comparação entre Huffman e LZW, tanto em compressão quanto em descompressão, promovendo análise detalhada de eficiência.
- O controle de versões permite manter múltiplos arquivos comprimidos e restaurar qualquer versão desejada.

#### **Testes e Resultados**
- Realizei testes com diferentes volumes de dados e padrões de registros, avaliando a taxa de compressão, tempo de execução e fidelidade dos dados após descompressão.
- Os resultados mostraram que o desempenho dos algoritmos varia conforme o perfil dos dados, sendo Huffman mais eficiente em dados com alta redundância e LZW em padrões repetitivos.
- Todos os testes confirmaram a integridade dos dados após compressão e descompressão, validando a robustez da implementação.

Essas funcionalidades atendem integralmente aos requisitos do trabalho, proporcionando controle total sobre a compressão e descompressão da base de dados, além de uma análise comparativa clara entre os algoritmos implementados.

---

## Parte IV
### **📌 Casamento de Padrões**

Implementação do algoritmo **KMP (Knuth-Morris-Pratt)** para busca eficiente de padrões em campos específicos das transações, como `userID` ou `transactionType`.

#### **Decisões de Implementação**
- Armazenamento em arquivo binário para eficiência e flexibilidade.
- Indexação com Árvore B+ e Hashing Estendido para otimização.
- Compressão com Huffman e LZW para redução de espaço.
- Criptografia em camadas (César e AES) para segurança.
- Busca eficiente com KMP em grandes volumes de dados.

---

## **📋 Testes e Resultados**

- Validação de métodos CRUD com diferentes tipos de dados e listas de tags.
- Testes de busca, inserção e remoção em Árvore B+ e Hashing Estendido.
- Comparação de eficiência entre algoritmos de compressão.
- Testes de criptografia e descriptografia em camadas.
- Busca de padrões com KMP, confirmando eficiência.

---
## **📌 Criptografia**

Implementei criptografia em camadas para proteger o arquivo de dados. O usuário pode criptografar a base de dados primeiro com a cifra de César e, em seguida, aplicar o AES sobre o resultado. O arquivo final contém ambas as camadas de proteção.  
A cifra de César foi escolhida por ser simples e ilustrar conceitos básicos de criptografia, enquanto o AES foi utilizado por ser um padrão moderno e seguro, amplamente adotado em aplicações reais.  
O processo inverso (descriptografia) remove as camadas na ordem correta, garantindo a restauração dos dados originais.  
Essa abordagem atende ao requisito de aplicar pelo menos dois algoritmos distintos e demonstra o entendimento tanto de técnicas clássicas quanto modernas de segurança da informação.

---

## **📋 Testes e Resultados**

- Validação de métodos CRUD com diferentes tipos de dados e listas de tags.
- Testes de busca, inserção e remoção em Árvore B+ e Hashing Estendido.
- Comparação de eficiência entre algoritmos de compressão.
- Testes de criptografia e descriptografia em camadas, confirmando a integridade dos dados após o processo.
- Busca de padrões com KMP, comprovando eficiência e precisão mesmo em bases grandes.

---

## **Autor**

Desenvolvido por:  
**Eron Arthur da Silva**

Este projeto foi desenvolvido como parte da disciplina de Algoritmos e Estruturas de Dados III, sob orientação do professor. Todos os testes, documentação e código são de autoria própria, salvo indicações em comentários ou referências bibliográficas.
