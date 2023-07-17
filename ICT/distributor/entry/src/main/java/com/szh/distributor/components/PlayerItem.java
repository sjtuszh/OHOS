package com.szh.distributor.components;

public class PlayerItem {
    private String player_id;
    private String player_name;
    private int player_level;
    private boolean status;

    public void PlayerItem(){
        Utils.log("角色已创建");
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public int getPlayer_level() {
        return player_level;
    }

    public void setPlayer_level(int player_level) {
        this.player_level = player_level;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
