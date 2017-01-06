package subd.ui;

import com.sun.rowset.CachedRowSetImpl;
import subd.ui.util.JDBCUtil;

import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 21.04.13
 * Time: 16:53
 * To change this template use File | Settings | File Templates.
 */
public class SQLFrame extends JFrame {
    protected String sqlConnectionUrl;
    protected String user;
    protected String password;
    protected Connection connection;
    public SQLFrame(){}
    public SQLFrame(String connectionUrl, String user, String password) {
        this.user = user;
        this.password = password;
        this.sqlConnectionUrl = connectionUrl;
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    JDBCUtil.printSQLException(sqle);
                }
            }
        });
    }

    protected CachedRowSet getRowSet(String query) throws SQLException {
        connection = getConnection();

        CachedRowSet rowSet = new CachedRowSetImpl();
        rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
        rowSet.setConcurrency(ResultSet.CONCUR_UPDATABLE);
        rowSet.setUsername(user);
        rowSet.setPassword(password);
        rowSet.setUrl(sqlConnectionUrl);
        rowSet.setCommand(query);
        rowSet.execute(connection);
        return rowSet;
    }


    protected Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.put("user", user);
        props.put("password", password);
        return DriverManager.getConnection(sqlConnectionUrl, props);
    }
}
