import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;


/**
 * Created by andrew_korneev on 18.02.2016.
 */
public class Main extends JFrame {

    MolecularDynamics md1 = new MolecularDynamics();
    MolecularDynamics md = md1.getInstance();
    int indentX = (int) md.Lx/20;
    int indentY = (int) md.Ly/20;
    static double E ;
    static double Eprev;
    static double dE;

    static void printDollas(){  System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");System.out.println();}

    static void interactive()
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Enter number of particles");
            try {
                String N = reader.readLine();
                MolecularDynamics.N = Integer.parseInt(N);
                break;
            } catch (Exception e){        }
        }

        printDollas();

        while(true) {
            System.out.println("Enter Lx and Ly using space button");
            try {
                String L = reader.readLine();
                if(L.contains(" "))
                {
                    MolecularDynamics.Lx = Integer.parseInt(L.split(" ")[0]);
                    MolecularDynamics.Ly = Integer.parseInt(L.split(" ")[1]);
                    break;
                }
                else throw new IOException();
            } catch (Exception ex) {        }
        }

        printDollas();

        while (true) {
            System.out.println("Enter Vmax to evaluate initial speed");
            try {
                String Vmax = reader.readLine();
                MolecularDynamics.Vmax = Integer.parseInt(Vmax);
                break;
            } catch (Exception e){        }
        }
    }

    Main(String s) {
        super(s);
        DrawPanel panel = new DrawPanel();
        panel.setPreferredSize(new Dimension((int)( md.Lx + indentX * 3 ),(int) (md.Ly + indentY * 3)));
        add(panel);
        pack();
        setVisible(true);
        this.setLocationRelativeTo(null);
        }

    public static void main(String[] args)  {

        //interactive();
        try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //adding a Frame with the illustration of molecular dynamics
                JFrame frame = new Main("Molecular Dynamics");
                //adding a Frame with the illustration of Total Energy
                JFrame graph = new TotalEnergyGrid("Total Energy grid");

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.getContentPane().setBackground(Color.cyan);
                graph.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                graph.getContentPane().setBackground(Color.white);
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
        while (true) {
            repaint();
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


        if(init) {
            md.init();
            md.accel();
            init = false;
        }
        for(int i = 0; i < md.nsnap; i++)
            md.verlet();

        E = (md.KE + md.PE)/md.N;
        dE = (E - Eprev)/Eprev;
        Eprev = E;

        //Checking coordinates for first two added particles
        //System.out.println(md.x[0]+ " : "+md.y[0]);
        //System.out.println(md.x[1]+ " : "+md.y[1]);


        for(int i = 0; i< md.N;i++)
        {
            g2d.setColor(i == 0 ? Color.blue : Color.red);
            g2d.fillOval( (int)md.x[i] + indentX, (int) md.y[i] + indentY, 10, 10);

            //If u like to draw trajectory uncomment this, but it ll eat ya memory almost instantly

            //for(int j = 0 ; j< xShadow.size() ; j++)
            //g2d.drawOval(xShadow.get(j),yShadow.get(j),1,1);

            //xShadow.add((int)md.x[i] + indentX);
            //yShadow.add((int)md.y[i] + indentY);
        }
    }
}
}
