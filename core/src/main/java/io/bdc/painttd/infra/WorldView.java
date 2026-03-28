package io.bdc.painttd.infra;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.utils.viewport.*;
import io.bdc.painttd.*;

public class WorldView {
    public OrthographicCamera camera;
    public ScreenViewport viewport;

    public float minZoom;
    public float maxZoom;

    public boolean dragging;
    public float lastScreenX;
    public float lastScreenY;

    public void create() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport(camera);

        minZoom = 0.25f;
        maxZoom = 6f;

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        reset();
    }

    public void reset() {
        dragging = false;
        lastScreenX = 0f;
        lastScreenY = 0f;

        camera.position.set(0f, 0f, 0f);
        camera.zoom = 1f;
        camera.update();
    }

    public void resize(int width, int height) {
        viewport.update(width, height, false);
        camera.update();
    }

    public Vector2 screenToWorld(float screenX, float screenY) {
        Vector3 projected = new Vector3(screenX, screenY, 0f);
        camera.unproject(
                projected,
                viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight()
        );
        return new Vector2(projected.x, projected.y);
    }

    public Vector2 worldToScreen(float worldX, float worldY) {
        Vector3 projected = new Vector3(worldX, worldY, 0f);
        camera.project(
                projected,
                viewport.getScreenX(),
                viewport.getScreenY(),
                viewport.getScreenWidth(),
                viewport.getScreenHeight()
        );
        return new Vector2(projected.x, projected.y);
    }

    public boolean canHandleWorldInput() {
        return !isUiHit(Gdx.input.getX(), Gdx.input.getY());
    }

    public boolean isUiHit(float screenX, float screenY) {
        if (PaintTD.app == null || PaintTD.app.ui == null || PaintTD.app.ui.stage == null) {
            return false;
        }

        Vector2 stageCoords = uiStageCoordinates(screenX, screenY);
        Actor hit = PaintTD.app.ui.stage.hit(stageCoords.x, stageCoords.y, true);
        return hit != null;
    }

    public Vector2 uiStageCoordinates(float screenX, float screenY) {
        if (PaintTD.app == null || PaintTD.app.ui == null || PaintTD.app.ui.stage == null) {
            return new Vector2(screenX, screenY);
        }

        return PaintTD.app.ui.stage.screenToStageCoordinates(new Vector2(screenX, screenY));
    }

    public boolean onScrolled(float amountY, float screenX, float screenY) {
        if (!canHandleWorldInput()) {
            return false;
        }

        Vector2 before = screenToWorld(screenX, screenY);
        camera.zoom = MathUtils.clamp(camera.zoom * zoomFactor(amountY), minZoom, maxZoom);
        camera.update();

        Vector2 after = screenToWorld(screenX, screenY);
        camera.position.add(before.x - after.x, before.y - after.y, 0f);
        camera.update();
        return true;
    }

    public boolean onRightDragStart(float screenX, float screenY) {
        if (!canHandleWorldInput()) {
            return false;
        }

        dragging = true;
        lastScreenX = screenX;
        lastScreenY = screenY;
        return true;
    }

    public boolean onRightDragMove(float screenX, float screenY) {
        if (!dragging) {
            return false;
        }

        Vector2 from = screenToWorld(lastScreenX, lastScreenY);
        Vector2 to = screenToWorld(screenX, screenY);
        camera.position.add(from.x - to.x, from.y - to.y, 0f);
        camera.update();

        lastScreenX = screenX;
        lastScreenY = screenY;
        return true;
    }

    public void onRightDragEnd() {
        dragging = false;
    }

    private float zoomFactor(float amountY) {
        float step = 1f + (Math.abs(amountY) * 0.1f);
        return amountY >= 0f ? step : 1f / step;
    }
}
