package prog1.kotprog.dontstarve.solution.character;


import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.character.actions.ActionAttack;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.utility.Position;

/**
 * A BaseCharacter implementációjára létrehozott osztály.<br>
 * A karakterek adatait és tulajdonsgágait kezeli és tárolja.
 */
public class Character implements BaseCharacter {
    /**
     * A karakter inventoryja.
     */
    private final BaseInventory inventory;

    /**
     * A karakter pozíciója.
     */
    private final Position currentPosition;

    /**
     * A karakter által utoljára végrehajtott akció.
     */
    private final Action lastAction;

    /**
     * A karakter neve.
     */
    private final String name;

    /**
     * A karatker sebessége.
     */
    private float speed;

    /**
     * A karaktér éhségét jelöli.<br>
     * Ha 100, akkor a karakter nem éhes.
     */
    private final float hunger;

    /**
     * A karakter életereje.<br>
     * Ha 100, akkor az életereje maximumon van.
     */
    private final float hp;

    /**
     * A karakter szélességi koordinátáját tároló változó.
     */
    private int x;

    /**
     * A karakter magassági koordinátáját tároló változó.
     */
    private int y;

    /**
     * A karakter osztály konstruktora.
     * @param name a karakter neve
     * @param x a karakter szélességi koordinátája
     * @param y a karakter magassági koordinátája
     */
    public Character(String name, int x, int y) {
        this.name = name;
        this.speed = 1.0f;
        this.hunger = 100.0f;
        this.hp = 100.f;
        this.inventory = new Inventory();
        lastAction = new ActionAttack();
        currentPosition = new Position(x, y);
    }

    /**
     * A karakter mozgási sebességének lekérdezésére szolgáló metódus.
     * @return a karakter mozgási sebessége
     */
    @Override
    public float getSpeed() {
        float speedMultiplier = 1.0f;
        float hp = getHp();
        float hunger = getHunger();
    
        if (hp >= 50 && hp <= 100) {
            speedMultiplier *= 1;
        } else if (hp >= 30) {
            speedMultiplier *= 0.9;
        } else if (hp >= 10) {
            speedMultiplier *= 0.75;
        } else if (hp > 0) {
            speedMultiplier *= 0.6;
        }
    
        if (hunger >= 50 && hunger <= 100) {
            speedMultiplier *= 1;
        } else if (hunger >= 20) {
            speedMultiplier *= 0.9;
        } else if (hunger > 0) {
            speedMultiplier *= 0.8;
        } else {
            speedMultiplier *= 0.5;
        }
    
        return this.speed * speedMultiplier;
    }

    /**
     * A karakter jóllakottságának mértékének lekérdezésére szolgáló metódus.
     * @return a karakter jóllakottsága
     */
    @Override
    public float getHunger() {
        return this.hunger;
    }

    /**
     * A karakter életerejének lekérdezésére szolgáló metódus.
     * @return a karakter életereje
     */
    @Override
    public float getHp() {
        return this.hp;
    }

    /**
     * A játékos nevének lekérdezésére szolgáló metódus.
     * @return a játékos neve
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * A karakter inventory-jának lekérdezésére szolgáló metódus.
     * <br>
     * A karakter inventory-ja végig ugyanaz marad, amelyet referencia szerint kell visszaadni.
     * @return a karakter inventory-ja
     */
    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    /**
     * A játékos aktuális pozíciójának lekérdezésére szolgáló metódus.
     * @return a játékos pozíciója
     */
    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Az utolsó cselekvés lekérdezésére szolgáló metódus.
     * <br>
     * Egy létező Action-nek kell lennie, nem lehet null.
     * @return az utolsó cselekvés
     */
    @Override
    public Action getLastAction() {
        return lastAction;
    }

    /**
     * Az x koordináta gettere.
     * @return x koordináta
     */
    public int getX2() {
        return this.x;
    }

    /**
     * Az y koordináta gettere.
     * @return y koordináta
     */
    public int getY2() {
        return this.y;
    }
}
