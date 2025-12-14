package net.Babychaosfloh.justvampires.item.Custom;

import net.Babychaosfloh.justvampires.JustVampires;
import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.Babychaosfloh.justvampires.effekt.ModEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.config.*;
//import net.minecraftforge.fml.config.ModConfig;
import org.jetbrains.annotations.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BloodSyringeItem extends Item {

    public BloodSyringeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, Player player, LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {

        Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, ConfigTracker.INSTANCE.getConfigFileName(JustVampires.MOD_ID, ModConfig.Type.COMMON)));
        EntityType mobType = pInteractionTarget.getType();

        ItemStack pItem = player.getItemInHand(pUsedHand);
        CompoundTag tag = pItem.getOrCreateTag();                 //NBTag
            UUID pIid = null;                                                //mobID from Syringe
                String pIshortID = null;                                         //shortened mobID from Syringe
        CompoundTag pItag = pItem.getOrCreateTag();               //get playerItemTag

        if (pItag.contains("mobID")) {
            pIid = pItag.getUUID("mobID");
            pIshortID = pIid.toString().substring(pIid.toString().length() - 5);
        }

        List<List<? extends String>> TagList = Arrays.asList(JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get());

        for(int i = 0; i < TagList.get(0).size(); i++) {
            String currentT = TagList.get(0).get(i);
            String[] splitCT = currentT.split(":");
            if (splitCT.length == 2 || splitCT.length == 3) {

                /*
                player.sendSystemMessage(Component.literal("[Debug-1] " + JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get().get(i)));
                player.sendSystemMessage(Component.literal("[Debug-2] " + TagList.get(0).get(i)));
                player.sendSystemMessage(Component.literal("[Debug-3] " + currentT));
                player.sendSystemMessage(Component.literal("[Debug-4] " + splitCT[1]));

                System.out.println("Two");
                player.sendSystemMessage(Component.literal("Two"));

                player.sendSystemMessage(Component.literal("" + splitCT.length));
                */

                TagKey<EntityType<?>> currentTag = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(splitCT[0], splitCT[1]));
                UUID mobID = pInteractionTarget.getUUID();
                String mobShortID = mobID.toString().substring(mobID.toString().length()-5); //shortened mobID

                //System.out.println("Three");
                //player.sendSystemMessage(Component.literal("Three"));

                if (pInteractionTarget.getType().is(currentTag)) {
                    if (!pItem.hasTag() || !pItag.contains("sourceMob")
                            || pItem.getTag().getString("sourceMob").equals("AIR")) {
                            tag.putInt("CustomColor", Integer.parseInt(splitCT[2].substring(2), 16));

                            tag.putString("bloodType", currentT);
                            tag.putString("sourceMob", mobType.toString());
                            tag.putUUID("mobID", mobID);
                            tag.putBoolean("dirty", true);

                            //System.out.println("Four");
                            player.sendSystemMessage(Component.literal("Extracting"));
                    }
                    else if (!pItag.getString("sourceMob").equals("AIR")) {
                        if ((pIshortID != null && !pIshortID.equals(mobShortID)) || (pItag.contains("dirty") && pItag.getBoolean("dirty"))) {
                            player.sendSystemMessage(Component.literal("Infacting"));
                            pInteractionTarget.addEffect(new MobEffectInstance(
                                    ModEffects.SEPSIS.get(),             //effect
                                    40,                                  //duration
                                    1                                    //amplifier
                            ));
                        }
                        tag.putString("bloodType", "AIR");
                        tag.putString("sourceMob", "AIR");
                        tag.remove("CustomColor");
                        tag.remove("mobID");
                    }
                }
            } else {
                player.sendSystemMessage(Component.literal("[JustVampires] Syntax ERROR! Bloodtypes have to be: \"<namespace>:<path>\" \n Example: \"justvampires:blootype_normal\" \n(Click to open config)").setStyle(style));
            }
        }

        //player.sendSystemMessage(Component.literal("YAY"));
        pInteractionTarget.hurt(pInteractionTarget.damageSources().playerAttack(player), 0.5F);
        player.getItemInHand(pUsedHand).setTag(tag);

        return super.interactLivingEntity(pStack, player, pInteractionTarget, pUsedHand);
    }
}
