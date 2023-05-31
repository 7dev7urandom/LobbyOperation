package com.micahhenney.lobbyoperation

import net.citizensnpcs.api.event.NPCRightClickEvent
import net.citizensnpcs.api.persistence.Persist
import net.citizensnpcs.api.trait.Trait
import net.citizensnpcs.api.trait.TraitName
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.inventory.ItemStack

@TraitName("bedwars")
class BedwarsJoinTrait : Trait("bedwars") {
    @Persist("arenaGroup") var arenaGroup: String = ""

    @EventHandler
    fun click(event: NPCRightClickEvent) {
        if(event.npc == this.npc) {
            event.clicker.inventory.setItem(1, ItemStack(Material.AIR))
            event.clicker.inventory.heldItemSlot = 1
            event.clicker.performCommand("bw gui $arenaGroup")
        }
    }
}