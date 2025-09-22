package io.blackdeluxecat.painttd.ui.fragment;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.components.logic.*;
import io.blackdeluxecat.painttd.game.*;
import io.blackdeluxecat.painttd.ui.*;

import static io.blackdeluxecat.painttd.game.Game.*;
import static io.blackdeluxecat.painttd.ui.Styles.*;

public class MapEditorFragment extends Table{
    public void rebuild(){
        clear();
        add(new ActorUtils<>(new Table()).with(t1 -> {
            t1.add(ActorUtils.wrapper
                       .set(new TextButton("设置核心", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawStainCore;
                       })
                       .actor);

            t1.add(ActorUtils.wrapper
                       .set(new TextButton("绘制墙体", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawWall;
                       })
                       .actor);

            t1.add(ActorUtils.wrapper
                       .set(new TextButton("染色", sTextB))
                       .click(b -> {
                           Vars.hud.current = drawStainColor;
                       })
                       .actor);

        }).actor).growX();
    }

    public HudGroup.MapEditBrush drawWall = new HudGroup.MapEditBrush("地形墙"){
        @Override
        public void draw(float wx, float wy){
            int x = Math.round(wx);
            int y = Math.round(wy);
            int tile = Game.map.getTile(x, y);
            if(tile == -1) return;
            var tileComp = utils.tileMapper.get(tile);
            tileComp.isWall = !tileComp.isWall;
        }
    }, drawStainCore = new HudGroup.MapEditBrush("核心"){
        @Override
        public void draw(float wx, float wy){
            var e = Game.map.getTileStain(Math.round(wx), Math.round(wy));
            if(e == -1) return;
            var mapper = world.getMapper(TileStainComp.class);
            if(!mapper.has(e)) return;
            var stain = mapper.get(e);
            stain.isCore = !stain.isCore;
        }
    }, drawStainColor = new HudGroup.MapEditBrush("地形染色"){
        @Override
        public void draw(float worldX, float worldY){
            var e = Game.map.getTileStain(Math.round(worldX), Math.round(worldY));
            if(e == -1) return;
            var mapper = world.getMapper(HealthComp.class);
            if(!mapper.has(e)) return;
            var hp = mapper.get(e);
            hp.health = Vars.hud.colors.selectColorIndex + 1;
        }
    };
}
