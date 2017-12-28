    package com.johanw.stackoverflow;

    import javafx.application.Application;
    import javafx.embed.swing.SwingFXUtils;
    import javafx.event.EventHandler;
    import javafx.geometry.HPos;
    import javafx.geometry.Pos;
    import javafx.geometry.VPos;
    import javafx.scene.Group;
    import javafx.scene.Node;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.Tab;
    import javafx.scene.control.TabPane;
    import javafx.scene.image.WritableImage;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.layout.*;
    import javafx.scene.paint.Color;
    import javafx.scene.web.WebEngine;
    import javafx.scene.web.WebView;
    import javafx.stage.Stage;

    import javax.imageio.ImageIO;
    import java.awt.image.RenderedImage;
    import java.io.File;
    import java.io.IOException;


    public class WebViewSample extends Application {
        private Scene scene;
        private TheBrowser theBrowser;

        private void setLabel(Label label) {
            label.setText("" + theBrowser.browser.isVisible());
        }

        @Override
        public void start(Stage primaryStage) {
            primaryStage.setTitle("Tabs");
            Group root = new Group();
            Scene scene = new Scene(root, 400, 250, Color.WHITE);

            TabPane tabPane = new TabPane();

            BorderPane borderPane = new BorderPane();

            theBrowser = new TheBrowser();
            {
                Tab tab = new Tab();
                tab.setText("Other tab");

                HBox hbox0 = new HBox();
                {
                    Button button = new Button("Screenshot");
                    button.addEventHandler(MouseEvent.MOUSE_PRESSED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) {
                                    WritableImage image = theBrowser.getBrowser().snapshot(null, null);
                                    File file = new File("test.png");
                                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(image, null);
                                    try {
                                        ImageIO.write(
                                                renderedImage,
                                                "png",
                                                file);
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                }
                            });
                    hbox0.getChildren().add(button);
                    hbox0.setAlignment(Pos.CENTER);
                }

                HBox hbox1 = new HBox();
                Label visibleLabel = new Label("");
                {
                    hbox1.getChildren().add(new Label("webView.isVisible() = "));
                    hbox1.getChildren().add(visibleLabel);
                    hbox1.setAlignment(Pos.CENTER);
                    setLabel(visibleLabel);
                }
                HBox hbox2 = new HBox();
                {
                    Button button = new Button("Refresh");
                    button.addEventHandler(MouseEvent.MOUSE_PRESSED,
                            new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent e) {
                                    setLabel(visibleLabel);
                                }
                            });
                    hbox2.getChildren().add(button);
                    hbox2.setAlignment(Pos.CENTER);
                }
                VBox vbox = new VBox();
                vbox.getChildren().addAll(hbox0);
                vbox.getChildren().addAll(hbox1);
                vbox.getChildren().addAll(hbox2);
                tab.setContent(vbox);
                tabPane.getTabs().add(tab);
            }
            {
                Tab tab = new Tab();
                tab.setText("Browser tab");
                HBox hbox = new HBox();
                hbox.getChildren().add(theBrowser);
                hbox.setAlignment(Pos.CENTER);
                tab.setContent(hbox);
                tabPane.getTabs().add(tab);
            }

            // bind to take available space
            borderPane.prefHeightProperty().bind(scene.heightProperty());
            borderPane.prefWidthProperty().bind(scene.widthProperty());

            borderPane.setCenter(tabPane);
            root.getChildren().add(borderPane);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args){
            launch(args);
        }
    }

    class TheBrowser extends Region {

        final WebView browser;
        final WebEngine webEngine;

        public TheBrowser() {
            browser = new WebViewChanges().newWebView();
            webEngine = browser.getEngine();
            getStyleClass().add("browser");
            webEngine.load("http://www.google.com");
            getChildren().add(browser);

        }
        private Node createSpacer() {
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            return spacer;
        }

        @Override protected void layoutChildren() {
            double w = getWidth();
            double h = getHeight();
            layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
        }

        @Override protected double computePrefWidth(double height) {
            return 750;
        }

        @Override protected double computePrefHeight(double width) {
            return 500;
        }

        public WebView getBrowser() {
            return browser;
        }

        public WebEngine getWebEngine() {
            return webEngine;
        }
    }