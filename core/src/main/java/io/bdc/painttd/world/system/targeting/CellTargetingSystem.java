package io.bdc.painttd.world.system.targeting;

import com.badlogic.gdx.math.*;
import io.bdc.painttd.world.*;
import io.bdc.painttd.world.family.targeting.*;
import io.bdc.painttd.world.family.turret.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

public class CellTargetingSystem extends WorldSystem {
    private CellTargetingStore cellTargetingStore;
    private TurretStore turretStore;
    private PositionStore positionStore;
    private EntityTeamStore teamStore;
    private MapStore mapStore;

    public CellTargetingSystem(WorldRuntime world, WorldPhase phase, int order) {
        super(world, phase, order);
    }

    @Override
    public void onBind(WorldAccess binder) {
        cellTargetingStore = binder.getStore(CellTargetingStore.class);
        turretStore = binder.getStore(TurretStore.class);
        positionStore = binder.getStore(PositionStore.class);
        teamStore = binder.getStore(EntityTeamStore.class);
        mapStore = binder.getStore(MapStore.class);
    }

    @Override
    public void run(float delta) {
        var strategyItems = cellTargetingStore.strategyArray.items;
        var targetItems = cellTargetingStore.targetCellArray.items;

        int mapW = mapStore.width;
        int mapH = mapStore.height;
        int[] teamMask = mapStore.teamMask;
        float[] hpMask = mapStore.hpMask;
        int[] cells = mapStore.cells;

        for (int slot = 0; slot < cellTargetingStore.size(); slot++) {
            int eid = cellTargetingStore.eidOf(slot);
            int turretSlot = turretStore.slotOf(eid);
            if (turretSlot == -1) continue;

            TurretDef def = turretStore.defArray.get(turretSlot);
            if (def == null) continue;
            float range = def.range;

            int posSlot = positionStore.slotOf(eid);
            float cx = positionStore.x.items[posSlot];
            float cy = positionStore.y.items[posSlot];

            int minX = MathUtils.clamp(MathUtils.floor(cx - range), 0, mapW - 1);
            int maxX = MathUtils.clamp(MathUtils.floor(cx + range), 0, mapW - 1);
            int minY = MathUtils.clamp(MathUtils.floor(cy - range), 0, mapH - 1);
            int maxY = MathUtils.clamp(MathUtils.floor(cy + range), 0, mapH - 1);

            int strategy = strategyItems[slot];
            int teamId = teamStore.teams.items[teamStore.slotOf(eid)];

            int bestCell = -1;
            float bestScore = 0;
            int seenCount = 0;

            for (int cellY = minY; cellY <= maxY; cellY++) {
                int rowBase = cellY * mapW;
                for (int cellX = minX; cellX <= maxX; cellX++) {
                    int cell = rowBase + cellX;
                    if (cells[cell] != 0) continue;

                    switch (strategy) {
                        case 0: {
                            if (teamMask[cell] != teamId) continue;
                            float hp = hpMask[cell];
                            if (hp < bestScore) {
                                bestCell = cell;
                                bestScore = hp;
                            }
                            break;
                        }
                        case 1: {
                            if (teamMask[cell] == teamId) continue;
                            if (!hasNeighbor(cell, cellX, cellY, teamId, mapW, mapH, teamMask)) continue;
                            float dx = (cellX + 0.5f) - cx;
                            float dy = (cellY + 0.5f) - cy;
                            float distSq = dx * dx + dy * dy;
                            if (bestCell == -1 || distSq < bestScore) {
                                bestCell = cell;
                                bestScore = distSq;
                            }
                            break;
                        }
                        case 2: {
                            seenCount++;
                            if (seenCount == 1 || MathUtils.random(seenCount - 1) == 0) {
                                bestCell = cell;
                            }
                            break;
                        }
                    }
                }
            }

            targetItems[slot] = bestCell;
        }
    }

    private static boolean hasNeighbor(int cell, int cx, int cy, int teamId, int mapW, int mapH, int[] teamMask) {
        if (cx > 0 && teamMask[cell - 1] == teamId) return true;
        if (cx < mapW - 1 && teamMask[cell + 1] == teamId) return true;
        if (cy > 0 && teamMask[cell - mapW] == teamId) return true;
        if (cy < mapH - 1 && teamMask[cell + mapW] == teamId) return true;
        return false;
    }
}
