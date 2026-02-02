import java.awt.*;
import java.util.ArrayList;

public class World
{
    private int[][] path = {{20, 380}, {60, 380}, {100, 380}, {100, 340}, {100, 300}, {100, 260}, {100, 220},
        {100, 180}, {100, 140}, {140, 140}, {180, 140}, {220, 140}, {260, 140}, {260, 180}, {260, 220}, {260, 260},
            {220, 260}, {180, 260}, {180, 300}, {180, 340}, {180, 380}, {180, 420}, {180, 460}, {140, 460}, {100, 460}};
    private ArrayList<Invader> invaderList;
    private ArrayList<Turret> turretList;
    private ArrayList<Shot> shotList;
    private int[][] levels = {{4, 3, 2}, {6, 1, 3}, {8, 3, 5}, {0, 0, 6}, {15, 5, 5}};
    private int[] spawnList;
    private int currentLevel;
    private final double SPAWN_LIKELIHOOD = 0.01;

    public World()
    {
        invaderList = new ArrayList<Invader>();

        turretList = new ArrayList<Turret>();

        shotList = new ArrayList<Shot>();
        currentLevel = 0;
        initializeLevel();
    }

    public void initializeLevel()
    {
        spawnList = new int[levels[currentLevel].length];
        for (int i=0; i< levels[currentLevel].length; i++)
            spawnList[i] = levels[currentLevel][i];
    }

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

    public Invader spawnInvader()
    {
        int remainingInvaders = getRemainingInvadersToSpawn();

        if (0 == remainingInvaders)
            return null;

        double rnd = remainingInvaders*Math.random();
        for (int i=0; i<spawnList.length; i++)
        {
            if (rnd < spawnList[i])
            {
                spawnList[i] -= 1;
                Invader nextInvader = new Invader(i, this);
                for (int j=0; j<currentLevel; j++)
                    nextInvader.levelUp();
                return nextInvader;
            }
            else
                rnd -= spawnList[i];
        }
        throw new RuntimeException("randomizing spawn didn't work.");
    }

    public int getRemainingInvadersToSpawn()
    {
        int remainingInvaders = 0;
        for (int count: spawnList)
            remainingInvaders+=count;
        return remainingInvaders;
    }

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

    public void drawInvaders(Graphics g)
    {
        for (Invader inv: invaderList)
            inv.drawSelf(g);
    }

    public void drawShots(Graphics g)
    {
        for (Shot s:shotList)
            s.drawSelf((Graphics2D)g);
        shotList.clear();
    }

    public void drawTurrets(Graphics g)
    {
        for (Turret tur: turretList)
            tur.drawSelf(g);
    }


    public int[][] getPath() { return path;}

    public ArrayList<Invader> getInvaderList() {return invaderList;}

    public void updateAllObjects(int deltaTimeInMS)
    {
        double deltaT = deltaTimeInMS/1000.0;
        boolean anyInvaderReachedEnd = false;
        for (Invader inv: invaderList)
            anyInvaderReachedEnd |= inv.advance(deltaT);
        if (anyInvaderReachedEnd)
        {
            System.out.println("Game over!");
            System.exit(0);
        }

        for (Turret t: turretList)
        {
            t.advance(deltaT);
            t.targetNearestInvader();
        }

        if (Math.random() < SPAWN_LIKELIHOOD)
        {
            Invader newInvader = spawnInvader();
            if (null != newInvader)
                invaderList.add(newInvader);
        }
    }

    public void damageInvader(Invader inv, int damage)
    {
        if (inv != null)
        {
            inv.takeDamage(damage);
            if (inv.isDead())
                invaderList.remove(inv);
        }
    }

    public void addShot(int[] turretLoc, double[] invLoc, Color col)
    {
        shotList.add(new Shot(turretLoc, invLoc, col));
    }

    public void addTurret(Turret newTurret, int x, int y)
    {
        for (int i=turretList.size()-1; i>=0; i-- )
        {
            int[] loc = turretList.get(i).getMyLoc();
            if (x==loc[0] && y==loc[1])
            {
                turretList.remove(i);
            }
        }

        turretList.add(newTurret);
        newTurret.setMyLoc(x, y);
    }
}
