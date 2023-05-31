package com.micahhenney.lobbyoperation

import net.citizensnpcs.api.CitizensAPI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class AddBedwarsTraitCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, text: String?, args: Array<out String>?): Boolean {
        CitizensAPI.getDefaultNPCSelector().getSelected(sender).addTrait(BedwarsJoinTrait())
        return true;
    }

}