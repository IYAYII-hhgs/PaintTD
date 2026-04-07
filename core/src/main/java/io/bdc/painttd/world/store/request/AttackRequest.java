package io.bdc.painttd.world.store.request;

public class AttackRequest {
    public float damage;
    public int source;
    public int target;

    public AttackRequest setup(float damage, int source, int target) {
        this.damage = damage;
        this.source = source;
        this.target = target;
        return this;
    }
}
