package io.bdc.painttd.world;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.*;
import io.bdc.painttd.infra.*;
import io.bdc.painttd.world.store.*;
import io.bdc.painttd.world.system.*;

import java.util.*;

public class WorldRuntime {
    public final PaintTD app;
    public final WorldView worldView;

    /* 请使用接口装配System和Store, 不应直接修改数组 */
    public final ObjectMap<Class<? extends WorldStore>, WorldStore> stores;
    public final Array<WorldSystem> systems;
    private boolean systemsSorted;

    public float time;
    public int tick;

    public WorldRuntime(PaintTD app, WorldView worldView) {
        this.app = app;
        this.worldView = worldView;
        this.stores = new ObjectMap<>();
        this.systems = new Array<>();
    }

    /** 添加指定的store */
    public void addStore(WorldStore store) {
        Class<? extends WorldStore> type = store.getClass();
        if (stores.containsKey(type)) {
            throw new IllegalStateException("Duplicate store: " + type.getSimpleName());
        }
        stores.put(type, store);
    }

    @SuppressWarnings("unchecked")
    public <T extends WorldStore> T getStore(Class<T> type) {
        WorldStore value = stores.get(type);
        if (value == null) {
            throw new IllegalStateException("Missing store: " + type.getSimpleName());
        }
        return (T) value;
    }

    public <T extends WorldStore> boolean hasStore(Class<T> type) {
        return stores.containsKey(type);
    }

    public void addSystem(WorldSystem system) {
        for (WorldSystem other : systems) {
            if (other.phase == system.phase && other.order == system.order) {
                throw new IllegalStateException(
                        "Duplicate phase/order: " + system.phase + " / " + system.order
                );
            }
        }

        system.onStoreBind(new WorldStoreBinder(this));
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
        for (WorldSystem system : systems) {
            if (system instanceof Disposable disposable) {
                disposable.dispose();
            }
        }

        for (WorldStore store : stores.values()) {
            if (store instanceof Disposable disposable) {
                disposable.dispose();
            }
        }

        systems.clear();
        stores.clear();
        systemsSorted = false;
        time = 0f;
        tick = 0;
    }
}
