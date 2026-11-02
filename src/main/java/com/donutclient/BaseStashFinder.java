package com.donutclient;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Heuristic scanner for servers/worlds where the player has permission to inspect
 * loaded client chunks. It never requests or loads chunks and does not bypass visibility.
 */
public final class BaseStashFinder {
    public record Candidate(int chunkX, int chunkZ, int storage, int utility, int score) {
        public String label() {
            return "Chunk " + chunkX + ", " + chunkZ + "  S:" + storage + " U:" + utility + "  score " + score;
        }
    }

    private static final class Counts {
        int storage;
        int utility;
    }

    private static final List<Candidate> BASES = new ArrayList<>();
    private static final List<Candidate> STASHES = new ArrayList<>();
    private static int scannedBlockEntities;

    private BaseStashFinder() {}

    public static void scanLoadedChunks() {
        BASES.clear();
        STASHES.clear();
        scannedBlockEntities = 0;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        Map<Long, Counts> chunks = new HashMap<>();
        for (BlockEntity entity : client.world.getBlockEntities()) {
            if (entity == null || entity.isRemoved()) continue;
            BlockPos pos = entity.getPos();
            BlockState state = entity.getCachedState();
            Block block = state.getBlock();
            boolean storage = isStorage(block);
            boolean utility = isUtility(block);
            if (!storage && !utility) continue;

            scannedBlockEntities++;
            int cx = Math.floorDiv(pos.getX(), 16);
            int cz = Math.floorDiv(pos.getZ(), 16);
            long key = (((long) cx) << 32) ^ (cz & 0xffffffffL);
            Counts counts = chunks.computeIfAbsent(key, ignored -> new Counts());
            if (storage) counts.storage++;
            if (utility) counts.utility++;
        }

        for (Map.Entry<Long, Counts> entry : chunks.entrySet()) {
            int cx = (int) (entry.getKey() >> 32);
            int cz = (int) (long) entry.getKey();
            Counts c = entry.getValue();
            int baseScore = c.storage * 3 + c.utility * 2;
            if (c.storage + c.utility >= 2) {
                BASES.add(new Candidate(cx, cz, c.storage, c.utility, baseScore));
            }
            if (c.storage >= 2) {
                int stashScore = c.storage * 5 + c.utility;
                STASHES.add(new Candidate(cx, cz, c.storage, c.utility, stashScore));
            }
        }
        Comparator<Candidate> byScore = Comparator.comparingInt(Candidate::score).reversed();
        BASES.sort(byScore);
        STASHES.sort(byScore);
    }

    private static boolean isStorage(Block block) {
        return block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST
                || block == Blocks.BARREL || block == Blocks.ENDER_CHEST
                || block instanceof ShulkerBoxBlock;
    }

    private static boolean isUtility(Block block) {
        return block == Blocks.FURNACE || block == Blocks.BLAST_FURNACE
                || block == Blocks.SMOKER || block == Blocks.HOPPER
                || block == Blocks.DROPPER || block == Blocks.DISPENSER;
    }

    public static List<Candidate> bases() { return List.copyOf(BASES); }
    public static List<Candidate> stashes() { return List.copyOf(STASHES); }
    public static int scannedBlockEntities() { return scannedBlockEntities; }
}
