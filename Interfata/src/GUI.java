import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame{
    private JButton select;
    private JButton update;
    private JButton delete;
    private JLabel tooltip1;
    private JLabel tooltip2;
    private JLabel tooltip3;
    private static JPanel panel;
    private static JTabbedPane tabbedPane = new MyTabbedPane();
    public GUI(){

    }

    public void addToFrame(){
        this.add(tabbedPane);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Baza de date a magazinului de bijuterii online");
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }

    public JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public JPanel getPanel(){
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setBackground(new Color(233, 247, 250));
        panel.setLayout(null);

        select = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Select", 30);
        panel.add(select);

        tooltip1 = new JLabel("Selecteaza coloanele unui tabel ales");
        tooltip1.setOpaque(true);
        tooltip1.setBorder(new RoundedCornerBorder(20));
        tooltip1.setForeground(new Color(44, 69, 107));
        tooltip1.setBackground(Color.WHITE);
        tooltip1.setSize(tooltip1.getPreferredSize());
        panel.add(tooltip1);
        tooltip1.setVisible(false);

        update = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Update", 30);
        panel.add(update);

        tooltip2 = new JLabel("Modifica o coloana a unei intrari dintr-un tabel ales");
        tooltip2.setOpaque(true);
        tooltip2.setBorder(new RoundedCornerBorder(20));
        tooltip2.setForeground(new Color(44, 69, 107));
        tooltip2.setBackground(Color.WHITE);
        tooltip2.setSize(tooltip2.getPreferredSize());
        panel.add(tooltip2);
        tooltip2.setVisible(false);

        delete = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Delete", 30);
        panel.add(delete);

        tooltip3 = new JLabel("Sterge o intrare dintr-un tabel ales");
        tooltip3.setOpaque(true);
        tooltip3.setBorder(new RoundedCornerBorder(20));
        tooltip3.setForeground(new Color(44, 69, 107));
        tooltip3.setBackground(Color.WHITE);
        tooltip3.setSize(tooltip3.getPreferredSize());
        panel.add(tooltip3);
        tooltip3.setVisible(false);

        select.setBounds(100, 100, 500, 100);
        tooltip1.setBounds(600, 160 + tooltip1.getHeight(), tooltip1.getWidth(), tooltip1.getHeight());
        update.setBounds(100, 300, 500, 100);
        tooltip2.setBounds(600, 360 + tooltip2.getHeight(), tooltip2.getWidth(), tooltip2.getHeight());
        delete.setBounds(100, 500, 500, 100);
        tooltip3.setBounds(600, 560 + tooltip3.getHeight(), tooltip3.getWidth(), tooltip3.getHeight());

        select.addActionListener(e -> {
            if(e.getSource() == select){
                SelectWindow selectWindow = new SelectWindow();
                tabbedPane.add(selectWindow.getPanel());
                tabbedPane.setSelectedIndex(1);
                tabbedPane.remove(panel);
            }
        });

        select.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tooltip1.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tooltip1.setVisible(false);
            }
        });

        update.addActionListener(e -> {
            if(e.getSource() == update){
                UpdateWindow updateWindow = new UpdateWindow();
                tabbedPane.add(updateWindow.getPanel());
                tabbedPane.setSelectedIndex(1);
                tabbedPane.remove(panel);
            }
        });

        update.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tooltip2.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tooltip2.setVisible(false);
            }
        });

        delete.addActionListener(e ->  {
            if(e.getSource() == delete){
                DeleteWindow deleteWindow = new DeleteWindow();
                tabbedPane.add(deleteWindow.getPanel());
                tabbedPane.setSelectedIndex(1);
                tabbedPane.remove(panel);
            }
        });

        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tooltip3.setVisible(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tooltip3.setVisible(false);
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.addToFrame();
        tabbedPane.add(gui.getPanel());
    }
}