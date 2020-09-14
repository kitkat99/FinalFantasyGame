package items;

public enum EffectType {NONE(false), MANA_BOOST(false), MANA_REPLENISH(true), HP_BOOST(false), HP_REPLENISH(true), DAMAGE_BOOST(false);
    private boolean useEffect;

    EffectType(boolean b) {
    }

    public boolean getUseEffect() {
        return this.useEffect;
    }
}
