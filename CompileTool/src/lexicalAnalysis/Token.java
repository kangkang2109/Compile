package lexicalAnalysis;

import java.io.Serializable;

public class Token implements Serializable {

    private String word;

    private int num;

    private int index;

    public Token(String word, int num, int index) {
        // TODO Auto-generated constructor stub
        this.word = word;
        this.num = num;
        this.index = index;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * @return the word
     */
    public String getWord() {
        return word;
    }

    /**
     * @param word the word to set
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * @return the num
     */
    public int getNum() {
        return num;
    }

    /**
     * @param num the num to set
     */
    public void setNum(int num) {
        this.num = num;
    }

    public String[] getTokenData() {
        String[] data = { Integer.toString(getIndex()), getWord(), Integer.toString(getNum()) };
        return data;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return index + "\t " + word + "\t" + +num + "\n";
    }
}
