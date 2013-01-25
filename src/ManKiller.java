package EMK;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.core.script.ActiveScript;
import org.powerbot.core.script.job.Task;
import org.powerbot.core.script.job.state.Node;
import org.powerbot.core.script.job.state.Tree;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Timer;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author PimGame
 */
@Manifest(authors = {"yPad Scripting"}, name = "Edgeville Man Killer", description = "Kills men in Edgeville, picking up their loots and banking it.")
public class ManKiller extends ActiveScript implements PaintListener, MouseListener {

    private Tree container = null;
    private List<Node> jobs = new ArrayList<Node>();
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
    boolean hide = false;
    boolean guiWait = true;
    String currentStatus = "";
    Image background = getImage("http://i48.tinypic.com/fd7sc6.jpg");
    Image hidebuttonenabled = getImage("http://i50.tinypic.com/a08p4x.jpg");
    Image hidebuttondisabled = getImage("http://i50.tinypic.com/16i6wz4.jpg");
    Rectangle hidebutton = new Rectangle(497, 395, 20, 20);
    Point p;
    Font font1 = new Font("Verdana", 0, 20);
    String version = "1.0";
    String runes = "0";
    String runesPerHour = "0";
    Timer runTime = new Timer(0);
    GUI g = new GUI();

    public void onStart() {
        System.out.println("Welcome to the Edgeville Man Killer!");
        Camera.setPitch(1000);

        g.setVisible(true);
    }

    @Override
    public void onStop() {
        g.dispose();
        System.out.println("Thanks for using my Edgeville Man Killer!");
    }

    @Override
    public int loop() {
        while (guiWait) {
            sleep(500);
        }
        if (container != null) {
            final Node job = container.state();

            if (job != null) {
                currentStatus = job.toString();
                container.set(job);
                getContainer().submit(job);
                job.join();

            }
        } else {
            jobs.add(new Combat());
            jobs.add(new Loot());
            jobs.add(new WalkToBank());
            jobs.add(new BankingLoot());
            jobs.add(new WalkToBank());
            jobs.add(new WalkToHouse());
            container = new Tree(jobs.toArray(new Node[jobs.size()]));
        }
        return Random.nextInt(150, 250);
    }

//Method to import
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        p = e.getPoint();
        if (hidebutton.contains(p) && !hide) {
            hide = true;
        } else if (hidebutton.contains(p) && hide) {
            hide = false;
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @SuppressWarnings("serial")
    private class MousePathPoint extends Point { // All credits to Enfilade

        private long finishTime;
        private double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }
        
    }

    @Override
    public void onRepaint(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;

        if (Game.getClientState() == 11) {
            if (!hide) {
                g.drawImage(background, 7, 395, null);
                g.setColor(Color.BLACK);
                g.setFont(font1);
                g.drawString(runTime.toElapsedString(), 330, 430);

                g.drawString("Status: " + "\n" + currentStatus, 7, 365);
                g.drawImage(hidebuttondisabled, 497, 395, null);

                double time = (double) runTime.getElapsed();
                double loot = (double) Loot.getTotalLoot();
                DecimalFormat df = new DecimalFormat("#,###,###");

                g.drawString(df.format((3600000 / time) * loot), 300, 502);
                g.drawString(df.format(Loot.getTotalLoot()), 330, 465);
            }
            if (hide) {
                g.drawImage(hidebuttonenabled, 497, 395, null);
            }
        }

        //Mouse cursor
        g.setColor(Color.YELLOW);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() - 5, Mouse.getX() + 5, Mouse.getY() + 5);
        g.drawLine(Mouse.getX() - 5, Mouse.getY() + 5, Mouse.getX() + 5, Mouse.getY() - 5);

        //Mouse trail
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = Mouse.getLocation();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
                200); //Lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        MousePathPoint lastPoint = null;
        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(Color.YELLOW);//Trail color
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
    }

    class GUI extends javax.swing.JFrame {

        /**
         * Creates new form GUI212
         */
        public GUI() {
            initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            HerbsBox = new javax.swing.JCheckBox();
            CoinsBox = new javax.swing.JCheckBox();
            BonesBox = new javax.swing.JCheckBox();
            TallyBox = new javax.swing.JCheckBox();
            startButton = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

            jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setText("Edgeville Man Killer by yPad");

            jLabel2.setText("Items to loot:");

            HerbsBox.setText("Herbs");

            CoinsBox.setText("Coins");

            BonesBox.setText("Bones");

            TallyBox.setText("Earth Talisman");

            startButton.setText("Start");
            startButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(HerbsBox).addGap(18, 18, 18).addComponent(CoinsBox).addGap(18, 18, 18).addComponent(BonesBox).addGap(18, 18, 18).addComponent(TallyBox)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(66, 66, 66).addComponent(jLabel1)).addGroup(layout.createSequentialGroup().addGap(164, 164, 164).addComponent(jLabel2)))).addGroup(layout.createSequentialGroup().addGap(177, 177, 177).addComponent(startButton))).addContainerGap(78, Short.MAX_VALUE)));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(60, 60, 60).addComponent(jLabel2).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(HerbsBox).addComponent(CoinsBox).addComponent(BonesBox).addComponent(TallyBox)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE).addComponent(startButton).addGap(52, 52, 52)));

            pack();
        }// </editor-fold>

        private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
            int[] loot = new int[15];
            if (BonesBox.isSelected()) {
                loot[0] = 526;
            }
            if (HerbsBox.isSelected()) {
                loot[1] = 207;
                loot[2] = 213;
                loot[3] = 209;
                loot[4] = 211;
                loot[5] = 215;
                loot[6] = 217;
                loot[7] = 2485;
            }
            if (TallyBox.isSelected()) {
                loot[8] = 1440;
            }
            if (CoinsBox.isSelected()) {
                loot[9] = 995;
            }
            this.dispose();
            guiWait = false;
            Variables.fillLootIds(loot);
        }

        /**
         * @param args the command line arguments
         */
        public void main(String args[]) {
            /*
             * Set the Nimbus look and feel
             */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
             * If Nimbus (introduced in Java SE 6) is not available, stay with
             * the default look and feel. For details see
             * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>

            /*
             * Create and display the form
             */
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    new GUI().setVisible(true);
                }
            });
        }
        // Variables declaration - do not modify
        private javax.swing.JCheckBox BonesBox;
        private javax.swing.JCheckBox CoinsBox;
        private javax.swing.JCheckBox HerbsBox;
        private javax.swing.JCheckBox TallyBox;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JButton startButton;
        // End of variables declaration
    }
}
