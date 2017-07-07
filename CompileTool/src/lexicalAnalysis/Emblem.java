package lexicalAnalysis;

//符号item
public class Emblem {
    int entrance;

    int tokenNum;

    String name;

    String type;

    String kind;

    String val;

    public Emblem() {
        // TODO Auto-generated constructor stub
    }

    public Emblem(Integer entrance, String s, int num) {
        // TODO Auto-generated constructor stub
        this.entrance = entrance;
        name = s;
        tokenNum = num;
    }

    public String[] getEmblemData() {
        return new String[] { Integer.toString(getEntrance()), getName(), Integer.toString(getName().length()), getType(), getKind(), getVal(), "未知" };
    }

    /**
     * @return the entrance
     */
    public int getEntrance() {
        return entrance;
    }

    /**
     * @param entrance the entrance to set
     */
    public void setEntrance(int entrance) {
        this.entrance = entrance;
    }

    /**
     * @return the tokenNum
     */
    public int getTokenNum() {
        return tokenNum;
    }

    /**
     * @param tokenNum the tokenNum to set
     */
    public void setTokenNum(int tokenNum) {
        this.tokenNum = tokenNum;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the kind
     */
    public String getKind() {
        return kind;
    }

    /**
     * @param kind the kind to set
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * @return the val
     */
    public String getVal() {
        return val;
    }

    /**
     * @param val the val to set
     */
    public void setVal(String val) {
        this.val = val;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name + "\t" + name.length() + "\n";
    }

}
