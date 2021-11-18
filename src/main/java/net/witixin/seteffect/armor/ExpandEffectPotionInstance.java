package net.witixin.seteffect.armor;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import net.minecraft.potion.EffectInstance;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Expansion("crafttweaker.api.potion.MCPotionEffectInstance")
public class ExpandEffectPotionInstance {
    @ZenCodeType.Method
    public static EffectInstance hideParticles(EffectInstance instance){
        instance.visible = false;
        return instance;
    }
}
