package prog1.kotprog.dontstarve.solution.character;


import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.character.actions.ActionAttack;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.Inventory;
import prog1.kotprog.dontstarve.solution.utility.Position;


public class Character implements BaseCharacter {
    private BaseInventory inventory;
    private Position currentPosition;
    private Action lastAction;
    private String name;
    private float speed;
    private float hunger;
    private float hp;
    private int x;
    private int y;

    public Character(String name, int x, int y) {
        this.name = name;
        this.speed = 1.0f;
        this.hunger = 100.0f;
        this.hp = 100.f;
        this.inventory = new Inventory();
        lastAction = new ActionAttack();
        currentPosition = new Position(x, y);
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public float getHunger() {
        return this.hunger;
    }

    @Override
    public float getHp() {
        return this.hp;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public BaseInventory getInventory() {
        return inventory;
    }

    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }
}