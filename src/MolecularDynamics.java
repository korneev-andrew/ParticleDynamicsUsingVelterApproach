/**
 * Created by andrew_korneev on 18.02.2016.
 */
public class MolecularDynamics {
    //Input
    static int N = 2;//number of particles
    static double Lx = 480,Ly = 480;//boarders
    double[] Vx = new double[N];
    double[] Vy = new double[N];
    double[] x = new double[N];
    double[] y = new double[N];
    double dt = 0.001d;
    double dt2 = dt * dt;
    static double Vmax = 10.0d;
    double[] ax = new double[N];
    double[] ay = new double[N];
    double PE,KE;

    int indentX = (int) Lx/20;
    int indentY = (int) Ly/20;

    public void init() {
        for (int i = 0; i < N; i++) {
                x[i] = Math.random()*Lx;
                //x[0] = 0 ;
                //y[0] = 0 ;
                y[i] = Math.random()*Ly;
                //x[1] = Lx- 10;
                //y[1] = Ly - 10;
                Vx[i] = Vmax * (Math.random() * 3 - 3);
                Vy[i] = Vmax * (Math.random() * 3 - 3);
        }

        double[] Vxmas = new double[N];
        double[] Vymas = new double[N];

        for (int i = 0; i < N; i++) {
            Vxmas[i] = Vxmas[i] + Vx[i];
            Vymas[i] = Vymas[i] + Vy[i];
        }

        for (int i = 0; i < N; i++)

        {
            Vxmas[i] = Vxmas[i] / N;
            Vymas[i] = Vymas[i] / N;
        }

        for (int i = 0; i < N; i++)

        {
            Vx[i] = Vx[i] - Vxmas[i];
            Vy[i] = Vy[i] - Vymas[i];
        }
    }

    public void verlet()
    {

    for(int i = 0;i<N;i++)
    {
        x[i] = x[i] + Vx[i] * dt + 0.5 * ax[i]*dt2;
        y[i] = y[i] + Vy[i] * dt + 0.5 * ay[i]*dt2;
        //period case
        if(x[i]<0) x[i] = x[i] + Lx;
        if(x[i]>Lx) x[i] = x[i] - Lx;
        if(y[i]<0) y[i] = y[i] + Ly;
        if(y[i]>Lx) y[i] = y[i] - Ly;
        //period case end
    }
        for(int i = 0;i<N;i++)
        {
            Vx[i] += 0.5 * ax[i] * dt;
            Vy[i] += 0.5 * ay[i] * dt;
        }

        accel();

        for(int i = 0;i<N;i++)
        {
            Vx[i] += 0.5 * ax[i] * dt;
            Vy[i] += 0.5 * ay[i] * dt;
            KE += 0.5 * ( Vx[i]*Vx[i] + Vy[i]*Vy[i] );
        }
    }

    public void accel()
    {
        double dx;
        double dy;
        double r;
        double ri;
        double ri3;
        double ri6;
        double g;
        double force;
        double potential;

        for(int i = 0; i < N - 1; i++)
        {
            for(int j = i + 1; j < N; j++)
            {
                dx = x[i] - x[j];
                dy = y[i] - y[j];
                //skip case
                if( Math.abs(dx) > 0.5*Lx ) dx = dx - Math.signum(dx) * Lx;
                else if( Math.abs(dx) > 0.5*Ly ) dy = dy - Math.signum(dy) * Ly;
                //skip case end
                r = Math.sqrt(dx * dx + dy * dy);
                //force
                ri = 1/r;
                ri3 = ri*ri*ri;
                ri6 = ri3*ri3;
                g = 24*ri*ri6*(2*ri6 - 1);
                force = g/r;
                potential = 4*ri6*(ri6 - 1);
                //force end
                ax[i] = ax[i] + force*dx;
                ay[i] = ay[i] + force*dy;
                ax[j] = ax[j] - force*dx;
                ay[j] = ay[j] - force*dy;

                PE += potential;
            }
        }
    }


    private static MolecularDynamics instance;

    public static synchronized MolecularDynamics getInstance() {
        if (instance == null) {
            instance = new MolecularDynamics();
        }
        return instance;
    }


}