package items;

import java.util.List;

public class MinorHealthPotion implements Usable {
    private final String itemName = "Minor Health Potion";
    private List<ItemEffect> itemEffectsList = List.of( new ItemEffect(EffectType.HP_REPLENISH, 5));;

    public String getItemName() {
        return itemName;
    }

    @Override
    public int use(){
        int amountEffect = 0 ;
        for (ItemEffect e : itemEffectsList) {
            amountEffect = e.getAmountEffect();
        }
        return amountEffect;
    }

    @Override
    public List<ItemEffect> getItemEffects() {
        return this.itemEffectsList;
    }
}
