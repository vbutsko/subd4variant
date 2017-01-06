package subd.ui;

import subd.ui.util.JDBCUtil;
import subd.ui.util.OracleTreeModel;
import subd.ui.util.RowOperationsDispatcher;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.spi.SyncProviderException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;


public class TableFrame extends SQLFrame implements RowSetListener {
    private JTable table = new JTable(); // The table for displaying data

    private JTextField[] textFields;
    private JLabel[] labels;

    private JButton button_ADD_ROW = new JButton("Add row to table");
//    private JButton button_UPDATE_ROW = new JButton("Update row");
    private JButton button_DEL_ROW = new JButton("Delete row from table");
    private JButton button_UPDATE_DATABASE = new JButton("Commit changes");
    private JButton button_DISCARD_CHANGES = new JButton("Rollback changes");

    private String query;
    private OracleTreeModel tableModel;

    public TableFrame(String connectionUrl, String user, String password, String query, RowOperationsDispatcher dispatcher) throws SQLException {
        super(connectionUrl, user, password);
        this.query = query;

        createNewTableModel(dispatcher);

        int columnCount = tableModel.getColumnCount();
        ResultSetMetaData metaData = tableModel.getMetaData();

        this.setSize(600, 400);

        Container contentPane = getContentPane();
        contentPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        contentPane.add(new JScrollPane(table), c);

        textFields = new JTextField[columnCount];
        labels = new JLabel[columnCount];
        for (int i = 0; i < columnCount; i++) {
            textFields[i] = new JTextField(20);
            labels[i] = new JLabel(metaData.getColumnLabel(i + 1));

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.LINE_START;
            c.weightx = 0.25;
            c.weighty = 0;
            c.gridx = 0;
            c.gridy = i + 1;
            c.gridwidth = 1;
            contentPane.add(labels[i], c);

            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.LINE_END;
            c.weightx = 0.75;
            c.weighty = 0;
            c.gridx = 1;
            c.gridy = i + 1;
            c.gridwidth = 1;
            contentPane.add(textFields[i], c);
        }

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = columnCount + 1;
        c.gridwidth = 1;
        contentPane.add(button_ADD_ROW, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = columnCount + 1;
        c.gridwidth = 1;
        contentPane.add(button_DEL_ROW, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = columnCount + 2;
        c.gridwidth = 1;
        contentPane.add(button_UPDATE_DATABASE, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 0.5;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = columnCount + 2;
        c.gridwidth = 1;
        contentPane.add(button_DISCARD_CHANGES, c);

        button_ADD_ROW.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.util.List<String> strings = new ArrayList<String>();
                    for (JTextField jf : textFields) {
                        strings.add(jf.getText().trim());
                    }
                    TableFrame.this.tableModel.insertRow(strings);
                } catch (SQLException sqlExc) {
                    displaySQLExceptionDialog(sqlExc);
                }
            }
        });

      /*  button_UPDATE_ROW.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowNum = table.getSelectedRow() + 1;
                if(rowNum > 0) {
                    try {
                        java.util.List<String> strings = new ArrayList<String>();
                        for (JTextField jf : textFields) {
                            strings.add(jf.getText().trim());
                        }
                        TableFrame.this.tableModel.insertRow(strings);
                    } catch (SQLException sqlExc) {
                        displaySQLExceptionDialog(sqlExc);
                    }
                }
            }
        });
*/
        button_DEL_ROW.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowNum = table.getSelectedRow() + 1;
                if(rowNum > 0) {
                    try {
                        tableModel.getRowSet().absolute(rowNum);
                        tableModel.getRowSet().deleteRow();
                    } catch (SQLException sqlExc) {
                        displaySQLExceptionDialog(sqlExc);
                    }
                }
            }
        });

        button_UPDATE_DATABASE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    TableFrame.this.
                            tableModel.getRowSet().
                            acceptChanges();
                }
                catch (SQLException sqle) {

                    displaySQLExceptionDialog(sqle);
                    // Now revert back changes
                    try {
                        createNewTableModel(tableModel.getDispatcher());
                    } catch (SQLException sqle2) {
                        displaySQLExceptionDialog(sqle2);
                    }
                }
            }
        });

        button_DISCARD_CHANGES.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    createNewTableModel(tableModel.getDispatcher());
                } catch (SQLException sqle) {
                    displaySQLExceptionDialog(sqle);
                }
            }
        });
    }

    private void createNewTableModel(RowOperationsDispatcher dispatcher) throws SQLException {
        CachedRowSet nRowSet = getRowSet(query);
        tableModel = new OracleTreeModel(nRowSet, dispatcher);
        tableModel.addEventHandlersToRowSet(this);
        table.setModel(tableModel);
    }

    private void displaySQLExceptionDialog(SQLException e) {
        // Display the SQLException in a dialog box
        JOptionPane.showMessageDialog(
                this,
                "error!"
               /*new String[]{
                        e.getClass().getName() + ": ",
                        e.getMessage()}*/

        );
    }

    public void rowChanged(RowSetEvent event) {
        CachedRowSet currentRowSet = this.tableModel.getRowSet();

        try {
            currentRowSet.moveToCurrentRow();
            tableModel = new OracleTreeModel(tableModel.getRowSet(), tableModel.getDispatcher());
            table.setModel(tableModel);
        } catch (SQLException ex) {
            JDBCUtil.printSQLException(ex);
            displaySQLExceptionDialog(ex);
        }
    }

    public void cursorMoved(RowSetEvent event) {
    }

    public void rowSetChanged(RowSetEvent event) {
    }
}
