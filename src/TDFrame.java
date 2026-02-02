import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * the window for this program, which contains a top panel with buttons, a main area with the gameplay, and a status
 * indicator at the bottom of the window.
 */
public class TDFrame extends JFrame implements ActionListener
{
    // stuff for top panel
    private JButton[] turretButtons; // a list of buttons at the top of the screen to let the user place turrets.
    private int userCash; // how much money the player has.
    private JLabel cashLabel; // the label that displays the money onscreen.
    private JButton startButton; // the button used to start the game

    // middle panel
    private final TDPanel mainPanel;  // the area with the path, invaders, turrets and shots

    // bottom panel
    private JLabel statusLabel;
    private int currentStatus;

    // constants
    public final static int NUM_TURRET_TYPES = 3;
    public final static int[] PRICES = {100, 150, 200};
    public final static int STATUS_WAITING = 0;
    public final static int STATUS_PLACING = 1;
    public final static int STATUS_RUNNING = 2;

    public TDFrame()
    {
        super("Tower Defense");
        setSize(800,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // if the user closes the window, quit the program.
        getContentPane().setLayout(new BorderLayout());
        userCash = 1500;
        buildTopPane();

        mainPanel = new TDPanel(this);
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        buildBottomPane();
        setStatus(STATUS_WAITING);
    }

    public void buildTopPane()
    {
        Box topPanel = Box.createHorizontalBox(); // a layout manager that arranges objects horizontally
        topPanel.add(Box.createHorizontalStrut(1)); // forces a 1 pixel gap from left edge of panel
        cashLabel = new JLabel("$"+userCash);
        topPanel.add(cashLabel);
        topPanel.add(Box.createHorizontalGlue()); // acts like a spring to the next object added.

        turretButtons = new JButton[NUM_TURRET_TYPES];
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            turretButtons[i] = buildTurretButton(i);
            topPanel.add(turretButtons[i]);
            if (i < NUM_TURRET_TYPES -1)
                topPanel.add(Box.createHorizontalStrut(30)); // forces a 30 pixel gap to next button
        }
        topPanel.add(Box.createHorizontalGlue()); // acts like a spring to the next object added
        startButton = new JButton("Start");
        startButton.setBackground(new Color(0,128,0));
        startButton.setOpaque(true);
        startButton.setBorderPainted(false);
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(this);
        topPanel.add(startButton);
        topPanel.add(Box.createHorizontalStrut(1)); // forces a one pixel gap to right edge of panel.
        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    /**
     * builds a custom button with an image of the given turret in it, over the price of the turret, and with a rollover
     * tooltip with this turret type's stats.
     * @param type - the type of turret to display.
     * @return - a custom JButton.
     */
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
    /**
     * The user has pressed one of the buttons... how should we respond?
     */
    public void actionPerformed(ActionEvent e)
    {
        for (int i=0; i<NUM_TURRET_TYPES; i++)
        {
            if (e.getSource() == turretButtons[i]) // if the button pressed is the ith turret button...
            {
                if (PRICES[i] > userCash)
                {
                    overrideStatusMessage("You can't afford that. Pick another type of Turret or Press Start.");
                    return;
                }
                userCash -= PRICES[i];
                cashLabel.setText("$"+userCash);
                // change the mode so that the buttons are deactivated and tell the main panel to start placing this turret.
                setStatus(STATUS_PLACING);
                mainPanel.placeTurret(i);
                return;
            }

            if (e.getSource() == startButton) // if the user pressed the start button
            {
                setStatus(STATUS_RUNNING); // deactivate the buttons.
                mainPanel.startRun(); // begin the level.
            }

        }
    }

    public void setStatus(int state)
    {
        currentStatus = state;
        switch (currentStatus)
        {
            case STATUS_WAITING: // the user can pick a turret button or start the level
                statusLabel.setText("Select a Turret to Place or Start.");
                setButtonsEnabled(true);
                break;
            case STATUS_PLACING: // the buttons are deactivated while the player places a turret in the world.
                statusLabel.setText("Place the Turret in the World.");
                setButtonsEnabled(false);
                break;
            case STATUS_RUNNING: // the buttons are deactivated while the level is running.
                statusLabel.setText("Running");
                setButtonsEnabled(false);
                break;
        }
    }

    /**
     * sometimes we want to display a message in the status at the bottom without changing the state of the game.
     * @param text - the words to display.
     */
    public void overrideStatusMessage(String text)
    {
        statusLabel.setText(text);
    }

    /**
     * activate or deactivate the turret buttons and the stort button.
     * @param enabled - whether to activate or deactivate them all.
     */
    public void setButtonsEnabled(boolean enabled)
    {
        for (JButton button:turretButtons)
            button.setEnabled(enabled);
        startButton.setEnabled(enabled);
    }

    /**
     * increase the amount of money the user has.
     * @param amount
     */
    public void addCash(int amount)
    {
        userCash += amount;
        cashLabel.setText("$"+userCash);
    }

}
