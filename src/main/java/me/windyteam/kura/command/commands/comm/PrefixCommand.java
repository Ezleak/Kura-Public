package me.windyteam.kura.command.commands.comm;

import me.windyteam.kura.Kura;
import me.windyteam.kura.command.Command;
import me.windyteam.kura.command.syntax.ChunkBuilder;
import me.windyteam.kura.utils.mc.ChatUtil;

public class PrefixCommand
extends Command {
    public PrefixCommand() {
        super("prefix", new ChunkBuilder().append("character").build());
        this.setDescription("Changes the prefix to your new key");
    }

    @Override
    public void call(String[] args2) {
        if (args2.length <= 0) {
            ChatUtil.NoSpam.sendErrorMessage("Please specify a new prefix!");
            return;
        }
        if (args2[0] != null) {
            Kura.commandPrefix.setValue(args2[0]);
            ChatUtil.NoSpam.sendMessage("Prefix set to "+SECTION_SIGN+"b" + Command.getCommandPrefix());
        } else if (args2[0].equals("\\")) {
            ChatUtil.NoSpam.sendErrorMessage("Error: \"\\\" is not a supported prefix");
        } else {
            ChatUtil.NoSpam.sendWarnMessage("Please specify a new prefix!");
        }
    }
}

