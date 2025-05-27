
import java.util.List;
import java.util.ArrayList;

public class BPlusNode {
    boolean isLeaf;
    List<Integer> keys;
    List<Long> values; // Só usado em folhas, mas sempre inicializado para evitar null
    List<BPlusNode> children; // Só usado em internos, mas sempre inicializado para evitar null
    BPlusNode next;
    BPlusNode parent; // Referência para o nó pai

    public BPlusNode(boolean isLeaf) {
        this.isLeaf = isLeaf;
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();      // Sempre inicializa, mesmo se não usar
        this.children = new ArrayList<>();    // Sempre inicializa, mesmo se não usar
        this.next = null; // Usado para encadear folhas
        this.parent = null;
    }

    public boolean isLeaf() {
        return isLeaf;
    }
}