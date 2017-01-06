package subd.ui;

import subd.ui.util.OracleTreeModel;
import subd.ui.util.RowOperationsDispatcher;

import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 21.04.13
 * Time: 13:43
 * To change this template use File | Settings | File Templates.
 */
public class QueryFrame extends SQLFrame {
    private JTable table = new JTable();
    private OracleTreeModel tableModel;
    private String query;

    public QueryFrame(String connectionUrl, String user, String password, String query) throws SQLException {
        super(connectionUrl, user, password);
        this.setSize(600, 400);
        this.query = query;
        createNewTableModel(null);
        this.add(new JScrollPane(table));
    }

    private void createNewTableModel(RowOperationsDispatcher dispatcher) throws SQLException {
        CachedRowSet nRowSet = getRowSet(query);
        tableModel = new OracleTreeModel(nRowSet, dispatcher);
        table.setModel(tableModel);
    }
}
