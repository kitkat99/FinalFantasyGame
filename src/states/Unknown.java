package states;

import tower.AbstractBlock;

import java.awt.*;

public class Unknown implements StateBlock {
    AbstractBlock block;
    public Unknown(AbstractBlock block){
        this.block = block;
    }
    @Override
    public Color stateColor() {
        return new Color(1, 0, 6);
    }
}
