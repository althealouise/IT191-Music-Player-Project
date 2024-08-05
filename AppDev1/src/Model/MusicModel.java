/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.MySQLController;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class MusicModel {
    private double volume;
    private boolean loop;
    private boolean isSeeked;
    private boolean isFirstRun;
    private boolean isLoaded;
    private boolean isSearched = false;
    private MySQLController music;
    private ArrayList<Music> song;
    private InputStream file;
    private InputStream art;
    private String timelength;
    private String query;
    private String title;
    private String artist;
    private String lyrics;
    private long totalLength;
    private long pauseLength;
    private int songCountTotal;
    private int id;

    public void getSongList(){
        try {
            music = new MySQLController();
            song = music.showSongList();
        } catch (SQLException ex) {
        } catch (IOException ex) {
        }    
        for (Music m : song) {
            
            title = m.getTitle();
            artist = m.getArtist();
            art = m.getArt();
            timelength = m.getTimelength();
            lyrics = m.getLyrics();  
            id= m.getNum();
            
        }    
    }
    
    public void selectSong() throws IOException{
        try {
            music = new MySQLController();
            song = music.selectSong(query);
        } catch (SQLException ex) {
        } catch (IOException ex) {
        } catch (PropertyVetoException ex) {
        }
        for (Music m : song) {
            file = m.getFile();
            title = m.getTitle();
            artist = m.getArtist();
            art = m.getArt();
            lyrics = m.getLyrics();
            timelength = m.getTimelength();
            id= m.getNum();
        }
        this.totalLength = file.available();
        this.isLoaded = true;
    }

    public double getVolume() {
        return volume;
    }

    public String getTimelength() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength = timelength;
    }   
    
    public void setVolume(double volume) {
        this.volume = volume;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isIsSeeked() {
        return isSeeked;
    }

    public void setIsSeeked(boolean isSeeked) {
        this.isSeeked = isSeeked;
    }

    public boolean isIsFirstRun() {
        return isFirstRun;
    }

    public void setIsFirstRun(boolean isFirstRun) {
        this.isFirstRun = isFirstRun;
    }

    public boolean isIsLoaded() {
        return isLoaded;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }

    public MySQLController getMusic() {
        return music;
    }

    public void setMusic(MySQLController music) {
        this.music = music;
    }

    public ArrayList<Music> getSong() {
        return song;
    }

    public void setSong(ArrayList<Music> song) {
        this.song = song;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public InputStream getArt() {
        return art;
    }

    public void setArt(InputStream art) {
        this.art = art;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        System.out.println(query);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public long getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(long totalLength) {
        this.totalLength = totalLength;
    }

    public long getPauseLength() {
        return pauseLength;
    }

    public void setPauseLength(long pauseLength) {
        this.pauseLength = pauseLength;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSongCountTotal() {
        return songCountTotal;
    }

    public void setSongCountTotal(int songCountTotal) {
        this.songCountTotal = songCountTotal;
    }

    public boolean isIsSearched() {
        return isSearched;
    }

    public void setIsSearched(boolean isSearched) {
        this.isSearched = isSearched;
    }

    
}
