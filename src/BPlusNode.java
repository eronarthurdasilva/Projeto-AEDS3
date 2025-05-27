

/**
 * Represents a node in a B+ tree data structure.
 * 
 * <p>The B+ tree is a self-balancing tree data structure that maintains sorted data 
 * and allows for efficient insertion, deletion, and search operations. This class 
 * serves as the building block for the B+ tree, encapsulating the properties and 
 * behaviors of a single node.</p>
 * 
 * <p>Each node in the B+ tree can either be an internal node or a leaf node. Internal 
 * nodes store keys and pointers to child nodes, while leaf nodes store keys and 
 * associated data values.</p>
 * 
 * <p>This class is designed to be extended or used in conjunction with other classes 
 * to implement the full functionality of a B+ tree.</p>
 * 
 * <p>Note: The implementation details of this class are currently undefined and 
 * should be completed to meet the specific requirements of the B+ tree.</p>
 * 
 * @author Eron Silva
 * @version 1.0
 * @mudanca 1.0
 * @data 16/04/2025
 */

import java.util.*;


public class BPlusNode {
    boolean isLeaf;
    List<Integer> keys = new ArrayList<>();
    List<Long> values; // Só para folhas
    List<BPlusNode> children = new ArrayList<>();
    BPlusNode parent;
    BPlusNode next; // Para folhas encadeadas

    public BPlusNode(boolean isLeaf) {
       this.isLeaf = isLeaf;
        if (isLeaf) {
            this.values = new ArrayList<>();
        } else {
            this.values = null; // Não usado em nós internos
        }
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    
}
