import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.ArrayList;
public class UpdateWindow{
    private static JPanel panel;
    private JTextField tableName;
    private JTextField id;
    private JLabel labTableName;
    private JLabel labId;
    private JButton run;
    private JLabel labColumnName;
    private JLabel labValue;
    private JTextField value;
    private JTextField columnName;
    private String identifierName;
    private ArrayList<String> columns = new ArrayList<>();
    private String[] foreignKeys = {"ID_CLIENT", "ID_CURIER", "ID_COMANDA", "ID_DISTRIBUITOR", "ID_MATERIAL"};
    GetConnection conn = GetConnection.getInstance();

    public UpdateWindow() {
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

        labId = new JLabel("Care este ID-ul intrarii modificate?");
        labId.setForeground(new Color(44, 69, 107));
        labId.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labId);

        id = new MyJTextField(100);
        panel.add(id);

        labColumnName = new JLabel("Ce coloana doriti sa modificati?");
        labColumnName.setForeground(new Color(44, 69, 107));
        labColumnName.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labColumnName);

        columnName = new MyJTextField(100);
        panel.add(columnName);
        
        labValue = new JLabel("Ce valoare doriti sa inserati in coloana data?");
        labValue.setForeground(new Color(44, 69, 107));
        labValue.setFont(new Font("Book Antiqua", Font.ITALIC|Font.BOLD, 30));
        panel.add(labValue);

        value = new MyJTextField(100);
        panel.add(value);

        JButton back = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Inapoi la meniul principal", 30);
        panel.add(back);

        run = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Compileaza", 30);
        panel.add(run);

        back.addActionListener(e -> {
            if (e.getSource() == back) {
                GUI gui = new GUI();
                gui.getTabbedPane().add(gui.getPanel());
                gui.getTabbedPane().setSelectedIndex(1);
                gui.getTabbedPane().remove(panel);
            }
        });

        run.addActionListener(e -> {
            if(e.getSource() == run){
                try {
                    Connection connection = conn.getConnection();
                    int ok = 0;
                    String tableNameText = tableName.getText();
                    if(tableNameText.isEmpty()){
                        throw new InvalidInputException("Completati toate campurile!");
                    }

                    String idText = id.getText();
                    if(idText.isEmpty()){
                        throw new InvalidInputException("Completati toate campurile!");
                    }

                    String columnNameText = columnName.getText();
                    if(columnNameText.isEmpty()){
                        throw new InvalidInputException("Completati toate campurile!");
                    }

                    String valueText = value.getText();
                    if(valueText.isEmpty()){
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
                    ResultSet result3 = statement3.executeQuery("select * from " + tableNameText);
                    ResultSetMetaData resultSetMetaData3 = result3.getMetaData();
                    columns.clear();
                    for (int i = 1; i <= resultSetMetaData3.getColumnCount(); i++)
                        columns.add(resultSetMetaData3.getColumnName(i));
                    for(int i = 0; i < columns.size(); i++){
                        if (columnNameText.equalsIgnoreCase(columns.get(i))) {
                            ok = 1;
                            break;
                        }
                    }
                    if(ok == 0) {
                        throw new InvalidColumnNameException("Introduceti o coloana care exista in tabelul ales!");
                    }
                    statement3.close();

                    if(columnNameText.equalsIgnoreCase(identifierName)){
                        throw new InvalidInputException("Introduceti o coloana diferita de cheia primara a tabelului ales!");
                    }

                    for(int i = 0; i < foreignKeys.length; i++){
                        if (columnNameText.equalsIgnoreCase(foreignKeys[i])) {
                            throw new InvalidInputException("Introduceti o coloana diferita de cheile straine ale tabelului ales!");
                        }
                    }

                    Statement statement4 = connection.createStatement();
                    ResultSet result4 = statement4.executeQuery("SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = '" + tableNameText.toUpperCase() + "'");

                    String tempColumn = "";
                    String tempDataType = "";
                    int tempDataLength = 0;
                    int tempDataPrecision = 0;

                    String column = "";
                    String dataType = "";
                    int dataLength = 0;
                    int dataPrecision = 0;

                    while (result4.next()){
                        if(tempColumn.equalsIgnoreCase(columnNameText)){
                            column = tempColumn;
                            dataType = tempDataType;
                            dataLength = tempDataLength;
                            dataPrecision = tempDataPrecision;
                        }
                        tempColumn = result4.getString(1);
                        tempDataType = result4.getString(2);
                        tempDataLength = result4.getInt(3);
                        tempDataPrecision = result4.getInt(4);
                    }
                    statement4.close();

                    if(dataType.equals("VARCHAR2")){
                        if(valueText.length() > dataLength){
                            throw new InvalidInputException("Introduceti o valoare compatibila cu tipul de date VARCHAR2(" + dataLength + ")!");
                        }
                    }

                    if(dataType.equals("NUMBER")){
                        if(valueText.matches("\\d+")){
                            int stringToNumber = Integer.parseInt(valueText);
                            int cpy = stringToNumber;
                            int count = 0;
                            while(cpy != 0){
                                cpy = cpy / 10;
                                ++count;
                            }
                            if(count > dataPrecision){
                                throw new InvalidInputException("Introduceti o valoare compatibila cu tipul de date NUMBER(" + dataPrecision + ")!");
                            }
                        }
                        else {
                            throw new InvalidInputException("Introduceti o valoare compatibila cu tipul de date NUMBER(" + dataPrecision + ")!");
                        }
                    }

                    if(dataType.equals("DATE")){
                        if(!valueText.matches("(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/(((200)|(201))[0-9]|(202)[012])")){
                            throw new InvalidInputException("Introduceti o valoare compatibila cu tipul de date DATE, formatul DD/MM/YYYY!");
                        }
                    }

                    if(dataType.equals("NUMBER")){
                        Statement statement5 = connection.createStatement();
                        statement5.executeUpdate("update " + tableNameText + " set " + columnNameText + " = " + valueText + " where " + identifierName + " = " + idText);
                        statement5.close();
                    }

                    if(dataType.equals("VARCHAR2")){
                        Statement statement6 = connection.createStatement();
                        statement6.executeUpdate("update " + tableNameText + " set " + columnNameText + " = '" + valueText + "' where " + identifierName + " = " + idText);
                        statement6.close();
                    }

                    if(dataType.equals("DATE")){
                        Statement statement7 = connection.createStatement();
                        statement7.executeUpdate("update " + tableNameText + " set " + columnNameText + " = TO_DATE('" + valueText + "', 'DD/MM/YYYY') where " + identifierName + " = " + idText);
                        statement7.close();
                    }

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

                    JOptionPane.showOptionDialog(panel, "Tabelul a fost actualizat cu succes!", "Informare", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, option, option[0]);

                } catch (InvalidInputException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidInputException");
                } catch (InvalidTableNameException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidTableNameException");
                } catch (InvalidIdException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidIdException");
                } catch (InvalidColumnNameException ex) {
                    new MyJOptionPane(ex.getMessage(), "InvalidColumnNameException");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
      });

        labTableName.setBounds(100, 100, 500, 35);
        tableName.setBounds(100, 150, 400, 35);
        labId.setBounds(100, 200, 500, 35);
        id.setBounds(100, 250, 400, 35);
        labColumnName.setBounds(100, 300, 500, 35);
        columnName.setBounds(100, 350, 400, 35);
        labValue.setBounds(100, 400, 700, 35);
        value.setBounds(100, 450, 400, 35);
        back.setBounds(100, 500, 400, 50);
        run.setBounds(100, 570, 400, 50);

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
