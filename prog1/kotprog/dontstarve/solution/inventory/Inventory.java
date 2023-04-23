package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemManager;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

import java.util.Set;
import java.util.HashSet;

/**
 * A BaseInventory interface implementációjához használt osztály.
 */
public class Inventory implements BaseInventory {
    private final Set<Integer> queriedItems;
    private AbstractItem[] inventoryManager;
    private EquippableItem hand;

    /**
     * Az inventory default konstruktora.
     */
    public Inventory() {
        inventoryManager = new AbstractItem[10];
        hand = null;
        queriedItems = new HashSet<>();
    }

    /**
     * Segítségével lekérdezhetjük, hogy az adott index valóban létezik-e az inventoryn.
     * @param index az index, amire kíváncsiak vagyunk az inventoryból
     * @return true, ha az index érvényes
     */
    public boolean isValidIndex(int index) {
        return (index >= 0 && index < 9);
    }

    /**
     * Megmondja, hogy az adott item stackelhető vagy sem.
     * @param stackable a stackelni kívánt tárgy
     * @return true, ha az item stackelhető
     */
    public boolean isStackable(ItemType stackable) {
        return !(stackable == ItemType.AXE || stackable == ItemType.PICKAXE || stackable == ItemType.TORCH || stackable == ItemType.SPEAR);
    }

    /**
     * Megmondja, hogy az adott tárgyat meg lehet-e enni vagy sem.
     * @param edible az eflogyasztandó tárgy
     * @return true, ha az item valóban ehető
     */
    public boolean isEdible(ItemType edible) {
        return (edible == ItemType.RAW_BERRY || edible == ItemType.RAW_CARROT || edible == ItemType.COOKED_BERRY || edible == ItemType.COOKED_CARROT);
    }

    /**
     * Megmondja, hogy az adott tárgyat meg lehet-e főzni vagy sem.
     * @param cookable a megfőzendő tárgy
     * @return true, ha az item valóban megfőzhető
     */
    public boolean isCookable(ItemType cookable) {
        return (isEdible(cookable) && cookable != ItemType.COOKED_BERRY && cookable != ItemType.COOKED_CARROT);
    }

    /**
     * Egy adott típusú item maximális stack méretét adja vissza.<br>
     * @param itemType a lekérdezendő item típusa
     * @return a lekérdezett item maximális stackmérete
     */
    public int getMaxAmount(ItemType itemType) {
        switch (itemType) {
            case LOG:
                return 15;
            case TWIG:
                return 20;
            case STONE:
            case RAW_CARROT:
            case COOKED_CARROT:
            case RAW_BERRY:
            case COOKED_BERRY:
                return 10;
            default:
                return 1;
        }
    }

    /**
     * Egy item hozzáadása az inventory-hoz.<br>
     * Ha a hozzáadni kívánt tárgy halmozható, akkor a meglévő stack-be kerül (ha még fér, különben új stacket kezd),
     * egyébként a legelső új helyre kerül.<br>
     * Ha egy itemből van több megkezdett stack, akkor az inventory-ban hamarabb következőhöz adjuk hozzá
     * (ha esetleg ott nem fér el mind, akkor azt feltöltjük, és utána folytatjuk a többivel).<br>
     * Ha az adott itemből nem fér el mind az inventory-ban, akkor ami elfér azt adjuk hozzá, a többit pedig nem
     * (ebben az esetben a hívó félnek tudnia kell, hogy mennyit nem sikerült hozzáadni).
     * @param item a hozzáadni kívánt tárgy
     * @return igaz, ha sikerült hozzáadni a tárgyat teljes egészében; hamis egyébként
     */
    @Override
    public boolean addItem(AbstractItem item) {
        if (item == null) {
            return false;
        }

        boolean itemAdded = false;
        int emptySlot = -1;
        int maxStackAmount = getMaxAmount(item.getType());

        for (int i = 0; i < inventoryManager.length; i++) {
            AbstractItem currentItem = inventoryManager[i];
            if (currentItem != null) {
                if (currentItem.getType() == item.getType() && currentItem.getAmount() < maxStackAmount) {
                    int spaceInStack = maxStackAmount - currentItem.getAmount();
                    int amountToAdd = Math.min(item.getAmount(), spaceInStack);
                    currentItem.setAmount(currentItem.getAmount() + amountToAdd);
                    inventoryManager[i] = item;
                    inventoryManager[i].setAmount(currentItem.getAmount() + amountToAdd);
                    item.setAmount(item.getAmount() - amountToAdd);
                    if (item.getAmount() == 0) {
                        itemAdded = true;
                        return true;
                    }
                }
            } else if (emptySlot == -1) {
                emptySlot = i;
            }
        }

        if (!itemAdded && emptySlot != -1) {
            if (item.getAmount() <= maxStackAmount) {
                inventoryManager[emptySlot] = item;
                itemAdded = true;
            } else if (itemAdded || emptySlot == -1){
                ItemManager stackableItem = new ItemManager(item.getType(), maxStackAmount);
                stackableItem.setAmount(maxStackAmount);
                inventoryManager[emptySlot] = stackableItem;
                item.setAmount(item.getAmount() - maxStackAmount);
            }
        }

        if (!itemAdded) {
            for (int i = 0; i < inventoryManager.length; i++) {
                AbstractItem currentItem = inventoryManager[i];
                if (currentItem != null && currentItem.getAmount() < maxStackAmount) {
                    int spaceInStack = maxStackAmount - currentItem.getAmount();
                    int amountToAdd = Math.min(item.getAmount(), spaceInStack);
                    currentItem.setAmount(currentItem.getAmount() + amountToAdd);
                    item.setAmount(item.getAmount() - amountToAdd);
                    inventoryManager[i] = item;
                    if (item.getAmount() == 0) {
                        itemAdded = true;
                        return true;
                    }
                }
            }
        }
        return itemAdded;
    }

    /**
     * Egy tárgy kidobása az inventory-ból.
     * Hatására a tárgy eltűnik az inventory-ból.
     * @param index a slot indexe, amelyen lévő itemet szeretnénk eldobni
     * @return az eldobott item
     */
    @Override
    public AbstractItem dropItem(int index) {
        AbstractItem actual = null;
        if (index >= 0 && index < inventoryManager.length) {
            actual = inventoryManager[index];
            inventoryManager[index] = null;
        }
        return actual;
    }

    /**
     * Bizonyos mennyiségű, adott típusú item törlése az inventory-ból. A törölt itemek véglegesen eltűnnek.<br>
     * Ha nincs elegendő mennyiség, akkor nem történik semmi.<br>
     * Az item törlése a legkorábban lévő stackből (stackekből) történik, akkor is, ha van másik megkezdett stack.<br>
     * @param type a törlendő item típusa
     * @param amount a törlendő item mennyisége
     * @return igaz, amennyiben a törlés sikeres volt
     */
    @Override
    public boolean removeItem(ItemType type, int amount) {
        int totalAmount = 0;
        for (AbstractItem invMan : inventoryManager) {
            AbstractItem item = invMan;
            if (item != null && item.getType() == type) {
                totalAmount += item.getAmount();
            }
        }

        if (totalAmount < amount) {
            return false;
        }

        for (int i = 0; i < inventoryManager.length && amount > 0; i++) {
            AbstractItem item = inventoryManager[i];
            if (item != null && item.getType() == type) {
                int currentAmount = item.getAmount();
                if (currentAmount <= amount) {
                    inventoryManager[i] = null;
                    amount -= currentAmount;
                } else {
                    item.setAmount(currentAmount - amount);
                    amount = 0;
                }
            }
        }
        return true;
    }

    /**
     * Két item pozíciójának megcserélése az inventory-ban.<br>
     * Csak akkor használható, ha mind a két pozíción már van item.
     * @param index1 a slot indexe, amelyen az első item található
     * @param index2 a slot indexe, amelyen a második item található
     * @return igaz, ha sikerült megcserélni a két tárgyat; hamis egyébként
     */
    @Override
    public boolean swapItems(int index1, int index2) {
        if (!isValidIndex(index1)) {
            return false;
        } else if (!isValidIndex(index2)) {
            return false;
        }

        AbstractItem item1 = inventoryManager[index1];
        AbstractItem item2 = inventoryManager[index2];

        if (item1 == null || item2 == null) {
            return false;
        }

        if (item1 != null && item2 != null) {
            if (item1.getType() == item2.getType()) {
                int totalAmount = item1.getAmount() + item2.getAmount();
                int maxStackAmount = getMaxAmount(item1.getType());
                if (totalAmount <= maxStackAmount) {
                    item1.setAmount(totalAmount);
                    inventoryManager[index2] = null;
                    return true;
                } else {
                    ItemManager stackableItem = new ItemManager(item1.getType(), maxStackAmount);
                    stackableItem.setAmount(maxStackAmount);
                    item1.setAmount(totalAmount - maxStackAmount);
                    inventoryManager[index2] = stackableItem;
                    return true;
                }
            } else {
                inventoryManager[index1] = item2;
                inventoryManager[index2] = item1;
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * Egy item átmozgatása az inventory egy másik pozíciójára.<br>
     * Csak akkor használható, ha az eredeti indexen van tárgy, az új indexen viszont nincs.
     * @param index a mozgatni kívánt item pozíciója az inventory-ban
     * @param newIndex az új pozíció, ahova mozgatni szeretnénk az itemet
     * @return igaz, ha sikerült a mozgatás; hamis egyébként
     */
    @Override
    public boolean moveItem(int index, int newIndex) {
        if (!isValidIndex(index)) {
            return false;
        } else if (!isValidIndex(newIndex)) {
            return false;
        } else if (index == newIndex) {
            return false;
        } else if (inventoryManager[index] == null) {
            return false;
        } else if (inventoryManager[newIndex] != null) {
            return false;
        }

        AbstractItem toMove = inventoryManager[index];
        inventoryManager[index] = null;
        inventoryManager[newIndex] = toMove;
        return true;
    }

    /**
     * Két azonos típusú tárgy egyesítése.<br>
     * Csak stackelhető tárgyakra használható. Ha a két stack méretének összege a maximális stack méreten belül van,
     * akkor az egyesítés az első pozíción fog megtörténni. Ha nem, akkor az első pozíción lévő stack maximálisra
     * töltődik, a másikon pedig a maradék marad.<br>
     * @param index1 első item pozíciója az inventory-ban
     * @param index2 második item pozíciója az inventory-ban
     * @return igaz, ha sikerült az egyesítés (változott valami a művelet hatására)
     */
    @Override
    public boolean combineItems(int index1, int index2) {
        if (isValidIndex(index1) && isValidIndex(index2) && index1 != index2 && inventoryManager[index1] != null && inventoryManager[index2] != null && inventoryManager[index1].getType() == inventoryManager[index2].getType() && !(inventoryManager[index1] instanceof EquippableItem) && !(inventoryManager[index2] instanceof EquippableItem)) {
            int amountCheck1 = inventoryManager[index1].getAmount();
            int amountCheck2 = inventoryManager[index2].getAmount();
            if (amountCheck1 + amountCheck2 < getMaxAmount(inventoryManager[index1].getType())) {
                inventoryManager[index1].setAmount(inventoryManager[index1].getAmount() + amountCheck2);
                inventoryManager[index2] = null;
                return true;
            } else if (amountCheck1 + amountCheck2 == getMaxAmount(inventoryManager[index1].getType())) {
                inventoryManager[index1].setAmount(getMaxAmount(inventoryManager[index1].getType()));
                inventoryManager[index2].setAmount(getMaxAmount(inventoryManager[index1].getType()));
                inventoryManager[index2] = null;
                return true;
            } else if (amountCheck1 + amountCheck2 > getMaxAmount(inventoryManager[index1].getType())) {
                int leftOver = getMaxAmount(inventoryManager[index1].getType()) - inventoryManager[index1].getAmount();
                inventoryManager[index1].setAmount(getMaxAmount(inventoryManager[index1].getType()));
                inventoryManager[index2].setAmount(amountCheck2 - leftOver);
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Egy item felvétele a karakter kezébe.<br>
     * Csak felvehető tárgyra használható. Ilyenkor az adott item a karakter kezébe kerül.
     * Ha a karakternek már tele volt a keze, akkor a kezében lévő item a most felvett item helyére kerül
     * (tehát gyakorlatilag helyet cserélnek).
     * @param index a kézbe venni kívánt tárgy pozíciója az inventory-ban
     * @return igaz, amennyiben az itemet sikerült a kezünkbe venni
     */
    @Override
    public boolean equipItem(int index) {
        if (index >= 0 && index <= inventoryManager.length - 1 && inventoryManager[index] != null && inventoryManager[index] instanceof EquippableItem) {
            if (hand == null) {
                hand = (EquippableItem) inventoryManager[index];
                inventoryManager[index] = null;
                return true;
            } else {
                AbstractItem temp = inventoryManager[index];
                inventoryManager[index] = hand;
                hand = (EquippableItem) temp;
                return true;
            }
        }
        return false;
    }

    /**
     * A karakter kezében lévő tárgy inventory-ba helyezése.<br>
     * A karakter kezében lévő item az inventory első szabad pozíciójára kerül.
     * Ha a karakternek üres volt a keze, akkor nem történik semmi.<br>
     * Ha nincs az inventory-ban hely, akkor a levett item a pálya azon mezőjére kerül, ahol a karakter állt.
     * @return a levetett item, amennyiben az nem fért el az inventory-ban; null egyébként
     */
    @Override
    public EquippableItem unequipItem() {
        if (hand == null) {
            return null;
        }

        if (emptySlots() != 0) {
            for (int i = 0; i < inventoryManager.length; i++) {
                if (inventoryManager[i] == null) {
                    inventoryManager[i] = hand;
                    hand = null;
                    return null;
                }
            }
        } else {
            EquippableItem unequipped = hand;
            hand = null;
            return unequipped;
        }
        return hand;
    }

    /**
     * Egy item megfőzése.<br>
     * Csak nyers étel főzhető meg. Hatására az inventory adott pozíciójáról 1 egységnyi eltűnik.
     * @param index A megfőzni kívánt item pozíciója az inventory-ban
     * @return A megfőzni kívánt item típusa
     */
    @Override
    public ItemType cookItem(int index) {
        AbstractItem item = inventoryManager[index];
        ItemType itemType = getItemType(item);

        if (!isCookable(itemType)) {
            return null;
        }

        eatItem(index);

        return itemType;
    }

    /**
     * Egy item elfogyasztása.<br>
     * Csak ételek ehetők meg. Hatására az inventory adott pozíciójáról 1 egységnyi eltűnik.
     * @param index A megenni kívánt item pozíciója az inventory-ban
     * @return A megenni kívánt item típusa
     */
    @Override
    public ItemType eatItem(int index) {
        if (!validateIndex(index)) {
            return null;
        }

        AbstractItem item = inventoryManager[index];
        ItemType itemType = getItemType(item);

        if (!isEdible(itemType)) {
            return null;
        }

        if (item.getAmount() - 1 == 0) {
            inventoryManager[index] = null;
        } else {
            item.setAmount(item.getAmount() - 1);
        }

        return itemType;
    }

    private boolean validateIndex(int index) {
        return index >= 0 && index < inventoryManager.length && inventoryManager[index] != null;
    }

    private ItemType getItemType(AbstractItem item) {
        return item.getType();
    }


    @Override
    /**
     * A rendelkezésre álló üres inventory slotok számának lekérdezése.
     * @return az üres inventory slotok száma
     */
    public int emptySlots() {
        int counter = 0;
        for (AbstractItem invMan : inventoryManager) {
            if (invMan == null) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    /**
     * Az aktuálisan viselt tárgy lekérdezése.<br>
     * Ha a karakter jelenleg egy tárgyat sem visel, akkor null.<br>
     * @return Az aktuálisan viselt tárgy
     */
    public EquippableItem equippedItem() {
        if (hand instanceof EquippableItem) {
            return hand;
        } else {
            return null;
        }
    }

    /**
     * Adott inventory sloton lévő tárgy lekérdezése.<br>
     * Az inventory-ban lévő legelső item indexe 0, a következőé 1, és így tovább.<br>
     * Ha az adott pozíció üres, akkor null.<br>
     * @param index a lekérdezni kívánt pozíció
     * @return az adott sloton lévő tárgy
     */
    @Override
    public AbstractItem getItem(int index) {
        if (index < 0 || index >= 9 || inventoryManager[index] == null) {
            return null;
        }

        if (!queriedItems.contains(index)) {
            queriedItems.add(index);
            return inventoryManager[index];
        }

        for (int i = 0; i < queriedItems.size(); i++) {
            if (queriedItems.contains(index)) {
                return inventoryManager[index];
            }
        }

        return null;
    }
}
