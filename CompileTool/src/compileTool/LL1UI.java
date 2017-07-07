package compileTool;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LL1UI {
    JFrame jFrame;

    Container container;

    JButton b1, b2, b3;

    public LL1UI() {
        // TODO Auto-generated constructor stub
        jFrame = new JFrame("LL(1)文法");
        container = jFrame.getContentPane();
        container.setLayout(null);
        jFrame.setBounds(230, 80, 800, 600);
        b1 = new JButton("读入文法");
        b2 = new JButton("求First集");
        b2 = new JButton("求Follow集");
        b1.setBounds(10, 10, 90, 20);
        container.add(b1);
        container.add(b2);
        jFrame.setVisible(true);
    }
}
