package prog1.kotprog.dontstarve.solution.inventory.items;

//import prog1.kotprog.dontstarve.solution.exceptions.NotImplementedException;

/**
 * Felvehető / kézbe vehető item leírására szolgáló osztály.
 */
public abstract class EquippableItem extends AbstractItem {
    /**
     * Az item tartóssága
     */
    private float durability;
    /**
     * Az item maximális tartóssága
     */
    private float maxDurability;
    /**
     * Konstruktor, amellyel a tárgy létrehozható.
     *
     * @param type   az item típusa
     */
    public EquippableItem(ItemType type) {
        super(type, 1);
    }

    public EquippableItem(ItemType type, int maxDurability, int durability) {
        super(type, 1);
        this.maxDurability = maxDurability;
        this.durability = durability;
    }

    /**
     * Megadja, hogy milyen állapotban van a tárgy.
     * @return a tárgy használatlansága, %-ban (100%: tökéletes állapot)
     */
    public float percentage() {
        return (durability / maxDurability) * 100;
    }
}
