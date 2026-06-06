package com.betterfire.betterfirehud.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

public class BetterFireConfig {

    public enum OverlayMode { ALWAYS, FIRE_RESISTANCE, CREATIVE }
    public enum FireType    { BOTH, FIRE, LAVA }

    // ── Fields (serialised by Gson) ──────────────────────────────────────────
    private boolean     enabled     = true;
    private OverlayMode overlayMode = OverlayMode.FIRE_RESISTANCE;
    private FireType    fireType    = FireType.BOTH;
    /** Packed ARGB int for the custom fire tint (used when mod is OFF). */
    private int         fireColor   = 0xFFFF6600; // default orange

    // ── Singleton ────────────────────────────────────────────────────────────
    private static BetterFireConfig INSTANCE = new BetterFireConfig();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("betterfire.json");
    }

    public static BetterFireConfig getInstance() { return INSTANCE; }

    // ── Persistence ──────────────────────────────────────────────────────────
    public static void load() {
        File f = configPath().toFile();
        if (f.exists()) {
            try (Reader r = new FileReader(f)) {
                INSTANCE = GSON.fromJson(r, BetterFireConfig.class);
                if (INSTANCE == null) INSTANCE = new BetterFireConfig();
            } catch (IOException e) {
                INSTANCE = new BetterFireConfig();
            }
        }
    }

    public static void save() {
        try (Writer w = new FileWriter(configPath().toFile())) {
            GSON.toJson(INSTANCE, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ── Getters / Setters ────────────────────────────────────────────────────
    public boolean     isEnabled()     { return enabled; }
    public OverlayMode getOverlayMode(){ return overlayMode; }
    public FireType    getFireType()   { return fireType; }
    public int         getFireColor()  { return fireColor; }

    public void setEnabled(boolean v)          { enabled     = v; }
    public void setOverlayMode(OverlayMode v)  { overlayMode = v; }
    public void setFireType(FireType v)        { fireType    = v; }
    public void setFireColor(int v)            { fireColor   = v; }
}
