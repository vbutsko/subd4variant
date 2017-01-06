package subd.ui.util;

import javax.sql.RowSetListener;
import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class OracleTreeModel implements TableModel {
    private CachedRowSet rowSet;
    private ResultSetMetaData metaData;
    private RowOperationsDispatcher dispatcher;
    int numRows, numCols;

    public ResultSetMetaData getMetaData() {
        return metaData;
    }

    public OracleTreeModel(CachedRowSet newRowSet, RowOperationsDispatcher dispatcher) throws SQLException {
        this.rowSet = newRowSet;
        this.dispatcher = dispatcher;
        this.metaData = this.rowSet.getMetaData();
        numCols = metaData.getColumnCount();

        // Retrieve the number of rows.
        this.rowSet.beforeFirst();
        this.numRows = 0;
        while (this.rowSet.next()) {
            this.numRows++;
        }
        this.rowSet.beforeFirst();
    }

    public void addEventHandlersToRowSet(RowSetListener listener) {
        this.rowSet.addRowSetListener(listener);
    }


    public void insertRow(List<String> fields) throws SQLException {
        try {
            if (dispatcher != null) {
                this.rowSet.moveToInsertRow();
                if(dispatcher.dispatchInsertRow(this.rowSet, fields)) {
                    rowSet.insertRow();
                }
                rowSet.moveToCurrentRow();
            }
        } catch (SQLException e) {
            JDBCUtil.printSQLException(e);
        }
    }

    public void close() {
        try {
            rowSet.getStatement().close();
        } catch (SQLException e) {
            JDBCUtil.printSQLException(e);
        }
    }

    /**
     * Automatically close when we're garbage collected
     */
    protected void finalize() {
        close();
    }

    /**
     * Method from interface TableModel; returns the number of columns
     */

    public int getColumnCount() {
        return numCols;
    }

    /**
     * Method from interface TableModel; returns the number of rows
     */

    public int getRowCount() {
        return numRows;
    }

    /**
     * Method from interface TableModel; returns the column name at columnIndex
     * based on information from ResultSetMetaData
     */

    public String getColumnName(int column) {
        try {
            return this.metaData.getColumnLabel(column + 1);
        } catch (SQLException e) {
            return e.toString();
        }
    }

    /**
     * Method from interface TableModel; returns the most specific superclass for
     * all cell values in the specified column. To keep things simple, all data
     * in the table are converted to String objects; hence, this method returns
     * the String class.
     */

    public Class getColumnClass(int column) {
        return String.class;
    }

    /**
     * Method from interface TableModel; returns the value for the cell specified
     * by columnIndex and rowIndex. TableModel uses this method to populate
     * itself with data from the row set. SQL starts numbering its rows and
     * columns at 1, but TableModel starts at 0.
     */

    public Object getValueAt(int rowIndex, int columnIndex) {

        try {
            this.rowSet.absolute(rowIndex + 1);
            Object o = this.rowSet.getObject(columnIndex + 1);
            if (o == null)
                return null;
            else
                return o.toString();
        } catch (SQLException e) {
            return e.toString();
        }
    }

    /**
     * Method from interface TableModel; returns true if the specified cell
     * is editable. This sample does not allow users to edit any cells from
     * the TableModel (rows are added by another window control). Thus,
     * this method returns false.
     */

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex > 0)
            return true;
        else return false;
    }

    // Because the sample does not allow users to edit any cells from the
    // TableModel, the following methods, setValueAt, addTableModelListener,
    // and removeTableModelListener, do not need to be implemented.

    public void setValueAt(Object value, int row, int column) {
        System.out.println("Calling setValueAt row " + row + ", column " + column);
        try {
            //get the value  and update cacheRowSet
            this.rowSet.absolute( row + 1 );
            this.rowSet.updateObject((column + 1), value);
            this.rowSet.updateRow();
        } catch (SQLException e) {

        }
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public CachedRowSet getRowSet() {
        return rowSet;
    }

    public RowOperationsDispatcher getDispatcher() {
        return dispatcher;
    }
}
