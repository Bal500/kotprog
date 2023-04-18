package prog1.kotprog.dontstarve.solution.level;

import java.util.ArrayList;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemFire;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemLog;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawCarrot;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemStone;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTwig;

public class Field implements BaseField {
    private int hex;
    private int x;
    private int y;

    public Field(int hex, int x, int y) {
        this.hex = hex;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean isWalkable() {
        return hex != MapColors.WATER;
    }

    @Override
    public boolean hasTree() {
        return hex == MapColors.TREE;
    }

    @Override
    public boolean hasStone() {
        return hex == MapColors.STONE;
    }

    @Override
    public boolean hasTwig() {
        return hex == MapColors.TWIG;
    }

    @Override
    public boolean hasBerry() {
        return hex == MapColors.BERRY;
    }

    @Override
    public boolean hasCarrot() {
        return hex == MapColors.CARROT;
    }

    @Override
    public boolean hasFire() {
        return hex == MapColors.FIRE;
    }

    @Override
    public AbstractItem[] items() {
        ArrayList<AbstractItem> itemsOnField = new ArrayList<>();
        
        if (hasBerry()) {
            itemsOnField.add(new ItemRawBerry(1));
        }
        if (hasCarrot()) {
            itemsOnField.add(new ItemRawCarrot(1));
        }
        if (hasTwig()) {
            itemsOnField.add(new ItemTwig(1));
        }
        if (hasStone()) {
            itemsOnField.add(new ItemStone(1));
        }
        if (hasTree()) {
            itemsOnField.add(new ItemLog(1));
        }
        if (hasFire()) {
            itemsOnField.add(new ItemFire());
        }
        
        return itemsOnField.toArray(new AbstractItem[0]);
    }

    public int getHex() {
        return hex;
    }
}