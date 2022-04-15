package net.witixin.seteffect.armor;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;


public class ArmorEffectAttributes {
    private final Attribute attr;
    private final AttributeModifier modifier;
    public ArmorEffectAttributes(Attribute attribute, AttributeModifier modifier){
        this.attr = attribute;
        this.modifier = modifier;

    }
    public void apply(LivingEntity living) {
        living.getArmorSlots().forEach(armor -> fancyArmorChange(armor));
    }
    public void remove(LivingEntity livingEntity){
        livingEntity.getArmorSlots().forEach(armor -> {
            armor.addAttributeModifier(attr, new AttributeModifier(modifier.getId().toString(), processAmount(modifier.getOperation(), modifier.getAmount()), modifier.getOperation()), armor.getEquipmentSlot());
        });
        //livingEntity.getAttributes().removeAttributeModifiers(getMap());
    }
    private double processAmount(AttributeModifier.Operation operation, double oldAmount){
        switch (operation){
            case ADDITION:
                return oldAmount * -1;
            case MULTIPLY_BASE:
            case MULTIPLY_TOTAL:
                return 1 / oldAmount;
        }
        return oldAmount;
    }
    private void fancyArmorChange(ItemStack armor){
        if (!armor.getOrCreateTag().contains("SETEFFECT")){
            armor.addAttributeModifier(attr, modifier, armor.getEquipmentSlot());
            armor.getTag().putBoolean("SETEFFECT", true);
            System.out.println("happened");
        }
    }
}
