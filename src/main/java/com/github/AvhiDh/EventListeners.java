package com.github.AvhiDh;

import com.github.AvhiDh.SqlUtilities.SqlDataReader;
import com.github.AvhiDh.SqlUtilities.SqlDatabaseConnection;
import com.github.AvhiDh.SqlUtilities.SqlQueryBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalDateTime;

public class EventListeners implements Listener {

    private SqlDatabaseConnection conn;

    public EventListeners(SqlDatabaseConnection conn) {
        this.conn = conn;
    }

    public void setConnection(SqlDatabaseConnection conn) {
        this.conn = conn;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();
        String firstLogin = getPlayerFirstLogin(pl.getUniqueId().toString());

        SqlQueryBuilder sql = new SqlQueryBuilder();
        sql.appendLine("REPLACE INTO av_tblPlayerJoin ");
        sql.appendLine("(fldPlayerId, fldPlayerName, fldFirstLogin, fldLastLogin) ");
        sql.appendLine("VALUES( ");
        sql.appendLine("  '%s' ", pl.getUniqueId().toString());
        sql.appendLine(", '%s' ", pl.getName());
        sql.appendLine(", '%s' ", firstLogin);
        sql.appendLine(", '%s' ", LocalDateTime.now().toString());
        sql.appendLine(")");
        conn.executeNonQuery(sql);
    }

    private String getPlayerFirstLogin(String id) {
        SqlQueryBuilder sql = new SqlQueryBuilder();
        sql.appendLine("SELECT fldFirstLogin FROM av_tblPlayerJoin ");
        sql.appendLine("WHERE fldPlayerId = '%s' ", id);

        String d;
        SqlDataReader dr = conn.execute(sql);

        if (dr.read()) {
            d = dr.getString("fldFirstLogin");
        } else {
            d = LocalDateTime.now().toString();
        }

        return d;
    }


}
