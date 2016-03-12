import java.util.ArrayList;

/**
 * Created by andrew_korneev on 10.03.2016.
 */
public class Deform
{
    public void RemoveParticlesByIndex (int index,int number)
    {
        for(int i = index;i < index + number;i++)
        {
            MolecularDynamics.x[i] = 0;
            MolecularDynamics.y[i] = 0;
        }

        ArrayList<Double> listX = new ArrayList<>();
        ArrayList<Double> listY = new ArrayList<>();

        for(int i = 0; i < MolecularDynamics.x.length ; i++)
        {
            if (MolecularDynamics.x[i]!=0)
            {
                listX.add(MolecularDynamics.x[i]);
                listY.add(MolecularDynamics.y[i]);
            }
        }

        MolecularDynamics.x = new double[listX.size()];
        MolecularDynamics.y = new double[listY.size()];

        for (int i = 0; i < listX.size(); i++)
        {
            MolecularDynamics.x[i] = listX.get(i);
            MolecularDynamics.y[i] = listY.get(i);
        }
        ControlPanel.N.setText(MolecularDynamics.x.length + "");
        ControlPanel.Lx.setText(String.format("%-6.2f",MolecularDynamics.Lx));
        ControlPanel.Ly.setText(String.format("%-6.2f",MolecularDynamics.Ly));
    }


    public void AddParticle(double x,double y)
    {
        double[] xbefore = MolecularDynamics.x;
        double[] ybefore = MolecularDynamics.y;

        MolecularDynamics.x = new double[MolecularDynamics.x.length + 1];
        MolecularDynamics.y = new double[MolecularDynamics.y.length + 1];

        for(int i = 0;i<xbefore.length;i++)
        {
            MolecularDynamics.x[i] = xbefore[i];
            MolecularDynamics.y[i] = ybefore[i];
        }

        //adding new particle with x and y coordinates

        MolecularDynamics.x[xbefore.length] = x;
        MolecularDynamics.y[ybefore.length] = y;
        MolecularDynamics.Vx = new double[MolecularDynamics.Vx.length + 1];
        MolecularDynamics.Vy = new double[MolecularDynamics.Vy.length + 1];
        MolecularDynamics.ax = new double[MolecularDynamics.ax.length + 1];
        MolecularDynamics.ay = new double[MolecularDynamics.ay.length + 1];
        MolecularDynamics.xprev = new double[MolecularDynamics.xprev.length + 1];
        MolecularDynamics.yprev = new double[MolecularDynamics.yprev.length + 1];
        ControlPanel.N.setText(MolecularDynamics.x.length + "");
        ControlPanel.Lx.setText(String.format("%-6.2f",MolecularDynamics.Lx));
        ControlPanel.Ly.setText(String.format("%-6.2f",MolecularDynamics.Ly));
    }
}
