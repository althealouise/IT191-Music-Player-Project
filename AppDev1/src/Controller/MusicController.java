/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Music;
import Model.MusicModel;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class MusicController {
    
    private Thread playerThread;
    private AtomicBoolean isPlaying;
    private BasicPlayer player;
    private MusicModel model = new MusicModel();

    public MusicController() {
        this.model = model;
        this.player = new BasicPlayer();
        model.setIsFirstRun(true);
        model.setIsLoaded(false);
        this.isPlaying = new AtomicBoolean(false);
        model.setVolume(0.5);

    }

    public void playMusic() throws IOException, JavaLayerException {
        try {

            //Execute SQL query
            if (!model.isIsLoaded()) {
                model.selectSong();
            }

            // Exit if it is currently playing already or if there is no file
            if (this.isPlaying.get()) {
                return;
            }

            // seeks the file if seeking was performed
            if (model.isIsSeeked()) {
                model.getFile().skip(model.getTotalLength() - model.getPauseLength());
                model.setIsSeeked(false);
            }

            // Resume playback if it is not a new file
            if (model.isIsFirstRun() == false) {
                this.player.resume();
                this.isPlaying.set(true);
                return;
            }

            // Always reset Player
            player.open(model.getFile());
            player.play();
            player.setGain(model.getVolume());

            // Start playing song
            this.isPlaying.set(true);

            if (model.isIsFirstRun()) {
                model.setTotalLength(model.getFile().available());
                model.setIsFirstRun(false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        try {
            // Exit if not playing currently
            if (!this.isPlaying.get()) {
                return;
            }

            //  Set variable values
            model.setIsFirstRun(false);
            this.isPlaying.set(false);

            //Close player
            this.player.pause();
            System.out.println(player.getStatus());
            model.getFile().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        try {
            // Exit if not playing currently
            System.out.println(isPlaying.get());

            if (!this.isPlaying.get() && player.getStatus() != 1) {
                return;
            }

            if (player.getStatus() == 1) {
                // Set variable values
                model.setIsFirstRun(true);
                this.isPlaying.set(false);
                model.setIsLoaded(false);

                //Close player
                this.player.stop();
                model.getFile().close();
                return;
            }

            if (!this.isPlaying.get()) {
                return;
            }

            // Set variable values
            model.setIsFirstRun(true);
            this.isPlaying.set(false);
            model.setIsLoaded(false);

            //Close player
            this.player.stop();
            model.getFile().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setVolume(double volume) throws BasicPlayerException{
        model.setVolume(volume);
        if(!this.isPlaying.get()){
            return;
        }else{
            this.player.setGain(model.getVolume());
        }
    }

    public DefaultTableModel populateTableDisplay(DefaultTableModel table) {
        model.getSongList();
        ArrayList<Music> songData = getModel().getSong();
        table.setRowCount(0);
        for (Music music : songData) {
            Object[] rowData = {createIconFromInputStream(music.getArt()), music.getTitle(), music.getArtist(), music.getTimelength()};
            table.addRow(rowData);
        }

        return table;
    }

    public Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public Icon createIconFromInputStream(InputStream inputStream) {
        try {
            byte[] imageBytes = inputStreamToByteArray(inputStream);
            return new ImageIcon(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public ImageIcon backgroundScale(ImageIcon originalImage){
            
            ImageIcon scaledImage = new ImageIcon(originalImage.getImage()
                .getScaledInstance(1400, 1000, Image.SCALE_SMOOTH));
            return scaledImage;
            
    }      
    
    public MusicModel getModel() {
        return model;
    }

    public AtomicBoolean getIsPlaying() {
        return isPlaying;
    }

    public BasicPlayer getPlayer() {
        return player;
    }


}
