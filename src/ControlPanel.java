import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


/**
 * Created by andrew_korneev on 06.03.2016.
 */


// This class is a panel for controlling some physical values in MolecularDynamics class

public class ControlPanel extends JFrame
{
    boolean go = true;
    static boolean Tchange = false;
    String Tprev;

    static JPanel jpanel = new JPanel();

    JButton startButton = new JButton();
    JButton saveButton = new JButton();
    JButton x100 = new JButton();
    JButton d100 = new JButton();
    JButton enter = new JButton();

    JLabel multy = new JLabel("1x");
    JLabel dtLabel = new JLabel("dt");
    JLabel NLabel = new JLabel("N particles");
    JLabel VmaxLabel = new JLabel("Vmax");
    JLabel LxLabel = new JLabel("Lx");
    JLabel LyLabel = new JLabel("Ly");
    static JLabel TLabel = new JLabel("T,K");


    JTextField dt = new JTextField(MolecularDynamics.dt + "");
    static JTextField N = new JTextField(MolecularDynamics.x.length+ "");
    JTextField Vmax = new JTextField(MolecularDynamics.Vmax + "");
    static JTextField Lx = new JTextField(String.format(Locale.CANADA,"%-6.2f",MolecularDynamics.Lx));
    static JTextField Ly = new JTextField(String.format(Locale.CANADA,"%-6.2f",MolecularDynamics.Ly));
    static JTextField T = new JTextField(String.format(Locale.CANADA,"%-9.6f",MolecularDynamics.T1));


    public ControlPanel()
    {
        setTitle("Control Panel");
        setPreferredSize(new Dimension(600, 75));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int) rect.getMaxX()/2 - getWidth()*2;
        int y = (int) rect.getMaxY() - getHeight()*4;
        setLocation(x, y);
        setContentPane(jpanel);
        setResizable(false);
        pack();

        // start/pause button
        startButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("start.jpg")));
        startButton.setBackground(Color.gray);
        startButton.setSize(48,48);
        startButton.setLocation(0,0);
        startButton.setToolTipText("Start / Pause");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(go)
                {
                    callMain();
                    go = false;
                }
                if(Main.pause == true)
                {
                    Main.pause = false;
                }
                else
                {
                    Main.pause = true;
                }
                N.setEditable(false);
                Vmax.setEditable(false);
                Lx.setEditable(false);
                Ly.setEditable(false);
                N.setBackground(Color.gray);
                Vmax.setBackground(Color.gray);
                Lx.setBackground(Color.gray);
                Ly.setBackground(Color.gray);
            }
        });

        // save button
        saveButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("save.png")));
        saveButton.setBackground(Color.gray);
        saveButton.setToolTipText("Save in txt format");
        saveButton.setSize(48,48);
        saveButton.setLocation(546,0);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    FileWriter FW = new FileWriter("data.txt");
                    FW.write("t,s   " + " " + "E,J/kg            " + " " + "KE,J/kg           " + " " + "PE,J/kg           " + " " + "Av.deviation       " + "T,K               "+"\r\n");
                    for(int i =0; i< Main.data.length; i++)
                    {
                        if(Main.data[i]!=null)
                        {
                            FW.write(Main.data[i]+"\r\n");
                        }
                    }
                    FW.close();
                    JOptionPane.showMessageDialog(ControlPanel.jpanel, "Data was saved in data.txt file","Saving status", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (IOException exe)
                {
                    JOptionPane.showMessageDialog(ControlPanel.jpanel, "Could not save to file", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // x100 potential
        x100.setText("x10");
        x100.setSize(68,15);
        x100.setLocation(50,0);
        x100.setToolTipText("Multiply potential value by 10");
        x100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(MolecularDynamics.forceMulty < 1000000)
                {
                    MolecularDynamics.forceMulty *= 10;
                    multy.setText(MolecularDynamics.forceMulty + "x");
                }
            }
        });

        // /100 potential
        d100.setText("/10");
        d100.setSize(68, 15);
        d100.setLocation(50, 33);
        d100.setToolTipText("Divide potential value by 10");
        d100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(MolecularDynamics.forceMulty > 1)
                {
                    MolecularDynamics.forceMulty /= 10;
                    multy.setText(MolecularDynamics.forceMulty + "x");
                }
            }
        });

        // potential multiplication label
        multy.setLocation(50,17);
        multy.setSize(68, 12);
        multy.setHorizontalAlignment(SwingConstants.CENTER);

        // number of particles adjust
        N.setHorizontalAlignment(SwingConstants.CENTER);
        N.setSize(68,18);
        N.setLocation(120,15);
        N.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    MolecularDynamics.N = Integer.parseInt(N.getText());
                }
        });

        // Number of particles label
        NLabel.setHorizontalAlignment(SwingConstants.CENTER);
        NLabel.setSize(68,15);
        NLabel.setLocation(120,0);

        // time step adjust
        dt.setSize(68,18);
        dt.setLocation(190,15);
        dt.setHorizontalAlignment(SwingConstants.CENTER);
        dt.setToolTipText("time step");
        dt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MolecularDynamics.dt = Double.parseDouble(dt.getText());
            }
        });

        // timestep label
        dtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dtLabel.setSize(68,15);
        dtLabel.setLocation(190,0);

        // Vmax adjust
        Vmax.setSize(68,18);
        Vmax.setLocation(260,15);
        Vmax.setHorizontalAlignment(SwingConstants.CENTER);
        Vmax.setToolTipText("Initial max speed");
        Vmax.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MolecularDynamics.Vmax = Double.parseDouble(Vmax.getText());
            }
        });

        // Vmax label
        VmaxLabel.setHorizontalAlignment(SwingConstants.CENTER);
        VmaxLabel.setSize(68,15);
        VmaxLabel.setLocation(260,0);

        // Width adjust
        Lx.setSize(68,18);
        Lx.setLocation(330,15);
        Lx.setHorizontalAlignment(SwingConstants.CENTER);
        Lx.setToolTipText("Boarder width");
        Lx.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MolecularDynamics.Lx = Integer.parseInt(Lx.getText());
            }
        });

        // Width label
        LxLabel.setHorizontalAlignment(SwingConstants.CENTER);
        LxLabel.setSize(68,15);
        LxLabel.setLocation(330,0);

        // Height adjust
        Ly.setSize(68,18);
        Ly.setLocation(400,15);
        Ly.setHorizontalAlignment(SwingConstants.CENTER);
        Ly.setToolTipText("Boarder height");
        Ly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                MolecularDynamics.Ly = Integer.parseInt(Ly.getText());
            }
        });

        // Height label
        LyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        LyLabel.setSize(68,15);
        LyLabel.setLocation(400,0);

        // Enter button
        enter.setSize(68,15);
        enter.setLocation(260,33);
        enter.setText("Enter");
        enter.setToolTipText("Accept input parameters");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                    MolecularDynamics.N = Integer.parseInt(N.getText());
                    MolecularDynamics.dt = Double.parseDouble(dt.getText());
                    MolecularDynamics.Vmax = Double.parseDouble(Vmax.getText());
                    MolecularDynamics.Lx = Double.parseDouble(Lx.getText());
                    MolecularDynamics.Ly = Double.parseDouble(Ly.getText());
                    Tchange = true;
            }
        });


        //adding temperature incr
        TLabel.setSize(68,15);
        TLabel.setLocation(470,0);
        TLabel.setHorizontalAlignment(SwingConstants.CENTER);

        T.setSize(68,18);
        T.setLocation(470,15);
        T.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(!T.getText().equals(Tprev))
                Tchange = true;

                Tprev = T.getText();
            }
        });

        // adding JObjects to JPanel
        jpanel.add(startButton);
        jpanel.add(saveButton);
        jpanel.add(x100);
        jpanel.add(d100);
        jpanel.add(multy);
        jpanel.add(dt);
        jpanel.add(dtLabel);
        jpanel.add(N);
        jpanel.add(NLabel);
        jpanel.add(Vmax);
        jpanel.add(VmaxLabel);
        jpanel.add(Lx);
        jpanel.add(Ly);
        jpanel.add(LxLabel);
        jpanel.add(LyLabel);
        jpanel.add(enter);
        jpanel.add(T);
        jpanel.add(TLabel);

        // JPanel settings
        jpanel.setOpaque(true);
        jpanel.setBackground(Color.getHSBColor(250,250,6));
        //jpanel.setLayout(new FlowLayout(-1));
        jpanel.setLayout(null);
        //add(jpanel);


        // this method provides to show my Icons
        validate();
    }

    // method to create an instance of a Main class
    public static void callMain()
    {
        Frame frame = new Main("Molecular Dynamics");
    }
}
