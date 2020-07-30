package ph.parcs.rmhometiles;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ph.parcs.rmhometiles.ui.scene.SceneManager;

import java.net.URISyntaxException;

@Component
public class StageInitializer implements ApplicationListener<JavaFxApplication.StageReadyEvent> {

    @Value("${spring.application.ui.title}")
    private String appTitle;

    @Value("${spring.application.ui.width}")
    private Integer appWidth;

    @Value("${spring.application.ui.height}")
    private Integer appHeight;

    private ApplicationContext applicationContext;
    private SceneManager sceneManager;
    private Stage stage;

    public StageInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(JavaFxApplication.StageReadyEvent stageReadyEvent) {
        sceneManager.load();
        stage = createStage();
        stage.show();
    }

    @SneakyThrows
    private Stage createStage() throws URISyntaxException {
        Parent content = sceneManager.getContent(State.LOGIN);
        Scene scene = new Scene(content, appWidth, appHeight);
        scene.setFill(Color.TRANSPARENT);

        Stage stage = new Stage();
        stage.getIcons().add(new Image(getClass().getResource("/image/logo.png").toURI().toString()));
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle(appTitle);
        return stage;
    }


    @Autowired
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @Bean
    public Stage getStage() {
        return stage;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


}
