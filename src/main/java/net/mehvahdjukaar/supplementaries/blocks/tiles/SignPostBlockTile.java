package net.mehvahdjukaar.supplementaries.blocks.tiles;


import net.mehvahdjukaar.supplementaries.common.CommonUtil.WoodType;
import net.mehvahdjukaar.supplementaries.common.ITextHolder;
import net.mehvahdjukaar.supplementaries.common.TextHolder;
import net.mehvahdjukaar.supplementaries.setup.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;


public class SignPostBlockTile extends TileEntity implements ITextHolder {

    public TextHolder textHolder;

    public BlockState fenceBlock = Blocks.OAK_FENCE.getDefaultState();
    public float yawUp = 0;
    public float yawDown = 0;
    public boolean leftUp = true;
    public boolean leftDown = false;
    public boolean up = false;
    public boolean down = false;

    public WoodType woodTypeUp = WoodType.OAK;
    public WoodType woodTypeDown = WoodType.OAK;

    public SignPostBlockTile() {
        super(Registry.SIGN_POST_TILE);
        this.textHolder = new TextHolder(2);
    }

    @Override
    public TextHolder getTextHolder(){return this.textHolder;}

    @Override
    public double getMaxRenderDistanceSquared() {
        return 128;
    }

    @Override
    public void markDirty() {
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
        super.markDirty();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(){
        return new AxisAlignedBB(this.getPos().add(-0.25,0,-0.25), this.getPos().add(1.25,1,1.25));
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);

        this.textHolder.read(compound);

        this.fenceBlock = NBTUtil.readBlockState(compound.getCompound("Fence"));
        this.yawUp = compound.getFloat("YawUp");
        this.yawDown = compound.getFloat("YawDown");
        this.leftUp = compound.getBoolean("LeftUp");
        this.leftDown = compound.getBoolean("LeftDown");
        this.up = compound.getBoolean("Up");
        this.down = compound.getBoolean("Down");
        //TODO:replace this with something else for modded woods
        this.woodTypeUp = WoodType.values()[compound.getInt("WoodTypeUp")];
        this.woodTypeDown = WoodType.values()[compound.getInt("WoodTypeDown")];


        //remove in the future
        if(compound.contains("Wood_type_up"))this.woodTypeUp = WoodType.values()[compound.getInt("Wood_type_up")];
        if(compound.contains("Wood_type_down"))this.woodTypeDown = WoodType.values()[compound.getInt("Wood_type_down")];
        if(compound.contains("Left_up"))this.leftUp=compound.getBoolean("Left_up");
        if(compound.contains("Left_down"))this.leftDown=compound.getBoolean("Left_down");
        if(compound.contains("Yaw_up"))this.yawUp=compound.getFloat("Yaw_up");
        if(compound.contains("Yaw_down"))this.yawDown=compound.getFloat("Yaw_down");

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        this.textHolder.write(compound);

        compound.put("Fence", NBTUtil.writeBlockState(fenceBlock));
        compound.putFloat("YawUp",this.yawUp);
        compound.putFloat("YawDown",this.yawDown);
        compound.putBoolean("LeftUp",this.leftUp);
        compound.putBoolean("LeftDown",this.leftDown);
        compound.putBoolean("Up", this.up);
        compound.putBoolean("Down", this.down);
        compound.putInt("WoodTypeUp", this.woodTypeUp.ordinal());
        compound.putInt("WoodTypeDown", this.woodTypeDown.ordinal());

        return compound;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
    }
}