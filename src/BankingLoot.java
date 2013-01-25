package EMK;

import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class BankingLoot extends Node {

    public final static Area area = new Area(new Tile[]{
                new Tile(3089, 3500, 0), new Tile(3099, 3500, 0),
                new Tile(3098, 3487, 0), new Tile(3090, 3487, 0)
            });
    public final static int[] BANK_IDS = {42377, 42378};

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && Inventory.isFull() && playerAtBank() && !Bank.isOpen()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        final SceneObject bankBooth = SceneEntities.getNearest(BANK_IDS);
        if (bankBooth.isOnScreen() && !Widgets.get(762).validate() && !Bank.isOpen()) {
            bankBooth.interact("Bank");
            Task.sleep(2000);
            Bank.depositInventory();
            Bank.close();
            Task.sleep(1000);

        }
    }
    
    @Override
    public String toString() {
        return "Banking";
    }

    public static boolean playerAtBank() {
        if (area.contains(Players.getLocal().getLocation())) {
            return true;
        } else {
            return false;
        }
    }
}
