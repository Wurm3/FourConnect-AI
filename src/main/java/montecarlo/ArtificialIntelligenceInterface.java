package montecarlo;

import gamecore.VierGewinntInterface;

public interface ArtificialIntelligenceInterface {
    public int[] makeNextMove();
    public void refreshBoard(VierGewinntInterface vierGewinnt);
}
