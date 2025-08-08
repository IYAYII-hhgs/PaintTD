package io.blackdeluxecat.painttd.content.components.logic;

public class LevelComp extends CopyableComponent{
    public int level;

    public LevelComp(){}

    public LevelComp(int level){
        this.level = level;
    }

    @Override
    public LevelComp copy(CopyableComponent other){
        LevelComp levelComp = (LevelComp)other;
        levelComp.level = level;
        return levelComp;
    }

    @Override
    protected void reset(){
        level = 0;
    }
}
