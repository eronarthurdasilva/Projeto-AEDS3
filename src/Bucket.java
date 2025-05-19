/**
 * The {@code Bucket} class serves as a placeholder for implementing bucket-related
 * functionalities. This class can be used to represent a container or storage
 * unit for organizing and managing data.
 * 
 * <p>Potential use cases for this class include:
 * <ul>
 *   <li>Storing and managing collections of objects.</li>
 *   <li>Implementing data structures such as hash tables or buckets for hashing algorithms.</li>
 *   <li>Organizing data for efficient retrieval and manipulation.</li>
 * </ul>
 * 
 * <p>Note: This class is currently empty and should be extended with fields,
 * methods, and logic as per the requirements of the project.
 * 
 * <p>Example usage:
 * <pre>{@code
 * Bucket bucket = new Bucket();
 * // Add functionality to the bucket as needed
 * }</pre>
 * 
 * @author Eron  Arthur 
 * @version 1.0
 * @since 2025-05-02
 */
import java.util.*;

public class Bucket {
    public List<Integer> keys;
    public List<Long> values;
    public int localDepth;

    public Bucket(int localDepth) {
        this.localDepth = localDepth;
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>(); 
    }

    public boolean isFull(int bucketSize){
        return keys.size() >= bucketSize;
    }
    
    public String toString() {
        return "Bucket{keys=" + keys + ", values=" + values + ", localDepth=" + localDepth + "}";
    }
}