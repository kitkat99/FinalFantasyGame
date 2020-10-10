package observers;

import items.*;
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
        String playerWeaponName = "No Weapon Name";
        int playerWeaponHPBoost = 0;
        int playerWeaponManaBoost = 0;
        int playerWeaponStrengthBoost = 0;
        int playerWeaponIntelligenceBoost = 0;


        if(tempPlayer.getEquippedItems().stream().findFirst().orElse(null) != null){
            playerWeaponName = tempPlayer.getEquippedItems().stream().findFirst().orElse(null).getItemName();
            playerWeaponHPBoost = ((AbstractWeapon) tempPlayer.getEquippedItems().stream().findFirst().orElse(null)).getHpBoost();
            playerWeaponManaBoost = ((AbstractWeapon) tempPlayer.getEquippedItems().stream().findFirst().orElse(null)).getMpBoost();
            playerWeaponStrengthBoost = ((AbstractWeapon) tempPlayer.getEquippedItems().stream().findFirst().orElse(null)).getStrengthBoost();
            playerWeaponIntelligenceBoost = ((AbstractWeapon) tempPlayer.getEquippedItems().stream().findFirst().orElse(null)).getIntellectBoost();
        }

        setText("<html>"+tempPlayer.getPlayerName()+"<br/>" +
                tempPlayer.toString() +"<br/>" +
                "HP: " + tempPlayer.getCurrentHitPoints()+" / " + tempPlayer.getMaxHP()+"<br/>" +
                "MP: " + tempPlayer.getCurrentManaPoints()+" / " + tempPlayer.getMaxMana()+"<br/>" +
                "Weapon: " +playerWeaponName  + "<br/>" +
                "Strength: " + tempPlayer.getStrength()+"<br/> " +
                "Damage: " + tempPlayer.getAttackDamage()+"<br/> " +
                "Intelligence: " + tempPlayer.getIntelligence()+"<br/> " +
                "Level: " + tempPlayer.getLevel(tempPlayer.getExperiencePoints())+"<br/> " +
                "Potions: " + tempPlayer.getInventory().stream().filter(x -> x instanceof HealthPotion || x instanceof MinorHealthPotion).count()+" (H) / "+ tempPlayer.getInventory().stream().filter(x -> x instanceof ManaPotion || x instanceof MinorManaPotion).count()+" (M) <br/> <br/> <br/> " +
                "HP Boost: " + playerWeaponHPBoost+"<br/> " +
                "Mana Boost: " + playerWeaponManaBoost+"<br/> " +
                "Strength Boost: " +  playerWeaponStrengthBoost +" <br/> " +
                "Intelligence Boost: " + playerWeaponIntelligenceBoost+"<br/> " +
                "</html>");
    }

}
