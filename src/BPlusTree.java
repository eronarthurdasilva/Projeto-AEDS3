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

    // Construtor da árvore B+
    public BPlusTree(int order) {
        this.order = order;
        this.root = new BPlusNode(true); // Cria a raiz como um nó folha
    }
    // Método para inserir um par chave-valor na árvore B+
    public void insert(int key, long value) {
        BPlusNode node = root; //
        //busca na folha 
        while(!node.isLeaf){
            int i = 0;
            while(i < node.keys.size() && key >= node.keys.get(i)) i++;
            //se o nó não for folha, desce para o nó filho
            node = node.children.get(i);
        }

        //insere ordenado 
        int i = 0;
        while (i < node.keys.size() && key > node.keys.get(i)) i++;
        node.keys.add(i, key);
        node.values.add(i, value); // Adiciona o valor associado à chave
    }

    // Método para buscar um valor associado a uma chave na árvore B+
    public Long search(int key) {
        BPlusNode node = root; // Começa na raiz
        // Percorre a árvore até encontrar a folha
        while (!node.isLeaf) {
            int i = 0;
            // Encontra o filho apropriado para descer
            while (i < node.keys.size() && key >= node.keys.get(i)) i++;
            node = node.children.get(i); // Desce para o filho
        }
        // Busca na folha
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                return node.values.get(i); // Retorna o valor associado à chave
            }
        }
        // Se a chave não for encontrada, retorna null
        return null;
    }

    // Método remover a chave 
    public void delete(int key) {
        BPlusNode node = root; // Começa na raiz
        // Percorre a árvore até encontrar a folha
        while (!node.isLeaf){
            int i = 0;
            // Encontra o filho apropriado para descer
            while (i < node.keys.size() && key >= node.keys.get(i)) i++;
            node = node.children.get(i); // Desce para o filho

        }
        // Busca na folha
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                node.keys.remove(i); // Remove a chave
                node.values.remove(i); // Remove o valor associado à chave
                break;
            }
        }

    }
    
}
