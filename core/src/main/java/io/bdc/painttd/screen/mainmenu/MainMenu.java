package io.bdc.painttd.screen.mainmenu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import io.bdc.painttd.screen.dev.*;
import io.bdc.painttd.screen.game.*;

import static io.bdc.painttd.PaintTD.*;

public class MainMenu extends Table {
    public Skin skin;
    public VerticalGroup menuItems;

    public MainMenu(Skin skin) {
        super(skin);
        this.skin = skin;
        menuItems = new VerticalGroup();

        setName("main-menu-root");
        setFillParent(true);

        Table content = new Table(skin);
        content.defaults().pad(8f).width(320f);
        content.add(new Label("PaintTD", skin));
        content.row();
        content.add(new Label("Main Menu", skin));
        content.row();
        Label hint = new Label("Start a game to verify the pause flow, or open both windows and press Esc to verify only the top one closes.", skin);
        hint.setWrap(true);

        content.add(hint).fillX();
        content.row();

        content.add(createButton("启动游戏", () -> {
            app.setScreen(new GameScreen(app));
        })).fillX();
        content.row();

        content.add(createButton("打开测试场景一", () -> {
            app.setScreen(new DevTestScreen(app));
        })).fillX();
        content.row();

        content.add(createButton("其他", () -> {
            var w = buildWindow("其他",
                "其他内容.");
            centerWindow(w);
            app.ui.pushWindow(w);
        })).fillX();
        content.row();

        content.add(createButton("关于", () -> {
            var w = buildWindow("关于",
                "作者: BlackDeluxeCat");
            centerWindow(w);
            app.ui.pushWindow(w);
        })).fillX();
        content.row();

        content.add(createButton("退出游戏", () -> {
            Gdx.app.exit();
        })).fillX();

        add(content).center();
    }

    private TextButton createButton(String text, Runnable action) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (action != null) {
                    action.run();
                }
            }
        });
        return button;
    }

    private Window buildWindow(String title, String body) {
        Window window = new Window(title, app.skins.skin);
        window.setName(title.toLowerCase() + "-window");
        window.setResizable(false);

        Label bodyLabel = new Label(body, app.skins.skin);
        bodyLabel.setWrap(true);

        TextButton closeButton = new TextButton("Close", app.skins.skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                app.ui.removeWindow(window);
            }
        });

        window.defaults().pad(8f);
        window.add(bodyLabel).width(320f);
        window.row();
        window.add(closeButton).right();
        window.pack();
        return window;
    }

    private void centerWindow(Window window) {
        window.pack();
        float x = Math.max(0f, (app.ui.stage.getWidth() - window.getWidth()) * 0.5f);
        float y = Math.max(0f, (app.ui.stage.getHeight() - window.getHeight()) * 0.5f);
        window.setPosition(x, y);
    }
}
