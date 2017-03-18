package opengl;

public interface IGameLogic {
    public void init() throws Exception;
    public void input(Window window);
    public void update(float interval);
    public void render(Window window);
}
