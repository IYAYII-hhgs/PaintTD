package io.bdc.painttd.world.family.weapon;

import io.bdc.painttd.world.*;
import io.bdc.painttd.world.family.*;
import io.bdc.painttd.world.store.request.*;

/**
 * Weapon家族开发契约
 * 武器定义只描述静态配置。
 * 武器装配期负责向对应 store 注册运行时数据。
 * 运行时状态放 store，行为流程放 system。
 * 不要把帧逻辑塞进 Def。
 */
public abstract class WeaponDef implements ModuleDef {
    public abstract boolean onAssemble(int eid, EntitySpawnRequest req, WorldAccess binder);
}
