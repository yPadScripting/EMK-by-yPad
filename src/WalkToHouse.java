
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Tile;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class WalkToHouse extends Node {

    public final Tile[] path = {
        new Tile(3090, 3499, 0), new Tile(3089, 3487, 0),
        new Tile(3098, 3487, 0), new Tile(3099, 3499, 0),
        new Tile(3095, 3496, 0), new Tile(3101, 3498, 0),
        new Tile(3102, 3504, 0), new Tile(3102, 3509, 0),
        new Tile(3096, 3510, 0)
    };
    public final static Area area = new Area(new Tile[]{
                new Tile(3087, 3516, 0), new Tile(3088, 3503, 0),
                new Tile(3103, 3502, 0), new Tile(3106, 3502, 0),
                new Tile(3105, 3517, 0), new Tile(3087, 3519, 0),
                new Tile(3104, 3518, 0), new Tile(3087, 3519, 0)
            });

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && playerAtHouse() != true && !Inventory.isFull()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        System.out.println("Tile found...");
        Walking.newTilePath(path).traverse();
        Task.sleep(3000);
    }

    @Override
    public String toString() {
        return "Walking to house";
    }
    public static boolean playerAtHouse() {
        if (area.contains(Players.getLocal().getLocation())) {
            return true;
        } else {
            return false;
        }
    }
}
