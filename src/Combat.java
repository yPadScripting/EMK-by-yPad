

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class Combat extends Node {

    private static Tile lootTile;
    
    @Override
    public boolean activate() {
        if (Players.getLocal() != null && Players.getLocal().getInteracting() == null && Players.getLocal().isIdle() && Inventory.getCount() < 28 && WalkToHouse.playerAtHouse() == true) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public void execute() {
        final NPC man = NPCs.getNearest(Variables.NPC_FILTER);
        final Player p = Players.getLocal();

        if (p != null && man != null) {
            if (man.isOnScreen()) {
                if (Random.nextInt(0, 7) % 2 == 0) {
                    if (man.click(false) && Menu.isOpen() && Menu.contains("Attack")) {
                        Menu.select("Attack");
                        Task.sleep(1000);
                    }
                } else {
                    man.interact("Attack", "Man");
                    Task.sleep(1000);
                }
            } else {
                Camera.turnTo(man);
            }
        }
        
        if (man.getHealthPercent() <= 0) {
            setLootTile(man.getLocation());
        }
        Task.sleep(1500);
    }
    
    @Override
    public String toString() {
        return "Engaging combat";
    }
    
    public static Tile setLootTile(Tile tile) {
        lootTile = tile;
        return lootTile;
    }
    
    public static Tile getLootTile() {
        return lootTile;
    }

}
