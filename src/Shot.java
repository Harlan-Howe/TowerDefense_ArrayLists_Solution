import java.awt.*;

public class Shot
{
    private int[] pt1, pt2;
    private Color myColor;
    private static Stroke shotThickLine;
    private static Stroke thinLine;

    public Shot(int[] gunLoc, double[] invaderLoc, Color color)
    {
        pt1 = gunLoc;
        pt2 = new int[2];
        pt2[0] = (int)invaderLoc[0];
        pt2[1] = (int)invaderLoc[1];
        myColor = color;
        if (null == shotThickLine)
        {
            shotThickLine = new BasicStroke(2);
            thinLine = new BasicStroke(1);
        }
    }

    public void drawSelf(Graphics2D g)
    {
        g.setStroke(shotThickLine);
        g.setColor(myColor);
        g.drawLine(pt1[0],pt1[1],pt2[0],pt2[1]);
        g.setStroke(thinLine);
    }

}
