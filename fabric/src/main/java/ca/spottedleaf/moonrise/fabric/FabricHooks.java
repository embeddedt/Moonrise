package ca.spottedleaf.moonrise.fabric;

import ca.spottedleaf.moonrise.common.PlatformHooks;
import ca.spottedleaf.moonrise.patches.chunk_system.scheduling.NewChunkHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import java.util.List;
import java.util.function.Predicate;

public final class FabricHooks implements PlatformHooks {

    @Override
    public int getLightEmission(final BlockState blockState, final BlockGetter world, final BlockPos pos) {
        return blockState.getLightEmission();
    }

    @Override
    public Predicate<BlockState> maybeHasLightEmission() {
        return (final BlockState state) -> {
            return state.getLightEmission() != 0;
        };
    }

    @Override
    public void onExplosion(final Level world, final Explosion explosion, final List<Entity> possiblyAffecting, final double diameter) {

    }

    @Override
    public Vec3 modifyExplosionKnockback(final Level world, final Explosion explosion, final Entity entity, final Vec3 original) {
        return original;
    }

    @Override
    public boolean hasCurrentlyLoadingChunk() {
        return false;
    }

    @Override
    public LevelChunk getCurrentlyLoadingChunk(final GenerationChunkHolder holder) {
        return null;
    }

    @Override
    public void setCurrentlyLoading(final GenerationChunkHolder holder, final LevelChunk levelChunk) {

    }

    @Override
    public void chunkFullStatusComplete(final LevelChunk newChunk, final ProtoChunk original) {

    }

    @Override
    public boolean allowAsyncTicketUpdates() {
        return false;
    }

    @Override
    public void onChunkHolderTicketChange(final ServerLevel world, final NewChunkHolder holder, final int oldLevel, final int newLevel) {

    }

    @Override
    public void chunkUnloadFromWorld(final LevelChunk chunk) {

    }

    @Override
    public void chunkSyncSave(final ServerLevel world, final ChunkAccess chunk, final CompoundTag data) {

    }

    @Override
    public void onChunkWatch(final ServerLevel world, final LevelChunk chunk, final ServerPlayer player) {

    }

    @Override
    public void onChunkUnWatch(final ServerLevel world, final ChunkPos chunk, final ServerPlayer player) {

    }

    @Override
    public void addToGetEntities(final Level world, final Entity entity, final AABB boundingBox, final Predicate<? super Entity> predicate,
                                 final List<Entity> into) {

    }

    @Override
    public <T extends Entity> void addToGetEntities(final Level world, final EntityTypeTest<Entity, T> entityTypeTest, final AABB boundingBox,
                                                    final Predicate<? super T> predicate, final List<? super T> into, final int maxCount) {

    }

    @Override
    public void entityMove(final Entity entity, final long oldSection, final long newSection) {

    }

    @Override
    public boolean screenEntity(final ServerLevel world, final Entity entity, final boolean fromDisk, final boolean event) {
        return true;
    }
}
