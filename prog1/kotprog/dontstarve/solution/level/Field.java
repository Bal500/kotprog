package prog1.kotprog.dontstarve.solution.level;

import java.util.ArrayList;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemFire;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemLog;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawBerry;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemRawCarrot;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemStone;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemTwig;

/**
 * A BaseField interface implementációja.
 */
public class Field implements BaseField {
    /**
     * Színek hex formátumú tárolására szolgáló változó.
     */
    private final int hex;

    /**
     * A pálya x koordinátáját tároló változó.
     */
    private final int x;

    /**
     * A pálya y koordinátáját tároló változó.
     */
    private final int y;

    /**
     * A field osztály konstruktora.
     * @param hex az adott mező színe
     * @param x az adott mező x koordinátája
     * @param y az adott mező y koordinátája
     */
    public Field(int hex, int x, int y) {
        this.hex = hex;
        this.x = x;
        this.y = y;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mező járható-e.
     * @return igaz, amennyiben a mező járható; hamis egyébként
     */
    @Override
    public boolean isWalkable() {
        return hex != MapColors.WATER;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e fa.
     * @return igaz, amennyiben van fa; hamis egyébként
     */
    @Override
    public boolean hasTree() {
        return hex == MapColors.TREE;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e kő.
     * @return igaz, amennyiben van kő; hamis egyébként
     */
    @Override
    public boolean hasStone() {
        return hex == MapColors.STONE;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e gally.
     * @return igaz, amennyiben van gally; hamis egyébként
     */
    @Override
    public boolean hasTwig() {
        return hex == MapColors.TWIG;
    }

    /**
     * Ezen metódus segítségével lekérdezheő, hogy a mezőn van-e bogyó.
     * @return igaz, amennyiben van bogyó; hamis egyébként
     */
    @Override
    public boolean hasBerry() {
        return hex == MapColors.BERRY;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e répa.
     * @return igaz, amennyiben van répa; hamis egyébként
     */
    @Override
    public boolean hasCarrot() {
        return hex == MapColors.CARROT;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy a mezőn van-e tűz rakva.
     * @return igaz, amennyiben van tűz; hamis egyébként
     */
    @Override
    public boolean hasFire() {
        return hex == MapColors.FIRE;
    }

    /**
     * Ezen metódus segítségével a mezőn lévő tárgyak lekérdezhetők.<br>
     * A tömbben az a tárgy jön hamarabb, amelyik korábban került az adott mezőre.<br>
     * A karakter ha felvesz egy tárgyat, akkor a legkorábban a mezőre kerülő tárgyat fogja felvenni.<br>
     * Ha nem sikerül felvenni, akkor a (nem) felvett tárgy a tömb végére kerül.
     * @return a mezőn lévő tárgyak
     */
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

    /**
     * A hex formátumú mező szín gettere.
     * @return az adott mező színe
     */
    public int getHex() {
        return hex;
    }

    /**
     * Az adott mező x koordinátájának gettere.
     * @return x koordináta
     */
    public int getXCoordinate() {
        return this.x;
    }

    /**
     * Az adott mező y koordinátájának gettere.
     * @return y koordináta
     */
    public int getYCoordinate() {
        return this.y;
    }
}
