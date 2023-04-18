package prog1.kotprog.dontstarve.solution.inventory;

import prog1.kotprog.dontstarve.solution.inventory.items.AbstractItem;
import prog1.kotprog.dontstarve.solution.inventory.items.EquippableItem;
import prog1.kotprog.dontstarve.solution.inventory.items.Item;
import prog1.kotprog.dontstarve.solution.inventory.items.ItemType;

import java.util.Map;
import java.util.Set;
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

    public boolean isValidIndex(int index) {
        if (index < 0 || index >= 9) {
            return false;
        }
        return true;
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
        if (!isValidIndex(index1) || !isValidIndex(index2)) {
            return false;
        }
    
        AbstractItem item1 = inventory[index1];
        AbstractItem item2 = inventory[index2];
    
        if (item1 == null || item2 == null) {
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
        } else {
            return false;
        }
    }
    
    @Override
    public boolean moveItem(int index, int newIndex) {
        if (!isValidIndex(index) || !isValidIndex(newIndex)) {
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
        if (!isValidIndex(index1) || !isValidIndex(index2)) {
            return false;
        }

        if (inventory[index1] == null || inventory[index2] == null) {
            return false;
        }

        if (inventory[index1] instanceof EquippableItem || !inventory[index1].equals(inventory[index2])) {
            return false;
        }

        int firstIndex = Math.min(index1, index2);
        int lastIndex = Math.max(index1, index2);

        AbstractItem item1 = inventory[firstIndex];
        AbstractItem item2 = inventory[lastIndex];
        int item1Amount = item1.getAmount();
        int item2Amount = item2.getAmount();
        int maxStack = getMaxAmount(item1.getType());

        if (item1Amount == maxStack || index1 == index2) {
            return false;
        }

        if (item1Amount + item2Amount <= maxStack) {
            item1.setAmount(item1Amount + item2Amount);
            inventory[lastIndex] = null;
        } else {
            int leftOver = item1Amount + item2Amount - maxStack;
            item1.setAmount(maxStack);
            inventory[lastIndex].setAmount(leftOver);
        }
        return true;
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
            for (int i = inventory.length - 1; i >= 0; i--) {
                if (inventory[i] != null) {
                    dropItem(i);
                    inventory[i] = null;
                    break;
                }
            }
            inventory[slot] = hand;
            hand = null;
            return null;
        }
    
        inventory[slot] = hand;
        hand = null;
        return null;
    }
    

    @Override
    public ItemType cookItem(int index) {
        if (index < 0 || index >= inventory.length) {
            return null;
        }

        AbstractItem item = inventory[index];
        
        if (item == null) {
            return null;
        }
        
        ItemType itemType = item.getType();
        
        if (!isEdible(itemType) || item.getAmount() == 0 || inventory[index].getType() == ItemType.COOKED_BERRY || inventory[index].getType() == ItemType.COOKED_CARROT) {
            return null;
        }
        
        int stackSize = getMaxAmount(itemType);
        int amount = item.getAmount();
        int remainingAmount = 0;
        ItemType cookedItemType = null;
        
        if (amount <= stackSize) {
            remainingAmount = 0;
            cookedItemType = cookSingleItem(itemType);
        } else {
            int fullStacks = amount / stackSize;
            int partialStack = amount % stackSize;
            remainingAmount = (fullStacks * stackSize) + (partialStack > 0 ? partialStack - 1 : 0);
            cookedItemType = cookSingleItem(itemType);
        }
        
        if (cookedItemType != null) {
            if (remainingAmount > 0) {
                item.setAmount(remainingAmount);
            } else {
                inventory[index] = null;
            }
        }
        return cookedItemType;
    }
    
    private ItemType cookSingleItem(ItemType itemType) {
        switch (itemType) {
            case RAW_CARROT:
                return ItemType.COOKED_CARROT;
            case RAW_BERRY:
                return ItemType.COOKED_BERRY;
            default:
                return null;
        }
    }
    

    
    
    @Override
    public ItemType eatItem(int index) {
        if (!isValidIndex(index)) {
            return null;
        }

        AbstractItem item = inventory[index];
        ItemType type = item.getType();

        if (inventory[index] == null) {
            return type;
        }

        if (item == null || !isEdible(type)) {
            return type;
        }

        int currentAmount = item.getAmount();
        if (currentAmount == 1) {
            inventory[index] = null;
        } else {
            inventory[index].setAmount(currentAmount - 1);
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
