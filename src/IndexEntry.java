/*
 * Repesenta uma entrada de indice primário, direto e denso 
 * Armazena cada entrada de índice com o id do registro
 */
public class IndexEntry {
    private int id;

    private long filePosition;

    /*
     * Construtor da classe IndexEntry
     * @param id O ID do registro associado a esta entrada de índice
     * @param filePosition A posição do registro no arquivo
     * 
     */
    public IndexEntry(int id, long filePosition) {
        this.id = id;
        this.filePosition = filePosition;
    }

    /*
     * Retorna o ID do registro associado a esta entrada de índice
     * @return O ID do registro
     */
    public int getId() {
        return id;
    }

    /*
     * Retorna a posição do registro no arquivo associado a esta entrada de índice
     * @return A posição do registro no arquivo
     */
    public long getFilePosition() {
        return filePosition;
    }

    /*
     * Define o ID do registro associado a esta entrada de índice
     * @param id O novo ID do registro
     */
    public void setId(int id) {
        this.id = id;
    }

    /*
     * Define a posição do registro no arquivo associado a esta entrada de índice
     * @param filePosition A nova posição do registro no arquivo
     */
    public void setFilePosition(long filePosition) {
        this.filePosition = filePosition;
    }
}
