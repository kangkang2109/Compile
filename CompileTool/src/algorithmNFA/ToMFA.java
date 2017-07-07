package algorithmNFA;

import java.util.ArrayList;

public class ToMFA {
    ArrayList<Item> map;

    ArrayList<Item> mapMFA = new ArrayList<>();

    ArrayList<Integer> arrayEndStatus;

    ArrayList<Character> s;

    ArrayList<Integer> arrayNotEndStatus;

    ArrayList<ArrayList<Integer>> stack;

    public ToMFA(ArrayList<Item> map, ArrayList<Integer> arrayEndStatus, ArrayList<Integer> arrayNotEndStatus, ArrayList<Character> s) {
        // TODO Auto-generated constructor stub
        this.map = map;
        this.arrayEndStatus = arrayEndStatus;
        this.arrayNotEndStatus = arrayNotEndStatus;
        this.s = s;
        init();
        parse();
        System.out.println("MFA:");
        showMap();
    }

    private void parse() {
        // TODO Auto-generated method stub
        ArrayList<ArrayList<Integer>> back = new ArrayList<>();
        for (char ch : s) {
            for (int i = 0; i < stack.size(); i++) {
                divide(back, stack.get(i), ch);
            }
            stack.clear();
            for (ArrayList<Integer> arrayList : back) {
                stack.add(arrayList);
            }
            back.clear();
        }
        int[] num = new int[stack.size()];
        for (char ch : s) {
            for (int j = 0; j < num.length; j++) {
                for (int m = 0; m < stack.size(); m++) {
                    int end = getEndStatu(stack.get(j).get(0), ch);
                    if (stack.get(m).contains(end)) {
                        mapMFA.add(new Item(j, m, ch));
                    }

                }
            }
        }
    }

    private void showMap() {
        for (Item item : mapMFA) {
            System.out.println(item.toString());
        }
    }

    private void divide(ArrayList<ArrayList<Integer>> back, ArrayList<Integer> arrayList, char ch) {
        // TODO Auto-generated method stub
        if (arrayList.size() == 1) {
            back.add(arrayList);
            return;
        }
        int[] temp = new int[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 0; j < stack.size(); j++) {
                Integer endStatu = getEndStatu(arrayList.get(i), ch);
                if (stack.get(j).contains(endStatu)) {
                    temp[i] = j;
                }
            }
        }
        int t = temp[0];
        while (true) {
            ArrayList<Integer> list = new ArrayList<>();
            for (int k = 0; k < temp.length; k++) {
                if (temp[k] == t) {
                    list.add(arrayList.get(k));
                    temp[k] = -1;
                }
            }
            back.add(copeArray(list));
            list.clear();
            int m;
            for (m = 0; m < temp.length; m++) {
                if (temp[m] != -1) {
                    t = temp[m];
                    break;
                }
            }
            if (m == temp.length) {
                break;
            }
        }
    }

    private ArrayList<Integer> copeArray(ArrayList<Integer> list) {
        // TODO Auto-generated method stub
        ArrayList<Integer> a = new ArrayList<>();
        for (Integer i : list) {
            a.add(i);
        }
        return a;
    }

    private Integer getEndStatu(Integer i, char ch) {
        // TODO Auto-generated method stub
        for (Item item : map) {
            if (item.getStartStatu() == i && item.getAccept() == ch) {
                return item.getEndStatu();
            }
        }
        return -1;
    }

    private void init() {
        // TODO Auto-generated method stub
        stack = new ArrayList();
        stack.add(arrayEndStatus);
        stack.add(arrayNotEndStatus);
    }
}
