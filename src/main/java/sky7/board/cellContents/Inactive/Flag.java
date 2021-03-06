package sky7.board.cellContents.Inactive;

import com.badlogic.gdx.graphics.Texture;
import sky7.board.ICell;
import sky7.board.cellContents.IInactive;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Flag implements IInactive {
    private int flagNumber;
    private final int PRIORITY = 3;
    private Texture texture;

    public Flag(int flagNumber) {
        this.flagNumber = flagNumber;
    }

    @Override
    public Texture getTexture() {
        if (texture == null) {
            switch (flagNumber) {
                case 1:
                    texture = new Texture("assets/flags/Flag1.png");
                    break;
                case 2:
                    texture = new Texture("assets/flags/Flag2.png");
                    break;
                case 3:
                    texture = new Texture("assets/flags/Flag3.png");
                    break;
                case 4:
                    texture = new Texture("assets/flags/Flag4.png");
                    break;
                default:
                    throw new IllegalArgumentException("unknown flag number");
            }
        }
        return texture;
    }

    @Override
    public int drawPriority() {
        return this.PRIORITY;
    }

    @Override
    public int compareTo(ICell other) {
        return Integer.compare(this.drawPriority(), other.drawPriority());
    }

    public static List<AbstractMap.SimpleEntry<String,Supplier<ICell>>> getSuppliers() {
        List<AbstractMap.SimpleEntry<String, Supplier<ICell>>> suppliers = new ArrayList<>();
        int maxNrOfFlags = 4;
        for (int i = 0; i < maxNrOfFlags; i++) {
            final int a = i+1;
            suppliers.add(new AbstractMap.SimpleEntry<>("g" + a, () -> new Flag(a)));
        }
        return suppliers;

    }

    /**
     * what number this flag has.
     *
     * @return the number of this flag
     */
    public int getFlagNumber(){
        return this.flagNumber;
    }
}
