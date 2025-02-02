package net.Babychaosfloh.justvampires.item.Custom;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
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
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, LivingEntity InteractionTarget, @NotNull InteractionHand UsedHand) {

        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ConfigTracker.INSTANCE.getConfigFileName(JustVampires.MOD_ID, ModConfig.Type.COMMON)));


        EntityType MobType = InteractionTarget.getType();

        CompoundTag bloodType = new CompoundTag(); //NBTag
        bloodType.putString("JustVampires:bloodType", "NONE");
        bloodType.putString("JustVampires:mob", "NONE");

        List<List<? extends String>> TagList = Arrays.asList(JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get());

        System.out.println("One");
        player.sendSystemMessage(Component.literal("One"));



        ItemStack Istack = new ItemStack(Items.AIR, 1).setHoverName(Component.literal("AIR"));
        player.getInventory().add(Istack);


        for (int i = 0; i < TagList.get(0).size(); i++) {
            String currentT = TagList.get(0).get(i);
            String[] splitCT = currentT.split(":");
            if (splitCT.length == 2) {

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

                if (InteractionTarget.getType().is(currentTag)) {
                    bloodType.putString("JustVampires:bloodType", currentT);
                    bloodType.putString("JustVampires:mob", MobType.toString());

                    System.out.println("Four");
                    player.sendSystemMessage(Component.literal("Four"));
                }
            } else {
                player.sendSystemMessage(Component.literal("[JustVampires] Syntax ERROR! Bloodtypes have to be: \"<namespace>:<path>\" \n Example: \"justvampires:blootype_normal\" \n(Click to open config)").setStyle(style));
            }
        }

        /*
        if(InteractionTarget.getType().is(tagNormal)) {
           bloodType.putString("JustVampires:bloodType", "NORMAL");
            bloodType.putString("JustVampires:mob", MobType.toString());
        }
        else if(InteractionTarget.getType().is(tagInsect)) {
            bloodType.putString("JustVampires:bloodType", "INSECT");
            bloodType.putString("JustVampires:mob", MobType.toString());
        }
         */

        player.sendSystemMessage(Component.literal("YAY"));
        InteractionTarget.hurt(InteractionTarget.damageSources().playerAttack(player), 0.5F);
        player.getItemInHand(UsedHand).setTag(bloodType);

        return super.interactLivingEntity(stack, player, InteractionTarget, UsedHand);
    }
}
