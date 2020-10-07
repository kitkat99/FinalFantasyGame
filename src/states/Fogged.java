package states;

import tower.AbstractBlock;

import java.awt.*;

public class Fogged implements StateBlock {
    AbstractBlock block;
    public Fogged(AbstractBlock block){
        this.block = block;
    }
    @Override
    public Color stateColor() {
        return new Color(150, 60, 60);
    }
}
