package observers;

public interface Subject {
    public void register(Observer o);
    public void notifyObserver(Observer o);
}
