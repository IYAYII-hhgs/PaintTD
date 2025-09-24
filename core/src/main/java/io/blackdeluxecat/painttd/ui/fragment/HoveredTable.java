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

import java.util.*;

import static io.blackdeluxecat.painttd.ui.Styles.*;

/**
 * 显示单个实体的状态信息, 对每个组件构建ui.
 */
//TODO基于Aspect的多组件UI
public class HoveredTable extends Table{
    public static ObjectMap<Class<? extends CopyableComponent>, ComponentInfoBuilder> builders = new ObjectMap<>();
    public static Array<Class<? extends Component>> componentOrder = new Array<>();

    public static <T extends CopyableComponent> void register(Class<T> clazz, ComponentInfoBuilder<T> builder){
        builders.put(clazz, builder);
        componentOrder.add(clazz);
    }

    static{
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

        register(SpawnGroupComp.class, (comp, t) -> {
            TextField field;
            TextField.TextFieldFilter filter = new TextField.TextFieldFilter.DigitsOnlyFilter();

            t.defaults().minWidth(80f);

            t.add(new Label("起始数量", sLabel));
            t.add(field = new TextField(String.valueOf(comp.amtStart), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.amtStart = Float.parseFloat(f.getText());
                }
            });

            t.add(new Label("增量数量", sLabel));
            t.add(field = new TextField(String.valueOf(comp.amtDelta), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.amtDelta = Float.parseFloat(f.getText());
                }
            });

            t.add(new Label("最大数量", sLabel));
            t.add(field = new TextField(String.valueOf(comp.amtMax), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.amtMax = Integer.parseInt(f.getText());
                }
            });

            t.add(new Label("起始血量", sLabel));
            t.add(field = new TextField(String.valueOf(comp.healthStart), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.healthStart = Float.parseFloat(f.getText());
                }
            });

            t.add(new Label("增量血量", sLabel));
            t.add(field = new TextField(String.valueOf(comp.healthDelta), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.healthDelta = Float.parseFloat(f.getText());
                }
            });

            t.add(new Label("生成间隔(tick)", sLabel));
            t.add(field = new TextField(String.valueOf(comp.spawnDelta), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.spawnDelta = Integer.parseInt(f.getText());
                }
            });

            t.add(new Label("起始波数", sLabel));
            t.add(field = new TextField(String.valueOf(comp.waveStart), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.waveStart = Integer.parseInt(f.getText());
                }
            });

            t.add(new Label("波次间隔", sLabel));
            t.add(field = new TextField(String.valueOf(comp.waveDelta), sTextF)).row();
            field.setTextFieldFilter(filter);
            ActorUtils.updater(field, f -> {
                if(!f.getText().isEmpty()){
                    comp.waveDelta = Integer.parseInt(f.getText());
                }
            });

        });
    }

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
                var builder = builders.get(cc.getClass());
                if(builder != null){
                    var cont = new Table();
                    builder.build(cc, cont);
                    add(cont).row();
                }
            }
        }
    }


    public interface ComponentInfoBuilder<T extends CopyableComponent>{
        void build(T comp, Table table);
    }
}
