package net.kobe.kobemod.item;


import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties STRAWBERRY = new FoodProperties.Builder().nutrition(5).fast()
            .saturationMod(8f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200), 1.0f).build();

}