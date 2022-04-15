package net.witixin.seteffect.armor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;


public class ArmorEffectAttributes implements IArmorEffect{
    private final Attribute attr;
    private final AttributeModifier modifier;
    public ArmorEffectAttributes(Attribute attribute, AttributeModifier modifier){
        this.attr = attribute;
        this.modifier = modifier;

    }
    @Override
    public void apply(LivingEntity living) {
        //living.getArmorSlots().forEach(armor -> armor.addAttributeModifier(attr, modifier, armor.getEquipmentSlot()));
        //living.getAttributes().addTransientAttributeModifiers(getMap());
    }
    public void remove(LivingEntity livingEntity){
        livingEntity.getArmorSlots().forEach(armor -> {
            armor.addAttributeModifier(attr, new AttributeModifier("empty", modifier.getAmount() * -1, modifier.getOperation()), armor.getEquipmentSlot());
        });
        //livingEntity.getAttributes().removeAttributeModifiers(getMap());
    }
}
