import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SelectTableWindow {
    private static JPanel panel;
    private JTable jTable;
    private JScrollPane jScrollPane;
    private DefaultTableModel defaultTableModel;
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<Integer> widths = new ArrayList<>();
    private ArrayList<ArrayList<String>> rows = new ArrayList<>(100);
    private String[][] tempData = new String[10][10];
    private String[] tempColumns = new String[10];
    private int columnCount;
    private int rowCount;
    GetConnection conn = GetConnection.getInstance();
    SelectWindow selectWindow = new SelectWindow();

    public SelectTableWindow() {
    }

    public JPanel getPanel() {
        for (int i = 0; i < 100; i++)
            rows.add(new ArrayList<>());

        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setBackground(new Color(233, 247, 250));
        panel.setBorder(BorderFactory.createTitledBorder("Tabelul " + selectWindow.getTableNameText().toUpperCase()));
        panel.setLayout(null);

        defaultTableModel = new DefaultTableModel(tempData, tempColumns);
        jTable = new JTable(defaultTableModel);
        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable.setBackground(Color.WHITE);
        jTable.setForeground(new Color(44, 69, 107));
        jTable.getTableHeader().setBackground(Color.WHITE);
        jTable.getTableHeader().setForeground(new Color(44, 69, 107));
        jTable.setBorder(BorderFactory.createLineBorder(new Color(44, 69, 107)));
        jTable.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(44, 69, 107)));

        JButton back = new MyButton(50, new Color(191, 214, 246), new Color(141, 189, 255), new Color(44, 69, 107), "Inapoi la pagina anterioara", 30);
        back.setBounds(10, 850, 500, 100);
        panel.add(back);

        jScrollPane = new JScrollPane(jTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getVerticalScrollBar().setUI(new MyScrollBarUI());
        jScrollPane.getHorizontalScrollBar().setUI(new MyScrollBarUI());

        JPanel buttonCorner1 = new JPanel();
        buttonCorner1.setVisible(true);
        buttonCorner1.setBackground(Color.WHITE);

        JPanel buttonCorner2 = new JPanel();
        buttonCorner2.setVisible(true);
        buttonCorner2.setBackground(Color.WHITE);

        JPanel buttonCorner3 = new JPanel();
        buttonCorner3.setVisible(true);
        buttonCorner3.setBackground(Color.WHITE);

        jScrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, buttonCorner1);
        jScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, buttonCorner2);
        jScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, buttonCorner3);
        jScrollPane.getViewport().setBackground(new Color(233, 247, 250));
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(jScrollPane);
        jScrollPane.setVisible(true);

        back.addActionListener(e -> {
            if (e.getSource() == back) {
                GUI gui = new GUI();
                gui.getTabbedPane().add(selectWindow.getPanel());
                gui.getTabbedPane().setSelectedIndex(1);
                gui.getTabbedPane().remove(panel);
            }
        });

        try {
            Connection connection = conn.getConnection();

            if(selectWindow.getYesOrNo().equals("nu")) {
                Statement statement1 = connection.createStatement();
                ResultSet result1 = statement1.executeQuery("select * from " + selectWindow.getTableNameText());
                ResultSetMetaData resultSetMetaData1 = result1.getMetaData();

                columnCount = resultSetMetaData1.getColumnCount();

                while (result1.next()) {
                    ++rowCount;
                    for (int i = 1; i <= resultSetMetaData1.getColumnCount(); i++) {
                        columns.add(resultSetMetaData1.getColumnName(i));
                        rows.get(rowCount - 1).add(String.valueOf(result1.getObject(i)));
                    }
                }
                statement1.close();
            }
            else {
                if (selectWindow.getAscOrDesc().equals("descrescator")) {
                    Statement statement2 = connection.createStatement();
                    ResultSet result2 = statement2.executeQuery("SELECT * FROM " + selectWindow.getTableNameText() + " ORDER BY " + selectWindow.getColumnNameText() + " DESC");
                    ResultSetMetaData resultSetMetaData2 = result2.getMetaData();

                    columnCount = resultSetMetaData2.getColumnCount();

                    while (result2.next()) {
                        ++rowCount;
                        for (int i = 1; i <= resultSetMetaData2.getColumnCount(); i++) {
                            columns.add(resultSetMetaData2.getColumnName(i));
                            rows.get(rowCount - 1).add(String.valueOf(result2.getObject(i)));
                        }
                    }
                    statement2.close();
                }
                else if (selectWindow.getAscOrDesc().equals("crescator")) {
                    Statement statement3 = connection.createStatement();
                    ResultSet result3 = statement3.executeQuery("SELECT * FROM " + selectWindow.getTableNameText() + " ORDER BY " + selectWindow.getColumnNameText());
                    ResultSetMetaData resultSetMetaData3 = result3.getMetaData();

                    columnCount = resultSetMetaData3.getColumnCount();

                    while (result3.next()) {
                        ++rowCount;
                        for (int i = 1; i <= resultSetMetaData3.getColumnCount(); i++) {
                            columns.add(resultSetMetaData3.getColumnName(i));
                            rows.get(rowCount - 1).add(String.valueOf(result3.getObject(i)));
                        }
                    }
                    statement3.close();
                }
            }
            defaultTableModel.setRowCount(rowCount);
            defaultTableModel.setColumnCount(columnCount);

            for (int i = 0; i < rowCount; i++)
                for (int j = 0; j < columnCount; j++)
                    defaultTableModel.setValueAt(rows.get(i).get(j), i, j);
            for (int j = 0; j < columnCount; j++)
                jTable.getTableHeader().getColumnModel().getColumn(j).setHeaderValue(columns.get(j));
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            int columnWidth = resizeColumnWidth();
            jScrollPane.setBounds(10, 15, columnWidth, 800);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return panel;
    }

    public int resizeColumnWidth () {
        TableColumnModel columnModel = jTable.getColumnModel();
        for (int col = 0; col < jTable.getColumnCount(); col++) {
            int maxWidth = 0;
            for (int row = 0; row < jTable.getRowCount(); row++) {
                TableCellRenderer rend = jTable.getCellRenderer(row, col);
                Object value = jTable.getValueAt(row, col);
                Component comp = rend.getTableCellRendererComponent(jTable, value, false, false, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width, maxWidth);
            }
            TableColumn column = columnModel.getColumn(col);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = jTable.getTableHeader().getDefaultRenderer();
            }
            Object headerValue = column.getHeaderValue();
            Component headerComp = headerRenderer.getTableCellRendererComponent(jTable, headerValue, false, false, 0, col);
            maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);
            columnModel.getColumn(col).setPreferredWidth(maxWidth + 5);
            widths.add(maxWidth + 5);
        }
        int sum = 0;
        for (int i = 0; i < widths.size(); i++) {
            sum = sum + widths.get(i);
        }
        return sum;
    }
}
