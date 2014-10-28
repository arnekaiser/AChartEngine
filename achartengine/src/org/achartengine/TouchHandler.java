/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Modified Version without Pinch-Zoom by Chris Verspohl
 */
package org.achartengine;

import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.RoundChart;
import org.achartengine.chart.XYChart;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.tools.Pan;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.Zoom;
import org.achartengine.tools.ZoomListener;

import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * The main handler of the touch events.
 */
public class TouchHandler implements ITouchHandler {
  /** The chart renderer. */
  private DefaultRenderer mRenderer;
  /** The old x coordinate. */
  private float oldX;
  /** The old y coordinate. */
  private float oldY;
  /** The old x2 coordinate. */
  /** The zoom buttons rectangle. */
  private RectF zoomR = new RectF();
  /** The pan tool. */
  private Pan mPan;
  /** The graphical view. */
  private GraphicalView graphicalView;

  /**
   * Creates a new graphical view.
   * 
   * @param view the graphical view
   * @param chart the chart to be drawn
   */
  public TouchHandler(GraphicalView view, AbstractChart chart) {
    graphicalView = view;
    zoomR = graphicalView.getZoomRectangle();
    if (chart instanceof XYChart) {
      mRenderer = ((XYChart) chart).getRenderer();
    } else {
      mRenderer = ((RoundChart) chart).getRenderer();
    }
    if (mRenderer.isPanEnabled()) {
      mPan = new Pan(chart);
    }
  }

  /**
   * Handles the touch event.
   * 
   * @param event the touch event
   */
  public boolean handleTouch(MotionEvent event) {
    int action = event.getAction();
    if (mRenderer != null && action == MotionEvent.ACTION_MOVE) {
      if (oldX >= 0 || oldY >= 0) {
        float newX = event.getX(0);
        float newY = event.getY(0);
        if (mRenderer.isPanEnabled()) {
          mPan.apply(oldX, oldY, newX, newY);
        }
        oldX = newX;
        oldY = newY;
        graphicalView.repaint();
        return true;
      }
    } else if (action == MotionEvent.ACTION_DOWN) {
      oldX = event.getX(0);
      oldY = event.getY(0);
      if (mRenderer != null && mRenderer.isZoomEnabled() && zoomR.contains(oldX, oldY)) {
        if (oldX < zoomR.left + zoomR.width() / 3) {
          graphicalView.zoomIn();
        } else if (oldX < zoomR.left + zoomR.width() * 2 / 3) {
          graphicalView.zoomOut();
        } else {
          graphicalView.zoomReset();
        }
        return true;
      }
    } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
      oldX = 0;
      oldY = 0;
      if (action == MotionEvent.ACTION_POINTER_UP) {
        oldX = -1;
        oldY = -1;
      }
    }
    return !mRenderer.isClickEnabled();
  }

  /**
   * Adds a new pan listener.
   * 
   * @param listener pan listener
   */
  public void addPanListener(PanListener listener) {
    if (mPan != null) {
      mPan.addPanListener(listener);
    }
  }

  /**
   * Removes a pan listener.
   * 
   * @param listener pan listener
   */
  public void removePanListener(PanListener listener) {
    if (mPan != null) {
      mPan.removePanListener(listener);
    }
  }

@Override
public void addZoomListener(ZoomListener listener) {
	// Do nothing here
	
}

@Override
public void removeZoomListener(ZoomListener listener) {
	// Do nothing here
	
}
}