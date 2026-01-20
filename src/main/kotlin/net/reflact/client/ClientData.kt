package net.reflact.client

import java.util.HashMap

object ClientData {
    var maxMana = 100.0
    var currentMana = 100.0
    var maxHealth = 20.0
    var currentHealth = 20.0

    val spellCooldowns: MutableMap<String, Long> = HashMap()
    val spellLastCast: MutableMap<String, Long> = HashMap()

    class Waypoint(var name: String, var x: Int, var y: Int, var z: Int, var color: Int)

    val waypoints: MutableList<Waypoint> = ArrayList()

    init {
        spellCooldowns["fireball"] = 5000L // 5 seconds
        spellCooldowns["heal"] = 12000L // 12 seconds

        waypoints.add(Waypoint("Spawn", 0, 100, 0, 0xFF00FF00.toInt()))
    }
}
