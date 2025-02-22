package com.hypherionmc.craterlib.client.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/** Copied from <a href="https://github.com/SleepyTrousers/EnderIO-Rewrite/blob/dev/1.18.x/enderio-machines/src/main/java/com/enderio/machines/client/FluidStackWidget.java">...</a>*/
public class FluidStackWidget extends AbstractWidget {

    private final Screen displayOn;
    private final Supplier<FluidTank> getFluid;

    private final String toolTipTitle;

    public FluidStackWidget(Screen displayOn, Supplier<FluidTank> getFluid, int pX, int pY, int pWidth, int pHeight, String toolTipTitle) {
        super(pX, pY, pWidth, pHeight, Component.empty());
        this.displayOn = displayOn;
        this.getFluid = getFluid;
        this.toolTipTitle = toolTipTitle;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics pPoseStack, int pMouseX, int pMouseY, float pPartialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        FluidTank fluidTank = getFluid.get();
        if (!fluidTank.isEmpty()) {
            FluidStack fluidStack = fluidTank.getFluid();
            IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fluidStack.getFluid());
            ResourceLocation still = props.getStillTexture(fluidStack);
            if (still != null) {
                AbstractTexture texture = minecraft.getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS);
                if (texture instanceof TextureAtlas atlas) {
                    TextureAtlasSprite sprite = atlas.getSprite(still);
                    RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);

                    int color = props.getTintColor();
                    RenderSystem.setShaderColor(
                            FastColor.ARGB32.red(color) / 255.0F,
                            FastColor.ARGB32.green(color) / 255.0F,
                            FastColor.ARGB32.blue(color) / 255.0F,
                            FastColor.ARGB32.alpha(color) / 255.0F);
                    RenderSystem.enableBlend();

                    int stored = fluidTank.getFluidAmount();
                    float capacity = fluidTank.getCapacity();
                    float filledVolume = stored / capacity;
                    int renderableHeight = (int)(filledVolume * height);

                    int atlasWidth = (int)(sprite.getY() / (sprite.getU1() - sprite.getU0()));
                    int atlasHeight = (int)(sprite.getY() / (sprite.getV1() - sprite.getV0()));

                    pPoseStack.pose().pushPose();
                    pPoseStack.pose().translate(0, height-16, 0);
                    for (int i = 0; i < Math.ceil(renderableHeight / 16f); i++) {
                        int drawingHeight = Math.min(16, renderableHeight - 16*i);
                        int notDrawingHeight = 16 - drawingHeight;
                        // TODO Double Check this
                        pPoseStack.blit(TextureAtlas.LOCATION_BLOCKS, getX(), getY() + notDrawingHeight, 0, sprite.getU0()*atlasWidth, sprite.getV0()*atlasHeight + notDrawingHeight, this.width, drawingHeight, atlasWidth, atlasHeight);
                        pPoseStack.pose().translate(0,-16, 0);
                    }

                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    pPoseStack.pose().popPose();
                }
            }
            //renderToolTip(pPoseStack, pMouseX, pMouseY);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput p_259858_) {

    }

    // TODO Fix Tooltips
    /*@Override
    public void renderToolTip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        if (isActive() && isHovered) {
            displayOn.renderTooltip(pPoseStack, Arrays.asList(LangUtils.getTooltipTitle(toolTipTitle).getVisualOrderText(), Component.literal((int) (((float)this.getFluid.get().getFluidAmount() / this.getFluid.get().getCapacity()) * 100) + "%").getVisualOrderText()), pMouseX, pMouseY);
        }
    }*/
}
