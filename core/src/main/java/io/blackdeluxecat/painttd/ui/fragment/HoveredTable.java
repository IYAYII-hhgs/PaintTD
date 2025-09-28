package io.blackdeluxecat.painttd.ui.fragment;

import com.artemis.*;
import com.artemis.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.content.components.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.content.components.marker.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.ui.*;
import io.blackdeluxecat.painttd.utils.*;
import io.blackdeluxecat.painttd.utils.func.*;

import java.util.*;

import static io.blackdeluxecat.painttd.ui.Styles.*;

/**
 * 显示单个实体的状态信息, 对每个组件构建ui.
 */
//TODO基于Aspect的多组件UI
public class HoveredTable extends Table{
    Bag<Component> tmpBag = new Bag<>();

    public HoveredTable(){
        pad(2);
        background(black3);
        defaults().minHeight(8).minWidth(200f);
    }

    public void build(int e){
        clear();
        if(e == -1) return;
        ActorUtils.wrapper.set(this).update(t -> {
            if(!Game.world.getEntityManager().isActive(e)) build(-1);
        });

        tmpBag.clear();
        Game.world.getComponentManager().getComponentsFor(e, tmpBag);
        tmpBag.sort(Comparator.comparingInt(o -> componentOrder.indexOf(o.getClass(), true)));
        for(Component comp : tmpBag){
            if(comp instanceof CopyableComponent cc){
                var cont = new Table();
                buildStrategy.buildObj(cc, cont);
                add(cont).row();
            }
        }
    }

    public static Array<Class<? extends Component>> componentOrder = new Array<>();

    public final static ObjBuildStrategy<CopyableComponent, Table> buildStrategy = new ObjBuildStrategy<>() {
        {
            register(EntityTypeComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(comp.type, sLabel))
                          .actor);
            });

            register(PositionComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(null, sLabel))
                          .with(l -> l.setColor(Color.GRAY))
                          .update(a -> ((Label)a).setText(comp.tileX() + ", " + comp.tileY()))
                          .actor);
            });

            register(HealthComp.class, (comp, t) -> {
                t.add(ActorUtils.wrapper.set(new Label(null, sLabel))
                          .update(a -> ((Label)a).setText("HP: " + Format.fixedBuilder(comp.health, 1)))
                          .actor);
            });

            register(SpawnGroupCompsComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);
                for(CopyableComponent c : comp.comps){

                }
            });

            register(SpawnGroupComp.class, (comp, t) -> {
                t.defaults().minWidth(80f);

                Cons4<String, Prov<String>, Cons<String>, TextField.TextFieldFilter> fieldp = (txt, prov, cons, filter1) -> {
                    t.add(new Label(txt, sLabel));
                    TextField field1 = new TextField(prov.get(), sTextF);
                    t.add(field1).row();
                    field1.setTextFieldFilter(filter1);
                    ActorUtils.updater(field1, f -> {
                        if(!f.getText().isEmpty()){
                            cons.get(f.getText());
                        }
                    });
                };

                fieldp.get("起始数量", () -> String.valueOf(comp.amtStart), s -> comp.amtStart = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("增量数量", () -> String.valueOf(comp.amtDelta), s -> comp.amtDelta = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("最大数量", () -> String.valueOf(comp.amtMax), s -> comp.amtMax = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("起始血量", () -> String.valueOf(comp.healthStart), s -> comp.healthStart = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("增量血量", () -> String.valueOf(comp.healthDelta), s -> comp.healthDelta = Float.parseFloat(s), ActorUtils.floatOnly);

                fieldp.get("生成间隔(tick)", () -> String.valueOf(comp.spawnDelta), s -> comp.spawnDelta = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("起始波数", () -> String.valueOf(comp.waveStart), s -> comp.waveStart = Integer.parseInt(s), ActorUtils.digitOnly);

                fieldp.get("波次间隔", () -> String.valueOf(comp.waveDelta), s -> comp.waveDelta = Integer.parseInt(s), ActorUtils.digitOnly);
            });
        }

        @Override
        public <T extends CopyableComponent> void register(Class<T> clazz, ObjBuilder<T, Table> cons){
            super.register(clazz, cons);
            componentOrder.add(clazz);
        }
    };
}
