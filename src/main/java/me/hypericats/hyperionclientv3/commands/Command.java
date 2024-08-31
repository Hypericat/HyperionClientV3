package me.hypericats.hyperionclientv3.commands;

import me.hypericats.hyperionclientv3.util.ChatUtils;

public abstract class Command {
    public abstract String getStart();
    public abstract void handle(String[] formatted);
    public abstract String getProperUsage();
    protected void throwError(String error) {
        ChatUtils.sendError("Error invalid command : " + error);
        printProperUsage();
    }
    protected void printProperUsage() {
        ChatUtils.sendError("&&6Proper Usage : " + this.getProperUsage());
    }
    protected boolean tryThrowErrorReturn(boolean condition, String error) {
        if (condition) throwError(error);
        return condition;
    }
}
