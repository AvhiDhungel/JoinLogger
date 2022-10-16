package com.github.AvhiDh;

import com.github.AvhiDh.SqlUtilities.SqlDatabaseConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    private Runnable reloadConfig;

    public Commands(Runnable reloadConfig) {
        this.reloadConfig = reloadConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        reloadConfig.run();
        return true;
    }

}
