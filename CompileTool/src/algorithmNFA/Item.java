package algorithmNFA;

public class Item {
    int startStatu;

    int endStatu;

    char accept;

    public Item() {
        // TODO Auto-generated constructor stub
    }

    public Item(int startStatu, int endStatu) {
        this.endStatu = endStatu;
        this.startStatu = startStatu;
        this.accept = '0';
    }

    public Item(int startStatu, int endStatu, char accept) {
        // TODO Auto-generated constructor stub
        this.startStatu = startStatu;
        this.endStatu = endStatu;
        this.accept = accept;
    }

    /**
     * @return the startStatu
     */
    public int getStartStatu() {
        return startStatu;
    }

    /**
     * @param startStatu the startStatu to set
     */
    public void setStartStatu(int startStatu) {
        this.startStatu = startStatu;
    }

    /**
     * @return the endStatu
     */
    public int getEndStatu() {
        return endStatu;
    }

    /**
     * @param endStatu the endStatu to set
     */
    public void setEndStatu(int endStatu) {
        this.endStatu = endStatu;
    }

    /**
     * @return the accept
     */
    public char getAccept() {
        return accept;
    }

    /**
     * @param accept the accept to set
     */
    public void setAccept(char accept) {
        this.accept = accept;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return startStatu + "\t" + accept + "\t" + endStatu;
    }
}
