package io.blackdeluxecat.painttd;

import com.badlogic.gdx.*;
import io.blackdeluxecat.painttd.lib.struct.*;

import static io.blackdeluxecat.painttd.Core.hud;
import static io.blackdeluxecat.painttd.Core.inputMultiplexer;

public class Input{
    public static LayerManager<InputProcessor> inputProcessors = new LayerManager<>();

    //layers
    public static LayerManager.Layer<InputProcessor>
        stage = inputProcessors.registerLayer("stage", 0),
        placement = inputProcessors.registerLayer("placement", 1);

    public static void create(){
        stage.add(Core.stage);
        placement.add(hud.placement.inputPlacement);
        sort();
    }

    /**当修改了某个Layer之后, 进行排序.*/
    public static void sort(){
        inputProcessors.sort();
        inputMultiplexer.clear();
        for(var layer : inputProcessors.layers){
            for(var processor : layer.objects){
                inputMultiplexer.addProcessor(processor);
            }
        }
    }
}
