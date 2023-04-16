package prog1.kotprog.dontstarve.solution.character;

import java.util.List;

import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.utility.Position;

import java.util.ArrayList;

public class Character implements BaseCharacter {
    private List<BaseCharacter> characters;
    private String name;
    private float speed;
    private float hunger;
    private float hp;

    public Character(String name, float speed, float hunger, float hp) {
        this.name = name;
        this.speed = hunger;
        this.hunger = hunger;
        this.hp = hp;
        characters = new ArrayList<>();
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

    public void addCharacter(BaseCharacter character) {
        characters.add(character);
    }

    @Override
    public BaseInventory getInventory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventory'");
    }

    @Override
    public Position getCurrentPosition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentPosition'");
    }

    @Override
    public Action getLastAction() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLastAction'");
    }
}