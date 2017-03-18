package opengl;

/**
 * Stolen from https://www.gitbook.com/book/lwjglgamedev/3d-game-development-with-lwjgl/details
 */
public class GameEngine implements Runnable {
    private final Thread gameLoopThread;
    private Window window;
    private IGameLogic gameLogic;
    
    public GameEngine(String windowTitle, int width, int height, boolean vsync, 
            IGameLogic gameLogic) {
        gameLoopThread = new Thread(this, "GAME_LOOP_THREAD");
        window = new Window(windowTitle, width, height, vsync);
        this.gameLogic = gameLogic;
    }
    
    public void start() {
        gameLoopThread.start();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void init() throws Exception {
        gameLogic.init();
        window.init();
    }
    
    private void gameLoop() {
        float secsPerUpdate = 1 / 60.0f;
        double previous = time();
        double steps = 0.0;
        
        boolean running = true;
        while (running && !window.windowShouldClose()) {
            double current = time();
            double elapsed = current - previous;
            previous = current;
            steps += elapsed;
            
            handleInput();
            
            while (steps >= secsPerUpdate) {
                updateGameState(secsPerUpdate);
                steps -= secsPerUpdate;
            }
            
            render();
            sync(current);
        }
    }
    
    private void sync(double current) {
        float loopSlot = 1f / 60;
        double endTime = current + loopSlot;
        while (time() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}
        }
    }
    
    private void handleInput() {
        gameLogic.input(window);
    }
    
    private void updateGameState(float interval) {
        gameLogic.update(interval);
    }
    
    private void render() {
        gameLogic.render(window);
        window.update();
    }
    
    private long time() {
        return System.currentTimeMillis();
    }
}
