package provided.jxMaps.utils.enhanced;

import java.util.function.Function;

import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.InfoWindowOptions;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapEvent;

import provided.jxMaps.utils.IJxMaps_Defs;
import provided.jxMaps.utils.IOptionedMapObject;
import provided.jxMaps.utils.IVisibleMapObject;

/**
 * An InfoWindow with enhanced capabilities, especially in terms of fixing 
 * the issue with InfoWindow where it needs to be both instantiated with a map 
 * and issued its open() methods with a map.   This class offers open() methods 
 * that do not require the map object, instead relying on the map the info window
 * was created with.<br>
 * This class has decoupled notions of open/close and visible/invisible.   A visible 
 * EnhancedInfoWindow can be opened and closed as normal where the window is displayed
 * when opened and not displayed when closed.   An invisible (isVisible=false) 
 * EnhancedInfoWindow can also be opened and closed but will never be displayed.  
 * This enables the info window's open/close state to be set independent of whether 
 * it can be seen on the screen which is useful when configuring the info window before it 
 * is used.   Likewise, an open EnhancedInfoWindow in either anchored or unanchored modes,
 * can be made to display or not using the setVisible() method. An invisible but open state window
 * will automatically display with the proper mode (anchored or not) when made visible.
 * This is particularly useful when the info window is added to an IMapLayer and the layer's 
 * visibility is used to control all of its contents.   Note that for anchored info 
 * windows, BOTH the info window and its anchor must be added to the layer to control the
 * visibility of both the anchor and its associated info window.   
 * @author swong
 *
 */
public class EnhancedInfoWindow extends InfoWindow implements IVisibleMapObject, IOptionedMapObject<InfoWindowOptions> {

	/**
	 * State Design Pattern: internal representation of the abstract open/closed state of the info window.  
	 * The state represented here may not correspond to whether or not the window is actually displayed.
	 * The displaying of the window is in conjunction with the visibility of the window.  That is.
	 * the window could be in an opened state but invisible.  Invisible windows can be opened and closed as
	 * if they were visible, they just won't show anything on the screen.   This is useful for manipulating 
	 * layers that are not yet visible.
	 * @author swong
	 *
	 */
	private abstract class AState {


		/**
		 * Try to set the window to an unanchored open state
		 */
		protected abstract void open();

		/**
		 * Try to set the window to an anchored open state.
		 * @param anchor The anchor to open the window with.
		 */
		protected abstract void open(Object anchor);

		/**
		 * Do any state-dependent behaviors associated with changing the current visibility 
		 * to the given visibility. EnhancedInfoWindow.this.isVisible has NOT yet been set so 
		 * that it can be used in comparison to the given isVisible.   This method does not and 
		 * should not set EnhancedInfoWindow.this.isVisible!
		 * @param isVisible The new visibility value: true if visible, false otherwise
		 */
		protected abstract void setVisible(boolean isVisible);


		/**
		 * The current open/close state of the window
		 * @return true if the window is open (anchored or unanchored), false otherwise.
		 */
		protected abstract boolean isOpen();

	

	}

	/**
	 * Factory for the concrete state representing a window that is in an anchored, open state.
	 * This has to be factory rather than a state object because it needs to capture the anchor 
	 * value in its closure.
	 */
	private Function<Object, AState> openAnchoredStateFac = (anchor) -> {
		return new AState() {
//			/**
//			 * Storage for the anchor value used when the info window is opened using open(anchor).
//			 * Used when making an invisible but opened window visible.
//			 */
//			private Object anchor;

			@Override
			protected void open() {
				System.err.println("[EnhancedInfoWindow.openAnchoredState.open()]  Ignored attempt to open the info window in an unanchored mode when it is already open in an anchored mode!");

			}

			@Override
			protected void open(Object anchor) {
				System.err.println("[EnhancedInfoWindow.openAnchoredState.open()]  Reopened the info window in an anchored mode even though it is already open in an anchored mode!");
				doOpen(anchor);
			}

			@Override
			protected void setVisible(boolean isVisible) {
				if(isVisible) {				
					doOpen(anchor); // Force the window to be displayed with the stored anchor value.
				}
				else {
					doClose(); // Stop displaying the window but don't change the state.
				}
			}

			@Override
			protected boolean isOpen() {
				return true;  // always true
			}

		};
	};


	/**
	 * Concrete state that represents the unanchored, open state of the window.
	 */
	private AState openUnanchoredState = new AState() {

		@Override
		protected void open() {
			System.err.println("[EnhancedInfoWindow.openUnanchoredState.open()]  Reopened the info window in an unanchored mode even though it is already open in an unanchored mode!");
			doOpen();
		}

		@Override
		protected void open(Object anchor) {
			System.err.println("[EnhancedInfoWindow.openUnanchoredState.open("+anchor+")]  Ignored attempt to open the info window in an anchored mode when it is already open in an unanchored mode!");		
		}

		@Override
		protected void setVisible(boolean isVisible) {
			if(isVisible) {
				new Thread(()->{
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						System.err.println("[EnhancedInfoWindow.openUnanchoredState.setVisible()] Exception while during state change delay: "+e);
						e.printStackTrace();
					}
					doOpen();  // Make sure that the window is displayed.  This could be redundant if the window was already visible.
				}).start();
			}
			else {
				doClose(); // Stop displaying the window but don't change the state
			}
		}

		@Override
		protected boolean isOpen() {
			return true;  // Always true
		}

	};	


	/**
	 * Concrete state that represents the closed state of the window
	 */
	private AState closedState = new AState() {
		@Override
		protected void open() {
			if(isVisible) {
				doOpen(); // Display the window if it is visible.
			}
			currentState = openUnanchoredState;  // Set the state to an open unanchored state
		}

		@Override
		protected void open(Object anchor) {
			if(isVisible) {
				doOpen(anchor);  // Display the window if it is visible
			}
			currentState = openAnchoredStateFac.apply(anchor);	// Set the state to a new open anchored state with the given anchor value.		
		}

		@Override
		protected void setVisible(boolean isVisible) {
			// Nothing to do here since the window isn't displayed anyway.
		}

		@Override
		protected boolean isOpen() {
			return false;  // always false
		}
	};

	/**
	 * The current state of the window.  Initializes to a closed state.
	 */
	private AState currentState = closedState;


	/**
	 * The current visibility state.  Defaults to true for normal info window behavior
	 */
	private boolean isVisible = true;


	/**
	 * The options that were stored with the setOptions() method, which may not reflect 
	 * subsequent mutations.
	 */
	private InfoWindowOptions options;

	/**
	 * Construct the class in the context of the given map
	 * @param map The context for this info window.
	 */
	public EnhancedInfoWindow(Map map) {
		super(map);
		addEventListener(IJxMaps_Defs.IEvent.CLOSE_CLICK, new MapEvent() {

			@Override
			public void onEvent() {
				System.out.println("[EnhancedInfoWindow] CLOSE_CLICK event detected. State set to closed.");
				currentState = closedState;
			}

		});			
	}

	/**
	 * Open the info window using the associated map, independent of the state of the info window. 
	 */
	private void doOpen() {
		open(getMap());
	}

	/**
	 * Equivalent to the inherited open(Map map) method but uses the internally
	 * stored map instead. Note that the info window may not display if its visibility is currently false.  
	 */
	public void open() {
		currentState.open();
	}


	/**
	 * Open the info window attached to the given anchor, independent of the state of the info window.
	 * @param anchor  The anchor to attach this info window to when it opens.
	 */
	private void doOpen(java.lang.Object anchor) {
		open(getMap(), anchor);
	}


	/**
	 * Make the info window stop being displayed, independent of the state of the window.
	 */
	private void doClose() {
		EnhancedInfoWindow.super.close(); 
	}

	/**
	 * Equivalent to the inherited open(Map map, Object anchor) method but uses 
	 * the internally stored map instead. Note that the info window may not display 
	 * if its visibility is currently false.  
	 * @param anchor  The anchor object to which this info window is attached.
	 */	
	public void open(java.lang.Object anchor) {
		currentState.open(anchor);
	}

	@Override
	public void close() {
		super.close(); // make sure that the window is not displayed.  Theoretically redundant if the window is invisible already.
		currentState = closedState; // set the state to closed.
	}


	/**
	 * Sets whether the info window will be displayed, independent of whether it was opened or closed. 
	 * The window will only be displayed if it is both visible and open.   
	 * That is, if isVisible=false, then the window can be opened but not displayed.   If a window 
	 * is set to visible (isVisible=true) when the window is an opened state, then the 
	 * info window will be immediately displayed.   The visibility is useful when one wants to turn the 
	 * displaying of the info window on and off without having to worry how it was opened, such as when using 
	 * a map layer to control the visibility of multiple components.
	 */
	@Override
	public void setVisible(boolean isVisible) {
		currentState.setVisible(isVisible);
		this.isVisible = isVisible;
	}

	@Override
	public boolean getVisible() {
		return isVisible;  
	}

	/**
	 * The current open/closed state of the window.  
	 * The open/close state of the info window is independent of its visibility. 
	 * The window will only be displayed if it is both visible and open. 
	 * @return true if the window is open, false otherwise
	 */
	public boolean isOpen() {
		return currentState.isOpen(); // delegate to the state
	}

	@Override
	public InfoWindowOptions getOptions() {
		return this.options;
	}

	@Override
	public void setOptions(InfoWindowOptions options) {
		super.setOptions(options);
		this.options = options;
	}

}
