package com.betterfire.betterfirehud.gui;

import com.betterfire.betterfirehud.config.BetterFireConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class BetterFireScreen extends Screen {

    private final Screen parent;
    private BetterFireConfig cfg;

    // Color channel values 0-255
    private int colorR;
    private int colorG;
    private int colorB;

    public BetterFireScreen(Screen parent) {
        super(Text.literal("BetterFire+ Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        cfg = BetterFireConfig.getInstance();

        // Unpack current fire color
        int packed = cfg.getFireColor();
        colorR = (packed >> 16) & 0xFF;
        colorG = (packed >> 8)  & 0xFF;
        colorB =  packed        & 0xFF;

        int centerX = this.width  / 2;
        int startY  = this.height / 2 - 90;
        int btnW    = 200;
        int btnH    = 20;
        int gap     = 24;

        // ── Toggle On/Off ────────────────────────────────────────────────────
        addDrawableChild(ButtonWidget.builder(
                Text.literal("Mod: " + (cfg.isEnabled() ? "§aOn" : "§cOff")),
                btn -> {
                    cfg.setEnabled(!cfg.isEnabled());
                    btn.setMessage(Text.literal("Mod: " + (cfg.isEnabled() ? "§aOn" : "§cOff")));
                })
                .dimensions(centerX - btnW / 2, startY, btnW, btnH)
                .build());

        // ── Overlay Mode ─────────────────────────────────────────────────────
        addDrawableChild(ButtonWidget.builder(
                overlayModeText(),
                btn -> {
                    BetterFireConfig.OverlayMode[] modes = BetterFireConfig.OverlayMode.values();
                    int next = (cfg.getOverlayMode().ordinal() + 1) % modes.length;
                    cfg.setOverlayMode(modes[next]);
                    btn.setMessage(overlayModeText());
                })
                .dimensions(centerX - btnW / 2, startY + gap, btnW, btnH)
                .build());

        // ── Fire Type ────────────────────────────────────────────────────────
        addDrawableChild(ButtonWidget.builder(
                fireTypeText(),
                btn -> {
                    BetterFireConfig.FireType[] types = BetterFireConfig.FireType.values();
                    int next = (cfg.getFireType().ordinal() + 1) % types.length;
                    cfg.setFireType(types[next]);
                    btn.setMessage(fireTypeText());
                })
                .dimensions(centerX - btnW / 2, startY + gap * 2, btnW, btnH)
                .build());

        // ── Color sliders (R, G, B) ──────────────────────────────────────────
        int sliderY = startY + gap * 3 + 10;

        addDrawableChild(new ColorSlider(centerX - btnW / 2, sliderY,      btnW, btnH, "Red",   colorR) {
            @Override protected void onValueChange(int v) { colorR = v; applyColor(); }
        });
        addDrawableChild(new ColorSlider(centerX - btnW / 2, sliderY + gap, btnW, btnH, "Green", colorG) {
            @Override protected void onValueChange(int v) { colorG = v; applyColor(); }
        });
        addDrawableChild(new ColorSlider(centerX - btnW / 2, sliderY + gap * 2, btnW, btnH, "Blue", colorB) {
            @Override protected void onValueChange(int v) { colorB = v; applyColor(); }
        });

        // ── Done ─────────────────────────────────────────────────────────────
        addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, btn -> close())
                .dimensions(centerX - btnW / 2, startY + gap * 7, btnW, btnH)
                .build());
    }

    private void applyColor() {
        int packed = 0xFF000000 | (colorR << 16) | (colorG << 8) | colorB;
        cfg.setFireColor(packed);
    }

    private Text overlayModeText() {
        String label = switch (cfg.getOverlayMode()) {
            case ALWAYS          -> "Always";
            case FIRE_RESISTANCE -> "Fire Resistance";
            case CREATIVE        -> "Creative Mode";
        };
        return Text.literal("Remove Overlay: §e" + label);
    }

    private Text fireTypeText() {
        String label = switch (cfg.getFireType()) {
            case BOTH -> "Both";
            case FIRE -> "Fire";
            case LAVA -> "Lava";
        };
        return Text.literal("Fire Type: §e" + label);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);

        // Title
        ctx.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 110, 0xFFFFFF);

        // Color preview swatch
        int packed = cfg.getFireColor();
        int swatchX = width / 2 + 108;
        int swatchY = height / 2 - 90 + 24 * 3 + 10;
        ctx.fill(swatchX, swatchY, swatchX + 20, swatchY + 20 + 24 * 2, 0xFF000000 | (packed & 0xFFFFFF));
        ctx.drawBorder(swatchX, swatchY, 20, 20 + 24 * 2, 0xFFAAAAAA);

        // Section labels
        ctx.drawTextWithShadow(textRenderer,
                Text.literal("§7Custom Fire Color (when mod is OFF):"),
                width / 2 - 100, height / 2 - 90 + 24 * 3 - 2, 0xFFFFFF);

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        BetterFireConfig.save();
        assert this.client != null;
        this.client.setScreen(parent);
    }

    // ── Inner slider helper ──────────────────────────────────────────────────
    abstract static class ColorSlider extends SliderWidget {
        private final String channelName;

        ColorSlider(int x, int y, int w, int h, String name, int initial) {
            super(x, y, w, h, Text.literal(name + ": " + initial), initial / 255.0);
            this.channelName = name;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            int v = (int) Math.round(value * 255);
            setMessage(Text.literal(channelName + ": " + v));
        }

        @Override
        protected void applyValue() {
            onValueChange((int) Math.round(value * 255));
        }

        protected abstract void onValueChange(int v);
    }
}
