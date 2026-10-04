package io.github.lxgaming.northerncompass.mixin;

import io.github.lxgaming.northerncompass.NorthernCompass;
import io.github.lxgaming.northerncompass.configuration.Config;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.CompassAngle;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.util.Mth;

@Mixin(CompassAngle.class)
public abstract class CompassAngleMixin {

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void onGet(ItemStack itemStack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed, CallbackInfoReturnable<Float> cir) {
        Level currentLevel = level;
        if (currentLevel == null && owner != null) {
            currentLevel = owner.level();
        }

        if (itemStack.has(DataComponents.LODESTONE_TRACKER) || !isDimensionAllowed(currentLevel)) {
            return; // Let the original CompassAngle code handle it
        }

        if (owner == null) {
            cir.setReturnValue(0.0F);
            return;
        }

        double rotation = owner.getVisualRotationYInDegrees();
        
        double angle = 360.0D - ((rotation + 180.0D) % 360.0D);
        double modulo = Mth.positiveModulo(angle / 360.0D, 1.0D);
        cir.setReturnValue((float) modulo);
    }

    private boolean isDimensionAllowed(@Nullable Level level) {
        Config config = NorthernCompass.getInstance().getConfiguration().getConfig();
        if (config == null) {
            return true;
        }

        if (level == null) {
            return config.isDimensionTypeFallback();
        }

        ResourceKey<DimensionType> dimensionTypeId = level.dimensionTypeRegistration().unwrapKey().orElse(null);
        if (dimensionTypeId == null) {
            return config.isDimensionTypeFallback();
        }

        String key = dimensionTypeId.identifier().toString();
        Boolean value = config.getDimensionTypes().get(key);
        return value != null ? value : config.isDimensionTypeFallback();
    }
}
