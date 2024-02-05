package net.kobe.kobemod.item.custom;

import net.kobe.kobemod.util.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import com.ibm.icu.text.AlphabeticIndex;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            BlockPos positionClicked = context.getClickedPos();
            Player player = context.getPlayer();
            boolean foundBlock = false;

            BlockState state = null;
            for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                state = context.getLevel().getBlockState(positionClicked.below(i));

                if (isValuableBlock(state)) {
                    outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                    foundBlock = true;
                    playSound(player);

                    // Add particle effect here
                    context.getLevel().addParticle(ParticleTypes.SMOKE, context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5, 0.0D, 0.0D, 0.0D);
                    context.getLevel().addParticle(ParticleTypes.FLAME, context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5, 0.0D, 0.0D, 0.0D);

                    break;
                }
            }

            if (!foundBlock) {
                String playerName = player.getName().getString();
                String message = "Dear " + playerName + ", unfortunately, our metal detector didn't detect any valuable ores nearby. Keep exploring!";
                player.sendSystemMessage(Component.literal(message));
            } else if (state.is(Blocks.DIAMOND_BLOCK)) {
                player.sendSystemMessage(Component.literal("Oh, expensive detection!"));
                playSound(player);
            }
        }

        context.getItemInHand().hurtAndBreak(1, context.getPlayer(), player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;

    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kobemod.metal_detector.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found " + I18n.get(block.getDescriptionId()) + " at " +
                "(" + blockPos.getX() + ", " + blockPos.getY() + "," + blockPos.getZ() + ")"));
    }

    private boolean isValuableBlock(BlockState state) {
        return state.is(ModTags.Blocks.METAL_DETECTOR_VALUABLE);
    }

    private void playSound(Player player) {
        player.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 1.0f, 1.0f);
    }

    // Add particle effect here

    public void onUseTick(Level level, Player player, UseOnContext context) {
        if (!level.isClientSide) {
            BlockPos positionClicked = context.getClickedPos();
            BlockState state = level.getBlockState(positionClicked);

            if (isValuableBlock(state)) {
                // Add particle effect here
                level.addParticle(ParticleTypes.SMOKE, context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5, 0.0D, 0.0D, 0.0D);
                level.addParticle(ParticleTypes.FLAME, context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 0.5, context.getClickedPos().getZ() + 0.5, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}