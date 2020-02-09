package ph.parcs.rmhometiles.scene;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ph.parcs.rmhometiles.StageInitializer;
import ph.parcs.rmhometiles.State;

import java.util.HashMap;
import java.util.Map;

@Component
public class SceneManager {

    private Map<State, Parent> states = new HashMap<>();
    private StageInitializer stageInitializer;

    @SneakyThrows
    public void load() {
        states = new HashMap<>();
        states.put(State.HOME, loadUI(State.HOME));
        states.put(State.CATEGORY, loadUI(State.CATEGORY));
        states.put(State.PRODUCT, loadUI(State.PRODUCT));
        states.put(State.LOGIN, loadUI(State.LOGIN));
        states.put(State.ERROR, loadUI(State.ERROR));
        states.put(State.DASHBOARD, loadUI(State.DASHBOARD));
    }

    @SneakyThrows
    public Parent loadUI(State view) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));
        loader.setControllerFactory(aClass -> stageInitializer.getApplicationContext().getBean(aClass));
        return loader.load();
    }

    @SneakyThrows
    public void changeScene(State state) {
        Stage root = stageInitializer.getStage();
        root.getScene().setRoot(states.get(state));
        root.setMaximized(true);
        root.centerOnScreen();
    }

    @SneakyThrows
    public Parent getContent(State state) {
        if (states.containsKey(state)) return states.get(state);
        return states.get(State.ERROR);
    }

    @Autowired
    public void setStageInitializer(StageInitializer stageInitializer) {
        this.stageInitializer = stageInitializer;
    }
}
