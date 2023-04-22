package prog1.kotprog.dontstarve.solution.inventory.items;

/**
 * Az AbstractItemből létrehozott Item típus.
 */
public class Item extends AbstractItem {
    /**
     * Az Item osztály konstruktora.
     * @param type az item típusa
     * @param amount az item mennyisége
     */
    public Item(ItemType type, int amount) {
        super(type, amount);
    }
}
