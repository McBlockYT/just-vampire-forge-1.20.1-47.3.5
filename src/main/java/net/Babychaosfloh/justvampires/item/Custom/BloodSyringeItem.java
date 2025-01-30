package net.Babychaosfloh.justvampires.item.Custom;

import net.Babychaosfloh.justvampires.config.JustVampiresCommonConfigs;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagFile;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BloodSyringeItem extends Item {

    public BloodSyringeItem(Properties pProperties) {
        super(pProperties);
        pProperties.stacksTo(1);
        pProperties.rarity(Rarity.UNCOMMON);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player, LivingEntity InteractionTarget, @NotNull InteractionHand UsedHand) {

        EntityType MobType = InteractionTarget.getType();

        CompoundTag bloodType = new CompoundTag(); //NBTag
        bloodType.putString("JustVampires:bloodType", "NONE");
        bloodType.putString("JustVampires:mob", "NONE");

        TagKey<EntityType<?>> tagNormal = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("justvampires", "bloodtype_normal"));
        TagKey<EntityType<?>> tagInsect = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("justvampires", "bloodtype_insect"));
        TagKey<EntityType<?>> tagDefine = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("justvampires", "bloodtype_insect"));

       List<List<? extends String>> TagList = Arrays.asList(JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get());

        for(int i = 0; i < TagList.get(0).size(); i++) {
            String current = TagList.get(0).get(i).toString();

            player.sendSystemMessage(Component.literal("[Debug] " + JustVampiresCommonConfigs.BLOOD_TYPE_ENTITY_TAGS.get().get(i)));
            player.sendSystemMessage(Component.literal("[Debug] " + TagList.get(0).get(i)));

           TagKey<EntityType<?>> currentTag = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("justvampires", current));

           if(InteractionTarget.getType().is(currentTag)) {
               bloodType.putString("JustVampires:bloodType", current);
               bloodType.putString("JustVampires:mob", MobType.toString());
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
