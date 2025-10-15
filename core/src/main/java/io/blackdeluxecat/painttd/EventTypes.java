package io.blackdeluxecat.painttd;

import io.blackdeluxecat.painttd.utils.*;

public class EventTypes{
    public static class CollideEvent extends Events.Event{
        public int source, target;
    }

    public static class DamageEvent extends Events.Event{
        public int source, target;
        public float damage;
    }

    public static class CollideDamageEvent extends Events.Event{
        public int source, target;
    }

    public static class SplashDamageEvent extends Events.Event{
        public int source;
        public float x, y;
        public float damage;
        public float radius;
    }

    public static class StainSplashDamageEvent extends Events.Event{
        public int source;
        public float x, y;
        public int team;
        public float damage;
        public float radius;
    }

    public static class StainDamageEvent extends Events.Event{
        public int source;
        public int team;
        public int target;
        public float damage;
    }
}
