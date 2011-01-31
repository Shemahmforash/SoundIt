/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import com.icdif.audio.graph.PlaybackPlot;
import com.icdif.audio.graph.Plot;
import com.icdif.audio.io.AudioDecoder;
import com.icdif.audio.io.MP3Decoder;
import com.icdif.audio.io.WaveDecoder;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wanderer
 */
public class PlotThread extends Thread {

    private ArrayList<Float> samples = new ArrayList<Float>();
    private String title;
    private int width;
    private int height;
    private float samplesPerPixel;
    private Color color;
    private String fileName;
    private boolean play;

    public PlotThread(ArrayList<Float> samples, String title, int width, int height, float samplesPerPixel, Color color) {
        super();
        this.samples = samples;
        this.title = title;
        this.width = width;
        this.height = height;
        this.samplesPerPixel = samplesPerPixel;
        this.color = color;
    }

    public PlotThread(ArrayList<Float> samples, String title, int width, int height, float samplesPerPixel, Color color, boolean play, String fileName) {
        super();
        this.samples = samples;
        this.title = title;
        this.width = width;
        this.height = height;
        this.samplesPerPixel = samplesPerPixel;
        this.color = color;

        this.play = play;
        this.fileName = fileName;
    }

    @Override
    public synchronized void run() {
        Plot plot = new Plot(title, width, height);
        // o 2º numero dá a "resolução", isto é, o numero total de pontos a
        // aparecer em cada pixel
        plot.plot(samples, samplesPerPixel, color);


        AudioDecoder decoder = null;

        String extension = FileUtils.getExtension(fileName);

        System.out.println("Extension = " + extension);


        if (extension.equals(".mp3")) {
            try {
                decoder = new MP3Decoder(new FileInputStream(fileName));

            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (extension.equals(".wav") || extension.equals(".wave")) {
            try {
                decoder = new WaveDecoder(new FileInputStream(fileName));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //TODO: O proximo 512, deve ser igual ao hopsize (q ainda n é passado para a threaad)
        try {
            new PlaybackPlot(plot, 512, decoder);
        } catch (Exception ex) {
            Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Finnished plot");
    }

}
