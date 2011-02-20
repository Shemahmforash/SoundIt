package com.icdif.app;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that keeps the main data of the analysis of the audio, i.e., the
 * spectral flux, threshold and peaks
 * 
 * @author wanderer
 */
public class AnalysisContent implements Serializable {

	/**
	 * Generated serial version
	 */
	private static final long serialVersionUID = 2726033402747625365L;

	/**
	 * The audio file from where the rest of the data was calculated
	 */
	private File file;

	/* private ArrayList<Float> samples = new ArrayList<Float>(); */

	/**
	 * The Spectral Flux (Spectral Difference)
	 */
	private ArrayList<Float> spectralFlux = new ArrayList<Float>();

	/**
	 * The Threshold
	 */
	private ArrayList<Float> threshold = new ArrayList<Float>();

	/**
	 * The Peaks (onsets)
	 */
	private ArrayList<Float> peaks = new ArrayList<Float>();

	/**
	 * Gets the File
	 * 
	 * @return the File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Sets the file
	 * 
	 * @param file
	 *            the file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Gets the Peaks
	 * 
	 * @return
	 */
	public ArrayList<Float> getPeaks() {
		return peaks;
	}

	/**
	 * Sets the Peaks
	 * 
	 * @param peaks
	 */
	public void setPeaks(ArrayList<Float> peaks) {
		this.peaks = peaks;
	}

	/**
	 * Gets the Spectral Flux
	 * 
	 * @return the Spectral Flux (spectral difference)
	 */
	public ArrayList<Float> getSpectralFlux() {
		return spectralFlux;
	}

	/**
	 * Sets the Spectral Flux (spectral difference)
	 * 
	 * @param spectralFlux
	 *            the Spectral Flux (spectral difference)
	 */
	public void setSpectralFlux(ArrayList<Float> spectralFlux) {
		this.spectralFlux = spectralFlux;
	}

	/**
	 * Gets the Threshold
	 * 
	 * @return the Threshold
	 */
	public ArrayList<Float> getThreshold() {
		return threshold;
	}

	/**
	 * Sets the Threshold
	 * 
	 * @param threshold
	 *            the Threshold
	 */
	public void setThreshold(ArrayList<Float> threshold) {
		this.threshold = threshold;
	}

}
