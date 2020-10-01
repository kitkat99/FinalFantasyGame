package items;

import java.util.List;

public class ManaPotion implements Usable {

    private final String itemName = "Mana Potion";
    private List<ItemEffect> itemEffectsList = List.of( new ItemEffect(EffectType.MANA_REPLENISH, 20));;

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
