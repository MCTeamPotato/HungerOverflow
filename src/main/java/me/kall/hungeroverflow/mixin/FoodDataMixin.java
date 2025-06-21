package me.kall.hungeroverflow.mixin;

import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {
    @Shadow private int foodLevel;

    @Shadow private float saturationLevel;

    @Inject(method = "eat(IF)V", at = @At("HEAD"), cancellable = true)
    private void onEat(int foodLevelModifier, float saturationLevelModifier, CallbackInfo ci) {
        float overflowFoodLevel = 0.0F;
        if (this.foodLevel + foodLevelModifier > 20) overflowFoodLevel = (float) this.foodLevel + (float) foodLevelModifier - 20.0F;
        if (overflowFoodLevel > 0.0F) {
            ci.cancel();
            this.foodLevel = 20;
            this.saturationLevel = Math.min(this.saturationLevel + ((float) foodLevelModifier) * saturationLevelModifier * 2.0F + overflowFoodLevel, 20.0F);
        }
    }
}