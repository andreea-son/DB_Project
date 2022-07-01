import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;

public class SelectWindow {
    private static JPanel panel;
    private JTextField tableName;
    private JTextField columnName;
    private JLabel labTableName;
    private JLabel labColumnName;
    private JLabel labSort;
    private JLabel labOption;
    private JRadioButton yes;
    private JRadioButton no;
    private JRadioButton ascending;
    private JRadioButton descending;
    private JButton run;
    private JButton back;
    private static String yesOrNo = "nu";
    private static String ascOrDesc = "crescator";
    private static String tableNameText = "";
    private static String columnNameText = "";
    private ArrayList<String> columns = new ArrayList<>();
    GetConnection conn = GetConnection.getInstance();

    public SelectWindow() {
    }

    public String getAscOrDesc() {
        return SelectWindow.ascOrDesc;
    }

    public static void setAscOrDesc(String ascOrDesc) {
        SelectWindow.ascOrDesc = ascOrDesc;
    }

    public String getYesOrNo() {
        return SelectWindow.yesOrNo;
    }

    public static void setYesOrNo(String yesOrNo) {
        SelectWindow.yesOrNo = yesOrNo;
    }

    public String getTableNameText() {
        return SelectWindow.tableNameText;
    }

    public static void setTableNameText(String tableNameText) {
        SelectWindow.tableNameText = tableNameText;
    }

    public String getColumnNameText() {
        return SelectWindow.columnNameText;
    }

    public static void setColumnNameText(String columnNameText) {
        SelectWindow.columnNameText = columnNameText;
    }

    public JPanel getPanel() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setBackground(new Color(233, 247, 250));
        panel.setLayout(null);

        labTableName = new JLabel("Care este numele tabelului?");
        labTableName.setForeground(new Color(44, 69, 107));
        labTableName.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labTableName);

        tableName = new MyJTextField(100);
        panel.add(tableName);

        labOption = new JLabel("Doriti sa sortati?");
        labOption.setForeground(new Color(44, 69, 107));
        labOption.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labOption);

        yes = new JRadioButton("Da");
        yes.setFocusPainted(false);
        yes.setForeground(new Color(44, 69, 107));
        yes.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        yes.setIcon(new ImageIcon("Resources/images/radio_button.png"));
        yes.setContentAreaFilled(false);
        panel.add(yes);

        no = new JRadioButton("Nu");
        no.setForeground(new Color(44, 69, 107));
        no.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        no.setFocusPainted(false);
        no.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
        no.setContentAreaFilled(false);
        panel.add(no);

        ButtonGroup group1 = new ButtonGroup();
        group1.add(yes);
        group1.add(no);

        labColumnName = new JLabel("In functie de ce coloana sortati?");
        labColumnName.setForeground(new Color(44, 69, 107));
        labColumnName.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labColumnName);
        labColumnName.setVisible(false);

        columnName = new MyJTextField(100);
        panel.add(columnName);
        columnName.setVisible(false);

        labSort = new JLabel("Sortati crescator sau descrescator?");
        labSort.setForeground(new Color(44, 69, 107));
        labSort.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        labSort.setVisible(false);
        panel.add(labSort);

        ascending = new JRadioButton("Crescator");
        ascending.setFocusPainted(false);
        ascending.setForeground(new Color(44, 69, 107));
        ascending.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        ascending.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
        ascending.setContentAreaFilled(false);
        ascending.setVisible(false);
        panel.add(ascending);

        descending = new JRadioButton("Descrescator");
        descending.setForeground(new Color(44, 69, 107));
        descending.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        descending.setFocusPainted(false);
        descending.setIcon(new ImageIcon("Resources/images/radio_button.png"));
        descending.setContentAreaFilled(false);
        descending.setVisible(false);
        panel.add(descending);

        ButtonGroup group2 = new ButtonGroup();
        group2.add(ascending);
        group2.add(descending);

        back = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Inapoi la meniul principal", 30);
        panel.add(back);

        run = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Compileaza", 30);
        panel.add(run);

        labTableName.setBounds(100, 100, 400, 35);
        tableName.setBounds(100, 150, 400, 35);
        labOption.setBounds(100, 200, 300, 35);
        yes.setBounds(100, 250, 300, 35);
        no.setBounds(100, 300, 300, 35);
        labColumnName.setBounds(100, 350, 600, 35);
        columnName.setBounds(100, 400, 400, 35);
        labSort.setBounds(100, 450, 600, 35);
        ascending.setBounds(100, 500, 300, 35);
        descending.setBounds(100, 550, 300, 35);
        back.setBounds(100, 350, 400, 50);
        run.setBounds(100, 420, 400, 50);

        back.addActionListener(e -> {
            if (e.getSource() == back) {
                GUI gui = new GUI();
                gui.getTabbedPane().add(gui.getPanel());
                gui.getTabbedPane().setSelectedIndex(1);
                gui.getTabbedPane().remove(panel);
            }
        });

        run.addActionListener(e -> {
            if (e.getSource() == run) {
                try {
                    int ok = 0;
                    Connection connection = conn.getConnection();
                    if (yesOrNo.equals("da")) {
                        setTableNameText(tableName.getText());
                        if (tableNameText.isEmpty()) {
                            throw new InvalidInputException("Completati toate campurile!");
                        }
                        Statement statement = connection.createStatement();
                        ResultSet result = statement.executeQuery("select table_name from user_tables where table_name = '" + tableNameText.toUpperCase() + "'");
                        if (!result.next()) {
                            throw new InvalidTableNameException("Introduceti un nume de tabel valid!");
                        }
                        statement.close();

                        setColumnNameText(columnName.getText());
                        if(columnNameText.isEmpty()) {
                            throw new InvalidInputException("Completati toate campurile!");
                        }
                        Statement statement1 = connection.createStatement();
                        ResultSet result1 = statement1.executeQuery("select * from " + tableNameText);
                        ResultSetMetaData resultSetMetaData = result1.getMetaData();
                        for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
                            columns.add(resultSetMetaData.getColumnName(i));
                        for(int i = 0; i < columns.size(); i++){
                            if(columnNameText.equalsIgnoreCase(columns.get(i))){
                                ok = 1;
                            }
                        }
                        if(ok == 0) {
                            throw new InvalidColumnNameException("Introduceti o coloana care exista in tabelul ales!");
                        }
                        statement1.close();
                    }
                    else {
                        setTableNameText(tableName.getText());
                        if (tableNameText.isEmpty()) {
                            throw new InvalidInputException("Completati toate campurile!");
                        }
                        Statement statement2 = connection.createStatement();
                        ResultSet result2 = statement2.executeQuery("select table_name from user_tables where table_name = '" + tableNameText.toUpperCase() + "'");
                        if (!result2.next()) {
                            throw new InvalidTableNameException("Introduceti un nume de tabel valid!");
                        }
                        statement2.close();
                    }
                    GUI gui = new GUI();
                    SelectTableWindow selectTableWindow = new SelectTableWindow();
                    gui.getTabbedPane().add(selectTableWindow.getPanel());
                    gui.getTabbedPane().setSelectedIndex(1);
                    gui.getTabbedPane().remove(panel);
                } catch (InvalidInputException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidInputException");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidTableNameException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidTableNameException");
                } catch (InvalidColumnNameException ex){
                    new MyJOptionPane(ex.getMessage(), "InvalidColumnNameException");
                }
            }
        });

        yes.addActionListener(e -> {
            if (e.getSource() == yes) {
                yes.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
                no.setIcon(new ImageIcon("Resources/images/radio_button.png"));
                labSort.setVisible(true);
                ascending.setVisible(true);
                descending.setVisible(true);
                labColumnName.setVisible(true);
                columnName.setVisible(true);
                back.setBounds(100, 600, 400, 50);
                run.setBounds(100, 670, 400, 50);
                setYesOrNo("da");
            }
        });

        no.addActionListener(e -> {
            if (e.getSource() == no) {
                no.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
                yes.setIcon(new ImageIcon("Resources/images/radio_button.png"));
                labSort.setVisible(false);
                ascending.setVisible(false);
                descending.setVisible(false);
                labColumnName.setVisible(false);
                columnName.setVisible(false);
                back.setBounds(100, 350, 400, 50);
                run.setBounds(100, 420, 400, 50);
                setYesOrNo("nu");
            }
        });

        ascending.addActionListener(e -> {
            if (e.getSource() == ascending) {
                ascending.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
                descending.setIcon(new ImageIcon("Resources/images/radio_button.png"));
                setAscOrDesc("crescator");
            }
        });

        descending.addActionListener(e -> {
            if (e.getSource() == descending) {
                descending.setIcon(new ImageIcon("Resources/images/radio_button_pressed.png"));
                ascending.setIcon(new ImageIcon("Resources/images/radio_button.png"));
                setAscOrDesc("descrescator");
            }
        });

        panel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"enter");
        panel.getActionMap().put("enter", new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                run.doClick();
            }
        });

        return panel;
    }
}
