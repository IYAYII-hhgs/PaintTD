package io.blackdeluxecat.painttd.utils;

import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.utils.func.*;

@SuppressWarnings("unchecked")
public class EventBus{
    public static ObjectMap<Class<?>, Array<Cons<?>>> listeners = new ObjectMap<>();

    public static ObjectMap<Object, Cons<?>> tokens = new ObjectMap<>();

    public static <T extends Event> Object on(Class<T> type, Cons<T> listener){
        if(!listeners.containsKey(type)){
            listeners.put(type, new Array<>());
        }

        listeners.get(type).add(listener);
        Object token = new Object();
        tokens.put(token, listener);
        return token;
    }

    public static <T extends Event> Object on(Class<T> type, Runnable listener){
        return on(type, e -> listener.run());
    }

    public static <T extends Event> void unregister(Class<T> type, Cons<T> listener){
        if(listeners.containsKey(type)){
            listeners.get(type).removeValue(listener, true);
        }
    }

    public static <T extends Event> void unregister(Object token){
        if(tokens.containsKey(token)){
            Cons<?> listener = tokens.get(token);
            for(var entry : listeners.entries())
                entry.value.removeValue(listener, true);

            tokens.remove(token);
        }
    }

    public static void fire(Class<?> event){
        fire(event, event);
    }

    public static <T extends Event> void fire(T event){
        fire(event.getClass(), event);
    }

    public static <T> void fire(Class<?> eventType, T event){
        Array<Cons<?>> eventListeners = listeners.get(eventType);
        if(eventListeners != null){
            for(Cons l : eventListeners){
                l.get(event);
            }
        }
    }

    public static abstract class Event{
    }
}
