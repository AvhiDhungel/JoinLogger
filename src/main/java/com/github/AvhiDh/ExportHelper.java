package com.github.AvhiDh;

import com.github.AvhiDh.SqlUtilities.SqlDataReader;
import com.github.AvhiDh.SqlUtilities.SqlDatabaseConnection;
import com.github.AvhiDh.SqlUtilities.SqlQueryBuilder;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExportHelper {

    private SqlDatabaseConnection conn;

    public ExportHelper(SqlDatabaseConnection conn) {
        this.conn = conn;
    }

    public Boolean exportToCSV() {
        try {
            String path = "plugins\\JoinLogger\\Exports\\";
            String filename = "JoinLogger-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-ddHHmmss"));

            File f = new File(path + filename + ".csv");
            f.getParentFile().mkdir();

            FileWriter fw = new FileWriter(f);
            fw.append("PlayerName, FirstLogin, LastLogin\n");

            SqlQueryBuilder sql = new SqlQueryBuilder();
            sql.appendLine("SELECT * FROM av_tblPlayerJoin");

            SqlDataReader dr = conn.execute(sql);
            Boolean isFirst = true;
            while (dr.read()) {
                if (!isFirst) { fw.append("\n"); } else { isFirst = false; }
                fw.append(dr.getString("fldPlayerName") + ",");
                fw.append(dr.getString("fldFirstLogin") + ",");
                fw.append(dr.getString("fldLastLogin"));
            }

            fw.flush();
            fw.close();

            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

}
