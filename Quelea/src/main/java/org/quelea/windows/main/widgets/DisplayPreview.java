/*
 * This file is part of Quelea, free projection software for churches.
 *
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.quelea.windows.main.widgets;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.quelea.services.utils.QueleaProperties;
import org.quelea.windows.main.DisplayCanvas;
import org.quelea.windows.main.QueleaApp;

/**
 * A display preview - shows a display canvas but preserves the aspect ratio of
 * the current main display. If the aspect ratio cannot be calculated, default
 * to the ratio given be DEFAULT_RATIO.
 * <p>
 *
 * @author Michael
 */
public class DisplayPreview extends StackPane {

    private static final double DEFAULT_RATIO = 4.0 / 3;
    private final DisplayCanvas canvas;

    /**
     * Create a new display preview.
     *
     * @param canvas the display canvas to use in this preview.
     */
    public DisplayPreview(DisplayCanvas canvas) {
        this.canvas = canvas;
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(6);
        dropShadow.setOffsetY(6);
        dropShadow.setColor(Color.GRAY);
        canvas.setEffect(dropShadow);
        if (!QueleaProperties.get().getUseDarkTheme()) {
            setStyle("-fx-background-color:#dddddd;");
        }
        getChildren().add(canvas);
        updateSize();
        ChangeListener<Number> cl = (ov, t, t1) -> updateSize();
        widthProperty().addListener(cl);
        heightProperty().addListener(cl);
        QueleaApp.get().getProjectionWindow().widthProperty().addListener(cl);
        QueleaApp.get().getProjectionWindow().heightProperty().addListener(cl);

    }

    /**
     * Calculate and update the new size of the display canvas. This maintains the same
     * aspect ratio as the projection window.
     */
    private void updateSize() {
        // For working out the current ratio, subtract 20 from width and height because
        // this is the minimum margin amount (10 on each side), otherwise the ratio being
        // calculate is of the whole split pane section not the actual preview/live display
        double width = getWidth() - 20;
        double height = getHeight() - 20;
        double currentRatio = width / height;
        double ratio = getRatio();
        if (currentRatio < ratio) { //height too big
            double hDiff = (height - (width / ratio)) / 2;
            hDiff += 10;  // Add back the minimum margin on each side
            setMargin(canvas, new Insets(hDiff, 10, hDiff, 10));
        } else { //width too big
            double vDiff = (width - (ratio * height)) / 2;
            vDiff += 10;  // Add back the minimum margin on each side
            setMargin(canvas, new Insets(10, vDiff, 10, vDiff));
        }
    }

    private double getRatio() {
        if (QueleaApp.get().getProjectionWindow() == null) {
            return DEFAULT_RATIO;
        }
        double width = QueleaApp.get().getProjectionWindow().getWidth();
        double height = QueleaApp.get().getProjectionWindow().getHeight();
        if (height == 0) {
            return DEFAULT_RATIO;
        }
        return width / height;
    }

    public DisplayCanvas getCanvas() {
        return canvas;
    }

}
