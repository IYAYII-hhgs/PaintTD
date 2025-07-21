package io.blackdeluxecat.painttd.enitiy;

import com.badlogic.gdx.math.*;

public abstract class Entity{
    public float x, y;
    public float health, maxHealth;

    abstract public void act();

    abstract public void move(Vector2 vec);

    abstract public void draw();
}
