package subd.ui.util;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;
import java.util.List;

public interface RowOperationsDispatcher {
    boolean dispatchInsertRow(CachedRowSet rowSet, List<String> fields) throws SQLException;
}
