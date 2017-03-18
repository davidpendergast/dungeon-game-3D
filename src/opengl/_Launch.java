package opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class _Launch {
    public static void main(String[] args) {
        IGameLogic dummyGame = new DummyGame();
        GameEngine engine = new GameEngine("Dungeon Crawler #372", 640, 480, true, dummyGame);
        engine.start();
    }
}
