package io.bdc.painttd.lib;

import com.badlogic.gdx.utils.*;
import io.bdc.painttd.lib.func.*;

@SuppressWarnings("unchecked")
public class Events {
    private static final Bus global = new Bus();

    public static Bus global() {
        return global;
    }

    /** 注册事件实例的监听器. 若注册事件类监听器, listener.get(event)将传入null */
    public static <T extends Event> Object on(Class<T> type, Cons<T> listener) {
        return global.on(type, listener);
    }

    /** 注册事件类的监听器 */
    public static <T extends Event> Object on(Class<T> type, Runnable listener) {
        return global.on(type, listener);
    }

    public static <T extends Event> void off(Class<T> type, Cons<T> listener) {
        global.off(type, listener);
    }

    public static <T extends Event> void off(Object token) {
        global.off(token);
    }

    /** 以事件类发射 */
    public static void fire(Class<?> event) {
        global.fire(event);
    }

    /** 以事件实例发射 */
    public static <T extends Event> void fire(T event) {
        global.fire(event);
    }

    /**
     * @param eventType 事件类型
     * @param event 事件实例
     */
    public static <T> void fire(Class<?> eventType, @Null T event) {
        global.fire(eventType, event);
    }

    public static final class Bus {
        private final ObjectMap<Class<?>, Array<Cons<?>>> listeners = new ObjectMap<>();
        private final ObjectMap<Object, Cons<?>> tokens = new ObjectMap<>();

        /** 注册事件实例的监听器. 若注册事件类监听器, listener.get(event)将传入null */
        public <T extends Event> Object on(Class<T> type, Cons<T> listener) {
            if (!listeners.containsKey(type)) {
                listeners.put(type, new Array<>());
            }

            listeners.get(type).add(listener);
            Object token = new Object();
            tokens.put(token, listener);
            return token;
        }

        /** 注册事件类的监听器 */
        public <T extends Event> Object on(Class<T> type, Runnable listener) {
            return on(type, e -> listener.run());
        }

        public <T extends Event> void off(Class<T> type, Cons<T> listener) {
            if (listeners.containsKey(type)) {
                listeners.get(type).removeValue(listener, true);
            }
        }

        public void off(Object token) {
            if (tokens.containsKey(token)) {
                Cons<?> listener = tokens.get(token);
                for (var entry : listeners.entries())
                    entry.value.removeValue(listener, true);

                tokens.remove(token);
            }
        }

        /** 以事件类发射 */
        public void fire(Class<?> event) {
            fire(event, null);
        }

        /** 以事件实例发射 */
        public <T extends Event> void fire(T event) {
            fire(event.getClass(), event);
        }

        /**
         * @param eventType 事件类型
         * @param event 事件实例
         */
        public <T> void fire(Class<?> eventType, @Null T event) {
            Array<Cons<?>> eventListeners = listeners.get(eventType);
            if (eventListeners != null) {
                for (Cons l : eventListeners) {
                    l.get(event);
                }
            }
        }

        public void clear() {
            listeners.clear();
            tokens.clear();
        }
    }

    public static abstract class Event implements Pool.Poolable {
        public boolean handled;

        public void handle() {
            handled = true;
        }

        @Override
        public void reset() {
            handled = false;
        }
    }
}
