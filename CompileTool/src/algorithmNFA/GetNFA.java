package algorithmNFA;

import java.util.ArrayList;
import java.util.Stack;

public class GetNFA {
    String str = null;

    int statuCount = 1;

    int nowStatus = 0;

    int index = 0;

    int start;

    int end;

    Stack<Item> stack = new Stack<>();

    // Stack<Integer> endStark = new Stack<>();

    // Stack<Integer> statuStark = new Stack<>();

    ArrayList<Item> map = new ArrayList<>();

    ArrayList<Character> s = new ArrayList<>();

    public GetNFA(String string) {
        // TODO Auto-generated constructor stub
        str = string;
        parse();
        System.out.println("NFA:" + map.get(map.size() - 1).getEndStatu());
        showMap();
    }

    private void parse() {
        // TODO Auto-generated method stub
        char ch = getNextChar();
        while (ch != ' ') {
            if (ch == '(') {
                Item item = new Item();
                item.setStartStatu(nowStatus);
                stack.push(item);
            } else if (ch == ')') {
                if (!stack.isEmpty() && stack.peek().getEndStatu() != 0 && nowStatus != stack.peek().getEndStatu()) {
                    map.add(new Item(nowStatus, stack.peek().getEndStatu(), '$'));
                }
                if (getNextChar() == '*') {
                    if (stack.peek().getEndStatu() == 0) {
                        map.add(new Item(nowStatus, stack.peek().getStartStatu(), '$'));
                    } else {
                        map.add(new Item(stack.peek().getEndStatu(), stack.peek().getStartStatu(), '$'));
                    }
                } else {
                    index--;
                }
                nowStatus = stack.peek().getEndStatu();
                stack.pop();
            } else if (ch == '|') {
                end = statuCount - 1;// 记录'|'前面的最后一个状态.
                stack.peek().setEndStatu(end);
                char temp = getNextChar();
                if (temp == '(') {
                    Item item = new Item();
                    item.setStartStatu(stack.peek().getStartStatu());
                    stack.push(item);
                } else if (temp == '*' || temp == ')' || temp == ' ') {
                    return;// 出错
                } else {
                    index--;
                }
                nowStatus = stack.peek().getStartStatu();
            } else {
                if (!s.contains(ch)) {
                    s.add(ch);
                }
                int t = getNewStatus();
                map.add(new Item(nowStatus, t, ch));
                if (getNextChar() == '*') {
                    // a*|b*和 a*|b
                    map.add(new Item(t, nowStatus, '$'));
                    int f = getNewStatus();
                    map.add(new Item(t, f, '$'));
                    nowStatus = f;
                } else {
                    index--;
                    nowStatus = t;
                }
            }
            ch = getNextChar();
        }
    }

    private int getNewStatus() {
        // TODO Auto-generated method stub
        return statuCount++;
    }

    private void showMap() {
        for (Item item : map) {
            System.out.println(item.toString());
        }
    }

    public char getNextChar() {
        if (index < str.length()) {
            return str.charAt(index++);
        } else {
            index++;
            return ' ';
        }
    }

    private ArrayList<Integer> getStart() {
        // TODO Auto-generated method stub
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(0);
        return arrayList;
    }

    private ArrayList<Character> getCharacter() {
        // TODO Auto-generated method stub
        return s;
    }

    private ArrayList<Item> getMap() {
        // TODO Auto-generated method stub
        return map;
    }

    private int getEnd() {
        // TODO Auto-generated method stub
        return map.get(map.size() - 1).getEndStatu();
    }

    public static void main(String[] args) {
        String str = "((a|b)*abb)";
        // String str = "((a|(b|c)*d)a)";
        // String str = "(a*|b*)";
        GetNFA getNFA = new GetNFA(str);
        ToDFA toDFA = new ToDFA(getNFA.getMap(), getNFA.getCharacter(), getNFA.getStart(), getNFA.getEnd());
        ToMFA toMFA = new ToMFA(toDFA.getList(), toDFA.getArrayEndStatus(), toDFA.getArrayNotEndStatus(), getNFA.getCharacter());
    }
}
