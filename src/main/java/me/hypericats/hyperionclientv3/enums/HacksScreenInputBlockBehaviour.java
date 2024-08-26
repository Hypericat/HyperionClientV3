package me.hypericats.hyperionclientv3.enums;

public enum HacksScreenInputBlockBehaviour {
    NOTHING("Nothing"),
    BLOCKTYPING("BlockTyping"),
    BLOCKMOVEMENT("BlockMovement");
    private final String name;
    HacksScreenInputBlockBehaviour(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
