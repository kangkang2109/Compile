package syntaxAnalysis;

import java.util.ArrayList;
import java.util.LinkedList;

import compileTool.Clain;
import compileTool.Error;
import lexicalAnalysis.Common;
import lexicalAnalysis.Emblem;

public class DealSyntax {
    int last_Fclain = 0;

    int last_Tclain = 0;

    int f_clain = 0;

    int t_clain = 0;

    Clain nowClain = null;

    LinkedList<FourCode> FourList = null;

    int nxq = 1;

    int tmp_num = 0;

    int index = 1;

    String token = null;

    ArrayList<Error> errors = new ArrayList<>();

    int errorCount = 0;

    Clain old_Clain = null;

    public DealSyntax() {
        // TODO Auto-generated constructor stub
        refresh();
    }

    public void refresh() {
        last_Tclain = 0;
        last_Fclain = 0;
        int i = 1;
        init();
        parse();
        if (FourList != null) {
            for (FourCode item : FourList) {
                System.out.println((i++) + "\t" + item.toString());
            }
        }
    }

    private void init() {
        // TODO Auto-generated method stub
        tmp_num = 0;
        nxq = 1;
        if (FourList != null && !FourList.isEmpty()) {
            FourList.clear();
        }
        index = 0;
        token = null;
        errorCount = 0;
        if (!errors.isEmpty()) {
            errors.clear();
        }
    }

    private void parse() {
        token = getNextToken();
        if (!token.equals("program")) {
            dealError(getTokenIndex(), "缺少program");
        }
        token = getNextToken();
        if (!isVariable(token)) {
            dealError(getTokenIndex(), "wrong name");
        }
        genCode("program", token, "_", "_");
        token = getNextToken();
        if (!token.equals(";")) {
            dealError(getTokenIndex(), "程序头缺少';'");
        }
        token = getNextToken();
        if (token.equals("const")) {
            dealVarConst(1);
        } else {
            index--;
        }
        token = getNextToken();
        if (token.equals("var")) {
            dealVarConst(0);
        } else {
            dealError(getTokenIndex(), "缺少var");
        }
        token = getNextToken();
        if (token.equals("begin")) {
            sort();
        } else {
            dealError(getTokenIndex(), "缺少begin!!!");
        }
        token = getNextToken();
        if (token.equals(".")) {
            genCode("sys", "_", "_", "_");
            return;
        } else {
            dealError(getTokenIndex(), "错误结尾 ");
        }
    }

    /*
     * dealVarConst -> <dealLine> <dealVarConst> <dealLine> -> <Varible>T : <VaribleType> T->,<Varible>T|空
     */
    public void dealVarConst(int temp) {
        dealLine(temp);
        token = getNextToken();
        if (isVariable(token)) {
            index--;
            dealVarConst(temp);
        } else {
            index--;
        }
    }

    private void dealLine(int temp) {
        ArrayList<String> vs = new ArrayList<>();
        token = getNextToken();
        if (isVariable(token)) {
            vs.add(token);
            T(vs);
        }
        token = getNextToken();
        // 常量处理
        if (temp == 1) {
            if (!token.equals("=")) {
                dealError(getTokenIndex(), "常量赋值缺少'=");
            }
            token = getNextToken();
            String string = getConstType(token);
            if (string != null) {
                ins_Emblem(vs, string, "常量", token);
            } else {
                dealError(getTokenIndex(), "常量类型错误");
            }

            // if (!isConstant(token)) {
            // dealError(getTokenIndex(), "常量赋值出错");
            // }
        } else {
            if (!token.equals(":")) {
                dealError(getTokenIndex(), "变量赋值缺少':'");
            }
            token = getNextToken();
            if (!isVaribleType(token)) {
                dealError(getTokenIndex(), "变量赋值缺少变量类型");
            } else {
                ins_Emblem(vs, token, "变量", "");
            }

        }
        token = getNextToken();
        if (!token.equals(";")) {
            dealError(getTokenIndex(), "赋值缺少';'");
        }
    }

    private void ins_Emblem(ArrayList<String> vs, String string, String str, String var) {
        // TODO Auto-generated method stub
        for (String s : vs) {
            for (Emblem e : Common.emblemsList) {
                if (s.equals(e.getName())) {
                    e.setType(string);
                    if (var != "") {
                        e.setVal(var);
                    }
                    e.setKind(str);
                    break;
                }
            }
        }
    }

    private String getConstType(String token) {
        // TODO Auto-generated method stub
        int flag = 1;
        if (!Character.isDigit(token.charAt(0))) {
            if (token.equals("true") || token.equals("false")) {
                return "布尔常数";
            }
            if (token.length() == 3 && token.charAt(0) == '\'' && token.charAt(2) == '\'') {
                return "字符常量";
            }
            return null;
        } else {
            for (int i = 1; i < token.length(); i++) {
                if (Character.isDigit(token.charAt(i))) {
                    ;
                } else if (token.charAt(i) == '.' && flag == 1) {
                    flag = 2;
                } else if (token.charAt(i) == 'E' || token.charAt(i) == 'e' && (flag == 1 || flag == 0)) {
                    i++;
                    if (!(token.charAt(i) == '+' || token.charAt(i) == '-')) {
                        i--;
                    }
                    flag = 3;
                } else {
                    return null;
                }
            }
            if (Character.isDigit(token.charAt(token.length() - 1))) {
                if (flag == 1) {
                    return "整常数";
                } else {
                    return "实常数";
                }
            } else
                return null;
        }
    }

    private void T(ArrayList<String> vs) {
        // TODO Auto-generated method stub
        token = getNextToken();
        if (token.equals(",")) {
            token = getNextToken();
            if (isVariable(token)) {
                vs.add(token);
                T(vs);
            } else {
                dealError(getTokenIndex(), "非法字符',");
            }
        } else {
            index--;
        }

    }

    private boolean isVaribleType(String token) {
        // TODO Auto-generated method stub
        if (token.equals("integer") || token.equals("bool") || token.equals("real"))
            return true;
        return false;
    }

    private boolean isVariable(String temp) {
        // TODO Auto-generated method stub
        if (getTokenNum() == 34) {
            return true;
        }
        return false;
    }

    private void sort() {
        // TODO Auto-generated method stub
        while (true) {
            token = getNextToken();
            if (token.equals("end") || token.equals("")) {
                return;
            }
            if (token.equals("if")) {
                ifs("");
            } else if (token.equals("while")) {
                whiles("");
            } else if (token.equals("for")) {
                fors("");
            } else if (isVariable(token)) {
                index--;
                evaluation("");
            } else {
                dealError(getTokenIndex(), '\t' + token + "未找到该命令开头---sort");
            }
        }
    }

    private Clain fors(String str) {
        // TODO Auto-generated method stub
        Clain clain = new Clain();
        String tempNxq = "";
        System.out.println(str + "进入for语句");
        token = getNextToken();
        String tempToken = token;
        if (isVariable(token) && getNextToken().equals(":") && getNextToken().equals("=")) {
            String s = aexpr();
            genCode("=", s, "_", tempToken);
        } else {
            dealError(getTokenIndex(), "for赋值符号出错");
            return null;
        }
        token = getNextToken();
        System.out.println(token);
        if (token.equals("to") || token.equals("downto")) {
            String s1 = aexpr();
            tempNxq = Integer.toString(nxq);
            clain.setT_clain(nxq);
            genCode("j<", tempToken, s1, "0");
            clain.setF_clain(nxq);
            genCode("j", "_", "_", "0");
        } else {
            dealError(getTokenIndex(), "缺少to");
            return null;
        }
        System.out.println(clain.getT_clain() + clain.getF_clain());
        token = getNextToken();
        if (token.equals("do")) {
            backPatch(clain.getT_clain(), nxq);
            System.out.println(str + '\t' + "进入do字段");
            st_Sort(clain, str + "\t\t");
            System.out.println(str + '\t' + "退出do字段");
            String t = newt();
            genCode("+", tempToken, "1", t);
            genCode("=", t, "_", tempToken);
            genCode("j", "_", "_", tempNxq);
        }
        if (clain != null) {
            backPatch(clain.getF_clain(), nxq);
        }

        System.out.println(str + "退出for语句");
        return clain;
    }

    private void repeats(String str) {
        // TODO Auto-generated method stub

    }

    private Clain whiles(String str) {
        // TODO Auto-generated method stub
        Clain clain = null;
        System.out.println(str + "进入while语句");
        int tempNxq = nxq;
        clain = bexp();
        token = getNextToken();
        if (!token.equals("do")) {
            dealError(getTokenIndex(), "while语句缺少do字段");
            return null;
        } else {
            if (clain != null) {
                backPatch(clain.getT_clain(), nxq);
            }
            System.out.println(str + "\t进入do字段");
            st_Sort(clain, str + "\t\t");
            genCode("j", "_", "_", Integer.toString(tempNxq));
            System.out.println(str + "\t退出do字段");
        }
        System.out.println(str + "退出while语句");
        if (clain != null) {
            backPatch(clain.getF_clain(), nxq);
        }
        return clain;
    }

    /*
     * 赋值语句 <赋值语句> -> <变量>：=<算术表达式> | <布尔表达式>
     */
    private void evaluation(String str) {
        System.out.println(str + "进入赋值语句");
        token = getNextToken();
        if (!isVariable(token)) {
            dealError(getTokenIndex(), "赋值语句出错！！！");
            return;
        }
        String str1 = token;
        token = getNextToken();
        if (token.equals(":") && (token = getNextToken()).equals("=")) {
            String temp = aexpr();
            genCode("=", temp, "_", str1);
            token = getNextToken();
            if (!token.equals(";")) {
                dealError(getTokenIndex(), "缺少';'");
            }
        } else {
            dealError(getTokenIndex(), "赋值符号出错");
        }
    }

    private Clain ifs(String str) {
        // TODO Auto-generated method stub
        System.out.println(str + "进入if语句");
        Clain temp = null;
        Clain clain = bexp();
        token = getNextToken();
        if (token.equals("then")) {
            System.out.println(str + "\t进入then字段");
            if (clain != null) {
                backPatch(clain.getT_clain(), nxq);
            }
            temp = st_Sort(clain, str + '\t' + '\t');
            if (clain != null) {
                backPatch(clain.getF_clain(), nxq);
            }
            System.out.println(str + "\t退出then字段");
        } else {
            dealError(getTokenIndex(), "缺少then");
            return null;
        }
        System.out.println(str + "退出if语句");
        return temp;

        // if (token.equals("else")) {
        // st_Sort();
        // } else
        // dealError("");
    }

    private boolean isEqual(Clain temp, Clain clain) {
        // TODO Auto-generated method stub
        if (temp != null && clain != null && temp.getF_clain() == clain.getF_clain() && temp.getT_clain() == clain.getT_clain()) {
            return true;
        }
        return false;
    }

    /*
     * 语句 -> {<程序体>}|<程序因子> <程序体> -> <程序因子> <程序体> <程序因子> -> <if语句> | <while语句> | <for语句> |<赋值语句>
     */
    private Clain st_Sort(Clain c, String str) {
        // TODO Auto-generated method stub
        Clain clain = null;
        token = getNextToken();
        if (token.equals("(")) {
            clain = programBody(c, str);
            token = getNextToken();
            if (!token.equals(")")) {
                dealError(getTokenIndex(), "程序体缺少)     programBody");
                return null;
            }
        } else {
            index--;
            clain = programFactor(c, str);
        }
        return clain;
    }

    private Clain programBody(Clain c, String str) {
        token = getNextToken();
        Clain clain = null;
        if (token.equals("if") || token.equals("while") || token.equals("for") | isVariable(token)) {
            index--;
            clain = programFactor(c, str);
            programBody(c, str);
        } else {
            index--;
        }
        return clain;
    }

    private Clain programFactor(Clain c, String str) {
        // TODO Auto-generated method stub
        Clain clain = null;
        token = getNextToken();
        if (token.equals("if")) {
            clain = ifs(str);
            return clain;
        } else if (token.equals("while")) {
            return whiles(str);
        } else if (token.equals("for")) {
            return fors(str);
        } else if (isVariable(token)) {
            index--;
            evaluation(str);
            return c;
        } else {
            dealError(getTokenIndex(), '\t' + token + "未找到该命令开头---programFactor");

        }
        return null;
    }

    // 布尔表达式
    private Clain bexp() {
        // TODO Auto-generated method stub
        Clain temp;
        temp = bt();
        token = getNextToken();
        if (token.equals("or")) {
            Clain tClain;
            tClain = bexp();
            if (temp != null && tClain != null) {
                backPatch(temp.getF_clain(), tClain.getF_clain());
                merge(temp.getT_clain(), tClain.getT_clain());
                tClain.setT_clain(temp.getT_clain());
            }
            return tClain;
        } else {
            index--;
        }
        return temp;
    }

    private Clain bt() {
        // TODO Auto-generated method stub
        Clain tempClain;
        tempClain = bf();
        token = getNextToken();
        if (token.equals("and")) {
            if (tempClain != null) {
                backPatch(tempClain.getT_clain(), nxq);
            }
            Clain tClain = bexp();
            if (tClain != null && tempClain != null) {
                merge(tempClain.getF_clain(), tClain.getF_clain());
                tClain.setF_clain(tempClain.getF_clain());
            }
            return tClain;
        } else {
            index--;
        }
        return tempClain;
    }

    private void merge(int old_clain, int new_clain) {
        // TODO Auto-generated method stub
        FourList.get(new_clain - 1).setStr4(Integer.toString(old_clain));
    }

    private void backPatch(int old_clain, int nxq2) {
        // TODO Auto-generated method stub
        FourList.get(old_clain - 1).setStr4(Integer.toString(nxq2));
    }

    private Clain bf() {
        // TODO Auto-generated method stub
        token = getNextToken();
        if (token.equals("not")) {
            bf();
        } else if (token.equals("(")) {
            Clain tempClain = bexp();
            token = getNextToken();
            if (!token.equals(")")) {
                dealError(getTokenIndex(), "'('不匹配 " + token);
            } else {
                ;
            }
            return tempClain;
        } else {
            index--;
            String t1 = aexpr();
            token = getNextToken();
            if (token.equals(">") || token.equals("<") || token.equals(">=") || token.equals("<=") || token.equals("=") || token.equals("<>")) {
                String symbolTemp = token;
                String t2 = aexpr();
                Clain clain = new Clain();
                clain.setT_clain(nxq);
                // 真出口
                genCode("j" + symbolTemp, t1, t2, "0");
                // 假出口
                clain.setF_clain(nxq);
                genCode("j", "_", "_", "0");
                return clain;
            } else {
                index--;
                dealError(getTokenIndex(), "缺少布尔量     bf()");
            }
        }
        return null;
    }

    private String aexpr() {
        // TODO Auto-generated method stub
        String temp;
        temp = A();
        return a(temp);
    }

    private String A() {
        // TODO Auto-generated method stub
        return b(B());
    }

    private String a(String str) {
        // TODO Auto-generated method stub
        token = getNextToken();
        String temp = "";
        if (token.equals("+") || token.equals("-")) {
            genCode(token, str, A(), temp = newt());
            a(temp);
            return temp;
        } else {
            index--;
            return str;
        }

    }

    private String b(String str) {
        // TODO Auto-generated method stub
        String temp = "";
        token = getNextToken();
        if (token.equals("*") || token.equals("/")) {
            genCode(token, str, B(), temp = newt());
            b(temp);
            return temp;
        } else {
            index--;
            return str;
        }

    }

    private void genCode(String str1, String str2, String str3, String str4) {
        // TODO Auto-generated method stub
        if (FourList == null) {
            FourList = new LinkedList<>();
        }
        FourList.add(new FourCode(str1, str2, str3, str4));
        nxq++;
    }

    private String newt() {
        // TODO Auto-generated method stub
        tmp_num++;
        return "T" + tmp_num;
    }

    private String B() {
        // TODO Auto-generated method stub
        token = getNextToken();
        if (token.equals("(")) {
            String temp = aexpr();
            token = getNextToken();
            if (!token.equals(")")) {
                dealError(getTokenIndex(), "'('不匹配   B()");
            }
            return temp;
        } else if (isVariable(token)) {
            return token;
        } else if (isConstant(token)) {
            return token;
        } else {
            dealError(getTokenIndex(), "错误字符 B()" + token);
            return "";
        }
    }

    /*
     * 算术表达式 <算术表达式> -> <算数表达式> +<项> | <算数表达式> - <项> | <项> <项> -> <项> * <因子> | <项> / <因子> | <因子> <因子> -> (<算术表达式>) |<常量>
     * | <变量> <变量> -> <标志符> <常量> -> <整型变量> | <实型变量>
     */
    // private void aexpr() {
    // // TODO Auto-generated method stub
    // A();
    // a();
    // }
    //
    // private void A() {
    // // TODO Auto-generated method stub
    // B();
    // b();
    // }
    //
    // private void a() {
    // // TODO Auto-generated method stub
    // token = getNextToken();
    // if (token.equals("+") || token.equals("-")) {
    // A();
    // a();
    // } else
    // index--;
    // }
    //
    // private void b() {
    // // TODO Auto-generated method stub
    // token = getNextToken();
    // if (token.equals("*") || token.equals("/")) {
    // B();
    // b();
    // } else {
    // index--;
    // }
    // }
    //
    // private void B() {
    // // TODO Auto-generated method stub
    // token = getNextToken();
    // if (token.equals("(")) {
    // aexpr();
    // token = getNextToken();
    // if (!token.equals(")")) {
    // dealError(getTokenIndex(), "'('不匹配 B()");
    // }
    // } else if (isVariable(token)) {
    //
    // } else if (isConstant(token)) {
    //
    // } else {
    // dealError(getTokenIndex(), "错误字符 B()" + token);
    // }
    // }

    private boolean isConstant(String token2) {
        // TODO Auto-generated method stub
        int num = getTokenNum();
        if (num == 35 || num == 36) {
            return true;
        }
        return false;
    }

    private void dealError(Integer row, String content) {
        // TODO Auto-generated method stub
        errorCount++;
        Error error = new Error(row, content);
        errors.add(error);
    }

    public ArrayList<Error> getError() {
        return errors;
    }

    public int getErrorCount() {
        return errorCount;
    }

    private String getNextToken() {
        if (Common.tokenList.isEmpty() || index >= Common.tokenList.size()) {
            return "";
        }
        return Common.tokenList.get(index++).getWord();
    }

    // 获取当前token的序号
    private int getTokenIndex() {
        if (Common.tokenList.isEmpty() || index - 1 >= Common.tokenList.size()) {
            return -1;
        }
        return Common.tokenList.get(index - 1).getIndex();
    }

    // 获得当前token的码
    private int getTokenNum() {
        if (Common.tokenList.isEmpty() || index - 1 >= Common.tokenList.size()) {
            return -1;
        }
        return Common.tokenList.get(index - 1).getNum();
    }

}
