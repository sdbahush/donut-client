package com.donutclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class DonutClickGui extends Screen {
    private int tab = 0;
    private static final String[] TABS = {"HUD", "Movement", "Combat", "Waypoints"};
    public DonutClickGui() { super(Text.literal("Donut Client")); }
    @Override public void render(DrawContext c, int mx, int my, float delta) {
        renderBackground(c, mx, my, delta);
        int left = width / 2 - 190, top = height / 2 - 125;
        c.fill(left, top, left + 380, top + 250, 0xF20B1020);
        c.fill(left, top, left + 380, top + 34, 0xFF171E35);
        c.fill(left, top + 33, left + 3, top + 250, 0xFF65E6D9);
        c.drawTextWithShadow(textRenderer, Text.literal("DONUT CLIENT"), left + 14, top + 12, 0xFF65E6D9);
        c.drawTextWithShadow(textRenderer, Text.literal("RIGHT SHIFT  •  CLOSE"), left + 250, top + 12, 0xFF9BA8C7);
        for (int i = 0; i < TABS.length; i++) {
            int y = top + 48 + i * 34;
            c.fill(left + 12, y - 5, left + 112, y + 20, i == tab ? 0xFF283453 : 0xFF131A2B);
            c.drawTextWithShadow(textRenderer, Text.literal(TABS[i]), left + 22, y + 3, i == tab ? 0xFF65E6D9 : 0xFFD4D9E8);
        }
        int x = left + 132, y = top + 55;
        c.drawTextWithShadow(textRenderer, Text.literal(TABS[tab].toUpperCase()), x, y, 0xFFC5A4FF);
        y += 28;
        if (tab == 0) {
            row(c, x, y, "Coordinates HUD", DonutClient.coordsHud, "Show your current XYZ");
            row(c, x, y + 48, "Target HUD", DonutClient.pvpInfo, "Show the entity under your crosshair");
        } else if (tab == 1) {
            row(c, x, y, "Sprint Assist", DonutClient.sprintAssist, "Hold sprint while enabled");
            c.drawTextWithShadow(textRenderer, Text.literal("No flight or server movement bypasses."), x, y + 58, 0xFF9BA8C7);
        } else if (tab == 2) {
            c.drawTextWithShadow(textRenderer, Text.literal("Combat information only"), x, y, 0xFFFFFFFF);
            c.drawTextWithShadow(textRenderer, Text.literal("Target HUD is available under HUD."), x, y + 22, 0xFF9BA8C7);
            c.drawTextWithShadow(textRenderer, Text.literal("No automated attacks or aim assistance."), x, y + 44, 0xFF9BA8C7);
        } else {
            c.drawTextWithShadow(textRenderer, Text.literal("Saved waypoints: " + Waypoints.all().size()), x, y, 0xFFFFFFFF);
            c.fill(x, y + 20, x + 180, y + 48, 0xFF283453);
            c.drawTextWithShadow(textRenderer, Text.literal("+  ADD CURRENT LOCATION"), x + 10, y + 30, 0xFF65E6D9);
            int py = y + 60;
            for (var p : Waypoints.all()) {
                if (py > top + 225) break;
                c.drawTextWithShadow(textRenderer, Text.literal(p.name() + "  " + p.x() + ", " + p.y() + ", " + p.z()), x, py, 0xFFD4D9E8); py += 17;
            }
        }
        super.render(c, mx, my, delta);
    }
    private void row(DrawContext c, int x, int y, String title, boolean on, String desc) {
        c.fill(x, y, x + 220, y + 40, 0xFF151D31);
        c.drawTextWithShadow(textRenderer, Text.literal(title), x + 10, y + 7, 0xFFFFFFFF);
        c.drawTextWithShadow(textRenderer, Text.literal(desc), x + 10, y + 22, 0xFF9BA8C7);
        c.fill(x + 178, y + 9, x + 210, y + 30, on ? 0xFF176D68 : 0xFF424B60);
        c.drawTextWithShadow(textRenderer, Text.literal(on ? "ON" : "OFF"), x + 185, y + 15, 0xFFFFFFFF);
    }
    @Override public boolean mouseClicked(double mx, double my, int button) {
        int left = width / 2 - 190, top = height / 2 - 125;
        if (button == 0) {
            for (int i = 0; i < TABS.length; i++) { int y = top + 43 + i * 34; if (mx >= left + 12 && mx <= left + 112 && my >= y && my <= y + 25) { tab = i; return true; } }
            int x = left + 132, y = top + 83;
            if (tab == 0 && mx >= x && mx <= x + 220) {
                if (my >= y && my <= y + 40) DonutClient.coordsHud = !DonutClient.coordsHud;
                if (my >= y + 48 && my <= y + 88) DonutClient.pvpInfo = !DonutClient.pvpInfo;
            } else if (tab == 1 && mx >= x && mx <= x + 220 && my >= y && my <= y + 40) DonutClient.sprintAssist = !DonutClient.sprintAssist;
            else if (tab == 3 && mx >= x && mx <= x + 180 && my >= y + 20 && my <= y + 48) Waypoints.addCurrent();
        }
        return super.mouseClicked(mx, my, button);
    }
    @Override public boolean shouldPause() { return false; }
}
