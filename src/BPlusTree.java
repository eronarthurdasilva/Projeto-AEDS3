/**
 * A classe BPlusTree representa uma estrutura de dados Árvore B+.
 * 
 * <p>
 * Uma Árvore B+ é uma estrutura de dados auto-balanceada que mantém os dados 
 * ordenados e permite operações eficientes de inserção, remoção e busca. É 
 * amplamente utilizada em sistemas de banco de dados e sistemas de arquivos para 
 * armazenar e recuperar dados de forma eficiente.
 * </p>
 * 
 * <p>
 * Esta classe implementa a estrutura básica de uma Árvore B+, incluindo métodos 
 * para inserção, busca e remoção de pares chave-valor, além de manter o 
 * balanceamento da árvore.
 * </p>
 * 
 * @author Eron Silva
 * @version 1.0
 * @data 16/04/2025
 */
import java.util.*;


public class BPlusTree {

    private int order; // Grau da árvore
    private BPlusNode root; // Raiz da árvore
    private int maxKeys; // Número máximo de chaves permitido em um nó

    // Construtor da árvore B+
    public BPlusTree(int order) {
        this.order = order;
        this.maxKeys = order - 1; // Define o número máximo de chaves permitido em um nó
    }
    
    // Método para inserir um par chave-valor na árvore B+
    public void insert(int key, long position) {
        if (root == null) {
            root = new BPlusNode(true); // Cria o nó raiz se ele não existir
            root.keys.add(key);
            root.values.add(position);
        } else {
            BPlusNode currentNode = root;
            while (!currentNode.isLeaf) {
                int i = 0;
                while (i < currentNode.keys.size() && key > currentNode.keys.get(i)) {
                    i++;
                }
                currentNode = currentNode.children.get(i);
            }
    
            // Insere a chave e o valor no nó folha
            int insertPosition = 0;
            while (insertPosition < currentNode.keys.size() && key > currentNode.keys.get(insertPosition)) {
                insertPosition++;
            }
            currentNode.keys.add(insertPosition, key);
            currentNode.values.add(insertPosition, position);
    
            // Verifica se o nó precisa ser dividido
            if (currentNode.keys.size() > maxKeys) {
                split(currentNode);
            }
        }
    }

    // Método para buscar um valor associado a uma chave na árvore B+
    public Long search(int key) {
        if (root == null) {
            System.out.println("A árvore B+ está vazia.");
            return null;
        }
    
        BPlusNode node = root;
        while (!node.isLeaf) {
            int i = 0;
            while (i < node.keys.size() && key > node.keys.get(i)) {
                i++;
            }
            node = node.children.get(i);
        }
    
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                return node.values.get(i);
            }
        }
    
        return null;
    }

    // Método remover a chave 
    public void delete(int key) {
        BPlusNode node = root;

        // Percorre a árvore até encontrar a folha
        while (!node.isLeaf) {
            int i = 0;
            while (i < node.keys.size() && key >= node.keys.get(i))
                i++;
            node = node.children.get(i);
        }

        // Remove a chave e o valor na folha
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                node.keys.remove(i);
                node.values.remove(i);
                break;
            }
        }

        // Verifica se ocorreu underflow
        if (node.keys.size() < (order - 1) / 2) {
            reequilibrar(node);
        }
    }

    /**
     * Exibe a estrutura da árvore B+ (IDs por nível e ponteiros next).
     */
    public void printTree() {
        if (root == null) {
            System.out.println("A árvore está vazia.");
            return;
        }

        Queue<BPlusNode> queue = new LinkedList<>();
        queue.add(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            System.out.print("Nível " + level + ": ");
            for (int i = 0; i < size; i++) {
                BPlusNode node = queue.poll();
                System.out.print("{ ");
                for (int j = 0; j < node.keys.size(); j++) {
                    System.out.print(node.keys.get(j));
                    if (j < node.keys.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print(" } ");

                // Adiciona os filhos à fila para o próximo nível
                if (!node.isLeaf && node.children != null) {
                    queue.addAll(node.children);
                }
            }
            System.out.println(); // Nova linha para cada nível
            level++;
        }

        // Exibe os nós folhas e seus ponteiros next
        System.out.println("Nós folhas e ponteiros next:");
        BPlusNode currentNode = root;
        while (!currentNode.isLeaf) {
            currentNode = currentNode.children.get(0); // Vai para o primeiro nó folha
        }

        while (currentNode != null) {
            System.out.print("{ ");
            for (int i = 0; i < currentNode.keys.size(); i++) {
                System.out.print(currentNode.keys.get(i));
                if (i < currentNode.keys.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print(" } -> ");
            currentNode = currentNode.next; // Move para o próximo nó folha
        }
        System.out.println("null"); // Indica o final da lista encadeada
    }
   
    
    private void split(BPlusNode node) {
        int midIndex = node.keys.size() / 2;
        int midKey = node.keys.get(midIndex);
    
        BPlusNode newNode = new BPlusNode(node.isLeaf);
        newNode.keys.addAll(node.keys.subList(midIndex + 1, node.keys.size()));
        newNode.values.addAll(node.values.subList(midIndex + 1, node.values.size()));
    
        if (!node.isLeaf) {
            newNode.children.addAll(node.children.subList(midIndex + 1, node.children.size()));
            node.children.subList(midIndex + 1, node.children.size()).clear();
        }
    
        node.keys.subList(midIndex, node.keys.size()).clear();
        node.values.subList(midIndex, node.values.size()).clear();
    
        if (node == root) {
            BPlusNode newRoot = new BPlusNode(false);
            newRoot.keys.add(midKey);
            newRoot.children.add(node);
            newRoot.children.add(newNode);
            root = newRoot;
        } else {
            BPlusNode parent = node.parent;
            int insertPosition = 0;
            while (insertPosition < parent.keys.size() && midKey > parent.keys.get(insertPosition)) {
                insertPosition++;
            }
            parent.keys.add(insertPosition, midKey);
            parent.children.add(insertPosition + 1, newNode);
            newNode.parent = parent;
    
            if (parent.keys.size() > maxKeys) {
                split(parent);
            }
        }
    }
    
    private void reequilibrar(BPlusNode node) {
        BPlusNode parent = node.parent;
        if (parent == null) {
            // Se o nó for a raiz e estiver vazio, redefine a raiz
            if (node.keys.isEmpty()) {
                root = node.isLeaf ? null : node.children.get(0);
                if (root != null)
                    root.parent = null;
            }
            return;
        }

        int index = parent.children.indexOf(node);
        BPlusNode leftSibling = index > 0 ? parent.children.get(index - 1) : null;
        BPlusNode rightSibling = index < parent.children.size() - 1 ? parent.children.get(index + 1) : null;

        // Tenta redistribuir com o irmão esquerdo
        if (leftSibling != null && leftSibling.keys.size() > (order - 1) / 2) {
            node.keys.add(0, parent.keys.get(index - 1));
            parent.keys.set(index - 1, leftSibling.keys.remove(leftSibling.keys.size() - 1));
            if (!node.isLeaf) {
                node.children.add(0, leftSibling.children.remove(leftSibling.children.size() - 1));
            }
            return;
        }

        // Tenta redistribuir com o irmão direito
        if (rightSibling != null && rightSibling.keys.size() > (order - 1) / 2) {
            node.keys.add(parent.keys.get(index));
            parent.keys.set(index, rightSibling.keys.remove(0));
            if (!node.isLeaf) {
                node.children.add(rightSibling.children.remove(0));
            }
            return;
        }

        // Mescla com o irmão esquerdo ou direito
        if (leftSibling != null) {
            leftSibling.keys.add(parent.keys.remove(index - 1));
            leftSibling.keys.addAll(node.keys);
            if (!node.isLeaf) {
                leftSibling.children.addAll(node.children);
            }
            parent.children.remove(index);
        } else if (rightSibling != null) {
            node.keys.add(parent.keys.remove(index));
            node.keys.addAll(rightSibling.keys);
            if (!node.isLeaf) {
                node.children.addAll(rightSibling.children);
            }
            parent.children.remove(rightSibling);
        }

        // Se o pai ficar vazio, reequilibra o pai
        if (parent.keys.size() < (order - 1) / 2) {
            reequilibrar(parent);
        }
    }

    private int findInsertIndex(List<Integer> keys, int key) {
        int i = 0;
        while (i < keys.size() && key > keys.get(i)) i++;
        return i;
    }
}
