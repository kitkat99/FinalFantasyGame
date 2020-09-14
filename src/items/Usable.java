package items;

import java.util.List;

public interface Usable extends Item {
    public abstract int usesLeft();
    public abstract List<ItemEffect> use();
}
