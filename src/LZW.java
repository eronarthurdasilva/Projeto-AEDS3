/**
 * Implementação do algoritmo de compressão e descompressão LZW (Lempel-Ziv-Welch).
 * 
 * Esta classe fornece dois métodos estáticos:
 * <ul>
 *   <li><b>compress(byte[] input):</b> Comprime um array de bytes utilizando o algoritmo LZW e retorna o resultado comprimido como um novo array de bytes.</li>
 *   <li><b>decompress(byte[] input):</b> Descomprime um array de bytes previamente comprimido pelo método compress, retornando o array de bytes original.</li>
 * </ul>
 * 
 * O algoritmo LZW é amplamente utilizado para compressão de dados sem perdas, construindo dinamicamente um dicionário de sequências de caracteres encontradas nos dados de entrada.
 * 
 * <p>
 * <b>Observação:</b> Esta implementação utiliza códigos de 16 bits para representar as entradas do dicionário.
 * </p>
 */
import java.util.*;
import java.io.*;

public class LZW {
    public static byte[] compress(byte[] input) {
        Map<String, Integer> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) dict.put("" + (char)i, i);
        String w = "";
        List<Integer> result = new ArrayList<>();
        int dictSize = 256;
        for (byte b : input) {
            String wc = w + (char)(b & 0xFF);
            if (dict.containsKey(wc)) w = wc;
            else {
                result.add(dict.get(w));
                dict.put(wc, dictSize++);
                w = "" + (char)(b & 0xFF);
            }
        }
        if (!w.equals("")) result.add(dict.get(w));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int code : result) {
            out.write((code >> 8) & 0xFF);
            out.write(code & 0xFF);
        }
        return out.toByteArray();
    }

    public static byte[] decompress(byte[] input) {
        List<Integer> codes = new ArrayList<>();
        for (int i = 0; i < input.length; i += 2) {
            int code = ((input[i] & 0xFF) << 8) | (input[i+1] & 0xFF);
            codes.add(code);
        }
        Map<Integer, String> dict = new HashMap<>();
        for (int i = 0; i < 256; i++) dict.put(i, "" + (char)i);
        String w = "" + (char)(int)codes.get(0);
        StringBuilder result = new StringBuilder(w);
        int dictSize = 256;
        for (int i = 1; i < codes.size(); i++) {
            int k = codes.get(i);
            String entry;
            if (dict.containsKey(k)) entry = dict.get(k);
            else if (k == dictSize) entry = w + w.charAt(0);
            else throw new IllegalArgumentException("Bad LZW code: " + k);
            result.append(entry);
            dict.put(dictSize++, w + entry.charAt(0));
            w = entry;
        }
        return result.toString().getBytes();
    }
}