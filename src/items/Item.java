package items;

import java.util.List;

public interface Item {
public abstract List<ItemEffect> getItemEffects();
public abstract String getItemName();

}
