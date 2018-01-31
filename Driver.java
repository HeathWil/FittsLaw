import java.util.ArrayList;
import java.util.Random;

import com.sun.javafx.geom.Point2D;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Driver extends Application{

	protected Random random= new Random();
	public int count=0;
	public ArrayList<Long> times= new ArrayList<Long>();
	public ArrayList<Long> results= new ArrayList<Long>();
	public ArrayList<Point2D> places= new ArrayList<Point2D>();
	public ArrayList<Double> difficulty= new ArrayList<Double>();
	public int totalCount=0;
	public Circle circle= circle();



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}


	@Override
	public void start(Stage stage) {
		Group root= new Group(rules(), circle);
		Scene scene= new Scene(root,700,500, Color.FLORALWHITE);
		times.add(0, System.currentTimeMillis());


		TranslateTransition transition= transition(circle);
		circleClick(scene, circle, transition);



		stage.setScene(scene);
		stage.show();
	}

	/* public Label label(){
    	Label label

    	return;
    }*/

	public Circle circle(){
		Circle circle = new Circle(200,150,50, Color.DARKCYAN);
		circle.setOpacity(0.8);
		return circle;
	}


	public TranslateTransition transition(Circle circle){
		Duration dur= new Duration(0.25);
		TranslateTransition trans= new TranslateTransition(dur,circle);
		trans.setOnFinished(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent e) {

				if(count<10){
					circle.setCenterX(circle.getTranslateX() + circle.getCenterX());
					circle.setCenterY(circle.getTranslateY() + circle.getCenterY());
					circle.setTranslateX(0);
					circle.setTranslateY(0);
				}
			}

		});

		return trans;
	}



	public void circleClick(Scene scene, Circle circle, TranslateTransition transition){

		scene.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
					totalCount++;
				
			}

		}
				);

		circle.setOnMousePressed(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {

				if(count<10){
					if(!event.isControlDown()){
						int x=getRandomX();
						int y=getRandomY();
						circle.setCenterX(x);
						circle.setCenterY(y);
						Point2D point= new Point2D(x,y);
						places.add(point);
					} 
					times.add(System.currentTimeMillis());
					count++;
				}
				else{
					//count == 10
					int j=1;
					for(int i=0; i<times.size(); i++){
						if(j<times.size() && i<times.size()-1){
							results.add(math(times.get(i), times.get(j)));
							j++;
						}
					}

					fittsLaw();

				}// end of else
			}
		});




	}


	public Label rules(){
		Label rules = new Label("Click on the shape");
		rules.setTextFill(Color.BLACK);
		return rules;
	}

	private int getRandomX(){
		int x=random.nextInt(700);

		return x;
	}
	

	private int getRandomY(){
		int y=random.nextInt(500);

		return y;
	}

	private long math(long first, long second){	
		return second-first;
	}
	
	private double distance(float x, float y, float x2, float y2){
		
		double res=0.0;
		
		res=Math.sqrt((y2-y)*(y2-y) + (x2-x)*(x2-x));
		
		return res;
	}
	
	private void fittsLaw(){
		double width= circle.getRadius()*2;
		double iD=0.0;
		
		int j=1;
		for(int i=0; i<places.size(); i++){
			if(j<places.size()){
		iD=Math.log((distance(places.get(i).x, places.get(i).y, places.get(j).x, places.get(j).y))/(width) +1 );
		difficulty.add(iD);
			}
			j++;
		}
		
	
		Stage secondStage= new Stage();
		NumberAxis xAxis= new NumberAxis();
		NumberAxis yAxis= new NumberAxis();
		xAxis.setLabel("Time");
		yAxis.setLabel("Difficulty");
		
		LineChart lineChart= new LineChart(xAxis, yAxis);
		
		XYChart.Series series= new XYChart.Series();
		for(int i=0; i< difficulty.size(); i++){
		series.getData().add(new XYChart.Data(results.get(i), difficulty.get(i)));
		}
		
		Scene scene1= new Scene(lineChart, 800,600);
		lineChart.getData().add(series);
		
		secondStage.setScene(scene1);
		secondStage.show();
		
		
		
		Stage thirdStage= new Stage();
		int finalC=0;
		if(totalCount>9){
			finalC=totalCount-9;
		}
		
		ObservableList<PieChart.Data> data= FXCollections.observableArrayList(
				new PieChart.Data("Failures", finalC),
				new PieChart.Data("Successes", 10));
		
		PieChart chart= new PieChart(data);
		chart.setTitle("MouseData");
		
		Scene scene2= new Scene(chart, 800,600);
		thirdStage.setScene(scene2);
		thirdStage.show();
		
	}


}






