import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by andrew_korneev on 25.02.2016.
 */

//This class implements Grids for potential, kinetic,total energies

public class TotalEnergyGrid extends JFrame
{
    MolecularDynamics md1 = new MolecularDynamics();
    MolecularDynamics md = md1.getInstance();

    int sizeX = 800;
    int sizeY = 300;
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

    public class DrawPanel extends JComponent implements Runnable
    {
        boolean init = true;

        LinkedList<Integer> xShadowE = new LinkedList<>();
        LinkedList<Integer> yShadowE = new LinkedList<>();

        LinkedList<Integer> xShadowPE = new LinkedList<>();
        LinkedList<Integer> yShadowPE = new LinkedList<>();

        LinkedList<Integer> xShadowKE = new LinkedList<>();
        LinkedList<Integer> yShadowKE = new LinkedList<>();

        public DrawPanel() {
            super();
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                if(!Main.pause)
                repaint();
                try {Thread.sleep(1);
                } catch (InterruptedException ex) {}
            }
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.blue);

            //refreshing case
            if(time > sizeX)
            {
                time = 0;
                clear2Arrays(xShadowE,yShadowE);
                clear2Arrays(xShadowPE,yShadowPE);
                clear2Arrays(xShadowKE,yShadowKE);
            }
            ////////////////

            //Showing variables : time ,E and relative difference between E on current step and previous
            g2d.setColor(Color.black);
            g2d.drawString("time = " + (int)timeSpent/1000 + "s",sizeX - 70,10);
            g2d.drawString("E = " + Main.E,sizeX/2 - 70,10);

            if(Main.dE > Ediffmax && Main.dE != Double.POSITIVE_INFINITY && Main.dE != Double.NEGATIVE_INFINITY)
            {
                Ediffmax = Main.dE;
            }
            g2d.drawString("Ediff max = " + Ediffmax + "%",sizeX/2 - 70,20);

            time ++;
            timeSpent += md.nsnap;

            ////////////////////////////////////////////////////////////////////////////////////////////

            //trajectories
            xShadowE.add(time);
            yShadowE.add((int)(sizeY-Main.E * sizeY));

            xShadowPE.add(time);
            yShadowPE.add((int)(sizeY-Main.PE * sizeY - 10));

            xShadowKE.add(time);
            yShadowKE.add((int)(sizeY-Main.KE * sizeY));

            trajectory(g2d,xShadowE,yShadowE,Color.red);
            trajectory(g2d,xShadowPE,yShadowPE,Color.blue);
            trajectory(g2d,xShadowKE,yShadowKE,Color.orange);
            /////////////

            //Legend
            g2d.setColor(Color.red);
            g2d.drawString("E",0,10);

            g2d.setColor(Color.blue);
            g2d.drawString("PE",0,20);

            g2d.setColor(Color.orange);
            g2d.drawString("KE",0,30);
            ////////

            //zero point
            g2d.setColor(Color.black);
            g2d.drawString("0",0,sizeY - 10);
        }

        public void trajectory(Graphics g,LinkedList<Integer> xShadow, LinkedList<Integer> yShadow,Color color)
        {
            //Illustrating trajectory
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);
            for(int j = 0 ; j < xShadow.size() ; j++)
                g2d.drawOval(xShadow.get(j),yShadow.get(j),1,1);

        }
        public void clear2Arrays(LinkedList<Integer> xShadow, LinkedList<Integer> yShadow)
        {
            xShadow.clear();
            yShadow.clear();
        }
    }

}
