package me.windyteam.kura.command.commands;

import me.windyteam.kura.command.Command;
import me.windyteam.kura.utils.mc.ChatUtil;
import me.windyteam.kura.utils.mc.ChatUtil;

public class CreditsCommand
extends Command {
    public CreditsCommand() {
        super("credits", null, new String[0]);
        this.setDescription("Prints KAMI Blue's authors and contributors");
    }

    @Override
    public void call(String[] args2) {
        ChatUtil.sendMessage("\nName (Github if not same as name)" +
                "\n&l&9Author:" +
                "\nPyWong_921" +
                "\n&l&9Contributors:" +
                "\nZenHao_123");
    }
}

