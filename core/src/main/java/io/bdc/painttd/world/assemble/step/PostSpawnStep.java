package io.bdc.painttd.world.assemble.step;

/**
 * 生成实体-后处理的步骤
 * 推荐场景: 在实体按EntityDef.steps装配完成后, 注入位置, 方向, 血量等属性
 * 建议具名实现并池化复用, 严禁单例模式
 */

@FunctionalInterface
public interface PostSpawnStep extends EntitySpawnStep {
}
