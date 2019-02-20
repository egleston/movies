package com.kamash.ccb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SqlHelper {
    protected static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(java.lang.invoke.MethodHandles.lookup().lookupClass());

    public static JSONObject rsToJson(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        JSONObject obj = new JSONObject();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String col = metaData.getColumnName(i);
            obj.put(col, rs.getObject(col));
        }
        return obj;
    }

    public static JSONArray queryArray(String sql, List<Object> varList) throws SQLException {
        logger.info("queryArray");
        logger.info(" - " + sql);
        logger.info(" - " + varList);
        Connection conn = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < varList.size(); i++) {
            stmt.setObject(i+1, varList.get(i));
        }
        ResultSet rs = stmt.executeQuery();

        JSONArray arr = new JSONArray();
        while (rs.next()) {
            arr.put(rsToJson(rs));
        }
        return arr;
    }

    public static JSONObject queryObject(String sql, List<Object> varList) throws SQLException {
        Connection conn = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        for (int i = 0; i < varList.size(); i++) {
            stmt.setObject(i+1, varList.get(i));
        }
        ResultSet rs = stmt.executeQuery();

        rs.next();
        return rsToJson(rs);
    }
}
