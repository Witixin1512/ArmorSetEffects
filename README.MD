#SetEffects

1.16 port of SetEffects, a mod that gives some customisable effects to the entities that are wearing certain, crafttweaker customisable, armor sets.

Includes most methods from the original mod: https://github.com/Lemonszz/SetEffect/

Package: `import mods.seteffect.ArmorSetEffect`

#Static Methods:

`new ArmorSetEffect(String name) as ArmorSetEffect` -> Creates a new ArmorSetEffect with the specified name. Standard name rules apply `[a-z0-9/._-]`.

`ArmorSetEffect.dumpParticleNames() as void` -> Prints a list of particle names to the crafttweaker.log file.

#Instance Methods: 

`MyArmorSetEffect.inSlot(slot as MCEquipmentSlotType, item as IItemStack) as ArmorSetEffect` -> Checks for a certain [IItemStack](https://docs.blamejared.com/1.16/en/vanilla/api/items/IItemStack) in the specified [slot](https://docs.blamejared.com/1.16/en/vanilla/api/util/MCEquipmentSlotType).

`MyArmorSetEffect.setIgnoreNBT() as ArmorSetEffect` -> Ignores NBT data for the specified Items in the ArmorSetEffect.

`MyArmorSetEffect.requireGamestages(stages as string[]) as ArmorSetEffect` -> Adds the specified GameStages to the required GameStages list. Requires GameStagesAPI to be installed.

`MyArmorSetEffect.setPackmode(packmode as string) as ArmorSetEffect` -> Makes the effect only happen in a certain packmode. 

`MyArmorSetEffect.setStrict() as ArmorSetEffect` -> Sets the comparing type of the NBT to strict.

`MyArmorSetEffect.addParticle(particleName as string, minx as float, miny as float, minz as float, maxx as float, maxy as float,  maxz as float,  minxoffset as float,  minyoffset as float,minzoffset as float, float maxxoffset,  maxyoffset as float,  maxzoffset as float,  minspeed as float,  maxspeed as float,  amount as int) as ArmorSetEffect` -> Adds a particle that appears when the player is wearing the full set.

`MyArmorSetEffect.applyFlight(flight as boolean) as ArmorSetEffect` -> Gives the player the ability to fly when wearing the armor

`MyArmorSetEffect.addParticleWithDefaultSpread(particleName as string) as ArmorSetEffect` -> Adds a non customisable particle spread with the specified particle.

`MyArmorSetEffect.addImmunity(effect as MCPotionEffect) as ArmorSetEffect` -> Makes the player wearing the set immune to that [effect](https://docs.blamejared.com/1.16/en/vanilla/api/potions/MCPotionEffect).

`MyArmorSetEffect.addEffect(effect as MCPotionEffectInstance) as ArmorSetEffect` -> Makes the player wearing the set receive that [effect](https://docs.blamejared.com/1.16/en/vanilla/api/potions/MCPotionEffectInstance).

`MyArmorSetEffect.addAttributeEffect(attribute as Attribute, modifier as AttributeModifier) as ArmorSetEffect` -> Adds the [attribute](https://docs.blamejared.com/1.16/en/vanilla/api/entity/Attribute) and it's [modifier](https://docs.blamejared.com/1.16/en/vanilla/api/entity/AttributeModifier) to all armor pieces when worn.

`MyArmorSetEffect.addAttackerEffect(effect as MCPotionEffectInstance) as ArmorSetEffect` -> Makes the entity that attacks the player wearing this set receive an [effect](https://docs.blamejared.com/1.16/en/vanilla/api/potions/MCPotionEffectInstance).

`MyArmorSetEffect.register() as void  ` -> Registers our ArmorSetEffect.



#Example Set:
`````
import mods.seteffect.ArmorSetEffect;

ArmorSetEffect.dumpParticleNames(); 
//Can be commented out after getting the right name for what you want.

val iron_set = new ArmorSetEffect("iron_set")
               .inSlot(<equipmentslottype:head>, <item:minecraft:iron_helmet>)
               .inSlot(<equipmentslottype:chest>, <item:minecraft:iron_chestplate>)
               .inSlot(<equipmentslottype:legs>, <item:minecraft:iron_leggings>)
               .inSlot(<equipmentslottype:feet>, <item:minecraft:iron_boots>)
               .setIgnoreNBT()
               .addEffect(<effect:minecraft:regeneration>.newInstance(100, 5))
               .addImmunity(<effect:minecraft:blindness>)
               .addAttackerEffect(<effect:minecraft:poison>.newInstance(60, 1))
               .requireGamestages("hello");
iron_set.register();

//You can also chain the register to remove the need for a variable.

 new ArmorSetEffect("iron_set")
                .inSlot(<equipmentslottype:head>, <item:minecraft:iron_helmet>)
               .inSlot(<equipmentslottype:chest>, <item:minecraft:iron_chestplate>)
               .inSlot(<equipmentslottype:legs>, <item:minecraft:iron_leggings>)
               .inSlot(<equipmentslottype:feet>, <item:minecraft:iron_boots>)
               .addEffect(<effect:minecraft:regeneration>.newInstance(10, 5))
               .addParticle("minecraft:angry_villager",  1.0f, 1.0f, 1.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, 10.0f, -5.0f, -5.0f, -5.0f, 2.0f, 0.3f, 2);
               .register();

`````

#Expanding MCPotionEffectInstance

`MCPotionEffectInstance.hideParticles()` -> Makes the particles invisible