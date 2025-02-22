package com.hypherionmc.craterlib.client;

import com.hypherionmc.craterlib.api.rendering.CustomRenderType;
import com.hypherionmc.craterlib.common.item.BlockItemDyable;
import com.hypherionmc.craterlib.core.platform.ClientPlatform;
import com.hypherionmc.craterlib.core.systems.reg.RegistryObject;
import com.hypherionmc.craterlib.util.ColorPropertyFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Collection;
import java.util.Objects;

/**
 * @author HypherionSA
 * @date 16/06/2022
 */
public class ForgeClientHelper implements ClientPlatform {

    public ForgeClientHelper() {}

    @Override
    public void registerItemProperty(BlockItemDyable item, String property) {
        if (FMLEnvironment.dist.isClient()) {
            ItemProperties.register(item, new ResourceLocation(property), new ColorPropertyFunction(item));
        }
    }

    @Override
    public void registerCustomRenderTypes(Collection<RegistryObject<Block>> blocks) {
        blocks.forEach(blk -> {
            if (blk.get() instanceof CustomRenderType type) {
                ItemBlockRenderTypes.setRenderLayer(blk.get(), type.getCustomRenderType());
            }
        });
    }

    @Override
    public Minecraft getClientInstance() {
        return Minecraft.getInstance();
    }

    @Override
    public Player getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public Level getClientLevel() {
        return Minecraft.getInstance().level;
    }

    @Override
    public Connection getClientConnection() {
        Objects.requireNonNull(Minecraft.getInstance().getConnection(), "Cannot send packets when not in game!");
        return Minecraft.getInstance().getConnection().getConnection();
    }

    @Override
    public void registerBlockEntityRenderer(BlockEntityType<? extends BlockEntity> blockEntityType, BlockEntityRendererProvider blockEntityRendererFactory) {
        BlockEntityRenderers.register(blockEntityType, blockEntityRendererFactory);
    }
}
