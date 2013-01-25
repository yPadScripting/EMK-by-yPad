package EMK;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.wrappers.node.GroundItem;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
public class Loot extends Node {

    private static double totalLoot = 0;

    @Override
    public boolean activate() {
        if (Players.getLocal() != null && Players.getLocal().getInteracting() == null && !Inventory.isFull() && WalkToHouse.playerAtHouse()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void execute() {
        final GroundItem item = GroundItems.getNearest(Variables.GROUND_FILTER);
        int invcount = Inventory.getCount();
        if (Players.getLocal() != null && item != null) {
            while (item.validate() && item.isOnScreen()) {
                if (item.isOnScreen() && item.getLocation().distanceTo() < 2) {
                    if (item.click(false) && Menu.isOpen() && Menu.contains("Take")) {
                        Menu.select("Take", item.getGroundItem().getName());
                        Task.sleep(150);
                    } else {
                        item.interact("Take", item.getGroundItem().getName());
                        Task.sleep(150);
                    }
                } else {
                    Walking.walk(item.getLocation());
                    Camera.turnTo(item);
                }
            }
            if (Inventory.getCount() == invcount + 1) {
                if (item.getId() != 995) {
                    totalLoot += getPrice(item.getId());
                }
            } else {
                totalLoot += item.getGroundItem().getStackSize();

            }
            Task.sleep(450);
        }
    }

    @Override
    public String toString() {
        return "Looting";
    }

    public static int getPrice(int itemID) {
        try {
            final URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + itemID);
            URLConnection con = url.openConnection();
            final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                line += inputLine;
                if (inputLine.contains("mark_price")) {
                    line = line.substring(line.indexOf("mark_price\":\"")
                            + "mark_price\":\"".length());
                    line = line.substring(0, line.indexOf("\""));
                    return Integer.parseInt(line.replaceAll(",", ""));
                }
            }
            in.close();
        } catch (Exception e) {
        }
        return -1;
    }

    public static double getTotalLoot() {
        return totalLoot;
    }
}
