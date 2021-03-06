package Controller;

import View.*;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.lang.StrictMath.sqrt;


public class GameController extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage theStage)
    {

        //TODO Create a class for each station (square, circle, etc) and put their events inside

        // Test

        double vitesseTrain = 0.13;
        double xCenterTri =800;
        double yCenterTri = 400;
        double tailleTri = 40;

        double xCenterCir = 120;
        double yCenterCir = 120;
        double radius = 20;


        StationTriangle st= new StationTriangle(400,100 , 60);
        st.setEvents(Color.ORANGE);
        st.setColor(Color.BLACK);

        StationTriangle st2= new StationTriangle(xCenterTri,yCenterTri , 60);
        st2.setEvents(Color.BLUE);
        st2.setColor(Color.BLACK);


        Canvas canvas = new Canvas( 1024, 1024 );
        Color colorLine = Color.ORANGE;

        theStage.setTitle( "Mini Metro" );
        Group root = new Group();


        Circle circle = new Circle(xCenterCir,yCenterCir,radius);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);

        //To set the width of the stroke using setStrokeWidth()
        circle.setStrokeWidth(3);



        Polygon triangle = new Polygon();
        triangle.getPoints().setAll(
                xCenterTri, yCenterTri- (sqrt(3/2)*tailleTri/2),
                xCenterTri- (tailleTri/2), yCenterTri+ (sqrt(3/2)*tailleTri/2),
                xCenterTri+ (tailleTri/2), yCenterTri+ (sqrt(3/2)*tailleTri/2)
        );

        triangle.setStroke(Color.BLACK);
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStrokeWidth(4);

        final Rectangle train = new Rectangle (xCenterCir, yCenterCir, 30, 20);
        train.setArcHeight(5);
        train.setArcWidth(5);
        train.setFill(colorLine);

        final Rectangle wagon = new Rectangle (train.getX()+35, yCenterCir, 30, 20);
        wagon.setArcHeight(5);
        wagon.setArcWidth(5);
        wagon.setFill(colorLine);
        wagon.setFill(colorLine);


        final View.Train train1 = new View.Train(200,200, 30);

        Group transport = new Group(train/*,wagon*/);

        //final Line line = new Line(xCenterCir, yCenterCir, xCenterTri ,yCenterTri);
        //line.setStroke(colorLine);
        //line.setStrokeWidth(4);

        Path path = new Path();
        path.getElements().add(new MoveTo(xCenterTri,yCenterTri));
        PathTransition pathTransition = new PathTransition();
        path.getElements().add(new LineTo(xCenterTri,yCenterTri));
        path.getElements().add(new LineTo(xCenterCir, yCenterCir));
        pathTransition.setDuration(Duration.millis(sqrt((xCenterTri - xCenterCir)*(xCenterTri - xCenterCir)
                                                    +(yCenterTri - yCenterCir) *(yCenterTri - yCenterCir))/
                                                    vitesseTrain));
        pathTransition.setPath(path);
        pathTransition.setNode(transport);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        //pathTransition.play();

        MetroLine l = new MetroLine(Color.BLUE);
        Square s = new Square(600 , 500, 30);





        circle.setOnDragDetected(new EventHandler <MouseEvent>() {
            public void handle(MouseEvent event) {
                /* drag was detected, start drag-and-drop gesture*/
                System.out.println("onDragDetected");

                /* allow any transfer mode */
                Dragboard db = circle.startDragAndDrop(TransferMode.ANY);

                /* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                final double xStart = circle.getCenterX();
                final double yStart = circle.getCenterY();
                content.putString(""+String.valueOf(xStart)+"/" + String.valueOf(circle.getCenterY())+"");
                db.setContent(content);

                event.consume();
            }
        });



        canvas.setOnDragOver(new EventHandler <DragEvent>() {


            public void handle(DragEvent event) {
                if (event.getGestureSource() != triangle &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                System.out.println("drag on canvas");



            }

        });


        triangle.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
                System.out.println("onDragOver");

                /* accept it only if it is  not dragged from the same node
                 * and if it has a string data */
                if (event.getGestureSource() != triangle &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }

                event.consume();
            }
        });

        triangle.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
                System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != triangle &&
                        event.getDragboard().hasString()) {
                    triangle.setFill(Color.ORANGE);
                }

                event.consume();
            }
        });

        triangle.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                triangle.setFill(Color.TRANSPARENT);

                event.consume();
            }
        });

        st2.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    success = true;
                }
                String s = db.getString();
                System.out.println(s);
                int index = s.indexOf("/");
                String s1 = s.substring(0,index-1);
                String s2 = s.substring(index+1);
                System.out.println(s1 +" "+s2);
                //pathTransition.play();
                root.getChildren().addAll(new MetroLine(Double.valueOf(s1),Double.valueOf(s2),st2.getCenterX(),st2.getCenterY(),Color.ORANGE));
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

        st.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
                System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                    success = true;
                }
                String s = db.getString();
                int separator = s.indexOf("/");
                String s1 = s.substring(0,separator-1);
                String s2 = s.substring(separator+1);

                System.out.println(s1 +" "+s2);
                //pathTransition.play();
                root.getChildren().addAll(new MetroLine(Double.valueOf(s1),Double.valueOf(s2),st.getCenterX(),st.getCenterY(),Color.LIGHTBLUE));
                /* let the source know whether the string was successfully
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });

        circle.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {

                }

                event.consume();
            }
        });




        root.getChildren().addAll( canvas, st2, circle, st, train1, s );



        GraphicsContext gc = canvas.getGraphicsContext2D();

        /*Image earth = new Image( "earth.png" );
        Image sun   = new Image( "sun.png" );
        Image space = new Image( "space.png" );*/

        //final long startNanoTime = System.nanoTime();


        /*
        new AnimationTimer()
        {
            public void handle(long currentNanoTime)
            {

                double t = (currentNanoTime - startNanoTime) / 100000000.0;

                // background image clears canvas

                circle.setCenterX(t);
                circle.setCenterY(t);

            }
        }.start();*/

        Scene theScene = new Scene( root );
        theStage.setScene( theScene );

        theStage.show();
    }
}

