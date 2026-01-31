package net.wili.wilispikmins.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.wili.wilispikmins.WilisPikmins;
import net.wili.wilispikmins.entity.custom.enums.PikminType;
import net.wili.wilispikmins.network.*;
import org.jetbrains.annotations.NotNull;

public class OnionScreen extends AbstractContainerScreen<OnionMenu> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(WilisPikmins.MOD_ID, "textures/gui/onion_gui.png");

    private static final int GUI_X = 0;
    private static final int GUI_Y = 0;
    private static final int GUI_WIDTH = 176;
    private static final int GUI_HEIGHT = 192;

    private static final int ARROW_WIDTH = 11;
    private static final int ARROW_HEIGHT = 7;

    private static final int ONION_ICON_X = 177;
    private static final int ONION_ICON_FIRST_Y = 17;
    private static final int ONION_ICON_WIDTH = 18;
    private static final int ONION_ICON_HEIGHT = 19;
    private static final int ONION_ICON_SPACING = 20;

    private Button confirmButton;
    private Button upgradeButton;
    private Button clearButton;
    private Button recallButton;

    private int totalToSpawn = 0;
    private static final int MAX_OUTSIDE = 100;

   private static final int COLUMNS = 7;
   private static final int COLUMN_WIDTH = 25;
   private static final int ROW_HEIGHT = 40;

    private static final int FIRST_COL_X = 1;
    private static final int FIRST_ROW_Y = 18;
    private static final int TOTAL_Y = 87;
    private static final int BUTTON_Y = 86;

    private static final int UPGRADE_BUTTON_X = 132;


    public OnionScreen(OnionMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = GUI_WIDTH;
        this.imageHeight = GUI_HEIGHT;
        this.titleLabelY = 6;
        this.inventoryLabelY = 10000;
    }

    @Override
    protected void init() {
        super.init();

        int centerX = leftPos + imageWidth / 2;

        this.upgradeButton = Button.builder(
                        Component.literal("↑"),
                        b -> upgradeCapacity()
                ).bounds(leftPos + UPGRADE_BUTTON_X, topPos + BUTTON_Y, 18, 18)
                .build();
        upgradeButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                Component.translatable("tooltip.wilispikmins.onion.upgrade")
        ));

        this.confirmButton = Button.builder(
                Component.literal("✓"),
                b -> confirmOperations()
        ).bounds(centerX - 56 / 2, topPos + BUTTON_Y, 18, 18).build();
        confirmButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                Component.translatable("tooltip.wilispikmins.onion.confirm")
        ));

        this.clearButton = Button.builder(
                Component.literal("✗"),
                b -> clearOperations()
        ).bounds(centerX + 9, topPos + BUTTON_Y, 18, 18).build();
        clearButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                Component.translatable("tooltip.wilispikmins.onion.cancel")
        ));

        this.recallButton = Button.builder(
                Component.literal("↓"),
                b -> recallPikmin()
        ).bounds(leftPos + 8, topPos + BUTTON_Y, 18, 18).build();
        recallButton.setTooltip(net.minecraft.client.gui.components.Tooltip.create(
                Component.translatable("tooltip.wilispikmins.onion.recall")
        ));

        addRenderableWidget(confirmButton);
        addRenderableWidget(upgradeButton);
        addRenderableWidget(clearButton);
        addRenderableWidget(recallButton);

        initPikminButtons();
        updateButtons();
    }

    private void initPikminButtons() {
        for (PikminType type : PikminType.values()) {

            int index = type.ordinal();
            int col = index % COLUMNS;
            int row = index / COLUMNS;

            int x = leftPos + FIRST_COL_X + (col * COLUMN_WIDTH);
            int y = topPos + FIRST_ROW_Y + (row * ROW_HEIGHT);

            final PikminType finalType = type;

            addRenderableWidget(Button.builder(
                    Component.literal("▲"),
                    b -> transferPikmin(finalType, false)
            ).bounds(x + 7, y + 30, ARROW_WIDTH, ARROW_HEIGHT).tooltip(net.minecraft.client.gui.components.Tooltip.create(
                    Component.translatable("tooltip.wilispikmins.onion.put_in", finalType.getDisplayName())
            )).build());

            addRenderableWidget(Button.builder(
                    Component.literal("▼"),
                    b -> transferPikmin(finalType, true)
            ).bounds(x + 7, y + 40, ARROW_WIDTH, ARROW_HEIGHT).tooltip(net.minecraft.client.gui.components.Tooltip.create(
                    Component.translatable("tooltip.wilispikmins.onion.take_out", finalType.getDisplayName())
            )).build());
        }
    }

    private void transferPikmin(PikminType type, boolean takeOut) {
        OnionAdjustPacket packet = new OnionAdjustPacket(type, takeOut);
        PacketDistributor.sendToServer(packet);

        updateButtons();
        updateTotal();
    }

    private void confirmOperations() {
            OnionConfirmPacket packet = new OnionConfirmPacket();
            PacketDistributor.sendToServer(packet);

            updateButtons();
            updateTotal();
    }

    private void clearOperations() {
        menu.clearOperations();
        updateButtons();
        updateTotal();
    }

    private void upgradeCapacity() {
        if (menu.hasUpgradeItem()) {
            OnionUpgradePacket packet = new OnionUpgradePacket();
            PacketDistributor.sendToServer(packet);

            updateButtons();
        }
    }

    private void recallPikmin() {
        RecallPikminPacket packet = new RecallPikminPacket();
        PacketDistributor.sendToServer(packet);

        updateTotal();
    }

    private void updateButtons() {
        boolean hasOperations = false;

        for (PikminType type : PikminType.values()) {
            if (menu.getPendingTakeOut(type) > 0 || menu.getPendingPutIn(type) > 0) {
                hasOperations = true;
                break;
            }
        }

        confirmButton.active = hasOperations;
        clearButton.active = hasOperations;
        upgradeButton.active = menu.hasUpgradeItem();

        boolean hasOutside = false;
        for(PikminType type : PikminType.values()) {
            if (menu.getOutside(type) > 0) {
                hasOutside = true;
                break;
            }
        }
        recallButton.active = hasOutside;
    }

    private void updateTotal() {
        totalToSpawn = 0;
        for (PikminType type : PikminType.values()) {
            totalToSpawn += menu.getPendingTakeOut(type);
        }
    }



    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(font, this.title,
                this.titleLabelX, this.titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        guiGraphics.blit(TEXTURE, leftPos, topPos, GUI_X, GUI_Y, GUI_WIDTH, GUI_HEIGHT);

        renderPikminInfo(guiGraphics);

        renderTotalInfo(guiGraphics);
    }

    private void renderPikminInfo(GuiGraphics guiGraphics) {
        for (PikminType type : PikminType.values()) {
            if (!menu.isUnlocked(type)) continue;

            int index = type.ordinal();

            int col = index % COLUMNS;
            int row = index / COLUMNS;

            int baseX = leftPos + FIRST_COL_X + (col * COLUMN_WIDTH);
            int baseY = topPos + FIRST_ROW_Y + (row * ROW_HEIGHT);

            int iconTextureY = ONION_ICON_FIRST_Y + (index * ONION_ICON_SPACING);

            int iconX = baseX + (COLUMN_WIDTH - ONION_ICON_WIDTH) / 2;
            guiGraphics.blit(TEXTURE,
                    iconX, baseY,
                    ONION_ICON_X, iconTextureY,
                    ONION_ICON_WIDTH, ONION_ICON_HEIGHT);

            int stored = menu.getStored(type);
            int capacity = menu.getCapacity(type);
            int outside = menu.getOutside(type);

            int pendingOut = menu.getPendingTakeOut(type);
            int pendingIn = menu.getPendingPutIn(type);

            int futureStored = stored - pendingOut + pendingIn;
            int futureOutside = outside + pendingOut - pendingIn;

            // stored amount
            String storedText = String.valueOf(futureStored);
            int storedWidth = font.width(storedText);
            guiGraphics.drawString(font, storedText,
                    baseX + (COLUMN_WIDTH - storedWidth) / 2, baseY + 20,
                    getStoredColor(stored, capacity), false);


            String futureText = String.valueOf(futureOutside);
            int futureWidth = font.width(futureText);
            int futureX = baseX + (COLUMN_WIDTH - futureWidth) / 2;
            int futureY = baseY + 50;

            guiGraphics.drawString(font, futureText,
                    futureX, futureY,
                    getFutureOutsideColor(futureOutside, pendingOut), false);

        }
    }

    private void renderTotalInfo(GuiGraphics guiGraphics) {
        int centerX = leftPos + imageWidth / 2;
        int totalY = topPos + TOTAL_Y;

        int futureTotal = 0;

        for (PikminType type : PikminType.values()) {
            futureTotal += menu.getOutside(type)
                    + menu.getPendingTakeOut(type)
                    - menu.getPendingPutIn(type);
        }

        Component totalText = Component.translatable("gui.wilispikmins.onion.total", futureTotal, MAX_OUTSIDE);
        int textWidth = font.width(totalText);
        int totalColor = getTotalColor(futureTotal);

        guiGraphics.drawString(font, totalText,
                centerX - textWidth / 2, totalY - 10, totalColor, false);
    }

    private int getStoredColor(int stored, int capacity) {
        if (stored >= capacity) return 0xFF5555;
        if (stored == 0) return 0x808080;
        return 0x404040;
    }

    private int getFutureOutsideColor(int futureOutside, int pending) {
        if (pending > 0) {
            return 0x55FF55;
        } else if (pending < 0) {
            return 0xFFAA00;
        }
        return 0x808080;
    }

    private int getTotalColor(int total) {
        if (total > MAX_OUTSIDE) return 0xFF5555;
        if (total > 0) return 0x404040;
        return 0x808080;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        renderBackground(pGuiGraphics, pMouseX, pMouseY, delta);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        renderTooltips(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        PikminType[] types = PikminType.values();

        for (int i = 0; i < types.length; i++) {
            PikminType type = types[i];
            if (!menu.isUnlocked(type)) continue;

            int col = i % COLUMNS;
            int row = i / COLUMNS;

            int x = leftPos + FIRST_COL_X + (col * COLUMN_WIDTH);
            int y = topPos + FIRST_ROW_Y + (row * ROW_HEIGHT);

            if (isMouseOverArrow(mouseX, mouseY, x, y)) {
                int stored = menu.getStored(type);
                int capacity = menu.getCapacity(type);
                int pending = menu.getPendingPutIn(type);

                int toStore = Math.max(0, -pending);

                Component tooltip = Component.translatable("tooltip.wilispikmins.onion.stored",
                                stored + toStore,
                                capacity
                );

                guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY);
            }
        }
    }

    private  boolean isMouseOverArrow(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + OnionScreen.COLUMN_WIDTH && mouseY >= y && mouseY < y + 20;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        updateButtons();
        updateTotal();
    }
}
