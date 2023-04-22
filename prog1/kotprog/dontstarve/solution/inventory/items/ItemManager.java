package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * Az itemek managelésének leírására szolgáló osztály.
 */
public class ItemManager extends AbstractItem {
    /**
     * Konstruktor, amellyel a tárgy managelhető.
     *
     * @param type az item típusa
     * @param amount az item mennyisége
     */
    public ItemManager(ItemType type, int amount) {
        super(type, amount);
    }
}
