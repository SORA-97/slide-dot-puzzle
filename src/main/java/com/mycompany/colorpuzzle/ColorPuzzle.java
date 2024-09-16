package com.mycompany.colorpuzzle;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.FontWeight;

////////////////////////////////////////////////////////////////////////////////
// 01, 31, 20, 12, 03, 23
// 013, 123, 012, 023
// 0123
class Stages {
    private final String[][] ANSWER = {
        {"0000111111111111", "3333311331133333", "0220222222220220", "1111111122222222", "0303030303030303", "2222233322233333", "1010001111000101", "1331311331131331", "2000220220220002", "1212212112122121"}, 
        {"0000111111113333", "3322332222112211", "2002011001102002", "3003220220223003", "0001001301331333", "2332333131112112", "0120120120120120", "2033200320032203", "1333131111010001", "3123112222113213"}, 
        {"0000111122223333", "0000011101220123", "0011001122332233", "0001323131013222", "0123123023013012", "0120233113320210", "0001301133213222", "3012301221032103", "0013221331223100", "2001302233120113"}
    };
    private final int ANSWER_LENGTH = 16;
    final int difficulty;
    final int stage;

    Stages(final int difficulty, final int stage) {
        this.difficulty = difficulty;
        this.stage = stage;
        
        for (int i = 0; i < ANSWER.length; i++) {
            for (int j = 0; j < ANSWER[i].length; j++) {
                if (ANSWER[i][j].length() != ANSWER_LENGTH) {
                    throw new IllegalArgumentException("エラー: answer[" + i + "][" + j + "] の長さが " + ANSWER[i][j].length() + " です。");
                }
            }
        }
    }
    
    boolean[][] setClear() {
        final boolean[][] clear;
        clear = new boolean[ANSWER.length][];
        for (int i = 0; i < ANSWER.length; i++) {
            clear[i] = new boolean[ANSWER[i].length];
        }
        return clear;
    }
    
    String getAnswer() {
        return ANSWER[difficulty][stage];
    }
    
    int getLastDifficulty() {
         return ANSWER.length - 1;
    }
    
    int getLastStage() {
         return ANSWER[getLastDifficulty()].length - 1;
    }
    
    int getDifficultyLength() {
         return ANSWER.length;
    }
    
    int getStageLength(int difficulty) {
         return ANSWER[difficulty].length;
    }
    
    Stages previous() {
        if(stage != 0) return new Stages(difficulty, stage - 1);
        if(difficulty != 0) return new Stages(difficulty - 1, ANSWER[difficulty - 1].length - 1);
        return new Stages(difficulty, stage);
    }

    Stages next() {
        if(stage != ANSWER[difficulty].length - 1) return new Stages(difficulty, stage + 1);
        if(difficulty != ANSWER.length - 1) return new Stages(difficulty + 1, 0);
        return new Stages(difficulty, stage);
    }
}

////////////////////////////////////////////////////////////////////////////////
class Grids {
    private final int GRID_SIZE = 4;
    private final int RECT_SIZE = 75;
    private final String answer;
    
    Grids(String answer) {
        this.answer = answer;
    }
    
    String createPuzzle() {
        final String order;
        order = shuffleOrder();
        return order;
    }
    
    String shuffleOrder() {
        final List<Integer> numbers = new ArrayList<>();
        for (char num : answer.toCharArray()) {
            numbers.add(Character.getNumericValue(num));
        }
        Collections.shuffle(numbers);
        final StringBuilder result = new StringBuilder();
        for (int number : numbers) {
            result.append(number);
        }
        final String order;
        if(result.toString().equals(answer)) {
            order = shuffleOrder(); // 振り直し
        } else {
            order = result.toString();
        }
        return order;
    }
    
    GridPane createPuzzleGrid(final String order) {
        final GridPane puzzleGridPane;
        puzzleGridPane = new GridPane();
        puzzleGridPane.setHgap(2);
        puzzleGridPane.setVgap(2);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                final int index = i * GRID_SIZE + j;
                final char colorCode = order.charAt(index);
                final Rectangle rectangle = new Rectangle(RECT_SIZE, RECT_SIZE);
                rectangle.setFill(getColorFromCode(colorCode));
                puzzleGridPane.add(rectangle, j, i);
            }
        }
        return puzzleGridPane;
    }
    
    GridPane createAnswerGrid() {
        final GridPane answerGridPane;
        answerGridPane = new GridPane();
        answerGridPane.setHgap(1);
        answerGridPane.setVgap(1);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                final int index = i * GRID_SIZE + j;
                final char colorCode = answer.charAt(index);
                final Rectangle rectangle = new Rectangle(RECT_SIZE / 2, RECT_SIZE / 2);
                rectangle.setFill(getColorFromCode(colorCode));
                answerGridPane.add(rectangle, j, i);
            }
        }
        return answerGridPane;
    }
    
    GridPane createSampleGrid() {
        final GridPane sampleGridPane;
        sampleGridPane = new GridPane();
        sampleGridPane.setHgap(1);
        sampleGridPane.setVgap(1);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                final int index = i * GRID_SIZE + j;
                final char colorCode = answer.charAt(index);
                final Rectangle rectangle = new Rectangle(RECT_SIZE / 5, RECT_SIZE / 5);
                rectangle.setFill(getColorFromCode(colorCode));
                sampleGridPane.add(rectangle, j, i);
            }
        }
        return sampleGridPane;
    }
    
    Color getColorFromCode(final char code) {
        return switch (code) {
            case '0' -> Color.rgb(227, 81, 40);
            case '1' -> Color.rgb(242, 178, 34);
            case '2' -> Color.rgb(123, 179, 69);
            case '3' -> Color.rgb(34, 164, 222);
            default -> throw new IllegalArgumentException("Invalid color number.");
        };
    }
}

////////////////////////////////////////////////////////////////////////////////
class Slide {
    private final int number;
    
    Slide(int number) {
        this.number = number;
    }
    
    int getDirection() {
        return number / 4;
    }
    
    ImageView createSlideButton(final String imagePath) {
        final int SLIDE_BUTTON_SIZE = 69;
        final Image image = new Image(imagePath);
        final ImageView imageView = new ImageView(image);
        if (getDirection() == 0 || getDirection() == 2) {
            imageView.setFitWidth(SLIDE_BUTTON_SIZE);
        } else {
            imageView.setFitHeight(SLIDE_BUTTON_SIZE);
        }
        imageView.setPreserveRatio(true);
        return imageView;
    }
    
    String slideOrder(final String order) {
        final int correctedNumber;
        final String correctedOrder;
        switch (number / 4) {
            case 0: // 上移動
                correctedNumber = number;
                correctedOrder = swapCharacters(order, correctedNumber, correctedNumber + 4, correctedNumber + 8, correctedNumber + 12);
                break;
            case 1: // 右移動
                correctedNumber = (number - 4) * 4;
                correctedOrder = swapCharacters(order, correctedNumber + 3, correctedNumber + 2, correctedNumber + 1, correctedNumber);
                break;
            case 2: // 下移動
                correctedNumber = number - 8;
                correctedOrder = swapCharacters(order, correctedNumber + 12, correctedNumber + 8, correctedNumber + 4, correctedNumber);
                break;
            case 3: // 左移動
                correctedNumber = (number - 12) * 4;
                correctedOrder = swapCharacters(order, correctedNumber, correctedNumber + 1, correctedNumber + 2, correctedNumber + 3);
                break;
            default:
                correctedOrder = order;
                break;
        }
        return correctedOrder;
    }
    
    String swapCharacters(final String str, final int index1, final int index2, final int index3, final int index4) {
        if (str == null || str.length() < Math.max(Math.max(index1, index2), Math.max(index3, index4)) + 1) {
            throw new IllegalArgumentException("Invalid string or indices");
        }
        final StringBuilder sb = new StringBuilder(str);
        final char char1 = sb.charAt(index1);
        final char char2 = sb.charAt(index2);
        final char char3 = sb.charAt(index3);
        final char char4 = sb.charAt(index4);
        sb.setCharAt(index1, char2);
        sb.setCharAt(index2, char3);
        sb.setCharAt(index3, char4);
        sb.setCharAt(index4, char1);
        return sb.toString();
    }
}
////////////////////////////////////////////////////////////////////////////////
class SwitchingButtons {
    private final ImageView button;
    
    SwitchingButtons(ImageView button) {
        this.button = button;
    }
    
    void setButtonDisable() {
        button.setOpacity(0.5);
        button.setDisable(true);
    }
    
    void setButtonAble() {
        button.setOpacity(1);
        button.setDisable(false);
    }
}

class SwitchingImages {
    private final String imagePath;
    
    SwitchingImages(final String imagePath) {
        this.imagePath = imagePath;
    }
    
    ImageView getImageView() {
        final Image image = new Image(imagePath);
        final ImageView imageView = new ImageView(image);
        imageView.setFitHeight(55);
        imageView.setPreserveRatio(true);
        return imageView;
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class ColorPuzzle extends Application {
    Stages stages;
    Grids createStage;
    String order;
    GridPane answerGridPane;
    GridPane puzzleGridPane;
    Label currentStageLabel;
    boolean[][] clear;
    
    @Override
    public void start(Stage primaryStage) {
        AudioClip chooseSound = new AudioClip("file:src/main/resources/audio/choose.mp3");
        AudioClip quitSound= new AudioClip("file:src/main/resources/audio/quit.mp3");
        AudioClip slideSound= new AudioClip("file:src/main/resources/audio/slide.mp3");
        slideSound.setVolume(0.5);
        AudioClip switchDifficultySound= new AudioClip("file:src/main/resources/audio/difficulty.mp3");
        switchDifficultySound.setVolume(0.6);
        AudioClip chooseSound2 = new AudioClip("file:src/main/resources/audio/choose2.mp3");
        AudioClip resetSound= new AudioClip("file:src/main/resources/audio/reset.mp3");
        
        SwitchingImages previousImage = new SwitchingImages("file:src/main/resources/images/previous.png");
        ImageView previousButton = previousImage.getImageView();
        SwitchingImages resetImage = new SwitchingImages("file:src/main/resources/images/reset.png");
        ImageView resetButton = resetImage.getImageView();
        SwitchingImages nextImage = new SwitchingImages("file:src/main/resources/images/next.png");
        ImageView nextButton = nextImage.getImageView();
        
        // コース選択される前に、レイアウトの設定用に初期化
        stages = new Stages(0, 0);
        createStage = new Grids(stages.getAnswer());
        order = createStage.createPuzzle();
        answerGridPane = createStage.createAnswerGrid();
        puzzleGridPane = createStage.createPuzzleGrid(order);
        clear = stages.setClear();
        
        ////////////////////////////////////////////////////////////////////////
        // scenes
        VBox titleVBox = new VBox(10);
        BorderPane easyRoot = new BorderPane();
        BorderPane normalRoot = new BorderPane();
        BorderPane hardRoot = new BorderPane();
        BorderPane puzzleRoot = new BorderPane();
        
        Scene titleScene = new Scene(titleVBox, 840, 600);
        Scene easyScene = new Scene(easyRoot, 840, 600);
        Scene normalScene = new Scene(normalRoot, 840, 600);
        Scene hardScene = new Scene(hardRoot, 840, 600);
        Scene puzzleScene = new Scene(puzzleRoot, 840, 600);

        Image kani = new Image("file:src/main/resources/images/kani.png");
        Image smallQuit = new Image("file:src/main/resources/images/smallQuit.png");
        
        ////////////////////////////////////////////////////////////////////////
        // titleScene
        Label title = new Label("SLIDE DOT PUZZLE");
        title.setPadding(new javafx.geometry.Insets(80, 0, 50, 0));
        title.setFont(Font.font("Calibri", FontWeight.BLACK, 80));
        title.setTextFill(Color.rgb(227, 81, 40));
        
        Image start = new Image("file:src/main/resources/images/start.png");
        ImageView startButton = new ImageView(start);
        startButton.setFitHeight(60);
        startButton.setPreserveRatio(true);
        startButton.setOnMouseClicked(event -> {
            chooseSound.play();
            primaryStage.setScene(easyScene);
        });
        
        Image quit = new Image("file:src/main/resources/images/quit.png");
        ImageView quitGameButton = new ImageView(quit);
        quitGameButton.setFitHeight(60);
        quitGameButton.setPreserveRatio(true);
        quitGameButton.setOnMouseClicked(event -> primaryStage.close());
        
        ImageView kaniView2 = new ImageView(kani);
        kaniView2.setFitWidth(100);
        kaniView2.setPreserveRatio(true);
        
        VBox.setMargin(kaniView2, new Insets(30, 0, 0, 0));
        titleVBox.setAlignment(Pos.CENTER);
        titleVBox.setFillWidth(false);
        titleVBox.getChildren().addAll(title, startButton, quitGameButton, kaniView2);
        titleVBox.setStyle("-fx-background-color: #eae1cf;");
        
        ////////////////////////////////////////////////////////////////////////
        // easy, normal, hard
        HBox easySampleH = new HBox(10);
        easySampleH.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
        HBox normalSampleH = new HBox(10);
        normalSampleH.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
        HBox hardSampleH = new HBox(10);
        hardSampleH.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
        final Stages checkLength = new Stages(0, 0); // 問題数を知りたいだけ
        
        for(int i = 0; i < checkLength.getDifficultyLength(); i++) {
            for(int j = 0; j < checkLength.getStageLength(i); j++) {
                final Stages everyClear = new Stages(i, j);
                final Grids createSample = new Grids(everyClear.getAnswer());
                final GridPane sampleGrid = createSample.createSampleGrid();
                sampleGrid.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                    chooseSound2.play();
                    currentStageLabel.setText((everyClear.difficulty + 1) + " - " + (everyClear.stage + 1));
                    stages = new Stages(everyClear.difficulty, everyClear.stage);
                    updateGrids();
                    handleSwitchingButtonState(previousButton, nextButton);
                    primaryStage.setScene(puzzleScene);
                });
                switch(i) {
                    case 0 -> easySampleH.getChildren().add(sampleGrid);
                    case 1 -> normalSampleH.getChildren().add(sampleGrid);
                    case 2 -> hardSampleH.getChildren().add(sampleGrid);
                    default -> {
                    }
                }
            }
        }
        
        ////////////////////////////////////////////////////////////////////////
        // easyScene
        Image easy = new Image("file:src/main/resources/images/easy.png");
        ImageView easyButton = new ImageView (easy);
        easyButton.setFitHeight(70);
        easyButton.setPreserveRatio(true);
        easyButton.setOnMouseClicked(event -> {
            switchDifficultySound.play();
            primaryStage.setScene(normalScene);
        });
        
        ImageView quitEasyButton = new ImageView (smallQuit);
        quitEasyButton.setFitHeight(50);
        quitEasyButton.setPreserveRatio(true);
        quitEasyButton.setOnMouseClicked(event -> {
            quitSound.play();
            primaryStage.setScene(titleScene);
        });
        
        HBox easyButtons = new HBox(150);
        easyButtons.setPadding(new javafx.geometry.Insets(40, 0, 0, 50));
        easyButtons.getChildren().addAll(quitEasyButton, easyButton);
        
        VBox easySampleV = new VBox(50);
        easySampleV.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        easySampleV.getChildren().addAll(easyButtons,easySampleH);

        easyRoot.setCenter(easySampleV);
        easyRoot.setStyle("-fx-background-color: #eae1cf;");
        
        ////////////////////////////////////////////////////////////////////////
        // scene3
        Image normal = new Image("file:src/main/resources/images/normal.png");
        ImageView normalButton = new ImageView (normal);
        normalButton.setFitHeight(70);
        normalButton.setPreserveRatio(true);
        normalButton.setOnMouseClicked(event -> {
            switchDifficultySound.play();
            primaryStage.setScene(hardScene);
        });
        
        ImageView quitNormalButton = new ImageView (smallQuit);
        quitNormalButton.setFitHeight(50);
        quitNormalButton.setPreserveRatio(true);
        quitNormalButton.setOnMouseClicked(event -> {
            quitSound.play();
            primaryStage.setScene(titleScene);
        });
        
        HBox normalButtons = new HBox(150);
        normalButtons.setPadding(new javafx.geometry.Insets(40, 0, 0, 50));
        normalButtons.getChildren().addAll(quitNormalButton, normalButton);
        
        VBox normalSampleV = new VBox(50);
        normalSampleV.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        normalSampleV.getChildren().addAll(normalButtons,normalSampleH);
        
        normalRoot.setCenter(normalSampleV);
        normalRoot.setStyle("-fx-background-color: #eae1cf;");
        
        ////////////////////////////////////////////////////////////////////////
        // scene4
        Image hard = new Image("file:src/main/resources/images/hard.png");
        ImageView hardButton = new ImageView (hard);
        hardButton.setFitHeight(70);
        hardButton.setPreserveRatio(true);
        hardButton.setOnMouseClicked(event -> {
            switchDifficultySound.play();
            primaryStage.setScene(easyScene);
        });
        
        ImageView quitHardButton = new ImageView (smallQuit);
        quitHardButton.setFitHeight(50);
        quitHardButton.setPreserveRatio(true);
        quitHardButton.setOnMouseClicked(event -> {
            quitSound.play();
            primaryStage.setScene(titleScene);
        });
        
        HBox hardButtons = new HBox(150);
        hardButtons.setPadding(new javafx.geometry.Insets(40, 0, 0, 50));
        hardButtons.getChildren().addAll(quitHardButton, hardButton);
        
        VBox hardSampleV = new VBox(50);
        hardSampleV.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        hardSampleV.getChildren().addAll(hardButtons ,hardSampleH);
        hardRoot.setCenter(hardSampleV);
        hardRoot.setStyle("-fx-background-color: #eae1cf;");
        
        ////////////////////////////////////////////////////////////////////////
        // puzzleScene
        // スライドボタン
        HBox upButtons = new HBox(8);
        upButtons.setPadding(new javafx.geometry.Insets(0, 0, 0, 47));
        VBox rightButtons = new VBox(8);
        rightButtons.setPadding(new javafx.geometry.Insets(2, 0, 0, 0));
        HBox downButtons = new HBox(8);
        downButtons.setPadding(new javafx.geometry.Insets(0, 0, 0, 47));
        VBox leftButtons = new VBox(8);
        leftButtons.setPadding(new javafx.geometry.Insets(2, 0, 0, 0));
        for (int i = 0; i < 16; i++) {
            Slide slide = new Slide(i);
            final int direction = slide.getDirection();
            final ImageView slideButton;
            if (direction < 0 || direction > 3) {
                throw new IllegalArgumentException("Invalid direction: " + direction);
            }
            switch (direction) {
                case 0:
                    slideButton = slide.createSlideButton("file:src/main/resources/images/slideUp.png");
                    upButtons.getChildren().add(slideButton);
                    break;
                case 1:
                    slideButton = slide.createSlideButton("file:src/main/resources/images/slideRight.png");
                    rightButtons.getChildren().add(slideButton);
                    break;
                case 2:
                    slideButton = slide.createSlideButton("file:src/main/resources/images/slideDown.png");
                    downButtons.getChildren().add(slideButton);
                    break;
                case 3:
                    slideButton = slide.createSlideButton("file:src/main/resources/images/slideLeft.png");
                    leftButtons.getChildren().add(slideButton);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + direction);
            }
            slideButton.setOnMouseClicked(event -> {
                if(clear[stages.difficulty][stages.stage] == false) {
                    slideSound.play();
                    order = slide.slideOrder(order); // orderの計算
                    GridPane newGridPane = createStage.createPuzzleGrid(order);
                    puzzleGridPane.getChildren().setAll(newGridPane.getChildren());
                    checkClear(order);
                }
            });
        }
        HBox puzzleHBox = new HBox(8);
        puzzleHBox.setPadding(new javafx.geometry.Insets(0, 0, 0, 0));
        puzzleHBox.getChildren().addAll(leftButtons, puzzleGridPane, rightButtons);
        VBox puzzleVBox = new VBox(8);
        puzzleVBox.setPadding(new javafx.geometry.Insets(60, 0, 0, 0));
        puzzleVBox.getChildren().addAll(upButtons, puzzleHBox, downButtons);
        
        // ステージ変更ボタン
        previousButton.setOnMouseClicked(event -> {
            chooseSound2.play();
            stages = stages.previous();
            currentStageLabel.setText((stages.difficulty + 1) + " - " + (stages.stage + 1));
            updateGrids();
            handleSwitchingButtonState(previousButton, nextButton);
        });
        resetButton.setOnMouseClicked(event -> {
            resetSound.play();
            clear[stages.difficulty][stages.stage] = false;
            updateGrids();
        });
        nextButton.setOnMouseClicked(event -> {
            chooseSound2.play();
            stages = stages.next();
            currentStageLabel.setText((stages.difficulty + 1) + " - " + (stages.stage + 1));
            updateGrids();
            handleSwitchingButtonState(previousButton, nextButton);
        });
        
        HBox switchingButtonsHBox = new HBox(6);
        switchingButtonsHBox.setAlignment(Pos.CENTER);
        switchingButtonsHBox.setPadding(new javafx.geometry.Insets(40, 0, 0, 0));
        switchingButtonsHBox.getChildren().addAll(previousButton, resetButton, nextButton);
        
        VBox switchingButtonsAndPuzzle = new VBox();
        switchingButtonsAndPuzzle.getChildren().addAll(switchingButtonsHBox, puzzleVBox);
        
        ImageView quitStageButton = new ImageView (smallQuit);
        quitStageButton.setFitHeight(50);
        quitStageButton.setPreserveRatio(true);
        quitStageButton.setOnMouseClicked(event -> {
            quitSound.play();
            if(stages.difficulty == 0) primaryStage.setScene(easyScene);
            if(stages.difficulty == 1) primaryStage.setScene(normalScene);
            if(stages.difficulty == 2) primaryStage.setScene(hardScene);
        });
        
        currentStageLabel = new Label(stages.difficulty + " - " + stages.stage);
        currentStageLabel.setPadding(new javafx.geometry.Insets(0, 0, 20, 0));
        currentStageLabel.setFont(Font.font("Calibri", FontWeight.BLACK, 60));
        
        ImageView kaniView = new ImageView(kani);
        kaniView.setFitWidth(60);
        kaniView.setPreserveRatio(true);
        StackPane kaniPane = new StackPane();
        kaniPane.getChildren().addAll(kaniView);
        
        VBox kaniAndAnswer = new VBox(-4);
        kaniAndAnswer.setAlignment(Pos.CENTER);
        kaniAndAnswer.setPadding(new javafx.geometry.Insets(140, 0, 0, 20));
        kaniAndAnswer.getChildren().addAll(currentStageLabel, kaniPane, answerGridPane);
        
        VBox leftSide = new VBox(0);
        leftSide.setPadding(new javafx.geometry.Insets(40, 0, 0, 50));
        leftSide.getChildren().addAll(quitStageButton, kaniAndAnswer);
        
        HBox hbox = new HBox(80);
        hbox.getChildren().addAll(leftSide, switchingButtonsAndPuzzle);
        puzzleRoot.setStyle("-fx-background-color: #eae1cf;");
        puzzleRoot.setCenter(hbox);
        
        primaryStage.setTitle("Slide Dot Puzzle");
        primaryStage.setScene(titleScene);
        primaryStage.show();
    }
    
    ////////////////////////////////////////////////////////////////////////////
    
    void checkClear(final String order) {
        if(clear[stages.difficulty][stages.stage] || !order.equals(stages.getAnswer())) return;
        cleared();
    }
    
    void cleared() {
        AudioClip clearSound= new AudioClip("file:src/main/resources/audio/clear.mp3");
        clearSound.play();
        currentStageLabel.setTextFill(Color.rgb(34, 164, 222));
        clear[stages.difficulty][stages.stage] = true;
    }
    
    private void updateGrids() {
        final Grids createNewStage = new Grids(stages.getAnswer());
        final GridPane newAnswerGrid = createNewStage.createAnswerGrid();
        answerGridPane.getChildren().setAll(newAnswerGrid.getChildren());
        order = createNewStage.createPuzzle();
        
        if (clear[stages.difficulty][stages.stage] == true) {
            final GridPane solvedPuzzleGrid = createStage.createPuzzleGrid(stages.getAnswer());
            puzzleGridPane.getChildren().setAll(solvedPuzzleGrid.getChildren());
            currentStageLabel.setTextFill(Color.rgb(34, 164, 222));
        } else {
            final GridPane newPuzzleGrid = createNewStage.createPuzzleGrid(order);
            puzzleGridPane.getChildren().setAll(newPuzzleGrid.getChildren());
            currentStageLabel.setTextFill(Color.rgb(227, 81, 40));
        }
    }
    
    private void handleSwitchingButtonState(ImageView previousButton, ImageView nextButton) {
        final SwitchingButtons previous = new SwitchingButtons(previousButton);
        final SwitchingButtons next = new SwitchingButtons(nextButton);
        if (stages.difficulty == 0 && stages.stage == 0) {
            previous.setButtonDisable();
        } else if (stages.difficulty == stages.getLastDifficulty() && stages.stage == stages.getLastStage()) {
            next.setButtonDisable();
        } else {
            previous.setButtonAble();
            next.setButtonAble();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}