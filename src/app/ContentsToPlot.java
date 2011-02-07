/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ArrayList;

/**
 *
 * @author wanderer
 */
public class ContentsToPlot {

    private ArrayList<Float> samples = new ArrayList<Float>();

    public ContentsToPlot(ArrayList<Float> samples) {
        this.samples = samples;
    }

    public ArrayList<Float> getSamples() {
        return samples;
    }
}
