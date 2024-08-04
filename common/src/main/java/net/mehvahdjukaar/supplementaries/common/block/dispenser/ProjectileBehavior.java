package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.common.entities.ThrowableBrickEntity;
import net.mehvahdjukaar.supplementaries.reg.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

abstract class ProjectileBehavior extends DispenserHelper.AdditionalDispenserBehavior {

    protected ProjectileBehavior(Item item) {
        super(item);
    }

    @Override
    protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
        Level world = source.getLevel();
        Position dispensePosition = DispenserBlock.getDispensePosition(source);
        Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
        BlockPos frontPos = source.getPos().relative(direction);
        if (world.getBlockState(frontPos).isFaceSturdy(world, frontPos, direction.getOpposite())) {
            return InteractionResultHolder.fail(stack);
        }
        Projectile projectileEntity = this.getProjectileEntity(source, dispensePosition,  stack);
        projectileEntity.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
        world.addFreshEntity(projectileEntity);
        stack.shrink(1);
        return InteractionResultHolder.success(stack);
    }

    @Override
    protected void playSound(BlockSource source, boolean success) {
        source.getLevel().playSound(null, source.x() + 0.5, source.y() + 0.5, source.z() + 0.5,
                getSound(),
                SoundSource.NEUTRAL, 0.5F,
                0.4F / (source.getLevel().getRandom().nextFloat() * 0.4F + 0.8F));
    }

    protected abstract SoundEvent getSound();

    protected abstract Projectile getProjectileEntity(BlockSource source, Position position, ItemStack stackIn);

    protected float getProjectileInaccuracy() {
        return 7.0F;
    }

    //TODO: fix throwable bricks rendering glitchyness
    protected float getProjectileVelocity() {
        return 0.8F;
    }

}
