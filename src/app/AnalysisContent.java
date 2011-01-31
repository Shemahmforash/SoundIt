/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package app;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that keeps the main data of the analysis
 * @author wanderer
 */
public class AnalysisContent implements Serializable {

    private File file;

    private ArrayList<Float> samples = new ArrayList<Float>();

    private ArrayList<Float> spectralFlux = new ArrayList<Float>();

    private ArrayList<Float> threshold = new ArrayList<Float>();

    private ArrayList<Float> peaks = new ArrayList<Float>();

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<Float> getPeaks() {
        return peaks;
    }

    public void setPeaks(ArrayList<Float> peaks) {
        this.peaks = peaks;
    }

    public ArrayList<Float> getSamples() {
        return samples;
    }

    public void setSamples(ArrayList<Float> samples) {
        this.samples = samples;
    }

    public ArrayList<Float> getSpectralFlux() {
        return spectralFlux;
    }

    public void setSpectralFlux(ArrayList<Float> spectralFlux) {
        this.spectralFlux = spectralFlux;
    }

    public ArrayList<Float> getThreshold() {
        return threshold;
    }

    public void setThreshold(ArrayList<Float> threshold) {
        this.threshold = threshold;
    }

    

}
