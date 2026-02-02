import java.awt.*;
import java.util.ArrayList;

public class World
{
    // location of the boxes' centers. Note each box is 40 x 40, so they differ by 40.
    private int[][] path = {{20, 380}, {60, 380}, {100, 380}, {100, 340}, {100, 300}, {100, 260}, {100, 220},
        {100, 180}, {100, 140}, {140, 140}, {180, 140}, {220, 140}, {260, 140}, {260, 180}, {260, 220}, {260, 260},
            {220, 260}, {180, 260}, {180, 300}, {180, 340}, {180, 380}, {180, 420}, {180, 460}, {140, 460}, {100, 460}};

    private final ArrayList<Invader> invaderList;
    private final ArrayList<Turret> turretList;
    private final ArrayList<Shot> shotList;
    private final TDPanel myParent;

    // how many of each type of invader per level?
    private final int[][] levels = {{4, 3, 2}, {6, 1, 3}, {8, 3, 5}, {0, 0, 6}, {15, 5, 5}};

    // this will start as a copy of the count of invaders per level and will decrease as invaders spawn.
    private int[] spawnList;
    private final double SPAWN_LIKELIHOOD = 0.01;

    private int currentLevel;


    public World(TDPanel parent)
    {
        myParent = parent;
        invaderList = new ArrayList<Invader>();

        turretList = new ArrayList<Turret>();

        shotList = new ArrayList<Shot>();
        currentLevel = 0;
        initializeLevel();
    }

    /**
     * copy the values of levels for the currentLevel into spawnList. This tells the computer how many of each type of
     * Invader to make before the level is over.
     */
    public void initializeLevel()
    {
        spawnList = new int[levels[currentLevel].length];
        for (int i=0; i< levels[currentLevel].length; i++)
            spawnList[i] = levels[currentLevel][i];
    }

    /**
     * the user has just cleared a level, so get ready for the next level.
     */
    public void advanceLevel()
    {
        currentLevel += 1;
        shotList.clear();
        initializeLevel();
        for (Turret t: turretList)
        {
            t.resetTimer();
        }
    }

    public int getCurrentLevel() {return currentLevel;}

    /**
     * create a new Invader, based on the number of Invaders yet to spawn and add it to the Invader list, if there
     * are any invaders left to spawn.
     */
    public void spawnInvader()
    {
        int numRemainingInvaders = getNumRemainingInvadersToSpawn();

        if (0 == numRemainingInvaders)
            return;

        // this is a trick for selecting what type of invader to make, with a probability proportional to how many of
        //   each kind are left.
        double rnd = numRemainingInvaders*Math.random();
        for (int i=0; i<spawnList.length; i++)
        {
            if (rnd < spawnList[i])
            {
                spawnList[i] -= 1;
                Invader nextInvader = new Invader(i, this);
                for (int j = 0; j < currentLevel; j++)
                    nextInvader.levelUp();
                invaderList.add(nextInvader);
                return;
            }
            else
                rnd -= spawnList[i];
        }
        // there really shouldn't be any way of getting out of the loop without returning an invader. If so, we should
        //    indicate that there was an error.
        throw new RuntimeException("randomizing spawn didn't work.");
    }

    /**
     * find the sum of the invaders left in spawnList.
     * @return how many invaders have yet to spawn for this level.
     */
    public int getNumRemainingInvadersToSpawn()
    {
        int remainingInvaders = 0;
        for (int count: spawnList)
            remainingInvaders+=count;
        return remainingInvaders;
    }

    /**
     * draw the chain of squares.
     * @param g - the Graphics object that indicates where on the screen to draw and the tools to do it.
     */
    public void drawPath(Graphics g)
    {
        for (int[] pt:path)
        {
            g.setColor(Color.WHITE);
            g.fillRect(pt[0] - 20, pt[1] - 20, 40, 40);
            g.setColor(Color.BLACK);
            g.drawRect(pt[0] - 20, pt[1] - 20, 40, 40);
        }
    }

    /**
     * tell all the invaders to draw themselves.
     * @param g - the Graphics object that indicates where on the screen to draw and the tools to do it.
     */
    public void drawInvaders(Graphics g)
    {
        for (Invader inv: invaderList)
            inv.drawSelf(g);
    }

    /**
     * tell all the shots to draw themselves, and then clear the list, so they are only drawn once.
     * @param g - the Graphics object that indicates where on the screen to draw and the tools to do it.
     */
    public void drawShots(Graphics g)
    {
        for (Shot s:shotList)
            s.drawSelf((Graphics2D)g);
        shotList.clear();
    }

    /**
     * tell all the turrets to draw themselves.
     * @param g - the Graphics object that indicates where on the screen to draw and the tools to do it.
     */
    public void drawTurrets(Graphics g)
    {
        for (Turret tur: turretList)
            tur.drawSelf(g);
    }


    public int[][] getPath() { return path;}

    public ArrayList<Invader> getInvaderList() {return invaderList;}

    /**
     * tell all the invaders and turrets to animate a step. After invaders move, if any of them have reached the end,
     * then end the game. Potentially spawn more invaders.
     * @param deltaTimeInMS - the number of milliseconds since the last animation step.
     */
    public void updateAllObjects(int deltaTimeInMS)
    {
        double deltaT = deltaTimeInMS/1000.0; // convert time to seconds, typically a fraction of a second.

        // loop through all the invaders and tell each one to advance(deltaT) - it returns a boolean indicating whether
        // it reached the end of the path. If so, end the game.
        boolean anyInvaderReachedEnd = false;
        for (Invader inv: invaderList)
            anyInvaderReachedEnd |= inv.advance(deltaT);
        if (anyInvaderReachedEnd)
        {
            System.out.println("Game over!");
            System.exit(0);
        }

        // tell all the turrets to advance(deltaT) - updates recharge of weapon;
        // and targetNearestInvader() - turns turret towards closest invader and fires, if gun recharged and invader in
        //      range.
        for (Turret t: turretList)
        {
            t.advance(deltaT);
            t.targetNearestInvader();
        }

        // decide whether to spawn another Invader.
        if (Math.random() < SPAWN_LIKELIHOOD)
        {
            spawnInvader();
        }
    }

    /**
     * a turret has fired at an invader, so change its health and potentially remove it if it dies as a result.
     * @param inv - which invader was hit
     * @param damage - how much damage to do to it
     */
    public void damageInvader(Invader inv, int damage)
    {
        if (inv != null)
        {
            inv.takeDamage(damage);
            if (inv.isDead())
            {
                myParent.turretKilledInvader(inv.getType());
                invaderList.remove(inv);
            }
        }
    }

    /**
     * creates a new shot object that will be shown for one frame.
     * @param turretLoc - the location of the center of the turret
     * @param invLoc - the location of the center of the invader
     * @param col - the color of this shot.
     */
    public void addShot(int[] turretLoc, double[] invLoc, Color col)
    {
        shotList.add(new Shot(turretLoc, invLoc, col));
    }

    /**
     * adds the given turret to the screen, replacing any turret that is at the same location.
     * @param newTurret - the turret to add
     * @param x - the x location where this turret should go
     * @param y - the y location where this turret should go
     */
    public void addTurret(Turret newTurret, int x, int y)
    {
        // remove any turrets in turretList that are already at x, y.
        for (int i=turretList.size()-1; i>=0; i-- )
        {
            int[] loc = turretList.get(i).getMyLoc();
            if (x==loc[0] && y==loc[1])
            {
                turretList.remove(i);
            }
        }

        // append this turret to the turretList, and set its location to (x,y).
        turretList.add(newTurret);
        newTurret.setMyLoc(x, y);
    }
}
