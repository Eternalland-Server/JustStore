package net.sakuragame.eternal.juststore.commands;

import lombok.Getter;

public enum CommandPerms {

    USER("juststore.user"),
    ADMIN("juststore.admin");

    @Getter
    private final String node;

    CommandPerms(String node) {
        this.node = node;
    }
}