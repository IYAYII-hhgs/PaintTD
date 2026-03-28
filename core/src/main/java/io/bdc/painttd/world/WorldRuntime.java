package io.bdc.painttd.world;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.infra.*;

import java.util.*;

public class WorldRuntime {
    public final PaintTD app;
    public final WorldView worldView;

    private final ObjectMap<Class<?>, Object> stores;
    private final Array<WorldSystem> systems;
    private boolean systemsSorted;

    public float time;
    public int tick;

    public WorldRuntime(PaintTD app, WorldView worldView) {
        this.app = app;
        this.worldView = worldView;
        this.stores = new ObjectMap<>();
        this.systems = new Array<>();
    }

    public <T> void addStore(Class<T> type, T store) {
        if (stores.containsKey(type)) {
            throw new IllegalStateException("Duplicate store: " + type.getSimpleName());
        }
        stores.put(type, store);
    }

    @SuppressWarnings("unchecked")
    public <T> T getStore(Class<T> type) {
        Object value = stores.get(type);
        if (value == null) {
            throw new IllegalStateException("Missing store: " + type.getSimpleName());
        }
        return (T) value;
    }

    public void addSystem(WorldSystem system) {
        for (WorldSystem other : systems) {
            if (other.phase == system.phase && other.order == system.order) {
                throw new IllegalStateException(
                        "Duplicate phase/order: " + system.phase + " / " + system.order
                );
            }
        }

        systems.add(system);
        systemsSorted = false;
    }

    public void sortSystems() {
        systems.sort(new Comparator<>() {
            @Override
            public int compare(WorldSystem left, WorldSystem right) {
                int phaseCompare = Integer.compare(left.phase.ordinal(), right.phase.ordinal());
                if (phaseCompare != 0) {
                    return phaseCompare;
                }
                return Integer.compare(left.order, right.order);
            }
        });
        systemsSorted = true;
    }

    public void runFrame(float delta) {
        if (!systemsSorted) {
            throw new IllegalStateException("WorldRuntime systems must be sorted before runFrame().");
        }

        time += delta;
        tick += 1;

        for (WorldSystem system : systems) {
            system.run(delta);
        }
    }

    public void dispose() {
    }
}
