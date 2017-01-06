package subd.ui;

import subd.ui.util.JDBCUtil;
import subd.ui.util.QueriesStorage;
import subd.ui.util.QueryType;
import subd.ui.util.RowOperationsDispatcher;

import javax.sql.rowset.CachedRowSet;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Lenovo
 * Date: 21.04.13
 * Time: 12:50
 * To change this template use File | Settings | File Templates.
 */
public class ControlFrame extends JFrame {
    private Properties connectionProperties = null;
    private String urlString = null;
    private String user = null;
    private String pass = null;
    private SimpleDateFormat strDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    private SimpleDateFormat strDate2 = new SimpleDateFormat("yyyy-MM-dd");
    private boolean triggerFlag1 = true;
    private boolean triggerFlag2 = true;
    private boolean triggerFlag3 = true;

    public ControlFrame() {
        getLocale().setDefault(Locale.ENGLISH);
        setConnectionProperties();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenu menuTables = new JMenu("View tables");
        JMenu menuQueries = new JMenu("Exec queries");
        JMenu menuTriggers = new JMenu("Triggers");
        JMenuItem menuItemExit = new JMenuItem("Exit");

        JMenuItem menuItem1 = new JMenuItem("Album");
        JMenuItem menuItem2 = new JMenuItem("Album_invited_musicians");
        JMenuItem menuItem3 = new JMenuItem("Band");
        JMenuItem menuItem4 = new JMenuItem("Band_members");
        JMenuItem menuItem5 = new JMenuItem( "Musician");
        JMenuItem menuItem6 = new JMenuItem( "Composition");
        JMenuItem menuItem7 = new JMenuItem( "Composition_dupation");


        JMenuItem menuItemQuery1_1 = new JMenuItem("query 1_1");
        JMenuItem menuItemQuery1_2 = new JMenuItem("query 1_2");
        JMenuItem menuItemQuery2 = new JMenuItem("query 2");
        JMenuItem menuItemQuery3 = new JMenuItem("query 3");

        JMenuItem menuItemTrigger1 = new JMenuItem("Disable trigger 1");
        JMenuItem menuItemTrigger2 = new JMenuItem("Disable trigger 2");
        JMenuItem menuItemTrigger3 = new JMenuItem("Disable trigger 3");

        menuTables.add(menuItem1);
        menuTables.add(menuItem2);
        menuTables.add(menuItem3);
        menuTables.add(menuItem4);
        menuTables.add(menuItem5);
        menuTables.add(menuItem6);
        menuTables.add(menuItem7);



        menuQueries.add(menuItemQuery1_1);
        menuQueries.add(menuItemQuery1_2);
        menuQueries.add(menuItemQuery2);
        menuQueries.add(menuItemQuery3);

        menuTriggers.add(menuItemTrigger1);
        menuTriggers.add(menuItemTrigger2);
        menuTriggers.add(menuItemTrigger3);


        menu.add(menuTables);
        menu.add(menuQueries);
        menu.add(menuTriggers);
        menu.add(menuItemExit);

        menuBar.add(menu);
        setJMenuBar(menuBar);

        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        menuItemTrigger1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "";
                String message = "Trigger CHECK_YEAR";
                if(triggerFlag1) {
                    query = QueriesStorage.getQuery(QueryType.DISABLE_TRIGGER1);
                    message += " disabled";
                }
                else{
                    query = QueriesStorage.getQuery(QueryType.ENABLE_TRIGGER1);
                    message += " enabled";
                }
                try {
                    Properties props = new Properties();
                    props.put("user", user);
                    props.put("password", pass);
                    Connection con = DriverManager.getConnection(urlString, props);
                    con.createStatement().execute(query);

                    JOptionPane.showMessageDialog(null, message);
                    triggerFlag1 = !triggerFlag1;
                    menuItemTrigger1.setText(triggerFlag1 ? "Disable Trigger1" : "Enable Trigger1");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        menuItemTrigger2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "";
                String message = "Trigger DRUM";
                if(triggerFlag2) {
                    query = QueriesStorage.getQuery(QueryType.DISABLE_TRIGGER2);
                    message += " disabled";
                }
                else{
                    query = QueriesStorage.getQuery(QueryType.ENABLE_TRIGGER2);
                    message += " enabled";
                }
                try {
                    Properties props = new Properties();
                    props.put("user", user);
                    props.put("password", pass);
                    Connection con = DriverManager.getConnection(urlString, props);
                    con.createStatement().execute(query);

                    JOptionPane.showMessageDialog(null, message);
                    triggerFlag2 = !triggerFlag2;
                    menuItemTrigger2.setText(triggerFlag2 ? "Disable Trigger2" : "Enable Trigger2");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        menuItemTrigger3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = "";
                String message = "Trigger CHECK_ALBUM";
                if(triggerFlag3) {
                    query = QueriesStorage.getQuery(QueryType.DISABLE_TRIGGER3);
                    message += " disabled";
                }
                else{
                    query = QueriesStorage.getQuery(QueryType.ENABLE_TRIGGER3);
                    message += " enabled";
                }
                try {
                    Properties props = new Properties();
                    props.put("user", user);
                    props.put("password", pass);
                    Connection con = DriverManager.getConnection(urlString, props);
                    con.createStatement().execute(query);

                    JOptionPane.showMessageDialog(null, message);
                    triggerFlag3 = !triggerFlag3;
                    menuItemTrigger3.setText(triggerFlag3 ? "Disable Trigger3" : "Enable Trigger3");
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        });

        menuItemQuery1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = QueriesStorage.getQuery(QueryType.SELECT1);
                    QueryFrame fr = new QueryFrame(urlString, user, pass, query);
                    fr.setTitle("get bands for musician...");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItemQuery1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = QueriesStorage.getQuery(QueryType.SELECT2);
                    QueryFrame fr = new QueryFrame(urlString, user, pass, query);
                    fr.setTitle("Get albums for which musiacian was invited...");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItemQuery2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = QueriesStorage.getQuery(QueryType.SELECT3);
                    QueryFrame fr = new QueryFrame(urlString, user, pass, query);
                    fr.setTitle("Have not changed group members");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItemQuery3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = QueriesStorage.getQuery(QueryType.SELECT4);
                    QueryFrame fr = new QueryFrame(urlString, user, pass, query);
                    fr.setTitle("get albums for group for last year");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });


        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from album";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_album", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("author_id", Integer.parseInt(fields.get(2)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for AUTHOR_ID field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("release_year", Integer.parseInt(fields.get(3)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for RELEASE_YEAR field");
                                return false;
                            }

                            rowSet.updateString("name", fields.get(1));
                            rowSet.updateString("album_nature", fields.get(4));
                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("Album table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from album_invited_musicians";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_album", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_ALBUM field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("id_musician", Integer.parseInt(fields.get(1)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_MUSICIAN field");
                                return false;
                            }
                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("Album_invited_musicians table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from band";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_band", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_BAND field");
                                return false;
                            }

                            rowSet.updateString("name", fields.get(1));
                            try {
                                rowSet.updateInt("carier_start", Integer.parseInt(fields.get(2)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for CARIER_START field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("carier_end", Integer.parseInt(fields.get(3)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for CARIER_END field");
                                return false;
                            }

                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("BAND table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from band_members";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_musician", Integer.parseInt(fields.get(0)));
                                rowSet.updateInt("id_band", Integer.parseInt(fields.get(1)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID fields");
                                return false;
                            }
                            if(fields.get(2) == "entered" || fields.get(2) == "leaved"){
                                rowSet.updateString("data_type", fields.get(2));
                            }else{
                                JOptionPane.showMessageDialog(null, "Wrong number format for DATE_TYPE field");
                                return false;
                            }
                            try{
                                rowSet.updateInt("date_value", Integer.parseInt(fields.get(3)));
                            }catch(NumberFormatException exc){
                                JOptionPane.showMessageDialog(null, "Wrong number format for DATE_VALUE field");
                                return false;
                            }
                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("BAND_MEMBERS table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });

        menuItem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from musician";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_musician", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for id_musician field");
                                return false;
                            }

                            rowSet.updateString("name_ru", fields.get(1));
                            rowSet.updateString("name_en", fields.get(2));
                            rowSet.updateString("specialization", fields.get(3));

                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("MUSICIAN table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });



        menuItem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from composition";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_composition", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_COMPOSITION field");
                                return false;
                            }

                            rowSet.updateString("name", fields.get(1));
                            try {
                                rowSet.updateInt("author_id", Integer.parseInt(fields.get(2)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for AUTHOR_ID field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("performer_id", Integer.parseInt(fields.get(3)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for PERFORMER_ID field");
                                return false;
                            }

                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("Composition table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });
        menuItem7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String query = "select * from composition_duration";
                    RowOperationsDispatcher d = new RowOperationsDispatcher() {
                        public boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException {
                            try {
                                rowSet.updateInt("id_composition", Integer.parseInt(fields.get(0)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_COMPOSITION field");
                                return false;
                            }
                            try {
                                rowSet.updateInt("id_album", Integer.parseInt(fields.get(1)));
                            } catch (NumberFormatException exc) {
                                JOptionPane.showMessageDialog(null, "Wrong number format for ID_ALBUM field");
                                return false;
                            }
                            rowSet.updateString("duration", fields.get(2));

                            return true;
                        }
                    };
                    TableFrame fr = new TableFrame(urlString, user, pass, query, d);
                    fr.setTitle("BAND table");
                    fr.setVisible(true);
                } catch (SQLException ex) {
                    JDBCUtil.printSQLException(ex);
                }
            }
        });
    }

    public void setConnectionProperties() {
        connectionProperties = new Properties();
        try {
            connectionProperties.load(new BufferedReader(new FileReader("config/db.properties")));
            String host = (String) connectionProperties.get("conn.server");
            String port = (String) connectionProperties.get("conn.port");
            String dbnm = (String) connectionProperties.get("conn.dbname");

            this.user = (String) connectionProperties.get("conn.user");
            this.pass = (String) connectionProperties.get("conn.pass");
            this.urlString = String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, dbnm);
        } catch (Exception e) {
            System.err.println("#### ERROR: config/db.properties read exception");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
