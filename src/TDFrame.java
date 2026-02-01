import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TDFrame extends JFrame implements ActionListener
{
    private TDPanel mainPanel;
    private JButton[] turretButtons;

    public int NUM_TURRET_TYPES = 3;

    public TDFrame()
    {
        super("Tower Defense");
        setSize(800,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        mainPanel = new TDPanel();
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        buildTopPane();
    }

    public void buildTopPane()
    {
        Box topPanel = Box.createHorizontalBox();
        turretButtons = new JButton[NUM_TURRET_TYPES];

        topPanel.add(Box.createHorizontalGlue());
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            turretButtons[i] = buildTurretButton(i);
            turretButtons[i].addActionListener(this);
            topPanel.add(turretButtons[i]);
            if (i < NUM_TURRET_TYPES -1)
                topPanel.add(Box.createHorizontalStrut(30));
        }
        topPanel.add(Box.createHorizontalGlue());

        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    public JButton buildTurretButton(int type)
    {
        BufferedImage bi = new BufferedImage(40,40,BufferedImage.TYPE_INT_RGB);
        Turret temp = new Turret(type,null);
        temp.setMyLoc(20,20);
        Graphics localGraphics = bi.createGraphics();
        localGraphics.setColor(Color.LIGHT_GRAY);
        localGraphics.fillRect(0,0,40,40);
        temp.drawSelf(localGraphics);
        return new JButton(new ImageIcon(bi));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            if (e.getSource() == turretButtons[i])
                System.out.println("User pressed button "+i+".");
        }
    }
}
