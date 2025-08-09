package com.scorn.gui.clickgui.imgui;

import com.scorn.Client;
import com.scorn.module.ModuleCategory;
import com.scorn.module.Module;
import com.scorn.module.setting.*;
import com.scorn.module.setting.impl.*;
import com.scorn.module.setting.impl.newmodesetting.NewModeSetting;
import imgui.ImGui;
import imgui.flag.ImGuiInputTextFlags;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.type.ImFloat;
import imgui.type.ImString;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;


public class ClickGui extends Screen {
    private final ImString searchText = new ImString(500);
    private ModuleCategory moduleCategory;
    private Module keyBindingModule = null;
    private Module module;

    public ClickGui() {
        super(Text.empty());
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        ImGuiImpl.render(io -> {
            // Set bright red theme colors
            ImGui.pushStyleColor(ImGuiCol.TitleBg, 0.8f, 0.1f, 0.1f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.TitleBgActive, 1.0f, 0.2f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Header, 0.9f, 0.15f, 0.15f, 0.8f);
            ImGui.pushStyleColor(ImGuiCol.HeaderHovered, 1.0f, 0.25f, 0.25f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.HeaderActive, 1.0f, 0.3f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.Button, 0.8f, 0.1f, 0.1f, 0.7f);
            ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 0.2f, 0.2f, 0.9f);
            ImGui.pushStyleColor(ImGuiCol.ButtonActive, 1.0f, 0.3f, 0.3f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.CheckMark, 1.0f, 0.2f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.SliderGrab, 1.0f, 0.2f, 0.2f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.SliderGrabActive, 1.0f, 0.4f, 0.4f, 1.0f);
            ImGui.pushStyleColor(ImGuiCol.FrameBg, 0.2f, 0.2f, 0.2f, 0.8f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgHovered, 0.3f, 0.3f, 0.3f, 0.9f);
            ImGui.pushStyleColor(ImGuiCol.FrameBgActive, 0.4f, 0.4f, 0.4f, 1.0f);
            
            // Set consistent styling
            ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 8.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.FrameRounding, 4.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 8.0f, 6.0f);
            ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 12.0f, 12.0f);
            
            int windowFlags = ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoScrollbar;

            if (ImGui.begin("Scorn Client", windowFlags)) {
                // Consistent tab bar styling
                if (ImGui.beginTabBar("Categories")) {
                    for (ModuleCategory moduleCategory1 : ModuleCategory.values()) {
                        if (ImGui.beginTabItem(moduleCategory1.name)) {
                            moduleCategory = moduleCategory1;
                            
                            // Add some spacing
                            ImGui.spacing();
                            
                            // Render modules in this category
                            for (Module module : Client.INSTANCE.getModuleManager().getModulesInCategory(moduleCategory)) {
                                renderModule(module);
                            }
                            
                            ImGui.endTabItem();
                        }
                    }
                    ImGui.endTabBar();
                }
            }
            ImGui.end();
            
            // Pop all style modifications
            ImGui.popStyleColor(14);
            ImGui.popStyleVar(4);
        });
    }
    
    private void renderModule(Module module) {
        ImGui.pushID(module.getName());
        
        // Module header with consistent styling
        ImGui.separator();
        ImGui.spacing();
        
        // Module toggle and name on same line
        boolean moduleEnabled = module.isEnabled();
        if (ImGui.checkbox("##ModuleToggle", moduleEnabled)) {
            module.toggle();
        }
        
        ImGui.sameLine();
        
        // Collapsing header for module
        boolean isOpen = ImGui.collapsingHeader(module.getName());
        
        if (isOpen) {
            ImGui.indent(16.0f);
            
            // Module description
            ImGui.textColored(0.7f, 0.7f, 0.7f, 1.0f, module.getDescription());
            
            // Key binding button
            String keyText = getKeyBindingText(module);
            if (ImGui.button(keyBindingModule == module ? "Listening..." : keyText)) {
                keyBindingModule = (keyBindingModule == module) ? null : module;
            }
            
            ImGui.spacing();
            
            // Render settings
            for (Setting property : module.getSettings()) {
                if (shouldSkipSetting(property)) {
                    continue;
                }
                
                renderSetting(property, module);
                ImGui.spacing();
            }
            
            ImGui.unindent(16.0f);
        }
        
        ImGui.popID();
    }
    
    private String getKeyBindingText(Module module) {
        String keyName = GLFW.glfwGetKeyName(module.getKey(), 0);
        return keyName != null ? "Key: " + keyName : "Not Bound";
    }
    
    private boolean shouldSkipSetting(Setting property) {
        // Check boolean dependencies
        if (!property.getDependencyBoolSettings().isEmpty()) {
            for (int i = 0; i < property.getDependencyBoolSettings().size(); i++) {
                Setting dependency = property.getDependencyBoolSettings().get(i);
                boolean expectedValue = property.getDependencyBools().get(i);
                
                if (dependency instanceof BooleanSetting && ((BooleanSetting) dependency).getValue() != expectedValue) {
                    return true;
                }
            }
        }
        
        // Check mode dependencies
        if (!property.getDependencyModeSettings().isEmpty()) {
            for (int i = 0; i < property.getDependencyModeSettings().size(); i++) {
                Setting dependency = property.getDependencyModeSettings().get(i);
                String expectedMode = property.getDependencyModes().get(i);
                
                if (!(dependency instanceof ModeSetting && ((ModeSetting) dependency).isMode(expectedMode))) {
                    return true;
                }
            }
        }
        
        // Check multi-mode dependencies
        if (!property.getDependencyMultiModeSettings().isEmpty()) {
            for (int i = 0; i < property.getDependencyMultiModeSettings().size(); i++) {
                Setting dependency = property.getDependencyMultiModeSettings().get(i);
                String expectedMode = property.getDependencyMultiModes().get(i);
                
                if (!(dependency instanceof MultiModeSetting && ((MultiModeSetting) dependency).isModeSelected(expectedMode))) {
                    return true;
                }
            }
        }
        
        // Check new mode dependencies
        if (!property.getDependencyNewModeSettings().isEmpty()) {
            for (int i = 0; i < property.getDependencyNewModeSettings().size(); i++) {
                Setting dependency = property.getDependencyNewModeSettings().get(i);
                String expectedMode = property.getDependencyNewModes().get(i);
                
                if (!(dependency instanceof NewModeSetting && ((NewModeSetting) dependency).isMode(expectedMode))) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private void renderSetting(Setting property, Module module) {
        if (property instanceof BooleanSetting booleanSetting) {
            if (ImGui.checkbox(property.getName(), booleanSetting.getValue())) {
                booleanSetting.setValue(!booleanSetting.getValue());
            }
        }
        
        else if (property instanceof NumberSetting numberProperty) {
            ImFloat imFloat = new ImFloat((float) numberProperty.getValue());
            
            String sliderId = "##" + numberProperty.getName() + "_" + module.getName();
            if (ImGui.sliderFloat(sliderId, imFloat.getData(), (float) numberProperty.getMin(), (float) numberProperty.getMax())) {
                numberProperty.setValue(imFloat.get());
            }
            
            // Add tooltip with min/max values when hovering
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip(String.format("Min: %.2f | Max: %.2f | Current: %.2f", 
                    numberProperty.getMin(), 
                    numberProperty.getMax(), 
                    numberProperty.getValue()));
            }
            
            ImGui.sameLine();
            ImGui.text(numberProperty.getName());
        }
        
        else if (property instanceof RangeSetting rangeProperty) {
            float[] imFloats = new float[]{(float) rangeProperty.getValueMin(), (float) rangeProperty.getValueMax()};
            
            String sliderId = "##" + rangeProperty.getName() + "_" + module.getName();
            if (ImGui.sliderFloat2(sliderId, imFloats, (float) rangeProperty.getMin(), (float) rangeProperty.getMax())) {
                rangeProperty.setValueMin(imFloats[0]);
                rangeProperty.setValueMax(imFloats[1]);
            }
            
            // Add tooltip with range info when hovering
            if (ImGui.isItemHovered()) {
                ImGui.setTooltip(String.format("Range: %.2f - %.2f | Current: %.2f - %.2f", 
                    rangeProperty.getMin(), 
                    rangeProperty.getMax(),
                    rangeProperty.getValueMin(), 
                    rangeProperty.getValueMax()));
            }
            
            ImGui.sameLine();
            ImGui.text(rangeProperty.getName());
        }
        
        else if (property instanceof StringSetting stringSetting) {
            ImString imString = new ImString(stringSetting.getValue(), 500);
            
            String inputId = "##" + stringSetting.getName() + "_" + module.getName();
            if (ImGui.inputText(inputId, imString, ImGuiInputTextFlags.None)) {
                stringSetting.setValue(imString.get());
            }
            
            ImGui.sameLine();
            ImGui.text(stringSetting.getName());
        }
        
        else if (property instanceof ModeSetting modeProperty) {
            String comboId = "##" + modeProperty.getName() + "_" + module.getName();
            
            if (ImGui.beginCombo(comboId, modeProperty.getMode())) {
                ImGui.inputTextWithHint(comboId + "_search", "Search modes...", searchText, ImGuiInputTextFlags.None);
                String search = searchText.get().toLowerCase();
                
                for (String mode : modeProperty.getModes()) {
                    if (search.isEmpty() || mode.toLowerCase().contains(search)) {
                        if (ImGui.selectable(mode, modeProperty.isMode(mode))) {
                            modeProperty.setMode(mode);
                            searchText.set("");
                        }
                    }
                }
                
                ImGui.endCombo();
            }
            ImGui.sameLine();
            ImGui.text(modeProperty.getName());
        }
        
        else if (property instanceof NewModeSetting newModeProperty) {
            String comboId = "##" + newModeProperty.getName() + "_" + module.getName();
            
            if (ImGui.beginCombo(comboId, newModeProperty.getCurrentMode().getName())) {
                ImGui.inputTextWithHint(comboId + "_search", "Search modes...", searchText, ImGuiInputTextFlags.None);
                String search = searchText.get().toLowerCase();
                
                for (String mode : newModeProperty.getModeNames()) {
                    if (search.isEmpty() || mode.toLowerCase().contains(search)) {
                        boolean isSelected = newModeProperty.isMode(mode);
                        if (ImGui.selectable(mode, isSelected)) {
                            newModeProperty.setMode(mode);
                            searchText.set("");
                        }
                    }
                }
                
                ImGui.endCombo();
            }
            ImGui.sameLine();
            ImGui.text(newModeProperty.getName());
        }
        
        else if (property instanceof MultiModeSetting multiModeProperty) {
            String comboId = "##" + multiModeProperty.getName() + "_" + module.getName();
            
            if (ImGui.beginCombo(comboId, "Select Modes")) {
                ImGui.inputTextWithHint(comboId + "_search", "Search modes...", searchText, ImGuiInputTextFlags.None);
                String search = searchText.get().toLowerCase();
                
                for (String mode : multiModeProperty.getModes()) {
                    if (search.isEmpty() || mode.toLowerCase().contains(search)) {
                        boolean isSelected = multiModeProperty.isModeSelected(mode);
                        ImGui.selectable(mode, isSelected);
                        
                        if (ImGui.isItemClicked(ImGuiMouseButton.Left)) {
                            if (isSelected) {
                                multiModeProperty.deselectMode(mode);
                            } else {
                                multiModeProperty.selectMode(mode);
                            }
                        }
                    }
                }
                
                ImGui.endCombo();
            }
            ImGui.sameLine();
            ImGui.text(multiModeProperty.getName());
        }
    }

    @Override
    public boolean charTyped(char typedChar, int modifiers) {
        int keycode = charToKey(typedChar);

        if (keycode == GLFW.GLFW_KEY_ESCAPE && keyBindingModule == null) {
            MinecraftClient.getInstance().setScreen(null);
        }

        if (keyBindingModule != null) {
            keyBindingModule.setKey(keycode == GLFW.GLFW_KEY_ESCAPE ? 0 : keycode);
            keyBindingModule = null;
        }

        return false;
    }

    private int charToKey(char character) {
        return switch (character) {
            case 'a' -> GLFW.GLFW_KEY_A;
            case 'b' -> GLFW.GLFW_KEY_B;
            case 'c' -> GLFW.GLFW_KEY_C;
            case 'd' -> GLFW.GLFW_KEY_D;
            case 'e' -> GLFW.GLFW_KEY_E;
            case 'f' -> GLFW.GLFW_KEY_F;
            case 'g' -> GLFW.GLFW_KEY_G;
            case 'h' -> GLFW.GLFW_KEY_H;
            case 'i' -> GLFW.GLFW_KEY_I;
            case 'j' -> GLFW.GLFW_KEY_J;
            case 'k' -> GLFW.GLFW_KEY_K;
            case 'l' -> GLFW.GLFW_KEY_L;
            case 'm' -> GLFW.GLFW_KEY_M;
            case 'n' -> GLFW.GLFW_KEY_N;
            case 'o' -> GLFW.GLFW_KEY_O;
            case 'p' -> GLFW.GLFW_KEY_P;
            case 'q' -> GLFW.GLFW_KEY_Q;
            case 'r' -> GLFW.GLFW_KEY_R;
            case 's' -> GLFW.GLFW_KEY_S;
            case 't' -> GLFW.GLFW_KEY_T;
            case 'u' -> GLFW.GLFW_KEY_U;
            case 'v' -> GLFW.GLFW_KEY_V;
            case 'w' -> GLFW.GLFW_KEY_W;
            case 'x' -> GLFW.GLFW_KEY_X;
            case 'y' -> GLFW.GLFW_KEY_Y;
            case 'z' -> GLFW.GLFW_KEY_Z;
            default -> 0;
        };
    }
}