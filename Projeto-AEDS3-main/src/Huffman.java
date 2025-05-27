import java.util.*;
import java.io.*;

/**
 * Implementação do algoritmo de compressão e descompressão de dados usando Huffman.
 * 
 * Esta classe fornece métodos estáticos para comprimir e descomprimir arrays de bytes
 * utilizando a codificação de Huffman. A árvore de Huffman é serializada junto com os dados
 * comprimidos para permitir a reconstrução durante a descompressão.
 * 
 * Métodos principais:
 * <ul>
 *   <li>{@link #compress(byte[])}: Comprime um array de bytes usando Huffman.</li>
 *   <li>{@link #decompress(byte[])}: Descomprime um array de bytes previamente comprimido.</li>
 * </ul>
 * 
 * Estruturas internas:
 * <ul>
 *   <li><b>Node</b>: Representa um nó da árvore de Huffman, podendo ser folha (com valor) ou interno (com filhos).</li>
 * </ul>
 * 
 * Funcionalidades auxiliares:
 * <ul>
 *   <li>Construção da árvore de Huffman a partir das frequências dos bytes.</li>
 *   <li>Geração dos códigos binários para cada byte.</li>
 *   <li>Serialização e desserialização da árvore de Huffman.</li>
 *   <li>Codificação e decodificação dos dados usando os códigos de Huffman.</li>
 * </ul>
 * 
 * Exemplo de uso:
 * <pre>
 * byte[] original = ...;
 * byte[] comprimido = Huffman.compress(original);
 * byte[] descomprimido = Huffman.decompress(comprimido);
 * </pre>
 * 
 * @author Eron Arthur
 */
public class Huffman {
    // Estrutura de nó para a árvore de Huffman
    private static class Node implements Comparable<Node> {
        int freq;
        byte value;
        Node left, right;
        Node(byte value, int freq) { this.value = value; this.freq = freq; }
        Node(Node l, Node r) { this.left = l; this.right = r; this.freq = l.freq + r.freq; }
        public int compareTo(Node o) { return freq - o.freq; }
        boolean isLeaf() { return left == null && right == null; }
    }

    // Compressão
    public static byte[] compress(byte[] data) throws IOException {
        int[] freq = new int[256];
        for (byte b : data) freq[b & 0xFF]++;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < 256; i++) if (freq[i] > 0) pq.add(new Node((byte)i, freq[i]));
        while (pq.size() > 1) pq.add(new Node(pq.poll(), pq.poll()));
        Node root = pq.poll();
        Map<Byte, String> codes = new HashMap<>();
        buildCode(root, "", codes);

        // Serializa árvore + dados
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTree(root, out);
        out.write(data.length >> 24); out.write(data.length >> 16); out.write(data.length >> 8); out.write(data.length);
        BitSet bits = new BitSet();
        int bitCount = 0;
        for (byte b : data) {
            String code = codes.get(b);
            for (char c : code.toCharArray()) bits.set(bitCount++, c == '1');
        }
        byte[] bitArr = bits.toByteArray();
        out.write(bitArr);
        return out.toByteArray();
    }

    // Descompressão
    public static byte[] decompress(byte[] comp) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(comp);
        Node root = readTree(in);
        int len = (in.read() << 24) | (in.read() << 16) | (in.read() << 8) | in.read();
        BitSet bits = BitSet.valueOf(in.readAllBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Node node = root;
        int bitCount = 0;
        for (int i = 0; i < len; i++) {
            while (!node.isLeaf()) {
                node = bits.get(bitCount++) ? node.right : node.left;
            }
            out.write(node.value);
            node = root;
        }
        return out.toByteArray();
    }

    private static void buildCode(Node node, String code, Map<Byte, String> codes) {
        if (node.isLeaf()) codes.put(node.value, code.length() > 0 ? code : "0");
        else {
            buildCode(node.left, code + "0", codes);
            buildCode(node.right, code + "1", codes);
        }
    }
    private static void writeTree(Node node, OutputStream out) throws IOException {
        if (node.isLeaf()) {
            out.write(1); out.write(node.value);
        } else {
            out.write(0);
            writeTree(node.left, out);
            writeTree(node.right, out);
        }
    }
    private static Node readTree(InputStream in) throws IOException {
        int flag = in.read();
        if (flag == 1) return new Node((byte)in.read(), 0);
        Node left = readTree(in), right = readTree(in);
        return new Node(left, right);
    }
}