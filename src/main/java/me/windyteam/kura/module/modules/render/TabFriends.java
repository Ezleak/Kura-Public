package me.windyteam.kura.module.modules.render;

import me.windyteam.kura.friend.FriendManager;
import me.windyteam.kura.module.Category;
import me.windyteam.kura.module.Module;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

@Module.Info(name = "TabFriends", description = "Highlights friends in the tab menu", category = Category.RENDER)
public class TabFriends extends Module {

    public static TabFriends INSTANCE;

    public TabFriends() {
        TabFriends.INSTANCE = this;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        String dname = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
        if (FriendManager.isFriend(dname)) return String.format("%sa%s", "§", dname);
        return dname;
    }
}
