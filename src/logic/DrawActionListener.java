package logic;

public interface DrawActionListener {
    //callback interface that the "Computer" class uses to notify about draw actions
    void onCardDrawn(Player player);
}
