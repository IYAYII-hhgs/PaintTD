package io.bdc.painttd.world.store;

/**
 * 标记一个 store 是否持有实体相关数据，并在实体销毁时负责按 eid 清理自身状态。
 * <p>
 * 约束：
 * <ul>
 *     <li>当 {@code entityId} 当前不存在于该 store 中时，必须按 no-op 处理，不得抛错。</li>
 *     <li>实现必须保持顺序无关；有依赖顺序的销毁准备应在进入 recycle 阶段前完成。</li>
 * </ul>
 */
public interface EntityOwner {
    void onEntityDestroy(int entityId);
}
