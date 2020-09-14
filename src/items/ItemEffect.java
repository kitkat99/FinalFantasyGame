package items;

public class ItemEffect {
    private EffectType effectType;
    private int amountEffect;

    public ItemEffect(EffectType effectType, int amountEffect) {
        this.amountEffect = amountEffect;
        this.effectType = effectType;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public int getAmountEffect() {
        return amountEffect;
    }
}
