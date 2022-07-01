import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;

public class DeleteWindow{
    private static JPanel panel;
    private JTextField tableName;
    private JLabel labTableName;
    private JTextField id;
    private JLabel labId;
    private JButton run;
    private JButton back;
    private String identifierName;
    GetConnection conn = GetConnection.getInstance();

    public DeleteWindow() {
    }
    public JPanel getPanel(){
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
        
        labId = new JLabel("Care este ID-ul intrarii sterse?");
        labId.setForeground(new Color(44, 69, 107));
        labId.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labId);

        id = new MyJTextField(100);
        panel.add(id);

        run = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Compileaza", 30);
        panel.add(run);

        back = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Inapoi la meniul principal", 30);
        panel.add(back);

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
                    Connection connection = conn.getConnection();
                    String tableNameText = tableName.getText();
                    if(tableNameText.isEmpty()){
                        throw new InvalidInputException("Completati toate campurile!");
                    }

                    String idText = id.getText();
                    if(idText.isEmpty()){
                        throw new InvalidInputException("Completati toate campurile!");
                    }

                    Statement statement = connection.createStatement();
                    ResultSet result = statement.executeQuery("select table_name from user_tables where table_name = '" + tableNameText.toUpperCase() + "'");
                    if (!result.next()) {
                        throw new InvalidTableNameException("Introduceti un nume de tabel valid!");
                    }
                    statement.close();

                    Statement statement1 = connection.createStatement();
                    ResultSet result1 = statement1.executeQuery("select * from " + tableNameText);
                    ResultSetMetaData resultSetMetaData1 = result1.getMetaData();
                    if(result1.next()) {
                        identifierName = resultSetMetaData1.getColumnName(1);
                    }
                    statement1.close();

                    Statement statement2 = connection.createStatement();
                    ResultSet result2 = statement2.executeQuery("select * from " + tableNameText + " where " + identifierName + " = " + idText);
                    if(!result2.next()) {
                        throw new InvalidIdException("Introduceti un ID valid!");
                    }
                    statement2.close();

                    Statement statement3 = connection.createStatement();
                    statement3.executeUpdate("delete from " + tableNameText + " where " + identifierName + " = " + idText);
                    statement3.close();

                    UIManager.put("OptionPane.background", new Color(216, 231, 250));
                    UIManager.put("Panel.background", new Color(216, 231, 250));
                    UIManager.put("Button.select", new Color(141, 189, 255));
                    UIManager.put("Button.focus", new Color(0, 0, 0, 0));
                    UIManager.put("Button.background", Color.WHITE);
                    UIManager.put("Button.border", new NormalBorder());

                    JButton button = new JButton("<Select");

                    Object[] option = {button};

                    button.addActionListener(ex2 -> {
                        if (ex2.getSource() == button){
                            SelectWindow selectWindow = new SelectWindow();
                            GUI gui = new GUI();
                            gui.getTabbedPane().add(selectWindow.getPanel());
                            gui.getTabbedPane().setSelectedIndex(1);
                            gui.getTabbedPane().remove(panel);

                            Window w = SwingUtilities.getWindowAncestor(button);
                            w.dispose();
                        }
                    });

                    JOptionPane.showOptionDialog(panel, "Intrarea a fost stearsa cu succes!", "Informare", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);

                } catch (InvalidInputException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidInputException");
                } catch (InvalidTableNameException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidTableNameException");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InvalidIdException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidIdException");
                }
            }
        });

        labTableName.setBounds(100, 100, 500, 35);
        tableName.setBounds(100, 150, 400, 35);
        labId.setBounds(100, 200, 500, 35);
        id.setBounds(100, 250, 400, 35);
        back.setBounds(100, 300, 400, 50);
        run.setBounds(100, 370, 400, 50);

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
