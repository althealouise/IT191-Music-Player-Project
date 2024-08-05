/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import Controller.MusicController;
import View.MusicView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import Controller.MusicController.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jlgui.basicplayer.BasicPlayerException;

/**
 *
 * @author katri
 */
public class customListener implements ActionListener, ChangeListener {
    private JSlider volume, seekbar;
    private JLabel stat, currentTime;
    private JButton pauseplayButton;
    private ImageIcon pauseIcon;
    private ImageIcon playIcon;
    private boolean isPaused = false;
    private MusicController musicController;

    public customListener(JButton pauseplayButton, MusicController musicController) {
        this.musicController = musicController;
        this.pauseplayButton = pauseplayButton;
        pauseIcon = new ImageIcon("src/Assets/pauseButton.png");
        playIcon = new ImageIcon("src/Assets/playButton.png");
        pauseplayButton.setIcon(pauseIcon);
    }
    
    public customListener(JSlider volume, MusicController musicController, JLabel stat){
        this.stat = stat;
        this.musicController = musicController;
        this.volume = volume;
        volume.setValue(50);
    } 
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pauseplayButton) {
            if(!musicController.getModel().isIsLoaded()){
                return;
            }
            if (!musicController.getIsPlaying().get()) {
                pauseplayButton.setIcon(pauseIcon);
                var query = "SELECT * FROM musicbackend.database WHERE id = '" + musicController.getModel().getId() + "';";
                musicController.getModel().setQuery(query);
                try {
                    musicController.playMusic();
                } catch (IOException ex) {
                    Logger.getLogger(customListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(customListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                isPaused = !isPaused;
                
            } 
            
            else {
                pauseplayButton.setIcon(playIcon); 
                var query = "SELECT * FROM musicbackend.database WHERE id = '" + musicController.getModel().getId() + "';";
                musicController.getModel().setQuery(query);
                musicController.pauseMusic();
               
                isPaused = true;
            }
             
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
    if (e.getSource() == volume){
            try {
                double vol = volume.getValue() / 100f;
                stat.setText(Integer.toString(volume.getValue()));
                musicController.setVolume(vol);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(customListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
