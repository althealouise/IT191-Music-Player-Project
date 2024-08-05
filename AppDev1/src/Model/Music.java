/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.InputStream;

/**
 *
 * @author boxro
 */
public class Music {
    private int num;
    private String title;
    private String artist;
    private InputStream art;
    private String timelength;
    private String lyrics;
    private InputStream file;


    public Music(int num, String title, String artist, InputStream art, String timelength) {
        this.num = num;
        this.title = title;
        this.artist = artist;
        this.art = art;
        this.timelength = timelength;
    }
    
    public Music(int num, String title, String artist, InputStream art, String lyrics, String timelength, InputStream file) {
        this.num = num;
        this.title = title;
        this.artist = artist;
        this.art = art;
        this.lyrics = lyrics;
        this.timelength = timelength;
        this.file = file;
    }    
    
    public Music(int num, String title, String artist, InputStream art, String lyrics, String timelength) {
        this.num = num;
        this.title = title;
        this.artist = artist;
        this.art = art;
        this.lyrics = lyrics;
        this.timelength = timelength;
    }


    public String getTimelength() {
        return timelength;
    }

    public void setTimelength(String timelength) {
        this.timelength = timelength;
    }    

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public InputStream getArt() {
        return art;
    }

    public void setArt(InputStream art) {
        this.art = art;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }    

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }
    
    
}

