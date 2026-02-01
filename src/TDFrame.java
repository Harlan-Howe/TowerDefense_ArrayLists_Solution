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
    private int currentStatus;

    public int NUM_TURRET_TYPES = 3;
    public final int[] PRICES = {100, 150, 200};
    public final static int STATUS_WAITING = 0;
    public final static int STATUS_PLACING = 1;
    public final static int STATUS_RUNNING = 2;

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
        setStatus(STATUS_WAITING);
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
            {
                System.out.println("User pressed button " + i + ".");
                setStatus(STATUS_PLACING);
                mainPanel.placeTurret(i);
                return;
            }
            if (e.getSource() == startButton)
            {
                System.out.println("User pressed start.");
                setStatus(STATUS_RUNNING);
            }

        }
    }

    public void setStatus(int state)
    {
        currentStatus = state;
        switch (currentStatus)
        {
            case STATUS_WAITING:
                statusLabel.setText("Select a Turret to Place or Start.");
                setButtonsEnabled(true);
                break;
            case STATUS_PLACING:
                statusLabel.setText("Place the Turret in the World.");
                setButtonsEnabled(false);
                break;
            case STATUS_RUNNING:
                statusLabel.setText("Running");
                setButtonsEnabled(false);
                break;
        }
    }

    public void overrideStatusMessage(String text)
    {
        statusLabel.setText(text);
    }

    public void setButtonsEnabled(boolean enabled)
    {
        for (JButton button:turretButtons)
            button.setEnabled(enabled);
        startButton.setEnabled(enabled);
    }


}
