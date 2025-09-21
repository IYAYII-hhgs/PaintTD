package io.blackdeluxecat.painttd.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.io.*;

public class PreferenceDialog extends BaseDialog{
    public Array<PrefCategory> categories = new Array<>();
    public int cateIndex = 0;

    Table cateTab = new Table(), prefTab = new Table();

    public PreferenceDialog(){
        super("选项");
        setBackground(Styles.black8);
        addCloseButton();

        var render = new PrefCategory("渲染", new TextureRegionDrawable(Core.atlas.findRegion("u-eraser")));
        addCategory(render);

        render.boolPref("test", null, true);

        rebuild();
    }

    public PrefCategory getCategory(String name){
        for(PrefCategory category : categories){
            if(category.name.equals(name)){
                return category;
            }
        }
        return null;
    }

    public void addCategory(PrefCategory category){
        categories.add(category);
    }

    public void rebuild(){
        cont.clear();
        ActorUtils.updater(cont, t -> {
            var hit = t.hit(Gdx.input.getX(), Gdx.input.getY(), false);
            if(hit != null && hit.isDescendantOf(cateTab)){
                cateTab.toFront();
                cateTab.setWidth(400f);
            }else{
                cateTab.toBack();
                cateTab.setWidth(Styles.buttonSize);
            }
        });

        cateTab.clear();

        for(int i = 0; i < categories.size; i++){
            int fi = i;
            PrefCategory category = categories.get(i);
            cateTab.add(new ActorUtils<>(new Button(Styles.sTextB))
                            .with(t -> {
                                if(category.icon != null) t.add(new Image(category.icon)).size(Styles.buttonSize);
                                t.add(new Label(category.name, Styles.sLabel)).growX();
                            })
                            .click(b -> {
                                cateIndex = fi;
                                rebuildPrefTab();
                            })
                            .actor).growX().pad(8f);
            cateTab.row();
        }

        rebuildPrefTab();

        cont.pad(20f, 100f, 20f, 100f);
        cont.add(cateTab);
        cont.add(prefTab).grow();
    }

    public void rebuildPrefTab(){
        prefTab.clear();
        prefTab.top();
        PrefCategory category = categories.get(cateIndex);
        builder.build(category, prefTab);
    }

    public PrefCategory.PrefCateElemBuilder builder = new PrefCategory.PrefCateElemBuilder(){
        {
            register(PrefCategory.BoolElem.class, (pe, t) -> {
                PrefCategory.BoolElem be = (PrefCategory.BoolElem)pe;
                be.updateValue();
                t.add(new ActorUtils<>(new Table())
                          .with(tt -> tt.add(ActorUtils.wrapper
                                                 .set(new Label(be.name, Styles.sLabel))
                                                 .update(l -> l.setColor(be.value ? Color.GREEN : Color.RED))
                                                 .actor).growX()
                          )
                          .click(tt -> Core.prefs.putBoolean(be.name, be.value = !be.value))
                          .actor);
            });
        }

        @Override
        public void build(PrefCategory category, Table table){
            for(PrefCategory.PrefElem elem : category.elems){
                var builder = builders.get(elem.getClass());
                if(builder != null){
                    var t = new Table();
                    builder.get(elem, t);
                    table.add(t).growX().minHeight(Styles.buttonSize * 2).pad(8f);
                }
            }
        }
    };
}
