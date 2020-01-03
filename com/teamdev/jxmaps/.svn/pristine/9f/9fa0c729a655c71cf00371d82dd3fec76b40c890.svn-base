/*
 * Copyright (c) 2000-2017 TeamDev Ltd. All rights reserved.
 * Use is subject to Apache 2.0 license terms.
 */
package com.teamdev.jxmaps.demo;

import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;

class SampleDescriptor {
    private final SampleIcon icon;
    private final String title;
    private final String description;
    private Class sampleClass;
	private String googleAPIKey = null;

    public SampleDescriptor(String iconPath, String title, String description, Class sampleClass) {
        this.icon = new SampleIcon(SampleDescriptor.class.getResource("res/" + iconPath + (SampleIcon.isRetina() ? "_retina.png" : ".png")));
        this.title = title;
        this.description = description;
        this.sampleClass = sampleClass;
    }
    
    /**
     * Constructor that explicitly takes the Google API Key.
     * @param iconPath
     * @param title
     * @param description
     * @param sampleClass
     * @param googleAPIKey
     */
    public SampleDescriptor(String iconPath, String title, String description, Class sampleClass, String googleAPIKey) {
    	this(iconPath, title, description, sampleClass);
    	this.googleAPIKey = googleAPIKey;
    }

    public SampleIcon getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public JComponent createInstance(Dimension size) {
        try {
        	JComponent view;
        	if(null == googleAPIKey) {
        		view = (JComponent) sampleClass.newInstance();
        	}
        	else {
        		MapViewOptions options = new MapViewOptions();
        		options.importPlaces();
        		options.setApiKey(googleAPIKey);

        		Constructor constr = sampleClass.getConstructor(MapViewOptions.class);
        		if(null==constr) {
        			System.err.println("[SampleDescriptor.createInstance("+size+")] No constructor that takes a MapViewOptions parameter found.  Using no parameter constructor.");
        			view = (JComponent) sampleClass.newInstance();
        		}
        		else {
        			view = (JComponent) constr.newInstance(options);
        		}
        	}
            if (view instanceof MapView) {
                ((MapView) view).setMapSize(size);
            }
            return view;
        } catch (Exception e) {
        	System.err.println("[SampleDescriptor.createInstance("+size+")] Exception = "+e);
        	e.printStackTrace();
            return null;
        }
    }
}
