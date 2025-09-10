package io.blackdeluxecat.painttd.io;

import com.artemis.io.*;
import com.artemis.managers.*;
import com.badlogic.gdx.files.*;
import io.blackdeluxecat.painttd.*;
import io.blackdeluxecat.painttd.game.*;

import java.io.*;

public class SaveHandler{
    public static FileHandle path = Core.gameDataFolder.child("save");

    public static void save(String name){
        SaveFileFormat format = new SaveFileFormat();
        format.entities = Game.utils.allEntitiesSub.getEntities();
        Game.worldSerializationManager.save(getSaveOutput(name), format);
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
