package uk.jamieisgeek.arcadegames.Commands.AdminCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.jamieisgeek.arcadegames.ArcadeGames;
import uk.jamieisgeek.arcadegames.Games.PigJousting.PigJousting;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class PigJoustingAdmin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            ArcadeGames.Log(Level.WARNING, "Only players can use this command!");
            return true;
        }

        if(!player.hasPermission("arcadegames.admin.pigjousting")) {
            ArcadeGames.Log(Level.WARNING, "You do not have permission to use this command!");
            return true;
        }

        if(args.length == 0) {
            ArcadeGames.Log(Level.WARNING, "You must specify a subcommand!");
            return true;
        }

        switch(args[0].toLowerCase()) {
            case "start":
                Set<UUID> players = new HashSet<>();
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                    players.add(onlinePlayer.getUniqueId());
                });
                PigJousting pigJousting = new PigJousting(players);
                pigJousting.setPlayers(players);
                pigJousting.start();
                break;
            case "stop":
                ArcadeGames.getPigJousting().stop();
                break;
            default:
                ArcadeGames.Log(Level.WARNING, "Invalid subcommand!");
                break;
        }

        return true;
    }
}
