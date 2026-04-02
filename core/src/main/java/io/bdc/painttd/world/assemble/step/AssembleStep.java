package io.bdc.painttd.world.assemble.step;

/**
 * 生成实体-装配阶段的步骤
 * 推荐场景: 注册到EntityDef.steps, 定义该实体登记哪些stores. 实体按EntityDef.steps装配进入WorldRuntime
 * 必须具名实现, 可注入参数, 内部状态不可影响装配结果
 */
public interface AssembleStep extends EntitySpawnStep {
}
