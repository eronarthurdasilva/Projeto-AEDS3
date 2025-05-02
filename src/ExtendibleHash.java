import java.util.*;

/**
 * Implementação de um Hash Estendido (Extendible Hashing).
 */
public class ExtendibleHash {
    private int globalDepth; // Profundidade global do diretório
    private int bucketSize; // Tamanho máximo de cada bucket
    private List<Bucket> directory; // Diretório que aponta para os buckets

    /**
     * Construtor para inicializar o hash estendido.
     *
     * @param initialDepth Profundidade inicial do diretório.
     * @param bucketSize   Tamanho máximo de cada bucket.
     */
    public ExtendibleHash(int initialDepth, int bucketSize) {
        this.globalDepth = initialDepth;
        this.bucketSize = bucketSize;
        this.directory = new ArrayList<>(1 << initialDepth); // Inicializa o diretório com 2^initialDepth entradas
        for (int i = 0; i < (1 << initialDepth); i++) {
            directory.add(new Bucket(initialDepth)); // Cria buckets iniciais
        }
    }

    /**
     * Função de hash para calcular o índice no diretório.
     *
     * @param key Chave a ser inserida.
     * @return Índice no diretório.
     */
    private int hash(int key) {
        return key & ((1 << globalDepth) - 1);
    }

    /**
     * Insere uma chave e um valor no hash.
     *
     * @param key   Chave a ser inserida.
     * @param value Valor associado à chave.
     */
    public void insert(int key, long value) {
        int index = hash(key);
        Bucket bucket = directory.get(index);

        if (!bucket.isFull(bucketSize)) {
            // Adiciona a chave e o valor ao bucket se não estiver cheio
            bucket.keys.add(key);
            bucket.values.add(value);
        } else {
            // Trata o caso de overflow do bucket
            handleBucketOverflow(bucket, key, value);
        }
    }

    /**
     * Trata o caso de overflow de um bucket.
     *
     * @param bucket Bucket que está cheio.
     * @param key    Chave a ser inserida.
     * @param value  Valor associado à chave.
     */
    private void handleBucketOverflow(Bucket bucket, int key, long value) {
        if (bucket.localDepth == globalDepth) {
            // Duplica o tamanho do diretório se a profundidade local do bucket for igual à global
            doubleDirectorySize();
        }
        // Divide o bucket e redistribui as chaves
        Bucket newBucket = splitBucket(bucket);
        redistributeKeys(bucket, newBucket);
        updateDirectory(bucket, newBucket);
        // Insere novamente a chave e o valor
        insert(key, value);
    }

    /**
     * Duplica o tamanho do diretório.
     */
    private void doubleDirectorySize() {
        int size = directory.size();
        for (int i = 0; i < size; i++) {
            directory.add(directory.get(i)); // Duplica as referências no diretório
        }
        globalDepth++; // Incrementa a profundidade global
    }

    /**
     * Divide um bucket em dois, aumentando sua profundidade local.
     *
     * @param bucket Bucket a ser dividido.
     * @return Novo bucket criado.
     */
    private Bucket splitBucket(Bucket bucket) {
        Bucket newBucket = new Bucket(bucket.localDepth + 1);
        bucket.localDepth++; // Incrementa a profundidade local do bucket original
        return newBucket;
    }

    /**
     * Redistribui as chaves entre o bucket original e o novo bucket.
     *
     * @param bucket    Bucket original.
     * @param newBucket Novo bucket criado.
     */
    private void redistributeKeys(Bucket bucket, Bucket newBucket) {
        List<Integer> oldKeys = new ArrayList<>(bucket.keys);
        List<Long> oldValues = new ArrayList<>(bucket.values);
        bucket.keys.clear();
        bucket.values.clear();

        for (int i = 0; i < oldKeys.size(); i++) {
            int key = oldKeys.get(i);
            long value = oldValues.get(i);
            int newIndex = hash(key);
            if (directory.get(newIndex).localDepth == bucket.localDepth) {
                directory.get(newIndex).keys.add(key);
                directory.get(newIndex).values.add(value);
            } else {
                newBucket.keys.add(key);
                newBucket.values.add(value);
            }
        }
    }

    /**
     * Atualiza o diretório para apontar para o novo bucket.
     *
     * @param bucket    Bucket original.
     * @param newBucket Novo bucket criado.
     */
    private void updateDirectory(Bucket bucket, Bucket newBucket) {
        for (int i = 0; i < directory.size(); i++) {
            if (directory.get(i).localDepth == bucket.localDepth) {
                directory.set(i, newBucket);
            }
        }
    }

    /**
     * Busca um valor associado a uma chave.
     *
     * @param key Chave a ser buscada.
     * @return Valor associado à chave ou -1 se a chave não for encontrada.
     */
    public long search(int key) {
        int index = hash(key);
        Bucket bucket = directory.get(index);
        int keyIndex = bucket.keys.indexOf(key);
        if (keyIndex != -1) {
            return bucket.values.get(keyIndex);
        } else {
            return -1; // Chave não encontrada
        }
    }

    /**
     * Remove uma chave e seu valor associado.
     *
     * @param key Chave a ser removida.
     */
    public void delete(int key) {
        int index = hash(key);
        Bucket bucket = directory.get(index);
        int keyIndex = bucket.keys.indexOf(key);
        if (keyIndex != -1) {
            bucket.keys.remove(keyIndex);
            bucket.values.remove(keyIndex);
        }
    }

    /**
     * Imprime a estrutura do hash estendido.
     */
    public void printHash() {
        System.out.println("Hashing Estendido:");
        for (int i = 0; i < directory.size(); i++) {
            Bucket b = directory.get(i);
            System.out.print("Bucket " + i + " (profundidade " + b.localDepth + "): ");
            for (int j = 0; j < b.keys.size(); j++) {
                System.out.print("(" + b.keys.get(j) + ", " + b.values.get(j) + ") ");
            }
            System.out.println();
        }
    }

    /**
     * Classe interna que representa um bucket.
     */
    private static class Bucket {
        private int localDepth; // Profundidade local do bucket
        private List<Integer> keys; // Lista de chaves no bucket
        private List<Long> values; // Lista de valores no bucket

        public Bucket(int localDepth) {
            this.localDepth = localDepth;
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
        }

        /**
         * Verifica se o bucket está cheio.
         *
         * @param bucketSize Tamanho máximo do bucket.
         * @return true se o bucket estiver cheio, false caso contrário.
         */
        public boolean isFull(int bucketSize) {
            return keys.size() >= bucketSize;
        }
    }
}
