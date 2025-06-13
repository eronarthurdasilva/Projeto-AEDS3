# **Projeto AEDs III - Sistema CRUD**

## Parte I
### **📌 Descrição do Projeto**

Este projeto consiste em um sistema CRUD (**Create, Read, Update, Delete**) para manipulação de dados de transações bancárias, utilizando um arquivo binário como base de dados. O sistema também permite importar dados de um arquivo CSV, facilitando a carga inicial da base.

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

## **📈 Melhorias Futuras**

- Implementação de uma interface gráfica para facilitar a interação.
- Validações mais robustas para entradas.
- Integração com banco de dados relacional para maior escalabilidade.
- Adição de algoritmos de detecção de fraudes.

---

## Parte II
### **📌 Manipulação Indexada da Base de Dados**

Nesta etapa, foi implementada indexação na base de dados utilizando **Árvore B+** e **Hashing Estendido**, otimizando as operações CRUD.

#### **Estrutura de Indexação com Árvore B+**
A Árvore B+ foi escolhida como índice principal pela eficiência em buscas sequenciais. As folhas armazenam pares `(id, posiçãoNoArquivo)` e a ordem da árvore é parametrizável.

#### **Estrutura de Indexação com Hashing Estendido**
O Hashing Estendido foi implementado para buscas exatas, utilizando um diretório com ponteiros para buckets que crescem dinamicamente.

#### **Campo Indexado**
O campo `transactionID` foi indexado por ser único e garantir eficiência nas operações.

---

## Parte III
### **📌 Compressão de Dados na Base de Dados**

Adicionou-se suporte à compressão e descompressão da base de dados utilizando os algoritmos **Huffman** e **LZW**, permitindo ao usuário reduzir o espaço ocupado pelo arquivo e comparar o desempenho dos algoritmos.

#### **Funcionalidades**
- **Compressão**: Geração de arquivos comprimidos e exibição de taxa e tempo de compressão.
- **Descompressão**: Restauração do arquivo original com exibição do tempo de descompressão.

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
