

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by andrew_korneev on 06.03.2016.
 */
public class ControlPanel extends JFrame
{
    int spaceX;
    JPanel jpanel = new JPanel();

    JButton startButton = new JButton();
    JButton saveButton = new JButton();
    JButton x100 = new JButton();
    JButton d100 = new JButton();
    JLabel multy = new JLabel("1x potential");


    public ControlPanel()
    {
        setTitle("Control Panel");
        setPreferredSize(new Dimension(470, 95));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();

        //start/pause button
        startButton.setIcon(new ImageIcon("start.jpg"));
        startButton.setBackground(Color.gray);
        startButton.setToolTipText("Start / Pause");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(Main.pause == true)
                {
                    Main.pause = false;
                }
                else
                {
                    Main.pause = true;
                }
            }
        });

        //save button
        saveButton.setIcon(new ImageIcon("save.png"));
        saveButton.setBackground(Color.gray);
        saveButton.setToolTipText("Save in txt format");

        //x100
        x100.setText("x100");
        x100.setPreferredSize(new Dimension(78, 58));
        x100.setToolTipText("Multiply potential value by 100");
        x100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(MolecularDynamics.forceMulty < 1000000)
                {
                    MolecularDynamics.forceMulty *= 100;
                    multy.setText(MolecularDynamics.forceMulty + "x potential");
                }
            }
        });

        // /100
        d100.setText("/100");
        d100.setPreferredSize(new Dimension(78, 58));
        d100.setToolTipText("divide potential value by 100");
        d100.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if(MolecularDynamics.forceMulty > 1)
                {
                    MolecularDynamics.forceMulty /= 100;
                    multy.setText(MolecularDynamics.forceMulty + "x potential");
                }
            }
        });



        jpanel.add(startButton);
        jpanel.add(saveButton);
        jpanel.add(x100);
        jpanel.add(d100);
        jpanel.add(multy);


        jpanel.setBackground(Color.getHSBColor(250,250,6));
        jpanel.setLayout(new FlowLayout(-1));
        add(jpanel);


        validate();
    }
}
