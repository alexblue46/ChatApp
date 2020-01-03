package provided.jxMaps.utils.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import provided.jxMaps.utils.IPlace;

/**
 * Some sample IPlaces
 * @author swong
 *
 */
public interface IPlace_Samples {
	
	/**
	 * An immutable mapping of names to sample places.
	 */
	public static final Map<String, IPlace> SAMPLE_PLACES_DICT = Collections.unmodifiableMap(new HashMap<String, IPlace>() {
		private static final long serialVersionUID = 2115150778720507647L;

		{
			put("RICE", new Place("Rice University", 29.71724, -95.40150, 15));
			put("WILLY", new Place("William Marsh Rice statue", 29.718545, -95.399054, 20));
			put("DUNCAN_HALL", new Place("Duncan Hall", 29.720353, -95.398744, 19));
			put("OEDK", new Place("Oshman Engineering Design Kitchen", 29.721088189527183, -95.40147180018715, 19));
			put("FONDREN", new Place("Fondren Library", 29.718128707827557, -95.40012306927687, 19));
			put("LOVETT_HALL", new Place("Lovett Hall", 29.719026792228018,-95.39784100535402, 19));
			put("GREENWICH", new Place("Greenwhich Prime Meridian", 51.477222, 0.0, 17));
			put("LOUVRE",  new Place("Louvre Art Gallery", 48.860930, 2.336461, 17));
			put("LONDON_EYE", new Place("London Eye Ferris Wheel", 51.503367, -0.119968, 18));
			put("ACROPOLIS", new Place("Acropolis", 37.971458, 23.726647, 17));
			put("COLOSSEUM", new Place("Colosseum", 41.890306, 12.492354, 18));
			put("TAJ_MAHAL", new Place("Tah Mahal", 27.174932, 78.042144, 17));
			put("PYRAMIDS", new Place("Pyramids at Giza", 29.976788, 31.134001, 15));
			put("STATUE_OF_LIBERTY", new Place("Statue of Liberty", 40.68925, -74.044493, 19));
			put("NYC", new Place("New York City", 40.748974, -73.990288, 11));
			put("LUXOR", new Place("Luxor Casino", 36.095568, -115.176033, 18));
			put("GRAND_CANYON", new Place("Grand Canyon", 36.108091, -113.214912, 8));
			put("GOLDEN_GATE", new Place("Golden Gate Bridge", 37.82035, -122.4778804, 14));
			put("EPCOT_CENTER", new Place("Disney Epcot Center", 28.374454, -81.549363, 15));;
		}
	});
	
	
	/**
	 * Get a mutable mapping of names to IPlaces, initialized to the entries in SAMPLE_PLACES_DICT.
	 * @return a mutable Map<String, Place> instance
	 */
	public static Map<String, IPlace> getSamplePlacesDict() {
		return new HashMap<String, IPlace>(SAMPLE_PLACES_DICT);
	}
}
