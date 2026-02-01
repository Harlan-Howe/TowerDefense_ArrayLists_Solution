import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class TDFrame extends JFrame implements ActionListener
{
    private TDPanel mainPanel;
    private JButton[] turretButtons;
    private int userCash;
    private JLabel cashLabel;
    private JButton startButton;
    private JLabel statusLabel;

    public int NUM_TURRET_TYPES = 3;
    public final int[] PRICES = {100, 150, 200};

    public TDFrame()
    {
        super("Tower Defense");
        setSize(800,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        userCash = 1000;
        mainPanel = new TDPanel(this);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        buildTopPane();
        buildBottomPane();
    }

    public void buildTopPane()
    {
        Box topPanel = Box.createHorizontalBox();
        topPanel.add(Box.createHorizontalStrut(1));
        cashLabel = new JLabel("$"+userCash);
        topPanel.add(cashLabel);
        topPanel.add(Box.createHorizontalGlue());

        turretButtons = new JButton[NUM_TURRET_TYPES];
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            turretButtons[i] = buildTurretButton(i);
            topPanel.add(turretButtons[i]);
            if (i < NUM_TURRET_TYPES -1)
                topPanel.add(Box.createHorizontalStrut(30));
        }
        topPanel.add(Box.createHorizontalGlue());
        startButton = new JButton("Start");
        startButton.setBackground(new Color(0,128,0));
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(this);
        topPanel.add(startButton);
        topPanel.add(Box.createHorizontalStrut(1));
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
        JButton result = new JButton("$"+PRICES[type], new ImageIcon(bi));
        result.setToolTipText("Range: "+Turret.RANGES[type]+" Recharge: "+Turret.RECHARGE_TIMES[type]+" Damage: "+Turret.DAMAGES[type]);
        result.addActionListener(this);
        result.setHorizontalTextPosition(SwingConstants.CENTER);
        result.setVerticalTextPosition(SwingConstants.BOTTOM);
        return result;
    }

    public void buildBottomPane()
    {
        statusLabel = new JLabel("Test");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(statusLabel);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            if (e.getSource() == turretButtons[i])
                System.out.println("User pressed button "+i+".");
            if (e.getSource() == startButton)
                System.out.println("User pressed start.");
        }
    }
}
