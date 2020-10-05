package observers;

import items.HealthPotion;
import items.ManaPotion;
import items.MinorHealthPotion;
import items.MinorManaPotion;
import player.AbstractPlayer;

import javax.swing.*;

public class playerObserver extends JLabel implements Observer  {
    private AbstractPlayer tempPlayer;

    public playerObserver(AbstractPlayer player){
        this.tempPlayer = player;
        player.register(this);
    }
    @Override
    public void update(AbstractPlayer player) {
        this.tempPlayer = player;
        String playerWeapon;
        if(tempPlayer.getEquippedItems().stream().findFirst().orElse(null) != null){
            playerWeapon = tempPlayer.getEquippedItems().stream().findFirst().orElse(null).getItemName();
        }
        else
            playerWeapon = "No Weapon Equipped";
        setText("<html>"+tempPlayer.getPlayerName()+"<br/>" +
                tempPlayer.toString() +"<br/>" +
                "HP: " + tempPlayer.getCurrentHitPoints()+" / " + tempPlayer.getMaxHP()+"<br/>" +
                "MP: " + tempPlayer.getCurrentManaPoints()+" / " + tempPlayer.getMaxMana()+"<br/>" +
                "Weapon: " + playerWeapon+"<br/>" +
                "Strength: " + tempPlayer.getStrength()+"<br/> " +
                "Damage: " + tempPlayer.getAttackDamage()+"<br/> " +
                "Intelligence: " + tempPlayer.getIntelligence()+"<br/> " +
                "Potions: " + tempPlayer.getInventory().stream().filter(x -> x instanceof HealthPotion || x instanceof MinorHealthPotion).count()+" (H) / "+ tempPlayer.getInventory().stream().filter(x -> x instanceof ManaPotion || x instanceof MinorManaPotion).count()+" (M) <br/> " +
                "</html>");
    }

}
