package client.game;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Afrikanov
 * Date: 04.03.14
 * Time: 8:47
 * To change this template use File | Settings | File Templates.
 */

/**
 * Класс описывающий сущность одной конкретной игры
 */
public class SimpleGameInfo implements Serializable {

    private GameType gameType;
    private Integer id; // идентификатор
    // Список игроков
    private String player_1;
    private String player_2;
    // Победитель
    private String winPlayer;


    // Default Constructor for Serialization
    public SimpleGameInfo(){}

    // Constructor for End game
    public SimpleGameInfo(Integer id, String player_1, String player_2, String winPlayer){
        this.id = id;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.winPlayer = winPlayer;
    }

    // Constructor for Open Game
    public SimpleGameInfo(Integer id, String player_1, String player_2, GameType gameType){
        this.id = id;
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.winPlayer = "";
        this.gameType = gameType;
    }

    // Setters and Getters


    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlayer_1() {
        return player_1;
    }

    public void setPlayer_1(String player_1) {
        this.player_1 = player_1;
    }

    public String getPlayer_2() {
        return player_2;
    }

    public void setPlayer_2(String player_2) {
        this.player_2 = player_2;
    }

    public String getWinPlayer() {
        return winPlayer;
    }

    public void setWinPlayer(String winPlayer) {
        this.winPlayer = winPlayer;
    }
}
