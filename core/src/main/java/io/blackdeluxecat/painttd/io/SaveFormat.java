package io.blackdeluxecat.painttd.io;

import com.artemis.io.*;

public class SaveFormat extends SaveFileFormat{
    public SaveFormat(){
    }

    public SaveFormat(int build, int saveVersion){
        var meta = new Metadata();
        meta.build = build;
        meta.version = saveVersion;
    }

    public Metadata getMetadata(){
        return (Metadata)metadata;
    }

    public static class Metadata extends SaveFileFormat.Metadata{
        public int build;

        public String name;
        public String description;
        public String author;
    }
}
