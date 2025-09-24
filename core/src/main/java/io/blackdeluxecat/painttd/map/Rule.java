package io.blackdeluxecat.painttd.map;

import static io.blackdeluxecat.painttd.game.Game.lfps;

/**
 * 世界规则.
 * */
public class Rule{
    public int ticks;

    /**地图总波数*/
    public int waves;
    /**当前波次*/
    public int wave;
    /**使用波次计时器*/
    public boolean waveTimerRun = true;
    /**波次计时器, tick*/
    public float waveTimer = 0;
    public float waveDelay = 10 * lfps;
    /**地图尺寸*/
    public int width, height;
    /**调色盘, 可用颜色*/
    public ColorPalette colorPalette;
}
