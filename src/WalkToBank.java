
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Tile;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class WalkToBank extends Node {

    public final Tile[] path = {
        new Tile(3097, 3509, 0), new Tile(3102, 3510, 0),
        new Tile(3102, 3504, 0), new Tile(3100, 3500, 0),
        new Tile(3096, 3497, 0)
    };

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && Inventory.isFull() && Players.getLocal().isIdle() && !Banking.playerAtBank()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        Walking.newTilePath(path).traverse();
        Task.sleep(3000);
    }
    
    @Override
    public String toString() {
        return "Walking to bank";
    }
}
