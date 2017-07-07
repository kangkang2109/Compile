package lexicalAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import compileTool.Error;

public class DealLexical {
    public String[] keyWord = { "program", "var", "integer", "bool", "real", "char", "const", "begin", "if", "then", "else", "while", "do", "for", "to", "end", "read", "write", "true", "false", "not", "and", "or" };

    private int colCount = 0;

    private int rowCount = 1;

    int entrance = 1;

    private String text = "";

    private String line = "";

    private char index;

    int noteFlag = 0;

    ArrayList<Error> errors = new ArrayList<>();

    public DealLexical(String text) {
        // TODO Auto-generated constructor stub

        refresh(text);
    }

    public void refresh(String text) {
        this.text = text;
        init(); // 读表
        dealString();
    }

    private void init() {
        colCount = 0;
        rowCount = 1;
        entrance = 1;
        String pathname = "List.txt";
        String str;
        // FileoutputStream in;
        if (!Common.tokenList.isEmpty()) {
            Common.tokenList.clear();
        }
        if (!Common.emblemsList.isEmpty()) {
            Common.emblemsList.clear();
        }
        if (!errors.isEmpty()) {
            errors.clear();
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(pathname)));
            while ((str = reader.readLine()) != null) {
                Token item = new Token(str.split(" ")[0], Integer.parseInt(str.split(" ")[1]), 0);
                Common.list.add(item);
                // System.out.println(item.toString());
            }
            reader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void dealString() {
        BufferedReader read = new BufferedReader(new StringReader(text));
        try {
            while ((line = read.readLine()) != null) {
                if (noteFlag == 1) {
                    if (findEnd()) {
                        noteFlag = 0;
                    } else {
                        continue;
                    }
                }
                while (colCount < line.length()) {
                    if ((index = line.charAt(colCount++)) == ' ')
                        continue;
                    sort(index);
                }
                colCount = 0;
                rowCount++;
            }
            if (noteFlag == 1) {
                errorDeal(rowCount, "多行注释结尾错误！！！");
            }
            read.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isKeyWord(String s) {

        for (String str : keyWord) {
            if (str.equals(s))
                return true;
        }
        return false;
    }

    public void sort(char index) {
        if (Character.isDigit(index)) {
            recog_dig1(index);
        } else if (Character.isLetter(index)) {
            recog_id(index);
        } else if (index == '\'') {
            recog_str(index);
        } else if (index == '/') {
            hand_com(index);
        } else {
            recog_del(index);
        }
    }

    private boolean findEnd() {
        char ch;
        while (colCount < line.length()) {
            ch = line.charAt(colCount++);
            if (ch == '*' && colCount < line.length() && line.charAt(colCount++) == '/')
                return true;
        }
        return false;
    }

    private void errorDeal(int row, String s) {
        compileTool.Error error = new compileTool.Error(row, s);
        errors.add(error);
    }

    private void recog_str(char temp) {
        String s = "";
        int find = 0;
        while (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (temp == '\'') {
                find = 1;
                break;
            } else
                s = s + temp;
        }
        if (find == 1 && s.length() == 1) {
            ins_token(s, getTokenNum(s));
        } else {

            if (find == 0)
                errorDeal(rowCount, '\'' + s);
            else if (s.length() != 1)
                errorDeal(rowCount, '\'' + s + '\'');

        }
    }

    private void recog_id(char temp) {
        String s = "" + temp;
        while (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (Character.isLetterOrDigit(temp))
                s = s + temp;
            else {
                colCount--;
                break;
            }
        }
        if ((!isKeyWord(s)) && !isExist_sym(s)) {
            ins_sym(s, getTokenNum(s));
        }
        ins_token(s, getTokenNum(s));
    }

    private void ins_sym(String s, int tokenNum) {
        // TODO Auto-generated method stub
        Emblem e = new Emblem(entrance++, s, tokenNum);
        Common.emblemsList.add(e);
    }

    private boolean isExist_sym(String s) {
        for (Emblem e : Common.emblemsList) {
            if (e.getName().equals(s))
                return true;
        }
        return false;
    }

    private int getTokenNum(String s) {
        for (Token a : Common.list) {
            if (a.getWord().equals(s)) {
                return a.getNum();
            }
        }
        return 34;
    }

    private void ins_token(String str, int num) {
        Token a = new Token(str, num, rowCount);
        Common.tokenList.add(a);
    }

    // 注释
    private void hand_com(char temp) {
        String s = "" + temp;
        int flag = 0;
        if (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (temp == '/') {
                colCount = line.length();
                return;
            } else if (temp == '*') {
                if (findEnd()) // 当前行找到就return
                    return;
                else
                    noteFlag = 1;
            } else {
                colCount--;
                ins_token(s, getTokenNum(s));
            }
        }

    }

    private void recog_del(char temp) {
        String s = temp + "";
        char c = temp;
        int flag = 0;
        if (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (c == '<' && (temp == '=' || temp == '>'))
                flag = 1;
            else if (c == '>' && temp == '=')
                flag = 1;
            else if (c == '=' && temp == '=')
                flag = 1;
            else {
                flag = 0;
            }
            if (flag == 0) {
                colCount--;
            } else {
                s = s + temp;
            }
        }
        if (getTokenNum(s) == 34)
            errorDeal(rowCount, s);
        else
            ins_token(s, getTokenNum(s));
    }

    private void recog_dig1(char temp) {
        int flag = 0;
        String s = "" + temp;
        while (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (temp == ' ' || temp == '*' || temp == '/' || temp == '=' || temp == '+' || temp == '-' || temp == ';' || temp == '>' || temp == '<' || temp == '(' || temp == ')' || temp == ',' || temp == ':') {
                colCount--;
                break;
            }
            if (Character.isDigit(temp)) {
                flag = 0;
            } else if (temp == '.' && flag == 0) {
                flag = 1;
            } else if ((temp == 'e' || temp == 'E') && Character.isDigit(s.charAt(s.length() - 1))) {
                flag = 2;
                s = s + temp;
                if (colCount < line.length()) {
                    temp = line.charAt(colCount++);
                    if (temp == '+' || temp == '-' || Character.isDigit(temp))
                        ;
                    else
                        flag = -1;
                }
            } else {
                flag = -1;
            }
            s = s + temp;
        }
        if (flag < 0 || !Character.isDigit(s.charAt(s.length() - 1)))
            errorDeal(rowCount, s);
        else
            ins_token(s, getTokenNum(s));
    }

    public ArrayList<Error> getError() {
        return errors;
    }

    public int getErrorCount() {
        return errors.size();
    }

    private void recog_dig(char temp) {
        // TODO Auto-generated method stub
        int flag = 0;
        String s = "" + temp;
        int isRead;
        while (colCount < line.length()) {
            temp = line.charAt(colCount++);
            if (Character.isDigit(temp)) {
                s = s + temp;
            } else if (temp == '.' && flag == 0) {
                flag = 1;
                s = s + temp;
            } else if ((temp == 'e' || temp == 'E') && Character.isDigit(line.charAt(s.length() - 1))) {
                s = s + temp;
                flag = 2;
                if (colCount < line.length()) {
                    temp = line.charAt(colCount++);
                    if (temp == '+' || temp == '-' || Character.isDigit(temp)) {
                        s = s + temp;
                        ;
                    } else {
                        colCount--;
                        break;
                    }
                } else
                    break;
            } else {
                colCount--;
                break;
            }
        }

        int n = 0;
        char ch = line.charAt(s.length() - 1);
        boolean isEnd = (colCount == line.length());
        if (!isEnd) {
            switch (flag) {
            case 0:
                break;
            case 1:
                if (ch == '.') {
                    colCount--;
                    n = 1;
                }
                break;
            case 2:
                if (ch == '+' || ch == '-') {
                    colCount -= 2;
                    n = 2;
                } else if (ch == 'e' || ch == 'E') {
                    colCount--;
                    n = 1;
                }
                break;
            default:
                break;
            }
        } else {
            switch (flag) {
            case 1:
                if (ch == '.') {
                    colCount--;
                    n = 1;
                }
                break;
            case 2:
                if (ch == '+' || ch == '-') {
                    colCount -= 2;
                    n = 2;
                } else if (ch == 'e' || ch == 'E') {
                    colCount--;
                    n = 1;
                }
                break;
            default:
                break;
            }
        }
        int end = s.length();
        s = s.substring(0, end - n);
        ins_token(s, 0);
        // System.out.println(s);
    }
}
