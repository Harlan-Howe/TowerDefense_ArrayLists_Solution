import java.awt.*;

public class Invader
{
    private double mySpeed; // the speed of this invader, in path squares/second.
    private int myHealth; // how many hit points this invader has right now.
    private final int myType;
    private final World myWorld; // a pointer to the world in which this invader resides.
    private double myProgress; // where along the path this invader is. (e.g., If it is 2.75, then it is 75% of the way
                               // between square 2 and square 3.

    // default speeds and hit points for the various types of invader.
    private final static double[] speeds = {1.0, 1.5, 0.5};
    private final static int[] healths = {100, 125, 225};

    public Invader(int myType, World myWorld)
    {
        this.mySpeed = speeds[myType];
        this.myHealth = healths[myType];
        this.myType = myType;
        this.myWorld = myWorld;
        myProgress = 0.0;
    }

    /**
     * increases either the speed or the hitpoints of this invader by 10%. Makes the invaders a bit faster and/or
     * tougher as the game progresses.
     */
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

    /**
     * calculates where (in pixels) this invader is, based on its progress and the location of the path squares.
     * @return an array of (x, y) doubles.
     */
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

    /**
     * draw this invader, which depends on which type it is, along with a health bar.
     * @param g - the Graphics object that indicates where on the screen to draw and the tools to do it.
     */
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

    /**
     * do one animation step... i.e., increase the progress a little bit, which will make this invader move.
     * @param deltaT - the time, in seconds, since the last animation step.
     * @return whether this invader has reached the end of the path!
     */
    public boolean advance(double deltaT)
    {
        myProgress += deltaT * mySpeed;
        return myProgress >= myWorld.getPath().length;
    }

    public void takeDamage(int damage) {myHealth -= damage;}

    public boolean isDead() {return myHealth <=0;}

}
