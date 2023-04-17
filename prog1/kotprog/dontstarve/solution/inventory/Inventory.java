package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.exceptions.NotImplementedException;
import prog1.kotprog.dontstarve.solution.inventory.BaseInventory;
import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.Item;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Inventory implements BaseInventory {
    private Set<Integer> queriedItems;
    private AbstractItem[] inventory;
    private final Map<ItemType, Integer> dictionary;
    private EquippableItem hand;

    public Inventory() {
        inventory = new AbstractItem[10];
        hand = null;
        dictionary = new HashMap<>();
        queriedItems = new HashSet<>();
        dictionary.put(ItemType.AXE, 1);
        dictionary.put(ItemType.PICKAXE, 1);
        dictionary.put(ItemType.TORCH, 1);
        dictionary.put(ItemType.SPEAR, 1);
        dictionary.put(ItemType.LOG, 15);
        dictionary.put(ItemType.STONE, 10);
        dictionary.put(ItemType.TWIG, 20);
        dictionary.put(ItemType.RAW_CARROT, 10);
        dictionary.put(ItemType.COOKED_CARROT, 10);
        dictionary.put(ItemType.RAW_BERRY, 10);
        dictionary.put(ItemType.COOKED_BERRY, 10);
    }

    public boolean isStackable(ItemType stackable) {
        if (stackable != ItemType.AXE || stackable != ItemType.PICKAXE || stackable != ItemType.TORCH || stackable != ItemType.SPEAR) {
            return false;
        }
        return true;
    }

    public boolean isEdible(ItemType edible) {
        if (edible != ItemType.RAW_BERRY || edible != ItemType.RAW_CARROT || edible != ItemType.COOKED_BERRY || edible != ItemType.COOKED_CARROT) {
            return false;
        }
        return true;
    }

    public int getMaxAmount(ItemType itemType) {
        switch (itemType) {
            case LOG:
            case TWIG:
                return 20;
            case RAW_CARROT:
            case COOKED_CARROT:
            case RAW_BERRY:
            case COOKED_BERRY:
                return 10;
            case STONE:
                return 15;
            default:
                return 1;
        }
    }

    @Override
    public boolean addItem(AbstractItem item) {
        if (item == null) {
        return false;
        }
        
        boolean itemAdded = false;
        int emptySlot = -1;
        int maxStackAmount = getMaxAmount(item.getType());

        for (int i = 0; i < inventory.length; i++) {
            AbstractItem currentItem = inventory[i];
            if (currentItem != null) {
                if (currentItem.getType() == item.getType() && currentItem.getAmount() < maxStackAmount) {
                    int spaceInStack = maxStackAmount - currentItem.getAmount();
                    int amountToAdd = Math.min(item.getAmount(), spaceInStack);
                    currentItem.setAmount(currentItem.getAmount() + amountToAdd);
                    item.setAmount(item.getAmount() - amountToAdd);
                    if (item.getAmount() == 0) {
                        itemAdded = true;
                        break;
                    }
                }
            } else if (emptySlot == -1) {
                emptySlot = i;
            }
        }

        if (!itemAdded && emptySlot != -1) {
            if (item.getAmount() <= maxStackAmount) {
                inventory[emptySlot] = item;
                itemAdded = true;
            } else {
                Item stackableItem = new Item(item.getType(), maxStackAmount);
                stackableItem.setAmount(maxStackAmount);
                inventory[emptySlot] = stackableItem;
                item.setAmount(item.getAmount() - maxStackAmount);
            }
        }

        if (!itemAdded) {
            for (int i = 0; i < inventory.length; i++) {
                AbstractItem currentItem = inventory[i];
                if (currentItem != null && currentItem.getAmount() < maxStackAmount) {
                    int spaceInStack = maxStackAmount - currentItem.getAmount();
                    int amountToAdd = Math.min(item.getAmount(), spaceInStack);
                    currentItem.setAmount(currentItem.getAmount() + amountToAdd);
                    item.setAmount(item.getAmount() - amountToAdd);
                    if (item.getAmount() == 0) {
                        itemAdded = true;
                        break;
                    }
                }
            }
        }

        return itemAdded;
    }

    private void setAmount(ItemType itemType, int index, int amount) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null && inventory[i].getType() == itemType && i != index) {
                continue;
            }
            if (inventory[i] != null && inventory[i].getType() == itemType && i == index) {
                inventory[i].setAmount(amount);
                return;
            }
        }
    }


    @Override
    public AbstractItem dropItem(int index) {
        AbstractItem actual = null;
        if (index >= 0 && index < inventory.length) {
            actual = inventory[index];
            inventory[index] = null;
        }
        return actual;
    }

    @Override
    public boolean removeItem(ItemType type, int amount) {
        int amountToRemove = amount;
        int totalAmount = 0;
        for (int i = 0; i < inventory.length; i++) {
            AbstractItem item = inventory[i];
            if (item != null && item.getType() == type) {
                totalAmount += item.getAmount();
            }
        }
    
        if (totalAmount < amountToRemove) {
            return false;
        }
    
        for (int i = 0; i < inventory.length && amountToRemove > 0; i++) {
            AbstractItem item = inventory[i];
            if (item != null && item.getType() == type) {
                int currentAmount = item.getAmount();
                if (currentAmount <= amountToRemove) {
                    inventory[i] = null;
                    amountToRemove -= currentAmount;
                } else {
                    item.setAmount(currentAmount - amountToRemove);
                    amountToRemove = 0;
                }
            }
        }
        return true;
    }    

    @Override
    public boolean swapItems(int index1, int index2) {
        boolean isValidIndex = index1 >= 0 && index1 < inventory.length && index2 >= 0 && index2 < inventory.length;
        if (!isValidIndex) {
            return false;
        }
    
        AbstractItem item1 = inventory[index1];
        AbstractItem item2 = inventory[index2];
    
        if (item1 == null && item2 == null) {
            return false;
        }
    
        if (item1 != null && item2 != null) {
            if (item1.getType() == item2.getType()) {
                int totalAmount = item1.getAmount() + item2.getAmount();
                int maxStackAmount = getMaxAmount(item1.getType());
                if (totalAmount <= maxStackAmount) {
                    item1.setAmount(totalAmount);
                    inventory[index2] = null;
                    return true;
                } else {
                    Item stackableItem = new Item(item1.getType(), maxStackAmount);
                    stackableItem.setAmount(maxStackAmount);
                    item1.setAmount(totalAmount - maxStackAmount);
                    inventory[index2] = stackableItem;
                    return true;
                }
            } else {
                inventory[index1] = item2;
                inventory[index2] = item1;
                return true;
            }
        } else if (item1 == null && item2 != null) {
            inventory[index1] = item2;
            inventory[index2] = null;
            return true;
        } else if (item1 != null && item2 == null) {
            inventory[index1] = null;
            inventory[index2] = item1;
            return true;
        } else {
            return false;
        }
    }
    
    

    @Override
    public boolean moveItem(int index, int newIndex) {
        boolean isValidIndex = index >= 0 && index < inventory.length && newIndex >= 0 && newIndex < inventory.length;
        if (!isValidIndex) {
            return false;
        }
        if (inventory[index] == null) {
            return false;
        }
        if (inventory[newIndex] != null) {
            return false;
        }
        AbstractItem toMove = inventory[index];
        inventory[index] = null;
        inventory[newIndex] = toMove;
        return true;
    }

    @Override
    public boolean combineItems(int index1, int index2) {
        AbstractItem item1 = inventory[index1];
        AbstractItem item2 = inventory[index2];

        if (item1 == null || item2 == null || !isStackable(item1.getType()) || item1.getType() != item2.getType()) {
            return false;
        }

        int sumAmount = item1.getAmount() + item2.getAmount();
        int maxAmount = getMaxAmount(item1.getType());

        if (sumAmount <= maxAmount) {
            item1.setAmount(sumAmount);
            inventory[index2] = null;
            return true;
        } else {
            item1.setAmount(maxAmount);
            item2.setAmount(sumAmount - maxAmount);
            return true;
        }
    }

    

    @Override
    public boolean equipItem(int index) {
        if (index >= 0 && index <= inventory.length - 1) {
            if (inventory[index] != null && inventory[index] instanceof EquippableItem) {
                if (hand == null) {
                    hand = (EquippableItem) inventory[index];
                    inventory[index] = null;
                    return true;
                } else {
                    AbstractItem temp = inventory[index];
                    inventory[index] = hand;
                    hand = (EquippableItem) temp;
                    return true;
                }
            }
        }
        return false;
    }    

    @Override
    public EquippableItem unequipItem() {
        EquippableItem item = hand;
    
        if (item == null) {
            return null;
        }
    
        int slot = -1;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                slot = i;
                break;
            }
        }
    
        if (slot == -1) {
            dropItem(slot);
            hand = null;
            return null;
        }
    
        inventory[slot] = hand;
        hand = null;
        return item;
    }
    

    @Override
    public ItemType cookItem(int index) {
        AbstractItem item = inventory[index];
        if (item == null) {
            return null;
        }
        ItemType itemType = item.getType();
        if (!isEdible(itemType) || item.getAmount() == 0) {
            return itemType;
        }
        if (itemType == ItemType.RAW_CARROT) {
            itemType = ItemType.COOKED_CARROT;
        } else if (itemType == ItemType.RAW_BERRY) {
            itemType = ItemType.COOKED_BERRY;
        }
        item.setAmount(item.getAmount() - 1);
        return itemType;
    }

    
    
    @Override
    public ItemType eatItem(int index) {
        AbstractItem item = inventory[index];
        if (item == null) {
            return null;
        }
        ItemType type = item.getType();
        if (!isEdible(type)) {
            return null;
        }
        int currentAmount = item.getAmount();
        if (currentAmount == 1) {
            inventory[index] = null;
        } else {
            item.setAmount(currentAmount - 1);
        }
        return type;
    }



    @Override
    public int emptySlots() {
        int counter = 0;
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public EquippableItem equippedItem() {
        if (this.hand != null) {
            if (!isStackable(this.hand.getType())) {
                return this.hand;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public AbstractItem getItem(int index) {
        if (index < 0 || index >= 9 || inventory[index] == null) {
            return null;
        }

        if (!queriedItems.contains(index)) {
            queriedItems.add(index);
            return inventory[index];
        }

        for (int i = 0; i < queriedItems.size(); i++) {
            if (queriedItems.contains(index)) {
                return inventory[index];
            }
        }

        return null;
    }
}
