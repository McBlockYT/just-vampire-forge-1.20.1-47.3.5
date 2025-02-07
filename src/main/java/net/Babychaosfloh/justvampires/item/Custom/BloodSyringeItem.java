package net.Babychaosfloh.justvampires.item.Custom;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BloodSyringeItem extends Item {

    public BloodSyringeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, Player player, LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {

        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ConfigTracker.INSTANCE.getConfigFileName(JustVampires.MOD_ID, ModConfig.Type.COMMON)));

        EntityType MobType = pInteractionTarget.getType();

        CompoundTag bloodType = new CompoundTag(); //NBTag
        bloodType.putString("JustVampires:bloodType", "NONE");
        bloodType.putString("JustVampires:mob", "NONE");

        CompoundTag colorTag = new CompoundTag();

        List<List<? extends String>> TagList = Arrays.asList(JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get());

        for(int i = 0; i < TagList.get(0).size(); i++) {
            String currentT = TagList.get(0).get(i);
            String[] splitCT = currentT.split(":");
            if (splitCT.length == 2 || splitCT.length == 3) {

                player.sendSystemMessage(Component.literal("[Debug-1] " + JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get().get(i)));
                player.sendSystemMessage(Component.literal("[Debug-2] " + TagList.get(0).get(i)));
                player.sendSystemMessage(Component.literal("[Debug-3] " + currentT));
                player.sendSystemMessage(Component.literal("[Debug-4] " + splitCT[1]));

                System.out.println("Two");
                player.sendSystemMessage(Component.literal("Two"));

                player.sendSystemMessage(Component.literal("" + splitCT.length));


                TagKey<EntityType<?>> currentTag = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(splitCT[0], splitCT[1]));

                System.out.println("Three");
                player.sendSystemMessage(Component.literal("Three"));

                if (pInteractionTarget.getType().is(currentTag)) {

                    colorTag.putInt("CustomColor", Integer.parseInt(splitCT[2].substring(2), 16));

                    bloodType.putString("JustVampires:bloodType", currentT);
                    bloodType.putString("JustVampires:mob", MobType.toString());

                    System.out.println("Four");
                    player.sendSystemMessage(Component.literal("Four"));

                }
            } else {
                player.sendSystemMessage(Component.literal("[JustVampires] Syntax ERROR! Bloodtypes have to be: \"<namespace>:<path>\" \n Example: \"justvampires:blootype_normal\" \n(Click to open config)").setStyle(style));
            }
        }

        player.sendSystemMessage(Component.literal("YAY"));
        pInteractionTarget.hurt(pInteractionTarget.damageSources().playerAttack(player), 0.5F);
        player.getItemInHand(pUsedHand).setTag(bloodType);
        player.getItemInHand(pUsedHand).setTag(colorTag);

        return super.interactLivingEntity(pStack, player, pInteractionTarget, pUsedHand);
    }
}
