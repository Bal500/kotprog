package prog1.kotprog.dontstarve.solution.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import prog1.kotprog.dontstarve.solution.exceptions.NotImplementedException;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemAxe;

public class Field implements BaseField {
    private int hex;

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
        throw new NotImplementedException();
    }
    
}
