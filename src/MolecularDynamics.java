/**
 * Created by andrew_korneev on 18.02.2016.
 */

// This class implements calculation of physical values

public class MolecularDynamics {
    //Input
    static int N = 36;//number of particles
    static int a = 60;//distance between particles for triangle and square lattices
    static double Lx = 400, Ly = 400;//boarders
    static double[] Vx = new double[N];
    static double[] Vy = new double[N];
    static double[] x = new double[N];
    static double[] y = new double[N];
    static double dt = 0.01d;
    static double dt2 = dt * dt;
    static double Vmax = 0;
    static double[] ax = new double[N];
    static double[] ay = new double[N];
    double PE, KE;
    int nsnap = 1;
    double sumImpulse = 0;
    static int forceMulty = 1;
    static double deviation = 0;
    double localDeviation;
    static double xprev[] = new double[N];
    static double yprev[] = new double[N];
    double m = 9.109383561 * Math.pow(10 , -31);// electron mass


    static int indentX = (int) Lx / 20;
    static int indentY = (int) Ly / 20;


    void initRandomLattice()
    {
        int n = 0;
        for (int i = 0; i < N; i++)
        {
            x[i] = Math.random() * Lx;
            y[i] = Math.random() * Ly;

            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
        }

    }


    void initTriangleLattice() {

        Lx = 6*a;
        Ly =  3 * Math.pow(3,0.5)* a;

        int n = 0;

        for (int i = 0; i < N; i++) {
            //1
            if (i < 6) {
                x[i] = a + a * i;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a;
            }
            //2
            else if (i < 12) {
                x[i] = a / 2 + a * n;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + 0.5 * Math.pow(3, 0.5) * a;
                n++;
            }
            //3
            else if (i < 18) {
                if (i == 12) n = 0;
                x[i] = a + a * n;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + 2 * 0.5 * Math.pow(3, 0.5) * a;
                n++;
            }
            //4
            else if (i < 24) {
                if (i == 18) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + 3 * 0.5 * Math.pow(3, 0.5) * a;
                n++;
            }
            //5
            else if (i < 30) {
                if (i == 24) n = 0;
                x[i] = a + a * n;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + 4 * 0.5 * Math.pow(3, 0.5) * a;
                n++;
            }
            //6
            else if (i < 36) {
                if (i == 30) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + 5 * 0.5 * Math.pow(3, 0.5) * a;
                n++;
            }

            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
        }
    }

    void initSquareLattice() {

        Lx = 6*a;
        Ly =  6*a;
        int n = 0;

        for (int i = 0; i < N; i++) {
            //1
            if (i < 6) {
                x[i] = a/2 + a * i;
                y[i] = a/2;
            }
            //2
            else if (i < 12) {
                x[i] = a / 2 + a * n;
                y[i] = a/2+ a;
                n++;
            }
            //3
            else if (i < 18) {
                if (i == 12) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = a/2+ 2*a;
                n++;
            }
            //4
            else if (i < 24) {
                if (i == 18) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = a/2+ 3*a;
                n++;
            }
            //5
            else if (i < 30) {
                if (i == 24) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = a/2+ 4*a;
                n++;
            }
            //6
            else if (i < 36) {
                if (i == 30) n = 0;
                x[i] = a / 2 + a * n;
                y[i] = a/2+ 5*a;
                n++;
            }

            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
        }
    }
    void Vmas() {
        double Vxmas = 0;
        double Vymas = 0;

        for (int i = 0; i < N; i++) {
            Vxmas = Vxmas + Vx[i];
            Vymas = Vymas + Vy[i];
        }

        Vxmas = Vxmas / N;
        Vymas = Vymas / N;

        for (int i = 0; i < N; i++) {
            Vx[i] = Vx[i] - Vxmas;
            Vy[i] = Vy[i] - Vymas;
        }

        for (int i = 0; i < N; i++) {
            sumImpulse += (Vx[i] + Vy[i]);
        }
    }

    public void verlet() {

        deviation = 0;
        KE = 0;

        for (int i = 0; i < x.length; i++)
        {
            double xnew;
            double ynew;

            xnew = x[i] + Vx[i] * dt + 0.5 * ax[i] * dt2;
            ynew = y[i] + Vy[i] * dt + 0.5 * ay[i] * dt2;

            //period case
            if (xnew < 0)
            {
                xnew = xnew + Lx;
            }
            if (xnew > Lx)
            {
                xnew = xnew - Lx;
            }
            if (ynew < 0)
            {
                ynew = ynew + Ly;
            }
            if (ynew > Ly)
            {
                ynew = ynew - Ly;
            }

            x[i] = xnew;
            y[i] = ynew;
            //period case end

            // calculating deviation for i particle
            localDeviation = Math.pow(x[i] + y[i] - xprev[i] - yprev[i],2);
            deviation+=localDeviation;

            xprev[i] = x[i];
            yprev[i] = y[i];
        }
            // average deviation among all N particles
            deviation = deviation / N;

        for (int i = 0; i < x.length; i++)
        {
            Vx[i] = Vx[i] + 0.5 * ax[i] * dt;
            Vy[i] = Vy[i] + 0.5 * ay[i] * dt;
        }

        accel();

        for (int i = 0; i < x.length; i++)
        {
            Vx[i] = Vx[i] + 0.5 * ax[i] * dt;
            Vy[i] = Vy[i] + 0.5 * ay[i] * dt;
            KE = KE + 0.5 * (Vx[i] * Vx[i] + Vy[i] * Vy[i]);
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

        PE = 0;

        for (int i = 0; i < x.length; i++)
        {
            ax[i] = 0.0d;
            ay[i] = 0.0d;
        }

        for (int i = 0; i < x.length - 1; i++)
        {
            for (int j = i + 1; j < x.length; j++)
            {
                dx = x[i] - x[j];
                dy = y[i] - y[j];
                //skip case
                if (Math.abs(dx) > 0.5 * Lx)
                {
                    dx = dx - Math.signum(dx) * Lx;
                }
                if (Math.abs(dy) > 0.5 * Ly)
                {
                    dy = dy - Math.signum(dy) * Ly;
                }
                //skip case end
                r = Math.sqrt(dx * dx + dy * dy);
                //force
                ri = 1 / r;
                ri3 = ri * ri * ri;
                ri6 = ri3 * ri3;
                g = 24 * ri * ri6 * (2 * ri6 - 1);
                force = -(Lx*Ly*g / r) * forceMulty;
                potential = -(4 *Lx*Ly* ri6 * (ri6 - 1)) * forceMulty;
                //force end
                ax[i] = ax[i] + force * dx;
                ay[i] = ay[i] + force * dy;
                ax[j] = ax[j] - force * dx;
                ay[j] = ay[j] - force * dy;

                PE = PE + potential;
            }
        }
    }

    private static MolecularDynamics instance;

    public static synchronized MolecularDynamics getInstance()
    {
        if (instance == null) {
            instance = new MolecularDynamics();
        }
        return instance;
    }



    }