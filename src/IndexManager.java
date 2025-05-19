import java.util.*;

public class IndexManager {

    private BPlusTree bPlusTree; // Instância da árvore B+ utilizada para o índice multinível

    public IndexManager(int order) {
        // Inicializa a árvore B+ com a ordem especificada
        this.bPlusTree = new BPlusTree(order);
    }

    public void insert(int key, long position) {
        try {
            // Insere uma chave e seu valor associado na árvore B+
            bPlusTree.insert(key, position);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erro ao inserir na árvore B+: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long search(int id) {
        Long position = bPlusTree.search(id);
        if (position == null) {
            System.out.println("Chave " + id + " não encontrada no índice.");
            return -1;
        }
        return position;
    }

    public void remove(int id) {
        try {
            System.out.println("Removendo índice para o ID: " + id);
            bPlusTree.delete(id); // Chama o método de remoção da Árvore B+
        } catch (Exception e) {
            System.out.println("Erro ao remover índice: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void printIndexTree() {
        try {
            bPlusTree.printTree();
        } catch (Exception e) {
            System.out.println("Erro ao imprimir a árvore B+: " + e.getMessage());
            e.printStackTrace();
        }
    }
}