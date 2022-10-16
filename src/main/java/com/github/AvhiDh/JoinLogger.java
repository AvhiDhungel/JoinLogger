package com.github.AvhiDh;

import com.github.AvhiDh.SqlUtilities.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class JoinLogger extends JavaPlugin {

    private FileConfiguration config = getConfig();
    private SqlDatabaseConnection conn;
    private EventListeners l;

    @Override
    public void onEnable() {
        setupConfig();
        setupDB();
        setupListeners();
        setupCommands();
    }

    @Override
    public void onDisable() { conn.close(); }

    private void setupConfig() {
        config.addDefault("sql.url", "localhost");
        config.addDefault("sql.database", "dbname");
        config.addDefault("sql.user", "username");
        config.addDefault("sql.password", "passwordhere");

        config.options().copyDefaults(true);
        saveConfig();
    }

    private void setupDB() {
        String url = config.getString("sql.url");
        String db = config.getString("sql.database");
        String user = config.getString("sql.user");
        String password = config.getString("sql.password");

        if (password.equals("passwordhere")) { return; }
        if (conn != null) { conn.close(); }

        conn = new SqlDatabaseConnection(url, db, user, password);
        initDatabase();
    }

    private void setupListeners() {
        l = new EventListeners(conn);
        getServer().getPluginManager().registerEvents(l, this);
    }

    private void initDatabase() {
        SqlQueryBuilder sql = new SqlQueryBuilder();

        sql.appendLine("CREATE TABLE IF NOT EXISTS av_tblPlayerJoin (");
        sql.appendLine("    fldPlayerId VARCHAR(36) NOT NULL, ");
        sql.appendLine("    fldPlayerName VARCHAR(50) NOT NULL, ");
        sql.appendLine("    fldFirstLogin DATETIME NOT NULL, ");
        sql.appendLine("    fldLastLogin DATETIME NOT NULL, ");
        sql.appendLine("    PRIMARY KEY (fldPlayerId) ");
        sql.appendLine(");");

        conn.executeNonQuery(sql);
    }

    private void setupCommands() {
        Runnable reloadConfig = new Runnable() {
            @Override
            public void run() { reload(); }
        };

        PluginCommand command = this.getCommand("jlreload");
        command.setExecutor(new Commands(reloadConfig));
    }

    private void reload() {
        reloadConfig();
        config = getConfig();
        setupDB();
        l.setConnection(conn);
    }

}
