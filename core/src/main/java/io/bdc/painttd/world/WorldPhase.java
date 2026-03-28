package io.bdc.painttd.world;

public enum WorldPhase {
    PREPARE,
    SPAWN,
    SIMULATE,
    APPLY,
    CLEANUP,

    RENDER_BACKGROUND,
    RENDER_TERRAIN,
    RENDER_ENTITY,
    RENDER_EFFECT,
    RENDER_OVERLAY,
    RENDER_DEBUG
}
