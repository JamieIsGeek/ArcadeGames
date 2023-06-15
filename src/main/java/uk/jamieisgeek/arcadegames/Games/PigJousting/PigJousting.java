package uk.jamieisgeek.arcadegames.Games.PigJousting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import uk.jamieisgeek.arcadegames.ArcadeGames;
import uk.jamieisgeek.arcadegames.Games.Helpers.State;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class PigJousting {
    private Set<UUID> players;
    private Map<UUID, Pig> pigs = new HashMap<UUID, Pig>();
    private State gameState;
    private final ArcadeGames plugin = ArcadeGames.getInstance();
    public PigJousting(Set<UUID> players) {
        this.players = players;
        this.gameState = State.WAITING;
        ArcadeGames.addPigJoustingGame(this);
    }

    private boolean canGameStart() {
        return players.size() >=2 && gameState == State.WAITING;
    }

    public void start() {
        if(!canGameStart()) {
            return;
        }

        ArcadeGames.Log("Starting Pig Jousting!");

        setGameState(State.STARTING);
        players.forEach(player -> {
            Player playerObj = Bukkit.getPlayer(player);
            playerObj.teleport(new Location(
                    Bukkit.getWorld(plugin.getConfigHandler().getFromConfig("pigjousting.world").toString()),
                    (double) plugin.getConfigHandler().getFromConfig("pigjousting.x"),
                    (double) plugin.getConfigHandler().getFromConfig("pigjousting.y"),
                    (double) plugin.getConfigHandler().getFromConfig("pigjousting.z")
            ));

            Pig pig = playerObj.getWorld().spawn(playerObj.getLocation(), Pig.class);
            pig.setAI(false);
            pig.setInvulnerable(true);
            pig.setGravity(false);
            pig.setSilent(true);
            pig.setSaddle(true);
            pig.addPassenger(playerObj);

            addPig(player, pig);
        });
        setGameState(State.RUNNING);
    }

    public void stop() {
        if(getGameState() != State.RUNNING) {
            return;
        }

        ArcadeGames.Log("Stopping Pig Jousting!");

        setGameState(State.STOPPING);
        Location defaultSpawn = Bukkit.getWorld((String) ArcadeGames.getInstance().getConfigHandler().getFromConfig("default.world")).getSpawnLocation();
        defaultSpawn.getChunk().load();

        ArcadeGames.Log(Level.WARNING, defaultSpawn.toString());
        players.forEach(player -> {
            Player playerObj = Bukkit.getPlayer(player);
            playerObj.teleport(defaultSpawn);

            playerObj.getInventory().clear();
        });

        getPigs().forEach((uuid, pig) -> pig.damage(2000.0));
        setGameState(State.WAITING);
    }

    public void setGameState(State state) {
        gameState = state;
    }

    public State getGameState() {
        return gameState;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(Set<UUID> players) {
        this.players = players;
    }

    public Map<UUID, Pig> getPigs() {
        return pigs;
    }

    public void addPig(UUID uuid, Pig pig) {
        pigs.put(uuid, pig);
    }
}