/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package com.mycompany.tetris;

/**
 *
 * @author puntual
 */
public class ConfigData {
    
    private static ConfigData configData = new ConfigData();
    
    private String playerName;
    private int level;
    private int score;
    
    private ConfigData() {
        playerName = "";
        level = 1;
        score = 0;
        
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public static ConfigData getInstance() {
        return configData;
    }
    
    
}
