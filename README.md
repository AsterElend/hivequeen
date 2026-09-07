# Hive Queen
A little mod that adds a few patterns to hexcasting, and 
a few integrations for Botania, Trinkets, and Spectrum. Also adds a recipe system for crafting
great scrolls, with no scroll recipes by default, nor a recipe for the Clockwork Hive workstation itself. 

Formerly named HexXSpectrum, the name was changed because I wanted to 
expand beyond just Spectrum compat. 


### An example great scroll recipe:
~~~
{
  "type": "hivequeen:clockwork_hive",
  "greatSpell": "hexcasting:create_lava",
  "inputs": [
    {"item": "hexcasting:charged_amethyst"},
    {"item": "minecraft:lava_bucket"},
    {"item": "minecraft:dispenser"}
  ]
}
~~~
It should support tags as well. It's recommended that you set showClockworkHiveEntry to true if you do add recipes for
the hive and scrolls. 
