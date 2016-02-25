/**
 * Created by andrew_korneev on 18.02.2016.
 */
public class MolecularDynamics {
    //Input
    static int N = 6;//number of particles
    static double Lx = 400, Ly = 400;//boarders
    double[] Vx = new double[N];
    double[] Vy = new double[N];
    double[] x = new double[N];
    double[] y = new double[N];
    double dt = 0.01d;
    double dt2 = dt * dt;
    double dtpow = 1/dt;
    static double Vmax = 0.2d;
    double[] ax = new double[N];
    double[] ay = new double[N];
    double PE, KE;
    int nsnap = 100;
    double sumImpulse = 0;

    int indentX = (int) Lx / 20;
    int indentY = (int) Ly / 20;


    public void init() {
        for (int i = 0; i < N; i++)
        {
            x[i] = Math.random() * Lx;
            y[i] = Math.random() * Ly;

            //x[0] = Lx/1.3 ;
            //y[0] = Ly/1.3 ;
            //x[1] = Lx- 10;
            //y[1] = Ly - 10;

            //x[0] = 0;
            //y[0] = Ly/2;
            //x[1] = Lx/2;
            //y[1] = Ly/2;

            //Vx[0] = Vmax;
            //Vy[0] = 0;
            //Vx[1] = 0;
            //Vy[1] = -Vmax;
            //Vx[0] = Vmax;
            //Vy[0] = Vmax;
            //Vx[1] = -Vmax;
            //Vy[1] = -Vmax;
            //Vx[0] = 0;
            //Vy[1] = 0;

            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
        }

        double Vxmas = 0;
        double Vymas = 0;

        for (int i = 0; i < N; i++)
        {
            Vxmas = Vxmas + Vx[i];
            Vymas = Vymas + Vy[i];
        }

        Vxmas = Vxmas / N;
        Vymas = Vymas / N;

        for (int i = 0; i < N; i++)
        {
            Vx[i] = Vx[i] - Vxmas;
            Vy[i] = Vy[i] - Vymas;
        }

        System.out.println("Summarized impulse is : ");
        for(int i = 0;i < N ; i++)
        {
            sumImpulse += (Vx[i] + Vy[i]);
        }
        System.out.println(sumImpulse);
    }

    public void verlet() {

        KE = 0;

        for (int i = 0; i < N; i++) {
            double xnew;
            double ynew;

            xnew = x[i] + Vx[i] * dt + 0.5 * ax[i] * dt2;
            ynew = y[i] + Vy[i] * dt + 0.5 * ay[i] * dt2;

            //period case
            if (xnew < 0) {
                xnew = xnew + Lx;
            }
            if (xnew > Lx) {
                xnew = xnew - Lx;
            }
            if (ynew < 0) {
                ynew = ynew + Ly;
            }
            if (ynew > Ly) {
                ynew = ynew - Ly;
            }

            x[i] = xnew;
            y[i] = ynew;
            //period case end
        }
        for (int i = 0; i < N; i++) {
            Vx[i] = Vx[i] + 0.5 * ax[i] * dt;
            Vy[i] = Vy[i] + 0.5 * ay[i] * dt;
        }

        accel();

        for (int i = 0; i < N; i++) {
            Vx[i] = Vx[i] + 0.5 * ax[i] * dt;
            Vy[i] = Vy[i] + 0.5 * ay[i] * dt;
            KE = KE + 0.5 * (Vx[i] * Vx[i] + Vy[i] * Vy[i]);
        }
    }

    public void accel() {
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

        for (int i = 0; i < N; i++)
        {
            ax[i] = 0.0d;
            ay[i] = 0.0d;
        }

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                dx = x[i] - x[j];
                dy = y[i] - y[j];
                //skip case
                if (Math.abs(dx) > 0.5 * Lx) {
                    dx = dx - Math.signum(dx) * Lx;
                }
                if (Math.abs(dy) > 0.5 * Ly) {
                    dy = dy - Math.signum(dy) * Ly;
                }
                //skip case end
                r = Math.sqrt(dx * dx + dy * dy);
                //force
                ri = 1 / r;
                ri3 = ri * ri * ri;
                ri6 = ri3 * ri3;
                g = 24 * ri * ri6 * (2 * ri6 - 1);
                force = -(Lx*Ly*g / r);
                potential = 4 * ri6 * (ri6 - 1);
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

    public static synchronized MolecularDynamics getInstance() {
        if (instance == null) {
            instance = new MolecularDynamics();
        }
        return instance;
    }


}