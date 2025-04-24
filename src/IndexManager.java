/**
 * A classe IndexManager é responsável por gerenciar os índices de um banco de dados.
 * Foi escolhido o índice multinível, pois ele é mais eficiente para buscas em grandes 
 * conjuntos de dados, como o que está sendo utilizado neste projeto.
 * 
 * <p>O índice multinível permite organizar os dados de forma hierárquica, 
 * reduzindo o número de acessos necessários para localizar informações específicas.
 * Isso é particularmente útil em bancos de dados de grande escala.</p>
 * 
 * <p>Esta classe será utilizada para criar, atualizar e consultar os índices 
 * associados ao banco de dados, garantindo eficiência e desempenho nas operações.</p>
 * 
 * @author Eron Silva
 * @version 1.0
 * @mudanca 1.0
 * @data 16/04/2025 
 */
import java.util.*;

public class IndexManager {
     
    private BPlusTree bPlusTree; // Instância da árvore B+ utilizada para o índice multinível
    
    public IndexManager(int order) {
        // Inicializa a árvore B+ com a ordem especificada
        this.bPlusTree = new BPlusTree(order);
    }

    public void insert(int id, long position) {
        // Insere uma chave e seu valor associado na árvore B+
        bPlusTree.insert(id, position);
    }

    public  long search(int id) {
        // Busca uma chave na árvore B+ e retorna seu valor associado
        return bPlusTree.search(id);
    }

    public void remove(int id) {
        // Implement the logic to remove the index associated with the given ID
        System.out.println("Removing index for ID: " + id);
        // Add actual removal logic here
    }

    public void printIndexTree() {
        bPlusTree.printTree();
    }

}
