package com.scorn.gui;

import com.scorn.utils.render.AnimationUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsMainScreen;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import java.util.Random;

public class CustomTitleScreen extends Screen {
    private float animationTime = 0.0f;
    private float titleAlpha = 0.0f;
    private boolean animationsInitialized = false;
    private final Random random = new Random();

    // Particle system for background
    private static final int PARTICLE_COUNT = 50;
    private final Particle[] particles = new Particle[PARTICLE_COUNT];
    
    public CustomTitleScreen() {
        super(Text.literal("Scorn Client"));
        initializeParticles();
    }

    @Override
    protected void init() {
        super.init();
        if (!animationsInitialized) {
            titleAlpha = 0.0f;
            animationsInitialized = true;
        }
        
        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;
        
        // Main menu buttons
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Singleplayer"), button -> {
            this.client.setScreen(new SelectWorldScreen(this));
        }).dimensions(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Multiplayer"), button -> {
            this.client.setScreen(new MultiplayerScreen(this));
        }).dimensions(centerX - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Alt Manager"), button -> {
            this.client.setScreen(new AltManagerGui());
        }).dimensions(centerX - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Minecraft Realms"), button -> {
            this.client.setScreen(new RealmsMainScreen(this));
        }).dimensions(centerX - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight).build());
        
        // Secondary buttons
        int smallButtonWidth = 95;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Options"), button -> {
            this.client.setScreen(new OptionsScreen(this, this.client.options));
        }).dimensions(centerX - smallButtonWidth - 5, startY + spacing * 4 + 10, smallButtonWidth, buttonHeight).build());
        
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Quit Game"), button -> {
            this.client.scheduleStop();
        }).dimensions(centerX + 5, startY + spacing * 4 + 10, smallButtonWidth, buttonHeight).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        try {
            // Update animations
            animationTime += delta * 0.02f;
            updateAnimations(delta);
            updateParticles(delta);

            // Render animated background
            renderAnimatedBackground(context);
            
            // Render particles
            renderParticles(context);
            
            // Render title
            renderTitle(context);
            
            // Render buttons (handled by super)
            super.render(context, mouseX, mouseY, delta);
            
            // Render version info
            renderVersionInfo(context);
            
        } catch (Exception e) {
            // Fallback rendering
            this.renderBackground(context, mouseX, mouseY, delta);
            super.render(context, mouseX, mouseY, delta);
        }
    }

    private void renderTitle(DrawContext context) {
        // Animated glow intensity
        float glowIntensity = (float) (0.8f + 0.3f * Math.sin(animationTime * 3.0f));
        
        // Main title
        String title = "SCORN CLIENT";
        int titleWidth = this.textRenderer.getWidth(title);
        int titleX = (this.width - titleWidth) / 2;
        int titleY = this.height / 2 - 120;
        
        // Create glow effect with multiple renders
        int glowColor = (int) (255 * titleAlpha * glowIntensity * 0.3f);
        int mainColor = (int) (255 * titleAlpha);
        
        // Glow layers
        for (int i = 1; i <= 3; i++) {
            int alpha = glowColor / (i * 2);
            int color = (alpha << 24) | (138 << 16) | (43 << 8) | 226; // Purple glow
            context.drawText(this.textRenderer, title, titleX - i, titleY - i, color, false);
            context.drawText(this.textRenderer, title, titleX + i, titleY + i, color, false);
        }
        
        // Main title
        int titleColor = (mainColor << 24) | 0xFFFFFF;
        context.drawText(this.textRenderer, title, titleX, titleY, titleColor, true);
        
        // Subtitle
        String subtitle = "Advanced Combat Client";
        int subtitleWidth = this.textRenderer.getWidth(subtitle);
        int subtitleX = (this.width - subtitleWidth) / 2;
        int subtitleY = titleY + 20;
        int subtitleColor = ((int) (200 * titleAlpha * 0.8f) << 24) | 0xCCCCFF;
        context.drawText(this.textRenderer, subtitle, subtitleX, subtitleY, subtitleColor, false);
    }

    private void renderAnimatedBackground(DrawContext context) {
        // Dark gradient background
        context.fillGradient(0, 0, this.width, this.height, 
            0xFF0a0a0f, 0xFF0f0f1a);
        
        // Add animated grid pattern
        renderAnimatedGrid(context);
    }

    private void renderAnimatedGrid(DrawContext context) {
        int gridSize = 40;
        float offsetX = (animationTime * 20) % gridSize;
        float offsetY = (animationTime * 15) % gridSize;
        
        int alpha = (int) (30 * titleAlpha); // Grid appears with title
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

    private void renderVersionInfo(DrawContext context) {
        String version = "Scorn Client v1.0 | Minecraft 1.21";
        int versionWidth = this.textRenderer.getWidth(version);
        int versionX = (this.width - versionWidth) / 2;
        int versionY = this.height - 20;
        
        float versionAlpha = titleAlpha * 0.6f * (0.8f + 0.2f * (float) Math.sin(animationTime * 2.0f));
        int versionColor = ((int) (255 * versionAlpha) << 24) | 0x9999CC;
        context.drawText(this.textRenderer, version, versionX, versionY, versionColor, false);
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
            particle.render(context, titleAlpha);
        }
    }

    private void updateAnimations(float delta) {
        // Fade in title
        if (titleAlpha < 1.0f) {
            titleAlpha = Math.min(1.0f, titleAlpha + delta * 0.01f);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // Particle class for animated background
    private class Particle {
        float x, y;
        float vx, vy;
        float alpha;
        float size;
        
        public Particle() {
            reset();
        }
        
        private void reset() {
            x = random.nextFloat() * 1920;
            y = random.nextFloat() * 1080;
            vx = (random.nextFloat() - 0.5f) * 0.5f;
            vy = (random.nextFloat() - 0.5f) * 0.5f;
            alpha = random.nextFloat() * 0.3f + 0.1f;
            size = random.nextFloat() * 2 + 1;
        }
        
        public void update(float delta, int screenWidth, int screenHeight) {
            x += vx * delta * 30;
            y += vy * delta * 30;
            
            if (x < 0 || x > screenWidth || y < 0 || y > screenHeight) {
                reset();
                if (x < 0) x = screenWidth;
                if (x > screenWidth) x = 0;
                if (y < 0) y = screenHeight;
                if (y > screenHeight) y = 0;
            }
        }
        
        public void render(DrawContext context, float globalAlpha) {
            int alphaInt = (int) (alpha * 255 * globalAlpha * 0.7f);
            if (alphaInt > 0) {
                int color = (alphaInt << 24) | 0x8060ff;
                context.fill((int) x, (int) y, (int) (x + size), (int) (y + size), color);
            }
        }
    }
}