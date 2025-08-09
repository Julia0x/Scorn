package com.scorn.gui;

import com.scorn.gui.clickgui.imgui.ImGuiImpl;
import com.scorn.utils.render.AnimationUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

public class CustomTitleScreen extends Screen {
    private float animationTime = 0.0f;
    private float logoScale = 0.8f;
    private float logoAlpha = 0.0f;
    private boolean animationsInitialized = false;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    // Particle system for background
    private static final int PARTICLE_COUNT = 100;
    private final Particle[] particles = new Particle[PARTICLE_COUNT];
    
    public CustomTitleScreen() {
        super(Text.literal("Scorn Client"));
        initializeParticles();
    }

    @Override
    protected void init() {
        super.init();
        if (!animationsInitialized) {
            logoAlpha = 0.0f;
            logoScale = 0.5f;
            animationsInitialized = true;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Update animations
        animationTime += delta * 0.05f;
        updateAnimations(delta);
        updateParticles(delta);

        // Render animated background
        renderAnimatedBackground(context);
        
        // Render particles
        renderParticles(context);

        // Render custom ImGui title screen
        ImGuiImpl.render(io -> {
            setupCustomTheme();
            renderMainMenu(mouseX, mouseY);
            ImGui.popStyleColor(20);
            ImGui.popStyleVar(12);
        });
    }

    private void setupCustomTheme() {
        // Dark cyberpunk theme with purple/blue accents
        ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.05f, 0.05f, 0.08f, 0.95f);
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.08f, 0.08f, 0.12f, 0.9f);
        ImGui.pushStyleColor(ImGuiCol.PopupBg, 0.06f, 0.06f, 0.10f, 0.98f);
        ImGui.pushStyleColor(ImGuiCol.Border, 0.4f, 0.3f, 0.8f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.BorderShadow, 0.0f, 0.0f, 0.0f, 0.0f);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.12f, 0.12f, 0.18f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.18f, 0.18f, 0.25f, 0.9f);
        ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.20f, 0.20f, 0.28f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.15f, 0.10f, 0.25f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 0.20f, 0.15f, 0.30f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.MenuBarBg, 0.12f, 0.12f, 0.18f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarBg, 0.08f, 0.08f, 0.12f, 0.6f);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrab, 0.3f, 0.25f, 0.6f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabHovered, 0.35f, 0.30f, 0.65f, 0.9f);
        ImGui.pushStyleColor(ImGuiCol.ScrollbarGrabActive, 0.4f, 0.35f, 0.7f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.CheckMark, 0.6f, 0.4f, 1.0f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.SliderGrab, 0.5f, 0.3f, 0.8f, 0.9f);
        ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, 0.6f, 0.4f, 0.9f, 1.0f);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.15f, 0.35f, 0.8f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.25f, 0.20f, 0.40f, 0.9f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.30f, 0.25f, 0.45f, 1.0f);

        // Enhanced styling variables
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 15.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 10.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 8.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, 10.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 8.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, 8.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 25.0f, 25.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 12.0f, 8.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 15.0f, 12.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, 10.0f, 8.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.IndentSpacing, 25.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarSize, 16.0f);
    }

    private void renderMainMenu(int mouseX, int mouseY) {
        int windowFlags = ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | 
                         ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoScrollbar |
                         ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoBringToFrontOnFocus;

        // Main menu window - centered
        ImGui.setNextWindowPos(
            (ImGui.getMainViewport().getSizeX() - 500) / 2,
            (ImGui.getMainViewport().getSizeY() - 600) / 2,
            imgui.flag.ImGuiCond.Always
        );
        ImGui.setNextWindowSize(500, 600, imgui.flag.ImGuiCond.Always);

        if (ImGui.begin("ScornMainMenu", windowFlags)) {
            renderLogo();
            renderMenuButtons();
            renderFooter();
        }
        ImGui.end();
    }

    private void renderLogo() {
        ImGui.spacing();
        ImGui.spacing();
        
        // Animated logo with glow effect
        float glowIntensity = (float) (0.8f + 0.3f * Math.sin(animationTime * 2.0f));
        ImGui.pushStyleColor(ImGuiCol.Text, 0.8f * glowIntensity, 0.6f * glowIntensity, 1.0f * glowIntensity, logoAlpha);
        
        // Center the logo text
        String logoText = "SCORN CLIENT";
        float textWidth = ImGui.calcTextSize(logoText).x;
        ImGui.setCursorPosX((ImGui.getWindowWidth() - textWidth) / 2);
        
        // Display logo without font scaling (ImGui limitation)
        ImGui.text(logoText);
        ImGui.popStyleColor();
        
        // Subtitle with animation
        ImGui.spacing();
        String subtitle = "Advanced Combat Client";
        float subtitleWidth = ImGui.calcTextSize(subtitle).x;
        ImGui.setCursorPosX((ImGui.getWindowWidth() - subtitleWidth) / 2);
        ImGui.textColored(0.7f, 0.7f, 0.9f, logoAlpha * 0.8f, subtitle);
        
        ImGui.spacing();
        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();
        ImGui.spacing();
    }

    private void renderMenuButtons() {
        float buttonWidth = 350;
        float buttonHeight = 45;
        float centerX = (ImGui.getWindowWidth() - buttonWidth) / 2;
        
        // Single Player button
        ImGui.setCursorPosX(centerX);
        if (renderAnimatedButton("Singleplayer", buttonWidth, buttonHeight, 0.3f, 0.7f, 0.3f)) {
            mc.setScreen(new SelectWorldScreen(this));
        }
        
        ImGui.spacing();
        
        // Multiplayer button
        ImGui.setCursorPosX(centerX);
        if (renderAnimatedButton("Multiplayer", buttonWidth, buttonHeight, 0.3f, 0.5f, 0.8f)) {
            mc.setScreen(new MultiplayerScreen(this));
        }
        
        ImGui.spacing();
        
        // Alt Manager button with special styling
        ImGui.setCursorPosX(centerX);
        if (renderAnimatedButton("Alt Manager", buttonWidth, buttonHeight, 0.8f, 0.3f, 0.8f)) {
            mc.setScreen(new AltManagerGui());
        }
        
        ImGui.spacing();
        
        // Realms button
        ImGui.setCursorPosX(centerX);
        if (renderAnimatedButton("Minecraft Realms", buttonWidth, buttonHeight, 0.9f, 0.7f, 0.2f)) {
            mc.setScreen(new RealmsMainScreen(this));
        }
        
        ImGui.spacing();
        ImGui.spacing();
        
        // Secondary buttons row
        float smallButtonWidth = 160;
        float spacing = 20;
        float startX = (ImGui.getWindowWidth() - (smallButtonWidth * 2 + spacing)) / 2;
        
        ImGui.setCursorPosX(startX);
        if (renderAnimatedButton("Options", smallButtonWidth, 35, 0.5f, 0.5f, 0.7f)) {
            mc.setScreen(new OptionsScreen(this, mc.options));
        }
        
        ImGui.sameLine();
        ImGui.setCursorPosX(startX + smallButtonWidth + spacing);
        if (renderAnimatedButton("Quit Game", smallButtonWidth, 35, 0.8f, 0.3f, 0.3f)) {
            mc.scheduleStop();
        }
    }

    private boolean renderAnimatedButton(String text, float width, float height, float r, float g, float b) {
        boolean hovered = ImGui.isMouseHoveringRect(
            ImGui.getCursorScreenPosX(), 
            ImGui.getCursorScreenPosY(),
            ImGui.getCursorScreenPosX() + width, 
            ImGui.getCursorScreenPosY() + height
        );
        
        float glowEffect = hovered ? (float) (1.0f + 0.2f * Math.sin(animationTime * 8.0f)) : 1.0f;
        float alpha = logoAlpha * (hovered ? 1.0f : 0.9f);
        
        ImGui.pushStyleColor(ImGuiCol.Button, r * glowEffect, g * glowEffect, b * glowEffect, alpha);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, r * 1.2f, g * 1.2f, b * 1.2f, alpha);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, r * 1.4f, g * 1.4f, b * 1.4f, alpha);
        
        boolean clicked = ImGui.button(text, width, height);
        
        ImGui.popStyleColor(3);
        return clicked;
    }

    private void renderFooter() {
        ImGui.spacing();
        ImGui.spacing();
        ImGui.separator();
        ImGui.spacing();
        
        // Version info with animation
        String version = "Scorn Client v1.0 | Minecraft 1.21";
        float versionWidth = ImGui.calcTextSize(version).x;
        ImGui.setCursorPosX((ImGui.getWindowWidth() - versionWidth) / 2);
        
        float versionAlpha = logoAlpha * 0.6f * (0.8f + 0.2f * (float) Math.sin(animationTime * 1.5f));
        ImGui.textColored(0.6f, 0.6f, 0.8f, versionAlpha, version);
    }

    private void renderAnimatedBackground(DrawContext context) {
        int width = this.width;
        int height = this.height;
        
        // Dark gradient background
        context.fillGradient(0, 0, width, height, 
            0xFF0a0a0f, 0xFF0f0f1a);
        
        // Add animated grid pattern
        renderAnimatedGrid(context);
    }

    private void renderAnimatedGrid(DrawContext context) {
        int gridSize = 40;
        float offsetX = (animationTime * 10) % gridSize;
        float offsetY = (animationTime * 8) % gridSize;
        
        int alpha = 20; // Very subtle
        int gridColor = (alpha << 24) | 0x6040ff;
        
        // Vertical lines
        for (int x = (int) -offsetX; x < this.width + gridSize; x += gridSize) {
            context.fill(x, 0, x + 1, this.height, gridColor);
        }
        
        // Horizontal lines
        for (int y = (int) -offsetY; y < this.height + gridSize; y += gridSize) {
            context.fill(0, y, this.width, y + 1, gridColor);
        }
    }

    private void initializeParticles() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles[i] = new Particle();
        }
    }

    private void updateParticles(float delta) {
        for (Particle particle : particles) {
            particle.update(delta, this.width, this.height);
        }
    }

    private void renderParticles(DrawContext context) {
        for (Particle particle : particles) {
            particle.render(context);
        }
    }

    private void updateAnimations(float delta) {
        // Fade in logo
        if (logoAlpha < 1.0f) {
            logoAlpha = Math.min(1.0f, logoAlpha + delta * 0.02f);
        }
        
        // Scale animation for logo
        if (logoScale < 1.0f) {
            logoScale = Math.min(1.0f, logoScale + delta * 0.015f);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // Particle class for animated background
    private static class Particle {
        float x, y;
        float vx, vy;
        float alpha;
        float size;
        
        public Particle() {
            reset();
        }
        
        private void reset() {
            x = (float) (Math.random() * 1920);
            y = (float) (Math.random() * 1080);
            vx = (float) (Math.random() * 0.5 - 0.25);
            vy = (float) (Math.random() * 0.5 - 0.25);
            alpha = (float) (Math.random() * 0.3 + 0.1);
            size = (float) (Math.random() * 2 + 1);
        }
        
        public void update(float delta, int screenWidth, int screenHeight) {
            x += vx * delta * 20;
            y += vy * delta * 20;
            
            if (x < 0 || x > screenWidth || y < 0 || y > screenHeight) {
                reset();
                x = x < 0 ? screenWidth : (x > screenWidth ? 0 : x);
                y = y < 0 ? screenHeight : (y > screenHeight ? 0 : y);
            }
        }
        
        public void render(DrawContext context) {
            int alphaInt = (int) (alpha * 255);
            int color = (alphaInt << 24) | 0x8060ff;
            context.fill((int) x, (int) y, (int) (x + size), (int) (y + size), color);
        }
    }
}