package com.totophoto.Models;

public class Settings {
    private String Lang;
    private String Mode;

    public Settings(String Lang, String Mode) {
        this.Lang = Lang;
        this.Mode = Mode;
    }

    public String getLang() {
        return this.Lang;
    }

    public String getMode() {
        return this.Mode;
    }
}
