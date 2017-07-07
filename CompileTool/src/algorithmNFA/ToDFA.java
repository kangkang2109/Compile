package algorithmNFA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ToDFA {
    ArrayList<Item> map;

    ArrayList<Character> s;

    ArrayList<Integer> start;

    int end;

    Integer index = 0;

    HashMap<ArrayList<Integer>, Integer> search = new HashMap<>();// 查找是否相对的 数组序号

    ArrayList<ArrayList<Integer>> array = new ArrayList<>();// 用来检查是否重复

    ArrayList<Item> list = new ArrayList<>();// 输出的列表

    Stack<ArrayList<Integer>> stack = new Stack<>();// 栈

    ArrayList<Integer> arrayEndStatus = new ArrayList<>();

    ArrayList<Integer> arrayNotEndStatus = new ArrayList<>();

    public ToDFA(ArrayList<Item> map, ArrayList<Character> s, ArrayList<Integer> start, int end) {
        // TODO Auto-generated constructor stub
        this.end = end;
        this.map = map;
        this.s = s;
        this.start = start;
        parse();
        System.out.println("DFA:");
        showMap();

    }

    private void parse() {
        // TODO Auto-generated method stub
        ArrayList<Integer> begin = getStartArray();
        search.put(begin, getNewStatu());
        array.add(begin);
        stack.push(begin);
        while (!stack.isEmpty()) {
            ArrayList<Integer> tempList = stack.pop();
            for (char c : s) {
                ArrayList<Integer> temp = getArray(tempList, c);
                if (!array.contains(temp)) {

                    // if (!contains(array, temp)) {
                    // ArrayList.contains不能判断出来内部是调用if (o.equals(elementData[i]))
                    // if (array.contains(temp)) {
                    stack.push(temp);// ruzhan
                    array.add(temp);
                }
                list.add(new Item(findIndex(tempList), findIndex(temp), c));
            }

        }
    }

    private boolean contains(ArrayList<ArrayList<Integer>> array, ArrayList<Integer> temp) {
        // TODO Auto-generated method stub
        int flag = 0;
        for (ArrayList<Integer> item : array) {
            if (item.size() == temp.size()) {
                for (Integer i : item) {
                    if (!temp.contains(i)) {
                        flag = 1;
                    }
                }
                if (flag == 0) {
                    return true;
                } else {
                    flag = 0;
                }
            }
        }
        return false;
    }

    private int findIndex(ArrayList<Integer> tempList) {
        // TODO Auto-generated method stub
        if (!search.isEmpty() && search.containsKey(tempList)) {
            Integer i = search.get(tempList);
            if (tempList.contains(end)) {
                if (!arrayEndStatus.contains(i)) {
                    addIntItemOrder(arrayEndStatus, i);
                    // arrayEndStatus.add(i);
                }
            } else {
                if (!arrayNotEndStatus.contains(i)) {
                    addIntItemOrder(arrayNotEndStatus, i);
                    // arrayNotEndStatus.add(i);
                }
            }
            return i;
        } else {
            int n = getNewStatu();
            search.put(tempList, n);
            return n;
        }
    }

    private int getNewStatu() {
        // TODO Auto-generated method stub
        return index++;
    }

    private ArrayList<Integer> getStartArray() {
        // TODO Auto-generated method stub
        ArrayList<Integer> a = getArray(start, '$');
        return a;
    }

    private ArrayList<Integer> getArray(ArrayList<Integer> a, char ch) {
        int flag = 0;
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (ch == '$') {
            Copy(arrayList, a);
        }
        for (Integer i : a) {
            for (Item item : map) {
                if (i == item.getStartStatu() && ch == item.getAccept()) {
                    if (!arrayList.contains(item.getEndStatu())) {
                        addIntItemOrder(arrayList, item.getEndStatu());
                        // arrayList.add(item.getEndStatu());
                        flag = 1;
                    }
                }
            }
        }
        if (flag == 0) {
            return arrayList;
        } else {
            return getArray(arrayList, '$');
        }
    }

    private void addIntItemOrder(ArrayList<Integer> arrayList, int endStatu) {
        // TODO Auto-generated method stub
        for (int i = 0; i < arrayList.size(); i++) {
            if (endStatu < arrayList.get(i)) {
                arrayList.add(i, endStatu);
                return;
            }
        }
        arrayList.add(endStatu);
    }

    private void Copy(ArrayList<Integer> arrayList, ArrayList<Integer> a) {
        // TODO Auto-generated method stub
        if (!a.isEmpty()) {
            for (Integer i : a) {
                arrayList.add(i);
            }
        }
    }

    private void showMap() {
        for (Item item : list) {
            System.out.println(item.toString());
        }
    }

    public ArrayList<Item> getList() {
        // TODO Auto-generated method stub
        return list;
    }

    public ArrayList<Integer> getArrayEndStatus() {
        // TODO Auto-generated method stub
        return arrayEndStatus;
    }

    public ArrayList<Integer> getArrayNotEndStatus() {
        // TODO Auto-generated method stub
        return arrayNotEndStatus;
    }
}
