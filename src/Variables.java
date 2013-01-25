

import java.util.ArrayList;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.widget.WidgetChild;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class Variables {

    public final static int[] MAN_IDS = {1, 2, 3};
   // public static int[] LOOT_IDS = new int[15]; //= {526, 995, 2485, 3049, 217, 211, 207, 1440};
    public static ArrayList<Integer> LOOT_IDS = new ArrayList<Integer>();
    public final static int[] ABILITY_IDS = {0, 35, 39, 42, 45, 48, 51, 54, 57, 60};
    public static Filter<WidgetChild> WIDGET_FILTER = new Filter<WidgetChild>() {

        @Override
        public boolean accept(WidgetChild t) {
            for (int i : ABILITY_IDS) {
                if (t.getId() == i && Widgets.get(640).validate()) {
                    return true;
                }
            }
            return false;
        }
    };
    public static Filter<NPC> NPC_FILTER = new Filter<NPC>() {

        @Override
        public boolean accept(NPC n) {
            for (int i : MAN_IDS) {
                if (n.getId() == i && n.getLocation().canReach()) {
                    return true;
                }
            }
            return false;
        }
    };
    public static Filter<GroundItem> GROUND_FILTER = new Filter<GroundItem>() {

        @Override
        public boolean accept(GroundItem item) {
            for (int i : LOOT_IDS) {
                if (item.getId() == i && item.getLocation().canReach()) {
                    return true;
                }
            }
            return false;
        }
    };

    public Variables() {
    }
}
