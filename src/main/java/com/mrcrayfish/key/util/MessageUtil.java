package com.mrcrayfish.key.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.TextComponentString;

public class MessageUtil {
	/* This only works on a server because it modifies the packet */
	public static void sendSpecial(EntityPlayer player, String message) {
		if (player.world.isRemote) {
			return;
		}

		EntityPlayerMP playerMp = (EntityPlayerMP) player;
		playerMp.connection.sendPacket(new SPacketChat((new TextComponentString(message))));
	}
}
