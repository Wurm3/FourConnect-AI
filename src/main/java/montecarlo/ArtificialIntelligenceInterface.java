package montecarlo;

import gamecore.VierGewinntCore;

public interface ArtificialIntelligenceInterface {
    public int[] makeNextMove();
    public void refreshBoard(VierGewinntCore vierGewinnt);
}
