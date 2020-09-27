package items;

import java.util.List;

public class HealthPotion implements Usable {

    private final String itemName = "Health Potion";
    private int usesLeft = 1;
    private List<ItemEffect> itemEffectsList = List.of( new ItemEffect(EffectType.HP_REPLENISH, 20));;

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
