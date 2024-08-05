package View;

import Controller.MySQLController;
import Model.Music;
import Controller.MusicController;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import javazoom.jl.decoder.JavaLayerException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author altheacruz
 */
public class MusicView extends javax.swing.JFrame {

    /**
     * Creates new form MusicView
     */
    private MySQLController music = new MySQLController();
    private ArrayList<Music> songData;
    private DefaultTableModel table;
    private MusicController controller;
    private ImageIcon pauseIcon, playIcon;

    public MusicView(){
        
        controller = new MusicController();
        initComponents();
        prepareTable();

        pauseIcon = new ImageIcon("src/Assets/pauseButton.png");
        playIcon = new ImageIcon("src/Assets/playButton.png");
        
        bg.setIcon(controller.backgroundScale(new ImageIcon("src/Assets/bg.jpg")));

        
        nextButton.setOpaque(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setBorderPainted(false);

        prevButton.setOpaque(false);
        prevButton.setContentAreaFilled(false);
        prevButton.setBorderPainted(false);

        pausePlayButton.setOpaque(false);
        pausePlayButton.setContentAreaFilled(false);
        pausePlayButton.setBorderPainted(false);

        playlist.setOpaque(false);
        playlist.getViewport().setOpaque(false);
        playlist.setBorder(null);
        playlistSongs.setOpaque(false);
        playlistSongs.setShowGrid(false);
        playlistSongs.setBorder(null);
        ((DefaultTableCellRenderer) playlistSongs.getDefaultRenderer(Object.class)).setOpaque(false);
        playlistSongs.setForeground(Color.WHITE);
        playlistSongs.setSelectionBackground(Color.WHITE);
        lyrics.setOpaque(false);
        lyrics.setForeground(Color.WHITE);
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        
        // side panel with layout manager
        sidePanel.setLayout(new BorderLayout());
        sidePanel.setSize(310, 785);
        sidePanel.add(logo, BorderLayout.NORTH);
//        JPanel volPanel = new JPanel();
//            volPanel.setLayout(new FlowLayout());
//            volPanel.add(volLabel);
//            volPanel.add(volstat);
//        JPanel blankPanel = new JPanel();
//        sidePanel.add(blankPanel, BorderLayout.CENTER);
//        sidePanel.add(volPanel, BorderLayout.SOUTH);
//        ////////////////////////
        
        musicButtonsPanel.setLayout(new GridLayout(1,3));
        musicButtonsPanel.setSize(205,60);
        musicButtonsPanel.add(prevButton);
        musicButtonsPanel.add(pausePlayButton);
        musicButtonsPanel.add(nextButton);
        
        customListener pButton;
        pButton = new customListener(pausePlayButton, controller);
        customListener vSlider = new customListener(volumeSlider,controller,volstat);
        pausePlayButton.addActionListener(pButton);
        volumeSlider.addChangeListener(vSlider);
        playlistSongs.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {

                try {
                    String searchPrev = playlistSongs.getValueAt(playlistSongs.getSelectedRow(), 1).toString();
                    var query = "SELECT * FROM musicbackend.database WHERE title = \"" + (searchPrev) + "\";";
                    controller.getModel().setQuery(query);
                    controller.stopMusic();
                    controller.playMusic();
                    pausePlayButton.setIcon(pauseIcon);
                    Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);
                    ImageIcon icon = new ImageIcon(img);
                    albumArt.setIcon(icon);
                    songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                    songArtist.setText(controller.getModel().getArtist());
                    lyrics.setText(controller.getModel().getLyrics());
                    lyrics.setCaretPosition(0);
                } catch (IOException ex) {
                    Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JavaLayerException ex) {
                    Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
        pausePlayButton.setIcon(playIcon);

    }   
    
    private void prepareTable() {

        table = (DefaultTableModel) playlistSongs.getModel();
        playlistSongs.setModel(controller.populateTableDisplay(table));
        playlistSongs.getColumnModel().getColumn(0).setCellRenderer(new CenteredIconCellRenderer());
        int iconHeight = 100; // Set the desired height for the icons
        playlistSongs.setRowHeight(iconHeight); // +4 to provide a small margin
        playlistSongs.setOpaque(false);
        playlistSongs.setBackground(new Color(0, 0, 0, 0));
        playlist.setColumnHeader(new JViewport());
        playlist.getColumnHeader().setOpaque(false);

    }

    private class IconCellRenderer extends JLabel implements TableCellRenderer {

        private final int ICON_SIZE = 60; // Change the value to adjust the icon size

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable playlistSongs, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Assuming the value is an Icon
            setIcon(getScaledIcon((Icon) value, ICON_SIZE, ICON_SIZE));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
            return this;
        }

        private Icon getScaledIcon(Icon icon, int width, int height) {
            if (icon == null) {
                return null;
            }

            ImageIcon imageIcon = (ImageIcon) icon;
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }

    }

    private class CenteredIconCellRenderer extends IconCellRenderer {

        public CenteredIconCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        volLabel = new javax.swing.JLabel();
        volumeSlider = new javax.swing.JSlider();
        volstat = new javax.swing.JLabel();
        sidePanel = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        playlist = new javax.swing.JScrollPane();
        playlistSongs = new javax.swing.JTable();
        nowplayingPanel = new javax.swing.JPanel();
        albumArt = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        songTitle = new javax.swing.JLabel();
        songArtist = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lyrics = new javax.swing.JTextArea();
        musicButtonsPanel = new javax.swing.JPanel();
        prevButton = new javax.swing.JButton();
        pausePlayButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1280, 823));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Songlist");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 20, -1, -1));

        jTextField1.setForeground(new java.awt.Color(204, 204, 204));
        jTextField1.setText("Search Title");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTextField1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jTextField1MouseReleased(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });
        getContentPane().add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 20, 140, 40));

        volLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        volLabel.setForeground(new java.awt.Color(255, 255, 255));
        volLabel.setText("Volume");
        getContentPane().add(volLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, -1, -1));
        getContentPane().add(volumeSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 620, 156, 31));

        volstat.setFont(new java.awt.Font("HelveticaNeue", 0, 14)); // NOI18N
        volstat.setForeground(new java.awt.Color(51, 153, 255));
        volstat.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        volstat.setText("50");
        getContentPane().add(volstat, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 620, 30, 30));

        sidePanel.setBackground(new java.awt.Color(52, 34, 84));
        sidePanel.setOpaque(false);

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/logo.png"))); // NOI18N
        logo.setRequestFocusEnabled(false);

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );
        sidePanelLayout.setVerticalGroup(
            sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sidePanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(283, Short.MAX_VALUE))
        );

        getContentPane().add(sidePanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 310, 440));

        playlist.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 2, true));

        playlistSongs.setBackground(new java.awt.Color(54, 25, 104));
        playlistSongs.setForeground(new java.awt.Color(102, 102, 102));
        playlistSongs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        playlistSongs.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        playlistSongs.setGridColor(new java.awt.Color(255, 255, 255));
        playlistSongs.setOpaque(false);
        playlistSongs.setRowHeight(60);
        playlistSongs.setSelectionBackground(new java.awt.Color(255, 255, 255));
        playlistSongs.setSelectionForeground(new java.awt.Color(153, 102, 255));
        playlistSongs.setShowGrid(true);
        playlist.setViewportView(playlistSongs);
        playlistSongs.getAccessibleContext().setAccessibleName("playlistSongs");

        getContentPane().add(playlist, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 70, 620, 720));

        nowplayingPanel.setBackground(new java.awt.Color(0, 0, 0));
        nowplayingPanel.setOpaque(false);

        albumArt.setForeground(new java.awt.Color(51, 51, 51));
        albumArt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/nptablabel.png"))); // NOI18N
        jLabel3.setText("jLabel3");

        songTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        songTitle.setForeground(new java.awt.Color(255, 255, 255));
        songTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        songTitle.setText(" ");
        songTitle.setToolTipText("");
        songTitle.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        songArtist.setForeground(new java.awt.Color(142, 101, 255));
        songArtist.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        songArtist.setText(" ");

        jScrollPane1.setBackground(new java.awt.Color(132, 94, 218));

        lyrics.setColumns(20);
        lyrics.setLineWrap(true);
        lyrics.setRows(5);
        lyrics.setBorder(null);
        jScrollPane1.setViewportView(lyrics);

        musicButtonsPanel.setOpaque(false);

        prevButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/prevButton.png"))); // NOI18N
        prevButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevButtonActionPerformed(evt);
            }
        });

        pausePlayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/pauseButton.png"))); // NOI18N

        nextButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/nextButton.png"))); // NOI18N
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout musicButtonsPanelLayout = new javax.swing.GroupLayout(musicButtonsPanel);
        musicButtonsPanel.setLayout(musicButtonsPanelLayout);
        musicButtonsPanelLayout.setHorizontalGroup(
            musicButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, musicButtonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(prevButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pausePlayButton)
                .addGap(18, 18, 18)
                .addComponent(nextButton)
                .addGap(15, 15, 15))
        );
        musicButtonsPanelLayout.setVerticalGroup(
            musicButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, musicButtonsPanelLayout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addGroup(musicButtonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pausePlayButton)
                    .addComponent(prevButton)
                    .addComponent(nextButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout nowplayingPanelLayout = new javax.swing.GroupLayout(nowplayingPanel);
        nowplayingPanel.setLayout(nowplayingPanelLayout);
        nowplayingPanelLayout.setHorizontalGroup(
            nowplayingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nowplayingPanelLayout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addGroup(nowplayingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nowplayingPanelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(songArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(songTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(albumArt, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(nowplayingPanelLayout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(musicButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        nowplayingPanelLayout.setVerticalGroup(
            nowplayingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nowplayingPanelLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(albumArt, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(songTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(songArtist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(musicButtonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
        );

        getContentPane().add(nowplayingPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(943, 0, 320, 788));

        jButton1.setText("Search");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 30, -1, -1));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/bg.jpg"))); // NOI18N
        bg.setInheritsPopupMenu(false);
        bg.setMaximumSize(new java.awt.Dimension(1267, 831));
        bg.setMinimumSize(new java.awt.Dimension(1267, 831));
        bg.setPreferredSize(new java.awt.Dimension(1267, 831));
        getContentPane().add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1290, 825));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        // TODO add your handling code here:
        int songID;

        songID = controller.getModel().getId();
        
        if (controller.getModel().isIsSearched()){
        try {

                var query = "SELECT * FROM musicbackend.database WHERE id = '" + songID + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }        
        }
        else{

        if (songID >= 1 && songID < 10) {
            try {
                int nextSongID = songID + 1;

                var query = "SELECT * FROM musicbackend.database WHERE id = '" + nextSongID + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {

                var query = "SELECT * FROM musicbackend.database WHERE id = '" + 1 + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
    }//GEN-LAST:event_nextButtonActionPerformed

    private void prevButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevButtonActionPerformed
        // TODO add your handling code here:
        int songID;

        songID = controller.getModel().getId();
        
        
        if(controller.getModel().isIsSearched()){
        try {

                var query = "SELECT * FROM musicbackend.database WHERE id = '" + songID + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        } else {
        if (songID > 1 && songID <= 10) {
            try {
                int prevSongID = songID - 1;

                var query = "SELECT * FROM musicbackend.database WHERE id = '" + prevSongID + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            try {
                var query = "SELECT * FROM musicbackend.database WHERE id = '" + 10 + "';";
                //controller.getModel().setId(nextSongID);
                controller.getModel().setQuery(query);
                controller.stopMusic();
                controller.playMusic();

                Image img = controller.getScaledImage(ImageIO.read(controller.getModel().getArt()), 240, 240);

                ImageIcon icon = new ImageIcon(img);
                albumArt.setIcon(icon);
                songTitle.setText("<html>" + controller.getModel().getTitle() + "</html>");
                songArtist.setText(controller.getModel().getArtist());
                lyrics.setText(controller.getModel().getLyrics());
                pausePlayButton.setIcon(pauseIcon);
            } catch (IOException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JavaLayerException ex) {
                Logger.getLogger(MusicView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }
        
        

    }//GEN-LAST:event_prevButtonActionPerformed

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_jTextField1KeyPressed

    private void jTextField1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseReleased
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1MouseReleased

    private void jTextField1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MouseClicked
        // TODO add your handling code here:
        jTextField1.setText("");
    }//GEN-LAST:event_jTextField1MouseClicked

    private void jTextField1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextField1MousePressed
        // TODO add your handling code here:

    }//GEN-LAST:event_jTextField1MousePressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String search = jTextField1.getText();
        prepareTable();

        table = (DefaultTableModel) playlistSongs.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(table);

        if (search.trim().length() == 0) {
            sorter.setRowFilter(null);
            controller.getModel().setIsSearched(false);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
            controller.getModel().setIsSearched(true);

        }
        playlistSongs.setRowSorter(sorter);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(MusicView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MusicView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MusicView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MusicView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PlaylistAFrame().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MusicView().setVisible(true);
//            }
//        });
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new MusicView().setVisible(true);
                }
            });
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel albumArt;
    private javax.swing.JLabel bg;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel logo;
    private javax.swing.JTextArea lyrics;
    private javax.swing.JPanel musicButtonsPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel nowplayingPanel;
    private javax.swing.JButton pausePlayButton;
    private javax.swing.JScrollPane playlist;
    public javax.swing.JTable playlistSongs;
    private javax.swing.JButton prevButton;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JLabel songArtist;
    private javax.swing.JLabel songTitle;
    private javax.swing.JLabel volLabel;
    private javax.swing.JLabel volstat;
    private javax.swing.JSlider volumeSlider;
    // End of variables declaration//GEN-END:variables
}
