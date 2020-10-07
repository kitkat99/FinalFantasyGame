package states;

import tower.AbstractBlock;

import java.awt.*;

public class Visible implements StateBlock {
    AbstractBlock block;
    public Visible(AbstractBlock block){
        this.block = block;
    }
    @Override
    public Color stateColor() {
        return new Color(180, 100, 100);
    }
}
