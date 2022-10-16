package com.github.AvhiDh;

import com.github.AvhiDh.SqlUtilities.SqlDatabaseConnection;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

    private Runnable reloadConfig;
    private ExportHelper exporter;
    private static String prefix = String.format("%s[%sJoinLogger%s] %s",
            ChatColor.GRAY, ChatColor.GREEN, ChatColor.GRAY, ChatColor.YELLOW);

    public Commands(Runnable reloadConfig, ExportHelper exporter) {
        this.reloadConfig = reloadConfig;
        this.exporter = exporter;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sendUsageMessage(sender);
        } else {
            String arg = args[0].toLowerCase();

            if (arg.length() == 0) {
                sendUsageMessage(sender);
            } else if (arg.equals("reload")) {
                reloadConfig.run();
                SendMessage(sender, "Config Reloaded!");
            } else if (arg.equals("export")) {
                if (exporter.exportToCSV()) {
                    SendMessage(sender, "Date Exported to CSV!");
                } else {
                    SendMessage(sender, ChatColor.RED + "Encountered an error while attempting to export");
                }
            } else {
                sendUsageMessage(sender);
            }

        }

        return true;
    }

    private void sendUsageMessage(CommandSender sender) {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage:\n");
        sb.append("/jl reload");
        sb.append("/jl export");
        SendMessage(sender, sb.toString());
    }

    private void SendMessage(CommandSender s, String msg) {
        s.sendMessage(prefix + msg);
    }

}
