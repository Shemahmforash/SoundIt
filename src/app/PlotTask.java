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
import javax.swing.SwingWorker;

/**
 *
 * @author wanderer
 */
public class PlotTask extends SwingWorker<Void, Void> {

    private ArrayList<Float> samples = new ArrayList<Float>();
    private String title;
    private int width;
    private int height;
    private int samplesPerPixel;
    private int hopSize = 0;
    private Color color;
    private String fileName;
    private boolean play = false;

    /**
     *
     * @param samples
     * @param title
     * @param width
     * @param height
     * @param samplesPerPixel
     * @param color
     */
    public PlotTask(ArrayList<Float> samples, String title, int width, int height, int samplesPerPixel, Color color) {
        this.samples = samples;
        this.title = title;
        this.width = width;
        this.height = height;
        this.samplesPerPixel = samplesPerPixel;
        this.color = color;
    }

    /**
     *
     * @param samples
     * @param title
     * @param width
     * @param height
     * @param samplesPerPixel
     * @param color
     * @param fileName
     * @param play
     */
    public PlotTask(ArrayList<Float> samples, String title, int width, int height, int samplesPerPixel, Color color, String fileName, boolean play) {

        this(samples, title, width, height, samplesPerPixel, color);

        this.fileName = fileName;
        this.play = play;
    }

    /**
     * When plotting processed results (spectralflux, threshold, peaks), we have also to supply the hopSize
     * @param samples
     * @param title
     * @param width
     * @param height
     * @param samplesPerPixel
     * @param color
     * @param hopSize
     */
    public PlotTask(ArrayList<Float> samples, String title, int width, int height, int samplesPerPixel, Color color, int hopSize) {
        this(samples, title, width, height, samplesPerPixel, color);

        this.hopSize = hopSize;
    }

    /**
     * 
     * @param samples
     * @param title
     * @param width
     * @param height
     * @param samplesPerPixel
     * @param color
     * @param fileName
     * @param play
     * @param hopSize
     */
    public PlotTask(ArrayList<Float> samples, String title, int width, int height, int samplesPerPixel, Color color, String fileName, boolean play, int hopSize) {

        this(samples, title, width, height, samplesPerPixel, color, fileName, play);

        this.hopSize = hopSize;
    }

    @Override
    protected Void doInBackground() throws Exception {

        //Initialize progress property.
        setProgress(0);

        setProgress(50);

        Plot plot = new Plot(title, width, height);

        //System.out.println("Hop: " + this.hopSize);

        // o 2º numero dá a "resolução", isto é, o numero total de pontos a
        // aparecer em cada pixel
        if (hopSize == 0) {
            plot.plot(samples, samplesPerPixel, color);
        } //no caso dos valores processados e não dos valores pcm originais, eles já apresentam apenas um valor para cada janela
        else {
            plot.plot(samples, 1, color);
        }

        setProgress(100);

        if (play) {
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
            //PlaybackPlot playbackPlot = new PlaybackPlot(plot, (int) samplesPerPixel, decoder);
            //plot.PlayInPlot(samplesPerPixel, decoder);

            //TODO: O proximo 512, deve ser igual ao hopsize (q ainda n é passado para a threaad)
            try {
                if (hopSize == 0) {
                    //new PlaybackPlot(plot, (int) samplesPerPixel, decoder);
                    //uso o samples per pixel
                    plot.PlayInPlot(samplesPerPixel, decoder);
                } else {
                    plot.PlayInPlot(hopSize, decoder);
                }

            } catch (Exception ex) {
                Logger.getLogger(PlotThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



        return null;
    }

    @Override
    protected void done() {
        super.done();

        System.out.println("Done");
        
    }

    
}
