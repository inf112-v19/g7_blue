package sky7.player;

import sky7.card.ICard;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class Player implements IPlayer {

    public static final int MAX_CARDS_IN_REGISTRY = 6;
    private int health = 10;
    private int lifeTokens = 3;
    private ArrayList<ICard> hand;
    private ArrayList<ICard> registry;
    private Set<ICard> discard;
    private boolean[] locked = new boolean[5];
    private int nLocked = 0;
    private int playerNumber = 1;


    public Player() {
        hand = new ArrayList<ICard>();
        registry = new ArrayList<ICard>();
        discard = new LinkedHashSet<>();

    }

    @Override
    public void applyDamage(int damage) {
        health -= damage;
        updateHealth(health);

        if (health < 6) {
            lockCards(damage);
        }

        // if health reaches 0, consume a life token and respawn
    }

    @Override
    public void repair(int damage) {
        if (health < 6) {
            unlockCards(Math.min(damage, 6 - health));
        }
        health = Math.min(10, health + damage);
        updateHealth(health);
    }

    @Override
    public void lockCards(int n) {
        for (int i = nLocked; i < nLocked + n; i++) {
            locked[i] = true;
        }
        nLocked += n;
    }

    @Override
    public void unlockCards(int n) {
        for (int i = nLocked - 1; i >= nLocked - n; i--) {
            locked[i] = false;
        }
        nLocked -= n;
    }

    @Override
    public void updateHealth(int x) {
        health = x;
    }

    @Override
    public ArrayList<ICard> getRegistry() {
        return registry;
    }

    @Override
    public void setHand(ArrayList<ICard> programCards) {
        hand = programCards;
    }

    @Override
    public void setRegistry(ArrayList<ICard> chosenCards) {
        registry = chosenCards;
    }

    @Override
    public ArrayList<ICard> getHand() {
        return hand;
    }

    @Override
    public int getPlayerNumber() {
        return this.playerNumber;
    }

    @Override
    public void setPlayerNumber(int playerNumber) throws IllegalArgumentException {
        if (playerNumber < 0) throw new IllegalArgumentException("playerNumber should be bigger than 0");
        this.playerNumber = playerNumber;
    }

    @Override
    public ArrayList<ICard> getDiscard() {
        return new ArrayList<>(discard);
    }

    @Override
    public void setCard(ICard chosenCard, int positionInRegistry) {
        if (positionInRegistry >= 0 && positionInRegistry < MAX_CARDS_IN_REGISTRY) {
            ICard temp = registry.get(positionInRegistry); // TODO add test for this.
            if (temp != null) discard.add(temp);
            discard.remove(chosenCard);
            registry.add(positionInRegistry, chosenCard);
        }

    }


}
