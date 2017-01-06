package subd.ui.util;

import javax.swing.*;
import java.util.EnumMap;

public class QueriesStorage {
    private static EnumMap<QueryType, QueryBuilder> queries = new EnumMap<QueryType, QueryBuilder>(QueryType.class);

    static {

        QueryBuilder sELECT1 = new QueryBuilder() {
            private String name = "";
            public String createQuery() {
                return
                        "SELECT BAND.* FROM BAND " +
                                "INNER JOIN BAND_MEMBERS ON BAND.ID_BAND = BAND_MEMBERS.ID_BAND AND BAND_MEMBERS.ID_MUSICIAN = "+
                                "(SELECT MUSICIAN.ID_MUSICIAN FROM MUSICIAN WHERE NAME_RU = '"+name +"' OR NAME_EN = '"+name +"')";

            }

            public boolean satisfyQueryParameters() {
                name = JOptionPane.showInputDialog(null, "Enter musician name: ", "Oracle", 1);
                return true;
            }
        };
        QueryBuilder sELECT2 = new QueryBuilder() {
            private String name = "";
            public String createQuery() {
                return
                        "SELECT ALBUM.* FROM ALBUM "+
                                "INNER JOIN ALBUM_INVITED_MUSICIANS ON ALBUM.ID_ALBUM = ALBUM_INVITED_MUSICIANS.ID_ALBUM "+
                                " AND ALBUM_INVITED_MUSICIANS.ID_MUSICIAN = (SELECT MUSICIAN.ID_MUSICIAN FROM MUSICIAN "+
                                "WHERE NAME_RU = '"+name+"' OR NAME_EN = '"+name+"')";

            }

            public boolean satisfyQueryParameters() {
                name = JOptionPane.showInputDialog(null, "Enter musician name: ", "Oracle", 1);
                return true;
            }
        };
        QueryBuilder sELECT3 = new QueryBuilder() {
            public String createQuery() {
                return
                        "SELECT * FROM BAND WHERE (SELECT COUNT(*) "+
                                "FROM BAND_MEMBERS where BAND_MEMBERS.ID_BAND = BAND.ID_BAND "+
                                "AND BAND_MEMBERS.DATE_VALUE > BAND.CARIER_START ) = 0";

            }

            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder sELECT4 = new QueryBuilder() {
            public String createQuery() {
                return
                        "SELECT * FROM ALBUM WHERE RELEASE_YEAR BETWEEN 2016 AND 2016";

            }

            public boolean satisfyQueryParameters() {
                return true;
            }
        };

        QueryBuilder disableTrigger1 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_album_year DISABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder enableTrigger1 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_album_year ENABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder disableTrigger2 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_drum_count DISABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder enableTrigger2 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_drum_count ENABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder disableTrigger3 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_album_count DISABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };
        QueryBuilder enableTrigger3 = new QueryBuilder() {
            public String createQuery() {
                return
                        "ALTER TRIGGER band_album_count ENABLE";
            }
            public boolean satisfyQueryParameters() {
                return true;
            }
        };





        queries.put(QueryType.DISABLE_TRIGGER1, disableTrigger1);
        queries.put(QueryType.ENABLE_TRIGGER1, enableTrigger1);
        queries.put(QueryType.DISABLE_TRIGGER2, disableTrigger2);
        queries.put(QueryType.ENABLE_TRIGGER2, enableTrigger2);
        queries.put(QueryType.DISABLE_TRIGGER3, disableTrigger3);
        queries.put(QueryType.ENABLE_TRIGGER3, enableTrigger3);
        queries.put(QueryType.SELECT1, sELECT1);
        queries.put(QueryType.SELECT2, sELECT2);
        queries.put(QueryType.SELECT3, sELECT3);
        queries.put(QueryType.SELECT4, sELECT4);
    }

    public static String getQuery(QueryType type) {
        if(type == null) {
            return null;
        }
        QueryBuilder qb = queries.get(type);
        if(qb == null) {
            return null;
        } else if(qb.satisfyQueryParameters()) {
            return qb.createQuery();
        }
        return null;
    }

}
