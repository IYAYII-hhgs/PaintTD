package io.blackdeluxecat.painttd.io;

import com.artemis.io.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.content.*;
import io.blackdeluxecat.painttd.game.Game;

import java.io.*;

public class SaveHandler{
    public static FileHandle path = Core.gameDataFolder.child("save");

    public static void save(String save){
        SaveFileFormat format = new SaveFileFormat();
        format.entities = Game.utils.allEntitiesSub.getEntities();
        Game.worldSerializationManager.save(getSaveOutput(save), format);
    }

    public static void load(String save){
        Game.setupWorld();
        SaveFileFormat format = Game.worldSerializationManager.load(getLoadInput(save), SaveFileFormat.class);
        Gdx.app.log("SaveHandler", "Loaded " + format.entities.size() + " entities");

        // 读取实体类型并重建完整实体
        var entities = format.entities;
        for(int i = 0; i < entities.size(); i++){
            var e = Game.world.getEntity(entities.get(i));
            Entities.getByName(Game.utils.entityTypeMapper.get(e).type).refill(e);
        }

        Game.rebindTileStain();
    }

    public static OutputStream getSaveOutput(String fileNameNoExtension){
        return path.child(fileNameNoExtension + ".json").write(false);
    }

    public static InputStream getLoadInput(String fileNameNoExtension){
        return path.child(fileNameNoExtension + ".json").read();
    }

    public static boolean exists(String fileNameNoExtension){
        return path.child(fileNameNoExtension + ".json").exists();
    }
}
