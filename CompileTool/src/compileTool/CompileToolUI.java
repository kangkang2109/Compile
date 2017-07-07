package compileTool;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import lexicalAnalysis.Common;
import lexicalAnalysis.DealLexical;
import lexicalAnalysis.Emblem;
import lexicalAnalysis.Token;
import syntaxAnalysis.DealSyntax;

public class CompileToolUI implements ActionListener {
    JFrame compileTool;

    JMenuBar menubar;

    JMenuItem open, save, saveAs, exit;

    JMenuItem revoke, copy, paste, shear;

    JMenuItem a, b, c, d;

    JMenuItem q, m, e, r;

    JTextArea codeContent, symTable;

    JTextArea errorList;

    JTextArea tokenList;

    int saveFlag = 0;

    String filePath = "";

    JTable tokenTable, errorTable, symbolTable;

    DefaultTableModel TokentableModel, errorTableModel, symbolTableModel;

    LineNumberHeaderView lineNumberHeader = null;

    DealLexical lexical = null;

    DealSyntax syntax = null;
    // private void setShortCut(MenuItem container) {
    // MenuShortcut shortcut = new MenuShortcut(KeyEvent.VK_E);
    // container.setShortcut(shortcut);
    // }

    public CompileToolUI() {
        // TODO Auto-generated constructor stub
        compileTool = new JFrame("compile");

        // 菜单
        menubar = new JMenuBar();
        // File
        JMenu file = new JMenu("文件");
        open = new JMenuItem("打开");
        open.addActionListener(this);
        file.add(open);
        save = new JMenuItem("保存");
        save.addActionListener(this);
        file.add(save);
        saveAs = new JMenuItem("另保存");
        saveAs.addActionListener(this);
        file.add(saveAs);
        exit = new JMenuItem("退出");
        exit.addActionListener(this);
        file.add(exit);
        menubar.add(file);
        // Edit
        JMenu edit = new JMenu("编辑");
        revoke = new JMenuItem("撤销");
        revoke.addActionListener(this);
        edit.add(revoke);
        copy = new JMenuItem("复制");
        copy.addActionListener(this);
        edit.add(copy);
        shear = new JMenuItem("剪切");
        shear.addActionListener(this);
        edit.add(shear);
        paste = new JMenuItem("粘贴");
        paste.addActionListener(this);
        edit.add(paste);
        menubar.add(edit);

        JMenu word = new JMenu("词法分析");
        a = new JMenuItem("词法分析器");
        a.addActionListener(this);
        word.add(a);
        b = new JMenuItem("正规式->NFA");
        b.addActionListener(this);
        word.add(b);
        c = new JMenuItem("NFA->DFA");
        c.addActionListener(this);
        word.add(c);
        d = new JMenuItem("DFA最小化");
        d.addActionListener(this);
        word.add(d);
        menubar.add(word);

        JMenu w = new JMenu("语法分析");
        q = new JMenuItem("语法分析器");
        q.addActionListener(this);
        w.add(q);
        m = new JMenuItem("LL(1)预测分析");
        m.addActionListener(this);
        w.add(m);
        e = new JMenuItem("运算符优先");
        e.addActionListener(this);
        w.add(e);
        r = new JMenuItem("LR分析");
        r.addActionListener(this);
        w.add(r);
        menubar.add(w);

        JMenu tempCode = new JMenu("中间代码");
        menubar.add(tempCode);

        JMenu finalCode = new JMenu("目标代码生成");
        menubar.add(finalCode);

        JMenu visit = new JMenu("查看");
        menubar.add(visit);
        JMenu help = new JMenu("帮助");
        menubar.add(help);
        compileTool.setJMenuBar(menubar);
        compileTool.setBounds(230, 80, 800, 600);

        Container container = compileTool.getContentPane();
        JSplitPane splitPane = new javax.swing.JSplitPane();
        container.add(splitPane, java.awt.BorderLayout.CENTER);
        JTextArea leftText = new JTextArea();
        JTextArea rightText = new JTextArea();
        JScrollPane leftPane = new JScrollPane(leftText);
        JScrollPane rightPane = new JScrollPane(rightText);
        leftText.setText("left");
        rightText.setText("right");

        JSplitPane leftJsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JSplitPane rightJspilt = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        splitPane.add(leftJsplit, JSplitPane.LEFT);
        splitPane.add(rightJspilt, JSplitPane.RIGHT);

        codeContent = new JTextArea();
        // symTable = new JTextArea();
        // tokenList = new JTextArea();
        // errorList = new JTextArea();

        String[] tokenData = { "行数", "单词", "码" };
        tokenTable = new JTable();
        TokentableModel = (DefaultTableModel) tokenTable.getModel();
        TokentableModel.addColumn("行数");
        TokentableModel.addColumn("单词");
        TokentableModel.addColumn("码");

        tokenTable = new JTable();
        TokentableModel = (DefaultTableModel) tokenTable.getModel();
        TokentableModel.addColumn("行数");
        TokentableModel.addColumn("单词");
        TokentableModel.addColumn("码");

        errorTable = new JTable();
        errorTableModel = (DefaultTableModel) errorTable.getModel();
        errorTableModel.addColumn("行数");
        errorTableModel.addColumn("错误");
        errorTable.setForeground(Color.red);

        symbolTable = new JTable();
        symbolTableModel = (DefaultTableModel) symbolTable.getModel();
        symbolTableModel.addColumn("入口");
        symbolTableModel.addColumn("单词名字");
        symbolTableModel.addColumn("单词长度");
        symbolTableModel.addColumn("类型");
        symbolTableModel.addColumn("种属");
        symbolTableModel.addColumn("值");
        symbolTableModel.addColumn("内存地址");

        JScrollPane codeScroll = new JScrollPane(codeContent);
        JScrollPane symScroll = new JScrollPane(symbolTable);
        JScrollPane tokenScroll = new JScrollPane(tokenTable);
        JScrollPane errorScroll = new JScrollPane(errorTable);

        if (lineNumberHeader == null) {
            lineNumberHeader = new LineNumberHeaderView();
            lineNumberHeader.setLineHeight(16);
        }
        codeScroll.setRowHeaderView(lineNumberHeader);
        leftJsplit.add(codeScroll, JSplitPane.TOP);
        leftJsplit.add(symScroll, JSplitPane.BOTTOM);
        rightJspilt.add(tokenScroll, JSplitPane.TOP);
        rightJspilt.add(errorScroll, JSplitPane.BOTTOM);
        rightJspilt.setDividerSize(4);
        leftJsplit.setDividerSize(4);
        splitPane.setDividerSize(4);

        // container.setLayout(new GridLayout(1, 2));
        // JPanel left = new JPanel();
        // JPanel right = new JPanel();
        // container.add(left);
        // container.add(right);
        // right.setLayout(new GridLayout(2, 1));
        // left.setLayout(new GridLayout(2, 1));
        // codeContent = new JTextArea("");
        // symTable = new JTextArea("符号表");
        // symTable.setEditable(false);
        // // codeContent.setLineWrap(true);
        // JScrollPane jsp = new JScrollPane(codeContent);
        // left.add(jsp);
        // JScrollPane j = new JScrollPane(symTable);
        // left.add(j);
        // top = new JTextArea("Token表");
        // buttom = new JTextArea("错误分析");
        // top.setEditable(false);
        // buttom.setEditable(false);
        // top.setLineWrap(true);
        // JScrollPane jPane = new JScrollPane(top);
        // buttom.setLineWrap(true);
        // JScrollPane s = new JScrollPane(buttom);
        // right.add(jPane);
        // right.add(s);
        compileTool.setVisible(true);
        splitPane.setDividerLocation(0.7);
        leftJsplit.setDividerLocation(0.7);
        rightJspilt.setDividerLocation(0.7);
    }

    private void initTable() {
        // TODO Auto-generated method stub
        if (errorTable != null) {
            errorTableModel.setRowCount(0);
        }
        if (symbolTable != null) {
            symbolTableModel.setRowCount(0);
        }
        if (tokenTable != null) {
            TokentableModel.setRowCount(0);
        }

    }

    private void openFile() {
        JFileChooser jfc = new JFileChooser(new File("C:\\Users\\kk\\Desktop"));
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = jfc.showDialog(new JLabel(), "选择");
        if (result == 1)// 取消
            return;
        else {
            int ch;
            File file = jfc.getSelectedFile();
            filePath = file.getPath();
            FileReader in;
            String content = "", temp;
            try {
                in = new FileReader(file);
                BufferedReader reader = new BufferedReader(in);
                while ((temp = reader.readLine()) != null) {
                    temp = temp + '\n';
                    content = content + temp;
                }
                codeContent.setText(content.toString());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }
        saveFlag = 1;
    }

    private void saveFile() {
        if (saveFlag == 1) {
            File file = new File(filePath);
            try {
                FileWriter fWriter = new FileWriter(file);
                fWriter.write(codeContent.getText());
                fWriter.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            if (codeContent.getText() == "") {
                JOptionPane.showMessageDialog(null, "文本为空");
            } else {
                JFileChooser jfc = new JFileChooser();
                int result = jfc.showDialog(new JLabel(), "save");
                if (result == 0) {
                    File file = jfc.getSelectedFile();
                    FileOutputStream fos = null;
                    filePath = file.getPath();
                    if (!file.exists()) {// 文件不存在 则创建一个
                        try {
                            file.createNewFile();
                            fos = new FileOutputStream(file);
                            fos.write(codeContent.getText().getBytes());
                            fos.flush();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else
                    return;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == open) {
            openFile();
        }
        if (e.getSource() == save || e.getSource() == saveAs) {
            saveFile();
        }
        if (e.getSource() == exit) {
            System.exit(0);
        }
        if (e.getSource() == m) {
            new LL1UI();
        }
        if (e.getSource() == a) {
            initTable();
            if (lexical == null) {
                lexical = new DealLexical(codeContent.getText());
            } else {
                lexical.refresh(codeContent.getText());
            }
            showInfo();
            showError(lexical);
        }
        if (e.getSource() == q) {
            if (errorTable != null) {
                errorTableModel.setRowCount(0);
            }
            if (lexical == null) {
                lexical = new DealLexical(codeContent.getText());
            } else if (lexical.getErrorCount() == 0) {
                if (syntax != null) {
                    syntax.refresh();
                } else {
                    syntax = new DealSyntax();
                }
                initTable();
                showInfo();
            } else {
                errorTableModel.addRow(new String[] { Integer.toString(1), "请先修改词法错误!!!" });
            }
            showError(syntax);
        }
    }

    public void showInfo() {
        // token
        String s = "";
        for (Token t : Common.tokenList) {
            TokentableModel.addRow(t.getTokenData());
        }

        s = "";
        for (Emblem e : Common.emblemsList) {
            symbolTableModel.addRow(e.getEmblemData());
        }
        // symTable.setText(s);
        // s = "";
    }

    public void showError(Object o) {
        if (o instanceof DealLexical) {
            ArrayList<Error> errors = ((DealLexical) o).getError();
            for (Error error : errors) {
                errorTableModel.addRow(error.getErrorData());
            }
        } else if (o instanceof DealSyntax) {
            ArrayList<Error> errors = ((DealSyntax) o).getError();
            for (Error error : errors) {
                errorTableModel.addRow(error.getErrorData());
            }
            // textError = textError + "\n错误总数" + ((DealSyntax) o).getErrorCount();
        }
        // errorList.setText(textError);
        // errorList.setForeground(Color.red);
    }

    public static void main(String[] args) {
        new CompileToolUI();
    }
}