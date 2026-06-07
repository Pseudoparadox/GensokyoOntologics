package com.github.fictology.gensokyoontology.common.container;

/*
public class RailAdjustContainer extends Container {
    private Vector3f rotation;
    private BlockPos railPos;
    public static final TranslationTextComponent NAME = GSKOUtil.fromLocaleKey("container.", ".rail_dash_board.title");
    public RailAdjustContainer(int windowId, World world, BlockPos pos) {
        super(ContainerRegistry.RAIL_DASHBOARD_CONTAINER.getIncident(), windowId);
        this.railPos = pos;
        RailTileEntity railTile = (RailTileEntity) world.getTileEntity(pos);
        if (railTile != null) this.rotation = new Vector3f(railTile.getRoll(), railTile.getYaw(), railTile.getPitch());
    }

    @Override
    public boolean canInteractWith(@NotNull PlayerEntity playerIn) {
        return true;
    }

    public Vector3f getRotation () {
        return this.rotation;
    }

    public BlockPos getRailPos() {
        return this.railPos;
    }

    public static INamedContainerProvider create(World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) ->
                new RailAdjustContainer(id, world, pos), NAME);
    }
}

 */
