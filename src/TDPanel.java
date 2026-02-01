import javax.swing.*;
import java.awt.*;

public class TDPanel extends JPanel
{
    private final World myWorld;
    private final TDFrame myParent;
    private final AnimationThread myThread;

    public TDPanel(TDFrame parent)
    {
        super();
        myParent = parent;
        setBackground(Color.LIGHT_GRAY);
        myWorld = new World();
        myThread = new AnimationThread();
        myThread.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        myWorld.drawPath(g);
        myWorld.drawShots(g);
        myWorld.drawInvaders(g);
        myWorld.drawTurrets(g);
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
