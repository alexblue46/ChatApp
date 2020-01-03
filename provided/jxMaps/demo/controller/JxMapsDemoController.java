package provided.jxMaps.demo.controller;

import java.awt.EventQueue;

import provided.jxMaps.demo.view.JxMapsDemoFrame;
import provided.jxMaps.utils.IJxMapsComponentsFactory;
import provided.jxMaps.utils.IPlace;
import provided.jxMaps.demo.view.IView2ModelAdapter;
import provided.jxMaps.demo.model.DemoModel;
import provided.jxMaps.demo.model.IModel2ViewAdapter;


/**
 * The controller for the JxMaps demo app
 * @author swong
 *
 */
public class JxMapsDemoController {
	
	/**
	 * The view of the app
	 */
	JxMapsDemoFrame<IPlace> view;
	
	/**
	 * The model of the app
	 */
	DemoModel model;
	
	
	/**
	 * Constructs an instance of the app using the given Google Maps API key string.
	 * @param googleApiKey  The Google Maps API key string
	 */
	public JxMapsDemoController(String googleApiKey) {
		view = new JxMapsDemoFrame<IPlace>(googleApiKey, "COMP 310 JxDemo", new IView2ModelAdapter<IPlace>() {

			@Override
			public void addMarkerAt() {
				model.addMarkerAtCenter();
			}

			@Override
			public void addMoveTo(IPlace place) {
				model.moveMapTo(place);
			}

			@Override
			public void geocodeLocation(String location) {
				model.geocodeRequest(location);
			}

			@Override
			public void makePolygon() {
				model.makePolygon();
			}
			
			@Override
			public void makePolyline() {
				model.makePolyline();
			}
			@Override
			public void clearPolygonPoints() {
				model.clearRecentLatLngs();
			}

			@Override
			public void searchNearby(String placeType, String radius) {
				model.searchNearbyPlaces(placeType, Double.valueOf(radius));
				
			}
			
		});
		
		model = new DemoModel(new IModel2ViewAdapter() {

			@Override
			public IJxMapsComponentsFactory getMapComponentsFactory() {
				return view.getMapComponentsFactory();
			}

			@Override
			public void addPlace(IPlace place) {
				view.addPlace(place);
			}

			@Override
			public void displayStatus(String str) {
				view.displayStatus(str);
			}

			@Override
			public void refresh() {
				view.refresh();
			}

			
		});
	}
	
	
	/**
	 * Start the app
	 */
	public void start() {
		view.start();
		model.start();
	}
	/**
	 * Launch the application where the first parameter on the 
	 * command line should be the Google Maps API key.
	 * @param args args[0] = Google Maps API key string
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if(0<args.length) {
						String googleApiKey = args[0];  
						new JxMapsDemoController(googleApiKey).start();
					}
					else {
						throw new IllegalArgumentException("No Google Maps API key string supplied on the command line!");
					}
				} catch (Exception e) {
					System.err.println("[JxMapsDemoController.main()] Exception while starting the system: "+e);
					e.printStackTrace();
				}
			}
		});
	}
}
