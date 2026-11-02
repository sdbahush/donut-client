package com.donutclient;

import net.minecraft.client.MinecraftClient;
import java.nio.file.*;
import java.util.*;

public final class Waypoints {
    public record Point(String name, int x, int y, int z) {}
    private static final List<Point> POINTS = new ArrayList<>();
    private static Path file() { return MinecraftClient.getInstance().runDirectory.toPath().resolve("config/donutclient-waypoints.txt"); }
    public static List<Point> all() { return Collections.unmodifiableList(POINTS); }
    public static void addCurrent() {
        var p = MinecraftClient.getInstance().player;
        if (p == null) return;
        POINTS.add(new Point("Waypoint " + (POINTS.size() + 1), p.getBlockX(), p.getBlockY(), p.getBlockZ()));
        save();
    }
    public static void load() {
        try {
            Path f = file(); if (!Files.exists(f)) return;
            for (String line : Files.readAllLines(f)) {
                String[] a = line.split("\\t");
                if (a.length == 4) POINTS.add(new Point(a[0], Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3])));
            }
        } catch (Exception ignored) {}
    }
    public static void save() {
        try {
            Files.createDirectories(file().getParent());
            List<String> lines = POINTS.stream().map(p -> p.name()+"\\t"+p.x()+"\\t"+p.y()+"\\t"+p.z()).toList();
            Files.write(file(), lines);
        } catch (Exception ignored) {}
    }
}
