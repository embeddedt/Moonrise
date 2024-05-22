package ca.spottedleaf.moonrise.mixin.fast_palette;

import ca.spottedleaf.moonrise.patches.fast_palette.FastPalette;
import ca.spottedleaf.moonrise.patches.fast_palette.FastPalettedContainer;
import net.minecraft.world.level.chunk.LinearPalette;
import net.minecraft.world.level.chunk.Palette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LinearPalette.class)
public abstract class LinearPaletteMixin<T> implements Palette<T>, FastPalette<T> {

    @Shadow
    @Final
    private T[] values;

    @Override
    public T[] moonrise$getRawPalette(final FastPalettedContainer<T> container) {
        return this.values;
    }
}
