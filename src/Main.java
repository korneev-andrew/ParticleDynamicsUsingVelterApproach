import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;


/**
 * Created by andrew_korneev on 18.02.2016.
 */

// This class creates frames : ControlPanel, MolecularDynamics illustration and Energy grid (if uncommented)

public class Main extends JFrame {

    transient static String [] data = new String[600];

    transient static boolean pause = true;

    NumberFormat formatter = new DecimalFormat("#0");
    NumberFormat formatterSave = new DecimalFormat("#0.0000000000000000");
    int timeToSkip = 0;

    MolecularDynamics md = new MolecularDynamics();

    double secs;
    int mins;
    transient int time;
    int ii = 0;
    int indentX = (int) md.Lx/20;
    int indentY = (int) md.Ly/20;
    static double E ;
    static double Eprev;
    static double dE;
    static double PE;
    static double KE;


    Main(String s) {
        super(s);
        DrawPanel panel = new DrawPanel();
        panel.setPreferredSize(new Dimension((int) (md.Lx + indentX * 3), (int) (md.Ly + indentY * 3)));
        add(panel);
        pack();
        setVisible(true);
        setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.cyan);
        }

    public static void main(String[] args)  throws IOException{

        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                //adding Control Panel and a Main frame eventually
                JFrame control = new ControlPanel();

                //adding a Frame with the illustration of Total Energy
                //JFrame graph = new TotalEnergyGrid("Total Energy grid");
                //graph.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                //graph.getContentPane().setBackground(Color.white);
            }
        });
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


        while (true)
        {
            if(!pause)
            {
                repaint();
                if(time%250==0 && time!=timeToSkip && ii<600)
                {
                    timeToSkip = time;
                    data[ii]= "" + (double)time/1000 + " " + formatterSave.format(E) + " " + formatterSave.format(KE) + " " + formatterSave.format(PE);
                    ii++;
                }
            }
            try {Thread.sleep(1);
            } catch (InterruptedException ex) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawRect(indentX,indentY,(int)md.Lx + indentX,(int)md.Ly + indentY);
        g2d.drawString("" + (int)md.Lx, (int) md.Lx + indentX , indentY);
        g2d.drawString("" + (int)md.Ly,  0, (int) md.Ly + indentY * 2);
        g2d.drawString("X", (int)md.Lx/2, indentY - indentY/2);
        g2d.drawString("Y", indentY / 2, (int) md.Ly/2);
        g2d.drawString(mins + "m" + formatter.format(secs/1000) + "s",md.Lx,md.Ly + indentY*3);

        secs++;
        time++;
        if(secs== 60 * 1000) {mins++;secs=0;}

        if (init)
        {
            md.init();
            md.accel();
            init = false;
        }
        for(int i = 0; i < md.nsnap; i++)
            md.verlet();

        KE = md.KE/md.N;
        PE = md.PE/md.N;

        E = (md.KE + md.PE)/md.N;
        dE = (E - Eprev)/Eprev;
        Eprev = E;


        for(int i = 0; i< md.N;i++)
        {
            g2d.setColor(i == 0 ? Color.blue : Color.red);
            g2d.fillOval( (int)md.x[i] + indentX, (int) md.y[i] + indentY, 10, 10);
        }
    }
}
}
