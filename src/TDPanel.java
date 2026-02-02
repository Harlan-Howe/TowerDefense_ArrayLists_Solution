import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class TDPanel extends JPanel implements MouseListener, MouseMotionListener
{
    private final World myWorld;
    private final TDFrame myParent;
    private final AnimationThread myThread;
    private int status;
    private Turret currentTurret;

    public TDPanel(TDFrame parent)
    {
        super();
        myParent = parent;
        setBackground(Color.LIGHT_GRAY);
        myWorld = new World();
        myThread = new AnimationThread();
        status = TDFrame.STATUS_WAITING;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public void placeTurret(int turretType)
    {
        currentTurret = new Turret(turretType, myWorld);
        status = TDFrame.STATUS_PLACING;
        requestFocus();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        myWorld.drawPath(g);
        myWorld.drawShots(g);
        myWorld.drawInvaders(g);
        myWorld.drawTurrets(g);
        if (TDFrame.STATUS_PLACING == status)
            currentTurret.drawSelf(g);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if ((TDFrame.STATUS_PLACING == status) && (null != currentTurret))
        {
            int destX = 20+(e.getX()/40)*40;
            int destY = 20+(e.getY()/40)*40;
            int[][] path = myWorld.getPath();
            for (int[] trackLoc: path)
            {
                if (destX == trackLoc[0] && destY == trackLoc[1])
                {
                    myParent.overrideStatusMessage("You cannot place the turret on the path.");
                    return;
                }
            }
            myWorld.addTurret(currentTurret, destX, destY);
            status = TDFrame.STATUS_WAITING;
            myParent.setStatus(TDFrame.STATUS_WAITING);
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {

    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if ((TDFrame.STATUS_PLACING== status) && (null != currentTurret ))
        {
            currentTurret.setMyLoc(20+(e.getX()/40)*40, 20+(e.getY()/40)*40);
            repaint();
        }
    }

    public void startRun()
    {
        status = TDFrame.STATUS_RUNNING;
        myThread.start();

    }


    class AnimationThread extends Thread
    {

        public void run() // this is what gets called when we tell this thread to start(). You should _NEVER_ call this
        // method directly.
        {
            long start = System.currentTimeMillis();
            long difference;
            System.out.println("Starting Expansion Thread.");
            while (true)
            {
                difference = System.currentTimeMillis() - start;
                start = System.currentTimeMillis();
//                if (difference >= MILLISECONDS_PER_STEP)
//                {
//                    doAnimationStep();
//                    start = System.currentTimeMillis();
//                }
                myWorld.updateAllObjects((int)difference);
                repaint();
                try
                {
                    //noinspection BusyWait
                    Thread.sleep(25); // wait a quarter second before you consider running again.
                }catch (InterruptedException iExp)
                {
                    System.out.println("AnimationThread was interrupted.");
                    break;
                }
            }
        }
    }

}
