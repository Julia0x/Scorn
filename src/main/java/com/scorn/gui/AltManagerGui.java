package com.scorn.gui;

import com.scorn.gui.clickgui.imgui.ImGuiImpl;
import com.scorn.utils.alt.AltManager;
import com.scorn.utils.mc.ChatUtils;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class AltManagerGui extends Screen {
    private final AltManager altManager;
    private final ImString emailInput = new ImString(256);
    private final ImString passwordInput = new ImString(256);
    private final ImString searchInput = new ImString(256);
    private boolean showConfirmClear = false;

    public AltManagerGui() {
        super(Text.literal("Alt Manager"));
        this.altManager = new AltManager();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ImGuiImpl.render(io -> {
            // Set modern dark theme with purple/violet accents for alt manager
            ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.15f, 0.10f, 0.20f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 0.20f, 0.15f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.WindowBg, 0.08f, 0.08f, 0.10f, 0.95f);
            ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.10f, 0.10f, 0.12f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.PopupBg, 0.08f, 0.08f, 0.10f, 0.98f);
            ImGui.pushStyleColor(ImGuiCol.Border, 0.30f, 0.25f, 0.35f, 0.8f);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.15f, 0.15f, 0.18f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.18f, 0.18f, 0.22f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.20f, 0.20f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Button, 0.25f, 0.20f, 0.35f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.30f, 0.25f, 0.40f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.35f, 0.30f, 0.45f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Header, 0.22f, 0.18f, 0.32f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 0.26f, 0.22f, 0.36f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.HeaderActive, 0.30f, 0.26f, 0.40f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Text, 0.95f, 0.95f, 0.97f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.TextDisabled, 0.60f, 0.60f, 0.65f, 1.0f);
            
            // Enhanced styling
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 12.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ChildRounding, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.PopupRounding, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ScrollbarRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.GrabRounding, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 10.0f, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 20.0f, 20.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 8.0f, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemInnerSpacing, 8.0f, 6.0f);

            renderAltManagerWindow();

            // Pop all style modifications
            ImGui.popStyleColor(17);
            ImGui.popStyleVar(10);
        });
    }

    private void renderAltManagerWindow() {
        int windowFlags = ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize;
        
        // Center the window
        ImGui.setNextWindowPos(
            ImGui.getMainViewport().getCenterX() - 350,
            ImGui.getMainViewport().getCenterY() - 300,
            imgui.flag.ImGuiCond.Appearing
        );
        ImGui.setNextWindowSize(700, 600, imgui.flag.ImGuiCond.Appearing);

        if (ImGui.begin("Alt Manager", windowFlags)) {
            // Elegant header
            ImGui.pushStyleColor(ImGuiCol.Text, 0.8f, 0.6f, 1.0f, 1.0f);
            ImGui.setWindowFontScale(1.3f);
            ImGui.textColored(0.8f, 0.6f, 1.0f, 1.0f, "Account Manager");
            ImGui.setWindowFontScale(1.0f);
            ImGui.popStyleColor();
            
            ImGui.sameLine();
            ImGui.setCursorPosX(ImGui.getWindowWidth() - 200);
            ImGui.textColored(0.7f, 0.9f, 0.7f, 1.0f, "(" + altManager.getAltCount() + " accounts)");
            
            ImGui.spacing();
            
            // Current account info
            ImGui.textColored(0.9f, 0.8f, 0.6f, 1.0f, "Current: " + altManager.getCurrentUsername());
            
            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            // Add account section
            ImGui.pushStyleColor(ImGuiCol.Text, 0.6f, 0.9f, 0.6f, 1.0f);
            ImGui.text("Add New Account");
            ImGui.popStyleColor();
            ImGui.spacing();
            
            ImGui.text("Email/Username:");
            ImGui.setNextItemWidth(300);
            ImGui.inputTextWithHint("##Email", "Enter email or username...", emailInput, ImGuiInputTextFlags.None);
            
            ImGui.sameLine();
            ImGui.setCursorPosX(350);
            ImGui.text("Password:");
            ImGui.sameLine();
            ImGui.setNextItemWidth(200);
            ImGui.inputTextWithHint("##Password", "Leave empty for cracked", passwordInput, ImGuiInputTextFlags.Password);
            
            ImGui.sameLine();
            ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.7f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.8f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.4f, 0.9f, 0.4f, 1.0f);
            if (ImGui.button("Add Account", 120, 26) && !emailInput.get().trim().isEmpty()) {
                addAccount(emailInput.get().trim(), passwordInput.get().trim());
                emailInput.set("");
                passwordInput.set("");
            }
            ImGui.popStyleColor(3);

            ImGui.spacing();
            ImGui.spacing();

            // Accounts list section
            ImGui.pushStyleColor(ImGuiCol.Text, 0.8f, 0.6f, 1.0f, 1.0f);
            ImGui.text("Accounts List");
            ImGui.popStyleColor();
            
            ImGui.sameLine();
            ImGui.setCursorPosX(ImGui.getWindowWidth() - 140);
            if (altManager.getAltCount() > 0) {
                ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.2f, 0.2f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.3f, 0.3f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.4f, 0.4f, 1.0f);
                if (ImGui.button("Clear All", 120, 26)) {
                    showConfirmClear = true;
                }
                ImGui.popStyleColor(3);
            }
            
            ImGui.spacing();
            
            // Search box
            ImGui.setNextItemWidth(400);
            ImGui.inputTextWithHint("##Search", "Search accounts...", searchInput, ImGuiInputTextFlags.None);

            ImGui.spacing();

            // Accounts list
            renderAccountsList();

            ImGui.spacing();
            ImGui.separator();
            ImGui.spacing();

            // Footer buttons
            ImGui.setCursorPosX((ImGui.getWindowWidth() - 120) / 2);
            ImGui.pushStyleColor(ImGuiCol.Button, 0.5f, 0.5f, 0.6f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.6f, 0.6f, 0.7f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.7f, 0.8f, 1.0f);
            if (ImGui.button("Close", 120, 32)) {
                close();
            }
            ImGui.popStyleColor(3);

            // Confirmation dialogs
            renderConfirmationDialogs();
        }
        ImGui.end();
    }

    private void renderAccountsList() {
        if (altManager.getAltCount() == 0) {
            // Enhanced empty state
            ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.12f, 0.12f, 0.15f, 0.8f);
            ImGui.beginChild("EmptyState", 0, 300, true);
            
            ImGui.setCursorPosY(ImGui.getCursorPosY() + 120);
            ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("No accounts added yet").x) / 2);
            ImGui.textColored(0.6f, 0.6f, 0.7f, 1.0f, "No accounts added yet");
            
            ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("Add accounts using the form above").x) / 2);
            ImGui.textColored(0.5f, 0.5f, 0.6f, 1.0f, "Add accounts using the form above");
            
            ImGui.endChild();
            ImGui.popStyleColor();
            return;
        }

        String searchTerm = searchInput.get().toLowerCase();
        List<AltManager.Alt> alts = altManager.getAlts();
        
        // Enhanced scrollable region for accounts list
        ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.10f, 0.10f, 0.12f, 1.0f);
        ImGui.beginChild("AccountsList", 0, 300, true);
        
        int index = 1;
        for (AltManager.Alt alt : alts) {
            if (searchTerm.isEmpty() || alt.getEmail().toLowerCase().contains(searchTerm)) {
                ImGui.pushID(alt.getEmail());
                
                // Alternating row colors for better readability
                if (index % 2 == 0) {
                    ImGui.pushStyleColor(ImGuiCol.ChildBg, 0.12f, 0.12f, 0.15f, 0.5f);
                    ImGui.beginChild("AccountRow", 0, 35, false);
                } else {
                    ImGui.beginChild("AccountRow", 0, 35, false);
                }
                
                // Account info with better styling
                ImGui.setCursorPosY(ImGui.getCursorPosY() + 6);
                ImGui.pushStyleColor(ImGuiCol.Text, 0.7f, 0.6f, 0.9f, 1.0f);
                ImGui.text(index + ". " + alt.getEmail());
                ImGui.popStyleColor();
                
                ImGui.sameLine();
                ImGui.setCursorPosX(300);
                if (alt.isCracked()) {
                    ImGui.textColored(0.9f, 0.7f, 0.3f, 1.0f, "Cracked");
                } else {
                    ImGui.textColored(0.3f, 0.9f, 0.3f, 1.0f, "Premium");
                }
                
                // Buttons
                ImGui.sameLine();
                ImGui.setCursorPosY(ImGui.getCursorPosY() - 4);
                ImGui.setCursorPosX(ImGui.getWindowWidth() - 170);
                
                // Login button
                ImGui.pushStyleColor(ImGuiCol.Button, 0.2f, 0.6f, 0.8f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.3f, 0.7f, 0.9f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.4f, 0.8f, 1.0f, 1.0f);
                if (ImGui.button("Login", 75, 24)) {
                    altManager.loginWithAlt(alt);
                }
                ImGui.popStyleColor(3);
                
                ImGui.sameLine();
                
                // Remove button
                ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.3f, 0.3f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.4f, 0.4f, 1.0f);
                ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.5f, 0.5f, 1.0f);
                if (ImGui.button("Remove", 75, 24)) {
                    altManager.removeAlt(alt);
                    ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "Removed account " + 
                        Formatting.RED + alt.getEmail());
                }
                ImGui.popStyleColor(3);
                
                ImGui.endChild();
                if (index % 2 == 0) {
                    ImGui.popStyleColor();
                }
                
                ImGui.popID();
                index++;
            }
        }
        
        ImGui.endChild();
        ImGui.popStyleColor();
    }

    private void renderConfirmationDialogs() {
        // Clear all confirmation dialog
        if (showConfirmClear) {
            ImGui.openPopup("Clear All Accounts?");
        }
        
        if (ImGui.beginPopupModal("Clear All Accounts?", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.pushStyleColor(ImGuiCol.Text, 1.0f, 0.8f, 0.4f, 1.0f);
            ImGui.text("Are you sure you want to remove all " + altManager.getAltCount() + " accounts?");
            ImGui.popStyleColor();
            ImGui.textColored(0.9f, 0.5f, 0.5f, 1.0f, "This action cannot be undone!");
            ImGui.spacing();
            ImGui.spacing();
            
            // Centered buttons
            float buttonWidth = 120;
            float spacing = 20;
            float totalWidth = (buttonWidth * 2) + spacing;
            ImGui.setCursorPosX((ImGui.getWindowWidth() - totalWidth) / 2);
            
            ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.2f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.9f, 0.3f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.4f, 0.4f, 1.0f);
            if (ImGui.button("Yes, Clear All", buttonWidth, 35)) {
                altManager.clearAlts();
                ChatUtils.addMessageToChat(Formatting.RED + "[S] " + Formatting.WHITE + "All accounts have been removed!");
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor(3);
            
            ImGui.sameLine();
            ImGui.pushStyleColor(ImGuiCol.Button, 0.5f, 0.5f, 0.6f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.6f, 0.6f, 0.7f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.7f, 0.8f, 1.0f);
            if (ImGui.button("Cancel", buttonWidth, 35)) {
                showConfirmClear = false;
                ImGui.closeCurrentPopup();
            }
            ImGui.popStyleColor(3);
            
            ImGui.endPopup();
        }
    }

    private void addAccount(String email, String password) {
        altManager.addAlt(email, password);
        String accountType = password.isEmpty() ? "cracked" : "premium";
        ChatUtils.addMessageToChat(Formatting.GREEN + "[S] " + Formatting.WHITE + "Added " + accountType + " account " + 
            Formatting.GREEN + email);
        ChatUtils.addMessageToChat(Formatting.GRAY + "[S] " + Formatting.WHITE + "Accounts count: " + 
            Formatting.AQUA + altManager.getAltCount());
    }

    @Override
    public boolean shouldPause() {
        return true; // Pause the game when alt manager is open
    }
}