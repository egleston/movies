package com.kamash.ccb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Set;
import java.util.TreeSet;

public class SqlHelper {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public static JSONArray doQuery(String sql) throws SQLException {
        JSONArray  arr = new JSONArray();
        Connection conn = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        Set<String> columnNames = new TreeSet<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int numColumns = metaData.getColumnCount();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            columnNames.add(metaData.getColumnName(i));
        }

        while (rs.next()) {
            JSONObject row = new JSONObject();
            for (String col : columnNames) {
                row.put(col, rs.getObject(col));
            }
            arr.put(row);
        }
        return arr;
    }
}
