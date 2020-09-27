package items;

import java.util.List;

public class Trap implements Usable {
    private final String itemName = "Trap";
    private int usesLeft = 1;
    private List<ItemEffect> itemEffectsList = List.of( new ItemEffect(EffectType.HP_REPLENISH, -5));;

    public String getItemName() {
        return itemName;
    }

    @Override
    public int usesLeft() {
        return usesLeft;
    }
    @Override
    public List<ItemEffect> use(){
        usesLeft--;
        return itemEffectsList;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return this.itemEffectsList;
    }
}
