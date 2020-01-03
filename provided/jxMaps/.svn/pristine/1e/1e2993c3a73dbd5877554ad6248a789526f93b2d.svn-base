package provided.jxMaps.demo.view;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import provided.jxMaps.ui.JxMapsPanel;
import provided.jxMaps.utils.IJxMapsComponentsFactory;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

/**
 * The JxDemo app's view
 * @author swong
 *
 * @param <TPlacesItem> The type of objects put on the places drop list
 */
public class JxMapsDemoFrame<TPlacesItem> extends JFrame {

	/**
	 * For serialization but don't do it!  Always instantiate GUI elements ON THE TARGET MACHINE.
	 */
	private static final long serialVersionUID = 5283849811019384190L;
	
	
	/**
	 * The adapter to the model
	 */
	private IView2ModelAdapter<TPlacesItem> view2ModelAdpt;
	
	/**
	 * The content pane of this frame
	 */
	private JPanel contentPane;
	/**
	 * The map panel
	 */
	private final JxMapsPanel pnlMap;
	/**
	 * The control panel
	 */
	private final JPanel pnlControl = new JPanel();
	/**
	 * A label to show on the view
	 */
	private final JLabel lblJxMaps = new JLabel("Right-click the map to create an info window!");
	/**
	 * A button for testing ideas
	 */
	private final JButton btnTest = new JButton("Make marker at map center");

	/**
	 * A button to move the map to the selected place
	 */
	private final JButton btnMove = new JButton("Move");
	
	/**
	 * A drop list of places to move the map to
	 */
	private final JComboBox<TPlacesItem> cbxPlaces = new JComboBox<TPlacesItem>();
	/**
	 * A label to display a system status notification.
	 */
	private final JLabel lblStatus = new JLabel("No status yet");
	/**
	 * Panel holding the movement drop list and button
	 */
	private final JPanel pnlMove = new JPanel();
	/**
	 * Panel to hold the geocoding components
	 */
	private final JPanel pnlGeocode = new JPanel();
	/**
	 * Textfield to input a geocoding location
	 */
	private final JTextField tfGeocodeLocation = new JTextField();
	/**
	 * button to make markers corresponding to the entered geocoding location
	 */
	private final JButton btnMakeGeocodeMarkers = new JButton("Make markers");
	/**
	 * panel for polygon & polyline controls
	 */
	private final JPanel pnlPolygon = new JPanel();
	/**
	 * Button to make a polygon
	 */
	private final JButton btnMakePolygon = new JButton("Make Polygon");
	/**
	 * Button to make a polyline
	 */
	private final JButton btnMakePolyline = new JButton("Make Polyline");
	/**
	 * Button to clear the list of locations used to create polygons and polylines
	 */
	private final JButton btnClearPts = new JButton("Clear Pts");
	/**
	 * Panel to hold the search nearby components
	 */
	private final JPanel pnlSearchNearby = new JPanel();
	/**
	 * Label for the search nearby type of search
	 */
	private final JLabel lblSearchNearbyType = new JLabel("Type:");
	/**
	 * Text field for the desired nearby search type
	 */
	private final JTextField tfSearchNearbyType = new JTextField();
	/**
	 * Label for the search nearby radius
	 */
	private final JLabel lblSearchNearbyRadius = new JLabel("Radius (m):");
	/**
	 * Text field to input the search radius in meters
	 */
	private final JTextField tfSearchNearbyRadius = new JTextField();
	/**
	 * Button to initiate the search
	 */
	private final JButton btnSearchNearby = new JButton("Search");



	/**
	 * Create the frame with the given Google Maps API key, title and adapter to the model.
	 * The Google Maps API key MUST be the LOCAL system's key, not a key from a remote entity!
	 * @param googleApiKey The local system's Google Maps API key 
	 * @param title The title of the frame
	 * @param view2ModelAdpt The adapter to the model
	 */
	public JxMapsDemoFrame(String googleApiKey, String title, IView2ModelAdapter<TPlacesItem> view2ModelAdpt) {
		super(title);
		tfSearchNearbyRadius.setColumns(10);
		tfSearchNearbyType.setColumns(10);
		this.view2ModelAdpt = view2ModelAdpt;
		pnlMap = JxMapsPanel.DEFAULT_FACTORY.apply(googleApiKey);
		initGUI();
	}
	
	/**
	 * Initialize the GUI
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(pnlMap, BorderLayout.CENTER);
		
		contentPane.add(pnlControl, BorderLayout.EAST);
		pnlControl.setLayout(new BoxLayout(pnlControl, BoxLayout.PAGE_AXIS));
		lblJxMaps.setToolTipText("Right-clicking will place an info window at that location.");
		lblJxMaps.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		pnlControl.add(lblJxMaps);
		btnTest.setToolTipText("Makes a marker at the center of the map with a info window that toggles open/close.");
		btnTest.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				view2ModelAdpt.addMarkerAt();
			}
		});
		
		pnlControl.add(btnTest);
		pnlMove.setToolTipText("Move the map to the selected location");
		pnlMove.setBorder(new TitledBorder(null, "Move the map", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlMove.setMaximumSize(new Dimension(32767, 30)); // keeps the drop list from being too tall
		
		pnlControl.add(pnlMove);
		cbxPlaces.setToolTipText("Locations to move the map to");
		pnlMove.add(cbxPlaces);
		cbxPlaces.setMaximumSize(new Dimension(32767, 25));
		btnMove.setToolTipText("Move the map to the selected location");
		pnlMove.add(btnMove);
		
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				view2ModelAdpt.addMoveTo(cbxPlaces.getItemAt(cbxPlaces.getSelectedIndex()));
			}
		});
		pnlGeocode.setToolTipText("Create possibly multiple markers at the requested location which have pop-up info windows when clicked.");
		pnlGeocode.setBorder(new TitledBorder(null, "Geocode a location", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlGeocode.setMaximumSize(new Dimension(32767, 30));
		
		pnlControl.add(pnlGeocode);
		
		tfGeocodeLocation.setToolTipText("A location to find using geocoding");
		tfGeocodeLocation.setText("Rice University");
		tfGeocodeLocation.setColumns(10);
		
		pnlGeocode.add(tfGeocodeLocation);
		btnMakeGeocodeMarkers.setToolTipText("Make the markers at the geocoded locations.");
		btnMakeGeocodeMarkers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				view2ModelAdpt.geocodeLocation(tfGeocodeLocation.getText());
			}
		});
		
		pnlGeocode.add(btnMakeGeocodeMarkers);
		pnlPolygon.setToolTipText("Right-click the map to add info windows at the desired vertices");
		pnlPolygon.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Add info windows to build list of locations", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlPolygon.setMaximumSize(new Dimension(32767, 30));
		pnlControl.add(pnlPolygon);
		btnMakePolygon.setToolTipText("Make a polygon using the recently created vertices");
		btnMakePolygon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makePolygon();
			}
		});
		
		pnlPolygon.add(btnMakePolygon);
		btnClearPts.setToolTipText("Clear the list of recently created vertices and start over. ");
		btnClearPts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.clearPolygonPoints();
			}
		});
		btnMakePolyline.setToolTipText("Make a polyline using the recently created vertices");
		btnMakePolyline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.makePolyline();
			}
		});
		
		pnlPolygon.add(btnMakePolyline);
		
		pnlPolygon.add(btnClearPts);
		pnlSearchNearby.setBorder(new TitledBorder(null, "Search nearby", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		pnlSearchNearby.setMaximumSize(new Dimension(32767, 30));
		
		pnlControl.add(pnlSearchNearby);
		
		pnlSearchNearby.add(lblSearchNearbyType);
		
		pnlSearchNearby.add(tfSearchNearbyType);
		
		pnlSearchNearby.add(lblSearchNearbyRadius);
		
		pnlSearchNearby.add(tfSearchNearbyRadius);
		btnSearchNearby.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view2ModelAdpt.searchNearby(tfSearchNearbyType.getText(), tfSearchNearbyRadius.getText());
			}
		});
		
		pnlSearchNearby.add(btnSearchNearby);
		lblStatus.setToolTipText("Info on the map");
		lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblStatus.setBackground(Color.CYAN);
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		pnlControl.add(lblStatus);
		pnlControl.add(Box.createVerticalGlue()); // keeps the components in the box layout all at the top.
	}
	
	/**
	 * Start the view
	 */
	public void start() {
		pnlMap.start();
		setVisible(true);
		refresh();
	}
	
	/**
	 * Add the given place to the drop lists
	 * @param place An object representing a place.   Must have an intelligible toString() method. 
	 */
	public void addPlace(TPlacesItem place) {
		cbxPlaces.addItem(place);
		refresh();
	}

	/**
	 * Get the map components factory associated with the map being displayed
	 * @return A new IJxMapsComponentsFactory instance
	 */
	public IJxMapsComponentsFactory getMapComponentsFactory() {
		return pnlMap.getComponentsFactory();
	}
	

	/**
	 * Display the given string on the status display string
	 * @param str  The string to display
	 */
	public void displayStatus(String str) {
		lblStatus.setText(str);
		refresh();
	}
	
	/**
	 * Refresh the view by refreshing the view and repainting it.
	 */
	public void refresh() {
		revalidate(); // make sure the layouts are ok.
		repaint();
	}

}
