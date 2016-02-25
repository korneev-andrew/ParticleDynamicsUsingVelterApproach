import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by admin on 25.02.2016.
 */
public class TotalEnergyGrid extends JFrame
{
    MolecularDynamics md1 = new MolecularDynamics();
    MolecularDynamics md = md1.getInstance();

    int sizeX = 1000;
    int sizeY = 200;
    int time = 0;
    double timeSpent = 0;
    double Ediffmax = Double.MIN_VALUE;

    TotalEnergyGrid(String s) {
        super(s);
        DrawPanel panel = new DrawPanel();
        panel.setPreferredSize(new Dimension(sizeX, sizeY));
        add(panel);
        pack();
        setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public class DrawPanel extends JComponent implements Runnable {
        boolean init = true;

        LinkedList<Integer> xShadow = new LinkedList<>();
        LinkedList<Integer> yShadow = new LinkedList<>();

        public DrawPanel() {
            super();
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                repaint();
                try {Thread.sleep(100);
                } catch (InterruptedException ex) {}
            }
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.blue);

            if(Main.E !=0)
            g2d.drawOval(time,(int)(Main.E * sizeY + sizeY/2),1,1);

            g2d.drawString("time = " + timeSpent/1000 + "s",sizeX - 70,10);
            g2d.drawString("E = " + Main.E,0,sizeY - 10);

            if(Main.dE > Ediffmax && Main.dE != Double.POSITIVE_INFINITY && Main.dE != Double.NEGATIVE_INFINITY)
            {
                Ediffmax = Main.dE;
            }
            g2d.drawString("Ediff max = " + Ediffmax + "%",0,sizeY);

            time ++;
            timeSpent += md.nsnap;

            //Illustrating trajectory
            for(int j = 0 ; j < xShadow.size() ; j++)
            g2d.drawOval(xShadow.get(j),yShadow.get(j),1,1);

            xShadow.add(time);
            yShadow.add((int)(Main.E * sizeY + sizeY/2));
        }
    }

}
