package com.github.barteks2x.worldgenloop;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiFlatPresets;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CustomizedPlanetTypeGUI extends GuiScreen {
	private final GuiCreateWorld createWorldGui;
	private String title;
	private String flatWorldTile;
	private String flatWorldHeight;
	private GuiButton addLayer;
	private GuiButton editLayer;
	private GuiButton field_146386_v;
	private int planetSize = Config.defaultLoopBits();

	private int btnIds = 0;

	private GuiButton SIZE_BTN, DONE_BTN, CANCEL_BTN;

	private String sizeText = "customizedplanet.size";

	public CustomizedPlanetTypeGUI(GuiCreateWorld guiCreateWorld) {
		this.createWorldGui = guiCreateWorld;
	}

	public String createWorldGenSettings() {
		return this.planetSize + "|";
	}

	public void initGui() {
		this.buttonList.clear();
		this.title = I18n.format("createWorld.customize.flat.title");

		this.DONE_BTN = this.addButtonAtLoc(this.width / 2 - 80, this.height - 18, 150, 20, "gui.done");
		this.CANCEL_BTN = this.addButtonAtLoc(this.width / 2 + 80, this.height - 18, 150, 20, "gui.cancel");
		this.SIZE_BTN = this.addButton(50, 50, 200, 20, sizeText);
		// this.buttonList.add(this.editLayer = new GuiButton(3, this.width / 2
		// - 50, this.height - 52, 100, 20, I18n
		// .format("createWorld.customize.flat.editLayer", new Object[0]) +
		// " (NYI)"));
		// this.buttonList.add(this.field_146386_v = new GuiButton(4, this.width
		// / 2 - 155, this.height - 52, 150, 20,
		// I18n.format("createWorld.customize.flat.removeLayer", new
		// Object[0])));
		// this.buttonList.add(new GuiButton(0, this.width / 2 - 155,
		// this.height - 28, 150, 20, I18n.format("gui.done",
		// new Object[0])));
		// this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height
		// - 52, 150, 20, I18n.format(
		// "createWorld.customize.presets", new Object[0])));
		// this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height
		// - 28, 150, 20, I18n.format("gui.cancel",
		// new Object[0])));
		// this.addLayer.visible = this.editLayer.visible = false;
		// this.func_146375_g();
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		// int i = 0;//this.theFlatGeneratorInfo.getFlatLayers().size() -
		// this.createFlatWorldListSlotGui.field_148228_k - 1;

		if (button.id == CANCEL_BTN.id) {
			this.mc.displayGuiScreen(this.createWorldGui);
			return;
		}
		if (button.id == DONE_BTN.id) {
			this.createWorldGui.chunkProviderSettingsJson = this.createWorldGenSettings();
			this.mc.displayGuiScreen(this.createWorldGui);
			return;
		}
		if (button.id == SIZE_BTN.id) {
			this.planetSize++;
			if (planetSize > 26) {
				planetSize = 7;
			}
			this.SIZE_BTN.displayString = I18n.format(sizeText) + ": " + (1 << planetSize);
			return;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, this.title, this.width / 2, 8, 16777215);
		int k = this.width / 2 - 92 - 16;
		this.drawString(this.fontRendererObj, this.flatWorldTile, k, 32, 16777215);
		this.drawString(this.fontRendererObj, this.flatWorldHeight,
				k + 2 + 213 - this.fontRendererObj.getStringWidth(this.flatWorldHeight), 32, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private GuiButton addButton(int xPercent, int yPercent, int sizeX, int sizeY, String unlocalizedText) {
		int xPos = this.width * xPercent / 100 - sizeX / 2;
		int yPos = this.height * yPercent / 100 - sizeY / 2;
		GuiButton btn = new GuiButton(this.btnIds++, xPos, yPos, sizeX, sizeY, I18n.format(unlocalizedText));
		this.buttonList.add(btn);
		return btn;
	}

	private GuiButton addButtonAtLoc(int xLoc, int yLoc, int sizeX, int sizeY, String unlocalizedText) {
		int xPos = xLoc - sizeX / 2;
		int yPos = yLoc - sizeY / 2;
		GuiButton btn = new GuiButton(this.btnIds++, xPos, yPos, sizeX, sizeY, I18n.format(unlocalizedText));
		this.buttonList.add(btn);
		return btn;
	}
}