/**
 * Created by andrew_korneev on 18.02.2016.
 */

// This class implements calculation of physical values

public class MolecularDynamics {
    //Input
    static int N = 144;//number of particles
    static int squareN = (int) Math.pow(N,0.5);
    static double a = 60;//distance between particles for triangle and square lattices
    static double Lx = 360*squareN/6, Ly = 3 * Math.pow(3,0.5)* a * squareN/6;//boarders
    static double[] Vx = new double[N];
    static double[] Vy = new double[N];
    static double[] x = new double[N];
    static double[] y = new double[N];
    static double dt = 0.01d;
    static double dt2 = dt * dt;
    static double Vmax = 0;
    static double[] ax = new double[N];
    static double[] ay = new double[N];
    static double PE, KE;
    int nsnap = 10;
    double sumImpulse = 0;
    static int forceMulty = 1;
    static double deviation = 0;
    double localDeviation;
    static double xprev[] = new double[N];
    static double yprev[] = new double[N];
    //static double m = 9.109383561 * Math.pow(10 , -31);// electron mass
    static double m = 9.109383561 * Math.pow(10 , -27);// electron mass
    static double kb = (1.38 * Math.pow(10, -23));
    static double T1 = KE * m / (kb*N);
    static double n = 0.0d;
    final int initialN = N;
    static double eps144 = 5099.584420093589 - 5028.756808881873; // при  T1 = T2 =  0,000000000013232 K
    static double Teps144 = 0.003850608332056;

    static double eps36 = 1270.261941459282 - 1199.691780688490;
    static double Teps36 = 0.000000002139057;

    static int indentX = (int) Lx / 20;
    static int indentY = (int) Ly / 20;


    void initRandomLattice()
    {
        for (int i = 0; i < N; i++)
        {
            x[i] = Math.random() * Lx;
            y[i] = Math.random() * Ly;

            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
        }

    }


    void initTriangleLattice() {

        boolean odd = true;
        int nx = 0;
        int ny = 0;

        for (int i = 0; i < N; i++) {

            if(i%squareN==0)
            {
                nx = 0;
                if(odd)
                    odd = false;
                else odd = true;
                ny++;
            }
            if(odd)
            {
                x[i] = a / 2 + a * nx;
            }
            else if(!odd)
            {
                x[i] = a + a * nx;
            }

            y[i] = 0.5 * 0.5 * Math.pow(3, 0.5) * a + ny *  0.5 * Math.pow(3, 0.5) * a;
            nx++;



            Vx[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)
            Vy[i] = Vmax * (Math.random() * 2 - 1);// [-Vmax,Vmax)


        }

        n = 144 / Math.exp(eps144*m/(kb*Teps144));
        //n = 36 / Math.exp(eps36*m/(kb*Teps36*36));

        System.out.println(n);
        //System.out.println(eps*m);//2.751471732235893E-24
        //System.out.println(kb*Teps);//1.8260159999999997E-34
        //System.out.println(eps36*m/(kb*Teps36 * 36));
    }

    void initSquareLattice()
    {

        Lx = Ly = Math.pow(Lx*Ly,0.5);
        a = Lx/squareN;

        int nx = 0;
        int ny = 0;

        for (int i = 0; i < N; i++) {

                if(i%squareN==0)
                {
                    nx = 0;
                    ny++;
                }

                x[i] = a / 2 + a * nx;
                y[i] = a/2+ ny*a;
                nx++;


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
        //System.out.println(sumImpulse);
    }

    public void verlet() {

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
                xprev[i] += Lx;
            }
            if (xnew > Lx)
            {
                xnew = xnew - Lx;
                xprev[i] -= Lx;
            }
            if (ynew < 0)
            {
                ynew = ynew + Ly;
                yprev[i] += Ly;
            }
            if (ynew > Ly)
            {
                ynew = ynew - Ly;
                yprev[i] -= Ly;
            }

            x[i] = xnew;
            y[i] = ynew;
            //period case end

            // calculating deviation for i particle
                if(xprev!=null)
                localDeviation = Math.pow(x[i] + y[i] - xprev[i] - yprev[i], 2);
                deviation += localDeviation;
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

        if(ControlPanel.Tchange)
        {
            double T2 =  Double.parseDouble(ControlPanel.T.getText());

            for(int i = 0; i < x.length;i++)
            {
                Vx[i] *= Math.pow(T2 / T1, 0.5);
                Vy[i] *= Math.pow(T2 / T1, 0.5);
                KE = KE + 0.5 * (Vx[i] * Vx[i] + Vy[i] * Vy[i]);
            }

            ControlPanel.Tchange = false;
        }

        T1 = KE * m / (kb * N);


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
                force = -(129600*g / r) * forceMulty;
                potential = -(4 *129600* ri6 * (ri6 - 1)) * forceMulty;
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