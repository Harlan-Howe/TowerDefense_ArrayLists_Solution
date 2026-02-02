import java.awt.*;

public class Invader
{
    private double mySpeed;
    private int myHealth;
    private int myType;
    private World myWorld;
    private double myProgress;
    private static double[] speeds = {1.0, 1.5, 0.5};
    private static int[] healths = {100, 125, 225};

    public Invader(int myType, World myWorld)
    {
        this.mySpeed = speeds[myType];
        this.myHealth = healths[myType];
        this.myType = myType;
        this.myWorld = myWorld;
        myProgress = 0.0;
    }

    public void levelUp()
    {
        int whichParameter = (int)(Math.random()*2);
        switch (whichParameter)
        {
            case 0:
                this.mySpeed *= 1.1;
                break;
            case 1:
                this.myHealth = this.myHealth * 11 /10;

        }
    }

    public double[] getLoc()
    {
        double[] loc = new double[2];
        int latestBase = (int)myProgress;
        double frac = myProgress - latestBase;


        if (latestBase < myWorld.getPath().length-1)
        {
            loc[0] = myWorld.getPath()[latestBase][0];
            loc[1] = myWorld.getPath()[latestBase][1];
            int nextX = myWorld.getPath()[latestBase+1][0];
            int nextY = myWorld.getPath()[latestBase+1][1];

            loc[0] += frac * (nextX-loc[0]);
            loc[1] += frac * (nextY-loc[1]);
        }
        else
        {
            loc[0] = myWorld.getPath()[latestBase][0];
            loc[1] = myWorld.getPath()[latestBase][1];
        }
        return loc;
    }

    public void drawSelf(Graphics g)
    {
        double[] loc = getLoc();
        switch (myType)
        {
            case 0:
                g.setColor(Color.GREEN);
                g.fillRect((int)(loc[0]-16), (int)(loc[1]-16), 32, 32);
                break;
            case 1:
                g.setColor(Color.YELLOW);
                g.fillOval((int)(loc[0]-16), (int)(loc[1]-16), 32, 32);
                break;
            case 2:
            default:
                g.setColor(Color.MAGENTA);
                int[] xLocs = {(int)loc[0], (int)loc[0]+16, (int)loc[0]-16, (int)loc[0]};
                int[] yLocs = {(int)loc[1]-16, (int)loc[1]+16, (int)loc[1]+16, (int)loc[1]-16};
                g.fillPolygon(xLocs, yLocs, 4);
                break;
        }
        g.setColor(Color.RED);
        g.fillRect((int)(loc[0]-16), (int)(loc[1]-18), myHealth*32/healths[myType], 2);
    }

    public boolean advance(double deltaT)
    {
        myProgress += deltaT * mySpeed;
        return myProgress >= myWorld.getPath().length;
    }

    public void takeDamage(int damage) {myHealth -= damage;}

    public boolean isDead() {return myHealth <=0;}

}
