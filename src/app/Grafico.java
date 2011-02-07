/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.icdif.audio.io.AudioDecoder;
import com.icdif.audio.io.AudioDevice;

/**
 *
 * @author wanderer
 */
public class Grafico extends JFrame implements WindowListener, Runnable {

    /** the scroll pane **/
    private JScrollPane scrollPane;
    /** the image gui component **/
    private JPanel panel;
    /** the image **/
    private BufferedImage buffImage;
    /**
     * the last scaling factor to normalize samples. It's useful to use the same
     * scaling for consecutive plots.
     */
    private float scalingFactor = 1;
    /**
     * The minimum of the values to plot
     */
    private float min = 0;
    /**
     * The maximum of the values to plot
     */
    private float max = 0;
    /**
     * Whether the plot was cleared, if true we have to recalculate the scaling
     * factor
     **/
    private boolean cleared = true;
    /** current marker position and color **/
    private int markerPosition = 0;
    private Color markerColor = Color.white;
    /**
     * In order to play the audio during the plot
     */
    private AudioDevice device;
    /**
     * If it is meant to play
     */
    private boolean play;
    private String title;
    private int width;
    private int height;
    private int samplesPerPixel;
    private ArrayList<ContentsToPlot> samplesToPlot = new ArrayList<ContentsToPlot>();

    private AudioDecoder decoder;

    /**
     *
     * @param title
     * @param width
     * @param height
     */
    public Grafico(final String title, final int width, final int height, final int samplesPerPixel, 
            final ArrayList<ContentsToPlot> samplesToPlot, AudioDecoder decoder, boolean play) {
        System.out.println("Grafico consctructor");
        this.title = title;
        this.width = width;
        this.height = height;
        this.samplesToPlot = samplesToPlot;
        this.samplesPerPixel = samplesPerPixel;
        this.decoder = decoder;
        this.play = play;

        addWindowListener(this);
    }

    public void run() {


        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(width
                + this.getInsets().left + this.getInsets().right,
                this.getInsets().top + this.getInsets().bottom
                + height));

        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graph = (Graphics2D) img.getGraphics();
        graph.setColor(Color.black);
        graph.fillRect(0, 0, width, height);
        graph.dispose();
        buffImage = img;
        panel = new JPanel() {

            /**
             * Generated serial
             */
            private static final long serialVersionUID = -491925060803523216L;

            @Override
            public void paintComponent(Graphics graph) {
                super.paintComponent(graph);
                // the image will be update from another method, so
                // it needs to be in sync
                synchronized (buffImage) {
                    graph.drawImage(buffImage, 0, 0, null);
                    graph.setColor(markerColor);
                    graph.drawLine(markerPosition, 0,
                            markerPosition, buffImage.getHeight());
                }

                // pauses the thread
                Thread.yield();

                this.repaint();
            }

            @Override
            public void update(Graphics g) {
                paint(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(buffImage.getWidth(),
                        buffImage.getHeight());
            }
        };
        // panel.setPreferredSize( new Dimension( width, height ) );
        scrollPane = new JScrollPane(panel);
        this.getContentPane().add(scrollPane);
        this.pack();
        this.setVisible(true);

        for (ContentsToPlot samples : samplesToPlot) {
            this.plot(samples.getSamples(), samplesPerPixel, Color.red);
        }

        if(play) {
            try {
                this.PlayInPlot(samplesPerPixel, decoder);
            } catch (Exception ex) {
                Logger.getLogger(Grafico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }

    /**
     * This method plots the arraylist of samples, with the Window defined as
     * parameter
     *
     * @param samples
     *            the samples to plot
     * @param samplesPerPixel
     *            The number of samples in each pixel
     * @param color
     *            The color of the line
     */
    private void plot(final ArrayList<Float> samples, final float samplesPerPixel,
            final Color color) {
        synchronized (buffImage) {

            /*
             * if the size of the image is smaller than the ammount of values
             * (converted to pixels), then we draw a new rectangule to serve as
             * background
             */
            if (buffImage.getWidth() < samples.size() / samplesPerPixel) {
                buffImage = new BufferedImage(
                        (int) (samples.size() / samplesPerPixel), this.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D graph = buffImage.createGraphics();
                graph.setColor(Color.black);
                graph.fillRect(0, 0, buffImage.getWidth(), buffImage.getHeight());
                graph.dispose();
                panel.setSize(buffImage.getWidth(), buffImage.getHeight());
            }

            /*
             * when the plot is cleared, we calculate the maximum and minimum of
             * all the values passed, i.e., the scaling factor
             */
            if (cleared) {
                for (int i = 0; i < samples.size(); i++) {
                    this.min = Math.min(samples.get(i), min);
                    this.max = Math.max(samples.get(i), max);
                }
                scalingFactor = max - min;
                cleared = false;
            }

            Graphics2D graph = buffImage.createGraphics();

            // draws the reference lines (max, min, zero)
            drawReferenceLines(graph, true, true, true);

            graph.setColor(color);

            // float lastValue = (samples.get(0) / scalingFactor)
            // * image.getHeight() / 3 + image.getHeight() / 2;

            // Scales the first value of the array in order to be plotted
            float lastSampleScaled = calculateScaledValue(buffImage, samples.get(0), 0);

            for (int i = 1; i < samples.size(); i++) {
                // float value = (samples.get(i) / scalingFactor)
                // * image.getHeight() / 3 + image.getHeight() / 2;

                // Scales the ith value of the array in order to be plotted
                float sampleScaled = calculateScaledValue(buffImage, samples.get(i), 0);

                /* it draws a line between the last and this value */
                graph.drawLine((int) ((i - 1) / samplesPerPixel), buffImage.getHeight()
                        - (int) lastSampleScaled, (int) (i / samplesPerPixel),
                        buffImage.getHeight() - (int) sampleScaled);

                /*
                 * the sample of this iteration will be next iteration's last
                 * sample
                 */
                lastSampleScaled = sampleScaled;
            }
            graph.dispose();
        }
    }

    /**
     * Plays the audio associated with the plot and adds a marker dynamically to
     * the plot
     *
     * @param samplesPerPixel
     * @param decoder
     * @throws Exception
     */
    public void PlayInPlot(float samplesPerPixel, AudioDecoder decoder)
            throws Exception {

        play = true;

        device = new AudioDevice();

        float[] samples = new float[1024];

        long startTime = 0;

        while (decoder.readSamples(samples) > 0) {

            if (play == false) {
                System.out.println("break");
                break;
            }

            // starts playing
            device.playSamples(samples);

            // at the first iteration I define the startingTime of the song
            if (startTime == 0) {
                startTime = System.nanoTime();
            }

            // TODO: Pq a divisão por 1 x 10^-9 ???
            float elapsedTime = (System.nanoTime() - startTime) / 1000000000.0f;

            // this gives us the position, in pixels, of the marker at the
            // elapsed time
            int position = (int) (elapsedTime * (44100 / samplesPerPixel));

            // sets the Marker in the plot at the position calculated
            this.setMarker(position, Color.white);

            // sleeps so that the repaint can take effect
            Thread.sleep(20);
        }

    }

    /**
     * Sets a marker (vertical line)
     *
     * @param x
     *            the position to set the marker
     * @param color
     *            the colour of the marker
     */
    public void setMarker(int x, Color color) {
        this.markerPosition = x;
        this.markerColor = color;
    }

    /**
     * Scales and returns the sample value converted to pixels in order to be
     * plotted
     *
     * @param img
     *            Buffered image where to plot
     * @param value
     *            The value to be plotted
     * @param offset
     *            The vertical offset (it's useful to draw several graphics in
     *            the same image)
     * @return the value converted to pixels
     */
    private float calculateScaledValue(BufferedImage img, final float value,
            final float offset) {

        /*
         * float scaled = (value / scalingFactor) * img.getHeight() / 3 +
         * img.getHeight() / 2 - offset * img.getHeight() / 3;
         */

        float scaled = (value / scalingFactor) * img.getHeight() / 2
                + img.getHeight() / 2 - offset * img.getHeight() / 2;

        return scaled;

    }

    /**
     * Function that draws reference lines (center, max and minimum)
     *
     * @param graph
     *            the graphic where to draw
     * @param drawZero
     *            boolean that tells where to draw the zero line
     * @param drawMax
     *            boolean that tells where to draw the max line
     * @param drawMin
     *            boolean that tells where to draw the min line
     */
    private void drawReferenceLines(Graphics2D graph, boolean drawZero,
            boolean drawMax, boolean drawMin) {
        /**
         * Draws the center line and the max/min
         */
        graph.setColor(Color.white);

        if (drawMin) {
            // draws the min
            // TODO: o sítio da recta ainda n está certo. Está como h/3, mas n
            // eh bem isto
            float minScaled = calculateScaledValue(buffImage, min, 0);
            graph.drawLine(0, (int) minScaled + buffImage.getHeight() / 2,
                    (int) buffImage.getWidth(), (int) minScaled
                    + buffImage.getHeight() / 2);

            char[] chars = new char[2];
            chars[0] = '-';
            chars[1] = '1';

            graph.drawChars(chars, 0, 2, 5, (int) minScaled
                    + buffImage.getHeight() / 2 - 10);
        }

        if (drawMax) {
            // draws the max
            float maxScaled = calculateScaledValue(buffImage, max, 0);
            graph.drawLine(0, buffImage.getHeight() - (int) maxScaled,
                    (int) buffImage.getWidth(), buffImage.getHeight()
                    - (int) maxScaled);

            char[] chars = new char[2];
            chars[0] = '+';
            chars[1] = '1';

            graph.drawChars(chars, 0, 2, 5, buffImage.getHeight()
                    - (int) maxScaled + 20);
        }

        if (drawZero) {
            // draws the zero
            graph.drawLine(0, buffImage.getHeight() / 2, (int) buffImage.getWidth(), buffImage.getHeight() / 2);

            char[] chars = new char[1];
            chars[0] = '0';
            graph.drawChars(chars, 0, 1, 5, buffImage.getHeight() / 2 - 10);
        }
    }

    /**
     * It clears the plot, i.e., paints the original rectangle with the
     * background color and sets the attribute cleared to true
     */
    public void clear() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                buffImage.flush();

                Graphics2D graph = buffImage.createGraphics();
                graph.setColor(Color.black);
                graph.fillRect(0, 0, buffImage.getWidth(), buffImage.getHeight());
                graph.dispose();

                cleared = true;
            }
        });
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("Janela a fechar (play = " + play + ")");

        if (play) {
            device.stopPlaying();
            play = false;
        }
        // by closing the window I release the memory associated with its
        // buffered image
        clear();
        System.out.println("Limpo a buffimage");

        // it actually closes the window and, if it is playing, it stops the
        // device
        this.dispose();
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub
    }
}
