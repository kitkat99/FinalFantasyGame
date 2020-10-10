package observers;

public interface Subject {
    void register(Observer o);
    void notifyObserver(Observer o);
}
