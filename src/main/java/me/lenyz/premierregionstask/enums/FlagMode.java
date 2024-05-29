package me.lenyz.premierregionstask.enums;

public enum FlagMode {
    EVERYONE("Everyone"),
    WHITELIST("Whitelist"),
    NONE("None");

    private String name;

    FlagMode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
