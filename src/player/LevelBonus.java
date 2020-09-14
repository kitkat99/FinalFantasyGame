package player;

public class LevelBonus {
    private int Str;
    private int Int;
    private int HP;
    private int MP;

    public int getHP() {
        return HP;
    }

    public int getMP() {
        return MP;
    }

    public int getInt() {
        return Int;
    }

    public int getStr() {
        return Str;
    }
    public LevelBonus(int strength, int intelligence, int hp, int mp){
        Str = strength;
        Int = intelligence;
        HP = hp;
        MP = mp;
    }
}
