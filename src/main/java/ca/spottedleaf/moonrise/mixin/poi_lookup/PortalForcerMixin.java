package ca.spottedleaf.moonrise.mixin.poi_lookup;

import ca.spottedleaf.moonrise.patches.poi_lookup.PoiAccess;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(PortalForcer.class)
public abstract class PortalForcerMixin {

    @Shadow
    @Final
    private ServerLevel level;

    /**
     * @reason Route to use PoiAccess
     * @author Spottedleaf
     */
    @Overwrite
    public Optional<BlockUtil.FoundRectangle> findClosestPortalPosition(BlockPos blockPos, boolean bl, WorldBorder worldBorder) {
        PoiManager poiManager = this.level.getPoiManager();
        int i = bl ? 16 : 128;
        List<PoiRecord> records = new ArrayList<>();
        PoiAccess.findClosestPoiDataRecords(
                poiManager, type -> type.is(PoiTypes.NETHER_PORTAL),
                (BlockPos pos) -> {
                    ChunkAccess lowest = this.level.getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.EMPTY);
                    BelowZeroRetrogen belowZeroRetrogen;
                    if (!lowest.getPersistedStatus().isOrAfter(ChunkStatus.FULL)
                            // check below zero retrogen so that pre 1.17 worlds still load portals (JMP)
                            && ((belowZeroRetrogen = lowest.getBelowZeroRetrogen()) == null || !belowZeroRetrogen.targetStatus().isOrAfter(ChunkStatus.SPAWN))) {
                        // why would we generate the chunk?
                        return false;
                    }
                    if (!worldBorder.isWithinBounds(pos)) {
                        return false;
                    }
                    return lowest.getBlockState(pos).hasProperty(BlockStateProperties.HORIZONTAL_AXIS);
                },
                blockPos, i, Double.MAX_VALUE, PoiManager.Occupancy.ANY, true, records
        );

        // this gets us most of the way there, but Vanilla biases lower y values.
        PoiRecord lowestYRecord = null;
        for (PoiRecord record : records) {
            if (lowestYRecord == null) {
                lowestYRecord = record;
            } else if (lowestYRecord.getPos().getY() > record.getPos().getY()) {
                lowestYRecord = record;
            }
        }
        // now we're done
        if (lowestYRecord == null) {
            return Optional.empty();
        }

        BlockPos blockPos1 = lowestYRecord.getPos();
        this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(blockPos1), 3, blockPos1);
        BlockState blockState = this.level.getBlockState(blockPos1);
        return Optional.of(BlockUtil.getLargestRectangleAround(blockPos1, blockState.getValue(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, (blockPosx) -> {
            return this.level.getBlockState(blockPosx) == blockState;
        }));
    }
}
