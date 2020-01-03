package arb19_kpa1.game.view;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.RectangleOptions;

import arb19_kpa1.game.model.GameState;
import arb19_kpa1.game.model.units.AGameUnit;
import arb19_kpa1.game.model.utility.LatLng;
import arb19_kpa1.game.model.utility.LatLngBounds;
import provided.jxMaps.ui.JxMapsPanel;
import provided.jxMaps.utils.IAction;
import provided.jxMaps.utils.IJxMapsComponentsFactory;
import provided.jxMaps.utils.IJxMaps_Defs.IEvent;
import provided.jxMaps.utils.IMapNavigator;
import provided.jxMaps.utils.enhanced.EnhancedInfoWindow;
import provided.jxMaps.utils.enhanced.EnhancedRectangle;

/**
 * The view for the game map
 * 
 * @author Owner
 *
 */
public class GameMapView {

	/**
	 * The adapter to the map
	 */
	private IMapView2MapAdpt map2view;

	/**
	 * A rotating queue of units
	 */
	private Queue<GameUnitView> unitViews = new ConcurrentLinkedQueue<GameUnitView>();

	/**
	 * The Jx map view
	 */
	private JxMapsPanel mapView;

	/**
	 * The coponent factory for maps
	 */
	private IJxMapsComponentsFactory compFactory;

	/**
	 * The navigator oject for maps
	 */
	private IMapNavigator navigator;

	/**
	 * The size of clicks
	 */
	private static final int CLICK_SIZE = 75;

	/**
	 * The zoom to initialize the map to
	 */
	private static final double INITIAL_ZOOM = 4.0;

	/**
	 * The current mouseEvent being listened to
	 */
	private MapMouseEvent mouseEvent;

	/**
	 * The rectangle enclosing the boundaries of the game
	 */
	@SuppressWarnings("unused")
	private EnhancedRectangle boundsRect;

	/**
	 * Gets the map view
	 * 
	 * @return a JxMapsPanel
	 */
	public JxMapsPanel getMapView() {
		return mapView;
	}

	/**
	 * Creates a new GameMapView
	 * 
	 * @param map2view     the adapter to the view
	 * @param apiKey       the map API key
	 * @param initialState the initial game state
	 * @param bounds       the boundary object
	 */
	public GameMapView(IMapView2MapAdpt map2view, String apiKey, GameState initialState, LatLngBounds bounds) {
		this.map2view = map2view;
		mapView = JxMapsPanel.DEFAULT_FACTORY.apply(apiKey);
		compFactory = mapView.getComponentsFactory();
		navigator = compFactory.makeNavigator();
		setMouseEvents();
		update(initialState);
		navigator.moveTo(bounds.getJxMapsEquiv().getCenter());
		drawBoundsRect(bounds);
		mapView.getMap().setZoom(INITIAL_ZOOM);
	}

	/**
	 * Draws the rectangle around the bounds
	 * 
	 * @param bounds the boundary object
	 */
	private void drawBoundsRect(LatLngBounds bounds) {
		RectangleOptions options = new RectangleOptions();
		options.setBounds(bounds.getJxMapsEquiv());
		options.setClickable(false);
		options.setVisible(true);
		options.setFillOpacity(0.0);
		options.setStrokeColor("#EE1111");
		options.setStrokeOpacity(0.8);

		boundsRect = getCompFactory().makeRectangle(options);
	}

	/**
	 * Updates the view to a new game state
	 * 
	 * @param state the new game state
	 */
	public void update(GameState state) {
		unitViews.forEach(unitView -> unitView.getMarker().remove());
		unitViews.clear();
		state.getGameUnits().forEach(unit -> unitViews.add(new GameUnitView(unit, compFactory)));
	}

	/**
	 * Changes the mouse event listener based on state
	 */
	public void setMouseEvents() {
		mouseEvent = navigator.addMapMouseEvent(IEvent.CLICK, new IAction<MouseEvent>() {

			@Override
			public void accept(MouseEvent t) {
				System.out.println("Before mouse event processed");
				for (int i = 0; i < unitViews.size(); i++) {
					GameUnitView unitView = unitViews.poll();
					if (unitView.getUnit().getPosition()
							.distance(new LatLng(t.latLng().getLat(), t.latLng().getLng())) < CLICK_SIZE) {
						map2view.putUnitInCntrlPanel(unitView.getUnit());
						unitViews.add(unitView);
						break;
					} else {
						unitViews.add(unitView);
					}
				}
				System.out.println("After mouse event processed");
			}

		});
	}

	/**
	 * Removes the current mouse event
	 */
	public void removeMouseEvents() {
		navigator.removeMapMouseEvent(mouseEvent);
	}

	/**
	 * Sets a label on a unit marker
	 * 
	 * @param unit  the unit corresponding to the marker
	 * @param title the new title
	 */
	public void setMarkerLabel(AGameUnit unit, String title) {
		for (GameUnitView unitView : unitViews) {
			if (unitView.getUnit().equals(unit))
				unitView.setMarker(title, compFactory);
		}
	}

	/**
	 * Displays a notification at a given location and centers the camera
	 * 
	 * @param string          the notification
	 * @param notificationLoc the location
	 */
	public void displayNotification(String string, LatLng notificationLoc) {
		InfoWindowOptions opt = new InfoWindowOptions();
		opt.setPosition(notificationLoc.getJxMapsEquiv());
		opt.setContent(string);
		EnhancedInfoWindow window = compFactory.makeInfoWindow(opt);
		window.open();
		navigator.moveTo(notificationLoc.getJxMapsEquiv());
	}

	/**
	 * GEts the component factory
	 * 
	 * @return the component factory
	 */
	public IJxMapsComponentsFactory getCompFactory() {
		return compFactory;
	}

	/**
	 * Gets the map navigatory object
	 * 
	 * @return navigator
	 */
	public IMapNavigator getNavigator() {
		return navigator;
	}

}
