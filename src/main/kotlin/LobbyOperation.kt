package com.micahhenney.lobbyoperation

import net.citizensnpcs.api.CitizensAPI
import net.citizensnpcs.api.event.CitizensEnableEvent
import net.citizensnpcs.api.trait.TraitInfo
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.spigotmc.event.player.PlayerSpawnLocationEvent

class LobbyOperation : JavaPlugin(), Listener {
    override fun onEnable() {
//        logger.info("Enabling LobbyOperation")
        server.pluginManager.registerEvents(this, this)
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(BedwarsJoinTrait::class.java))
        getCommand("addBedwarsTrait").executor = AddBedwarsTraitCommand()
    }

    @EventHandler
    fun onCitizensEnable(event: CitizensEnableEvent) {
    }

    @EventHandler()
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.inventory.clear()
        event.player.inventory.setItem(0, ItemStack(Material.WOOL, 64))
        event.player.inventory.setItem(7, ItemStack(Material.ENDER_PEARL, 1))
        event.joinMessage = ""
        event.player.teleport(Location(event.player.world, 0.0, 23.0, 0.0, 0f, 0f))
    }
    @EventHandler
    fun onPlayerSpawn(event: PlayerRespawnEvent) {
        event.player.inventory.clear()
        event.player.inventory.setItem(0, ItemStack(Material.WOOL, 64))
        event.player.inventory.setItem(7, ItemStack(Material.ENDER_PEARL, 1))
    }

    @EventHandler()
    fun onPlayerPlaceBlock(event: BlockPlaceEvent) {
        if (event.block.type == Material.WOOL) {
            if (event.block.location.blockY > 28) {
                event.isCancelled = true
                return
            }
            object : BukkitRunnable() {
                override fun run() {
                    event.block.type = Material.AIR
                }
            }.runTaskLater(this, 20 * 5)
            event.player.inventory.setItem(0, ItemStack(Material.WOOL, 64))
        } else {
            if(event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        if (event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    fun onPlayerCraft(event: CraftItemEvent) {
        event.isCancelled = true
    }

    @EventHandler()
    fun onPlayerPearl(event: PlayerInteractEvent) {
        if(event.player.itemInHand.type == Material.ENDER_PEARL) {
            object : BukkitRunnable() {
                override fun run() {
                    event.player.inventory.setItem(7, ItemStack(Material.ENDER_PEARL, 1))
                }
            }.runTaskLater(this, 7*20)
        }
    }

    @EventHandler
    fun onPlayerHit(event: EntityDamageEvent) {
        if(event.cause == DamageCause.ENTITY_ATTACK) event.damage = 2.0
        else if (event.cause != DamageCause.VOID) event.damage = 0.0
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        if(event.toWeatherState()) event.isCancelled = true
        object : BukkitRunnable() {
            override fun run() {
                event.world.setStorm(false)
            }
        }.runTaskLater(this, 1)
    }

    @EventHandler
    fun onPlayerDrop(event: PlayerDropItemEvent) {
        if(event.player.gameMode != GameMode.CREATIVE) event.isCancelled = true
    }

    @EventHandler
    fun onPlayerDie(event: PlayerDeathEvent) {
        event.drops.clear()
    }
}