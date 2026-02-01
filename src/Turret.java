import java.awt.*;

public class Turret
{
    private int myRange;
    private double myPeriod;
    private int myDamage;
    private int myType;
    private World myWorld;
    private double myTimeSinceLastFire;
    private int[] myLoc;
    private double myGunAngle;

    private static Stroke thinLine;
    private static Stroke thickLine;
    private static Stroke gunLine;

    public Turret(int range, double period, int damage, int type, World myWorld)
    {
        this.myRange = range;
        this.myPeriod = period;
        this.myDamage = damage;
        this.myType = type;
        this.myWorld = myWorld;
        myTimeSinceLastFire = 999;
        myLoc = new int[2];
        myLoc[0] = -100;
        myLoc[1] = -100;
        myGunAngle = 0;

        if (null == thickLine)
            thinLine = new BasicStroke(1);
        if (null == thickLine)
            thickLine = new BasicStroke(2);
        if (null == gunLine)
            gunLine = new BasicStroke(4);
    }

    public void setMyLoc(int[] myLoc)
    {
        this.myLoc = myLoc;
    }

    public void setMyLoc(int x, int y)
    {
        myLoc[0] = x;
        myLoc[1] = y;
    }

    public void drawSelf(Graphics g)
    {
        switch (myType)
        {
            case 0:
                g.setColor(Color.BLACK);
                g.drawOval(myLoc[0]-18, myLoc[1]-18, 36, 36);
                g.drawLine(myLoc[0]-12, myLoc[1]-12, myLoc[0]+12, myLoc[1]+12);
                g.drawLine(myLoc[0]-12, myLoc[1]+12, myLoc[0]+12, myLoc[1]-12);
                break;
            case 1:
                g.setColor(Color.BLACK);
                ((Graphics2D)g).setStroke(thickLine); // use a thicker pen.
                g.drawOval(myLoc[0]-18, myLoc[1]-18, 36, 36);
                g.drawLine(myLoc[0]-12, myLoc[1]-12, myLoc[0]+12, myLoc[1]+12);
                g.drawLine(myLoc[0]-12, myLoc[1]+12, myLoc[0]+12, myLoc[1]-12);
                ((Graphics2D)g).setStroke(thinLine); // restore the pen to thin for the next thing we draw.
                break;
            case 2:
            default:
                g.setColor(Color.BLACK);
                int[] x_vals = {myLoc[0]-18, myLoc[0]-9, myLoc[0]+9, myLoc[0]+18,
                myLoc[0]+9, myLoc[0]-9, myLoc[0]-18};
                int[] y_vals = {myLoc[1], myLoc[1]-18, myLoc[1]-18, myLoc[1], myLoc[1]+18, myLoc[1]+18, myLoc[1]};
                g.drawPolygon(x_vals, y_vals, 7);
                for (int i=0; i<3; i++)
                    g.drawLine(x_vals[i], y_vals[i], x_vals[i+3], y_vals[i+3]);
        }
        drawRecharge((Graphics2D)g);
        drawGun((Graphics2D)g);
    }

    private void drawRecharge(Graphics2D g)
    {
        g.setStroke(thickLine);
        if (myTimeSinceLastFire >= myPeriod)
        {
            g.setColor(Color.RED);
            g.drawOval(myLoc[0]-7, myLoc[1]-7, 14, 14);
        }
        else
        {
            int angle = (int)( 360 * myTimeSinceLastFire/myPeriod);
            g.setColor(new Color(255, 255-255*angle/540, 0));
            g.drawArc(myLoc[0]-7, myLoc[1]-7, 14, 14, 0, angle);
        }
        g.setStroke(thinLine);
    }

    private void drawGun(Graphics2D g)
    {
        g.setColor(Color.BLACK);
        g.setStroke(gunLine);
        g.drawLine(myLoc[0],myLoc[1],(int)(myLoc[0]+20*Math.cos(myGunAngle)), (int)(myLoc[1]+20*Math.sin(myGunAngle)));
        g.setStroke(thinLine);
    }
}
