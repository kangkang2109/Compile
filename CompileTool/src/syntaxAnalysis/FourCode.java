package syntaxAnalysis;

public class FourCode {
    String str1 = "";

    String str2 = "";

    String str3 = "";

    String str4 = "";

    public FourCode() {
        // TODO Auto-generated constructor stub
    }

    public FourCode(String str1, String str2, String str3, String str4) {
        // TODO Auto-generated constructor stub
        this.str1 = str1;
        this.str2 = str2;
        this.str3 = str3;
        this.str4 = str4;
    }

    /**
     * @return the str1
     */
    public String getStr1() {
        return str1;
    }

    /**
     * @param str1 the str1 to set
     */
    public void setStr1(String str1) {
        this.str1 = str1;
    }

    /**
     * @return the str2
     */
    public String getStr2() {
        return str2;
    }

    /**
     * @param str2 the str2 to set
     */
    public void setStr2(String str2) {
        this.str2 = str2;
    }

    /**
     * @return the str3
     */
    public String getStr3() {
        return str3;
    }

    /**
     * @param str3 the str3 to set
     */
    public void setStr3(String str3) {
        this.str3 = str3;
    }

    /**
     * @return the str4
     */
    public String getStr4() {
        return str4;
    }

    /**
     * @param str4 the str4 to set
     */
    public void setStr4(String str4) {
        this.str4 = str4;
    }

    public String toString() {
        // TODO Auto-generated method stub
        return str1 + '\t' + str2 + '\t' + str3 + '\t' + str4;
    }

}
