package com.donutclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class DonutClient implements ClientModInitializer {
    public static KeyBinding openGuiKey;
    public static boolean coordsHud = true, sprintAssist = false, pvpInfo = true;
    @Override public void onInitializeClient() {
        Waypoints.load();
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.donutclient.open_gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.donutclient"));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) client.setScreen(new DonutClickGui());
            if (sprintAssist && client.player != null) client.options.sprintKey.setPressed(true);
        });
        HudRenderCallback.EVENT.register((ctx, tickCounter) -> {
            MinecraftClient c = MinecraftClient.getInstance(); if (c.player == null) return;
            int y = 10;
            if (coordsHud) {
                String s = String.format("XYZ  %d  %d  %d", c.player.getBlockX(), c.player.getBlockY(), c.player.getBlockZ());
                ctx.fill(5, 5, 12 + c.textRenderer.getWidth(s), 23, 0x990D1020); ctx.drawTextWithShadow(c.textRenderer, Text.literal(s), 10, 10, 0xFF65E6D9); y += 20;
            }
            if (pvpInfo && c.targetedEntity != null) {
                String s = "TARGET  " + c.targetedEntity.getName().getString();
                ctx.fill(5, y - 5, 12 + c.textRenderer.getWidth(s), y + 13, 0x990D1020); ctx.drawTextWithShadow(c.textRenderer, Text.literal(s), 10, y, 0xFFC5A4FF);
            }
        });
    }
}
