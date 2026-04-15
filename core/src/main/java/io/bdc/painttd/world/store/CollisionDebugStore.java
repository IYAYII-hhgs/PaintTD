package io.bdc.painttd.world.store;

public class CollisionDebugStore implements WorldStore {
    public int eeQueryCount;
    public int eeCandidateCount;
    public int eeOverlapCount;
    public int eeUniquePairCount;
    public int ecWallCount;

    public void resetFrame() {
        eeQueryCount = 0;
        eeCandidateCount = 0;
        eeOverlapCount = 0;
        eeUniquePairCount = 0;
        ecWallCount = 0;
    }
}
