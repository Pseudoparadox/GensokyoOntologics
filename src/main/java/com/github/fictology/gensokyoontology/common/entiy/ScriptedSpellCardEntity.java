package com.github.fictology.gensokyoontology.common.entiy;

/*
public class ScriptedSpellCardEntity extends SpellCardEntity {
    public static final DataParameter<CompoundNBT> DATA_SCRIPT = SyncedEntityData.defineId(ScriptedSpellCardEntity.class,
            DataSerializers.COMPOUND_NBT);
    private CompoundNBT scriptsNBT = new CompoundNBT();

    public ScriptedSpellCardEntity(World worldIn, LivingEntity living, IExpressionType exp) {
        super(EntityRegistry.SCRIPTED_SPELL_CARD_ENTITY.get(), worldIn, living);
        this.setLocationAndAngles(living.getPosX(), living.getPosY(), living.getPosZ(), living.rotationYaw, living.rotationPitch);

    }

    public ScriptedSpellCardEntity(EntityType<? extends SpellCardEntity> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setLocationAndAngles(0, 0, 0, 0, 0);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.copy(DATA_SCRIPT, this.scriptsNBT);
    }

    @Override
    protected void readAdditional(@NotNull CompoundNBT compound) {
        super.readAdditional(compound);
        this.scriptsNBT = compound.getCompound("script");
        this.dataManager.set(DATA_SCRIPT, this.scriptsNBT);
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.put("script", this.scriptsNBT);
    }

    public CompoundNBT getScript() {
        return this.dataManager.get(DATA_SCRIPT);
    }

    public void setScript(CompoundNBT scriptNBT) {
        this.scriptsNBT = scriptNBT;
        this.dataManager.set(DATA_SCRIPT, scriptNBT);
    }

    @Override
    public void tick() {
        super.tick();
        onScriptTick(this.world, this.getOwner(), ticksExisted);
    }

    @Override
    public void onScriptTick(World world, Entity owner, int ticksIn) {
        super.onScriptTick(world, owner, ticksIn);
        this.runScript();
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }

    private void runScript() {
        if (!(this.getOwner() instanceof PlayerEntity)) return;
        PlayerEntity player = (PlayerEntity) this.getOwner();
        // /data codec entity @e[type=gensokyoontology:scripted_spell_card,limit=1]
        // player.sendMessage(new StringTextComponent(this.getScript().toString()), player.getUniqueID());

        ListNBT listNBT = getListOrSendFeedback(this.getScript(), player);
        // GSKOUtil.showChatMsg(player, this.dataManager.codec(DATA_SCRIPT).toString(), 40);
        if (!canExecute(player)) return;
        for (INBT inbt : listNBT) {
            if (!(inbt instanceof CompoundNBT)) {
                sendTypeExceptionFeedback(player, "type_of_each_script_in_list_not_allowed");
                return;
            }
            runEachScript(castToCompound(inbt), player);
        }
    }

    private void runEachScript(CompoundNBT scriptNBT, PlayerEntity player) {
        if (scriptNBT.getString("methodName").equals(StaticFunc.SHOOT.methodName)) {
            runShootFunc(scriptNBT, player);
        }
    }

    private void runShootFunc(CompoundNBT shootEntry, PlayerEntity player) {
        ListNBT parameters = getListOrSendFeedback(shootEntry, "parameters", player);

        List<CompoundNBT> params = new ArrayList<>();
        if (strictMatches(parameters, StaticFunc.SHOOT, player)) {
            for (INBT inbt : parameters) {
                params.add(castToCompound(inbt));
                params.get(0).getString("danmakuType");
            }
            AbstractDanmakuEntity danmaku = createDanmakuIf(params.get(0), player);
            // GSKOUtil.showChatMsg(player, params.codec(1).getCompound("value").toString(), 80);
            Vector3d shootVec = new Vector3d(
                    params.get(1).getCompound("value").getDouble("x"),
                    params.get(1).getCompound("value").getDouble("y"),
                    params.get(1).getCompound("value").getDouble("z"));

            shootVec = shootVec.normalize();
            setDanmakuInit(danmaku, this.getPositionVec());
            danmaku.shoot(shootVec.x, shootVec.y, shootVec.z, params.get(2).getFloat("value"), params.get(3).getFloat("value"));

            // GSKOUtil.showChatMsg(player, danmaku.getPosition().toString(), 60);
            this.world.addEntity(danmaku);
        }
    }

    private AbstractDanmakuEntity createDanmakuIf(CompoundNBT danmakuData, PlayerEntity player) {
        AtomicReference<DanmakuType> danmakuTypeAtom = new AtomicReference<>();
        AtomicReference<DanmakuColor> danmakuColorAtom = new AtomicReference<>();
        Arrays.stream(DanmakuType.values()).forEach(dt -> danmakuTypeAtom.set(dt.getIfMatches(danmakuData.getString("danmakuType"))));
        Arrays.stream(DanmakuColor.values()).forEach(dc -> danmakuColorAtom.set(dc.getIfMatches(danmakuData.getString("danmakuColor"))));

        return createDanmakuInstance(danmakuTypeAtom.get(), danmakuColorAtom.get(), player);
    }

    private AbstractDanmakuEntity createDanmakuInstance(DanmakuType danmakuType, DanmakuColor danmakuColor, PlayerEntity player) {
        switch (danmakuType) {
            case INYO_JADE:
            case LARGE_SHOT:
            default:
                return new LargeShotEntity(player, player.world, danmakuType, danmakuColor);
            case SMALL_SHOT:
                return new SmallShotEntity(player, player.world, danmakuType, danmakuColor);
            case RICE_SHOT:
                return new RiceShotEntity(player, player.world, danmakuType, danmakuColor);
            case SCALE_SHOT:
                return new ScaleShotEntity(player, player.world, danmakuType, danmakuColor);
            // case HEART_SHOT:
            //     return new HeartShotEntity(player, player.world, danmakuType, danmakuColor);
            case TALISMAN_SHOT:
                return new TalismanShotEntity(player, player.world, danmakuType, danmakuColor);
            case STAR_SHOT_SMALL:
                return new SmallStarShotEntity(player, player.world, danmakuType, danmakuColor);
            case STAR_SHOT_LARGE:
                return new LargeStarShotEntity(player, player.world, danmakuType, danmakuColor);
            case FAKE_LUNAR:
                return new FakeLunarEntity(player, player.world, danmakuType, danmakuColor);

        }
    }

    private boolean strictMatches(INBT inbt, String type) {
        return castToCompound(inbt).getString("type").equals(type);
    }

    private boolean strictMatches(ListNBT listNBT, StaticFunc staticFunc, PlayerEntity player) {
        if (listNBT.size() != staticFunc.parameters.size()) {
            sendNullPointerFeedback(player, "not_provide_enough_parameters");
            return false;
        }
        List<String> typeList = listNBT.stream().map(inbt -> {
            if (inbt instanceof CompoundNBT) {
                CompoundNBT nbt = (CompoundNBT) inbt;
                return nbt.getString("type");
            }
            return "";
        }).collect(Collectors.toList());

        for (StaticFunc func : StaticFunc.values()) {
            if (typeList.stream().sorted().collect(Collectors.joining()).equals(
                    func.parameters.stream().sorted().collect(Collectors.joining()))) {
                return true;
            }
        }
        return false;
    }

    private boolean strictMatches(ListNBT listNBT, V3dFunc v3dFunc, PlayerEntity player) {
        if (listNBT.size() != v3dFunc.paramTypes.size()) {
            sendNullPointerFeedback(player, "not_provide_enough_parameters");
            return false;
        }
        return listNBT.stream().allMatch(inbt -> {
            boolean flag = false;
            for (String type : v3dFunc.paramTypes) {
                flag = castToCompound(inbt).getString("type").equals(type);
            }
            return flag;
        });
    }

    public CompoundNBT castToCompound(INBT inbt) {
        return inbt instanceof CompoundNBT ? (CompoundNBT) inbt : new CompoundNBT();
    }

    private ListNBT getListOrSendFeedback(CompoundNBT nbtIn, PlayerEntity player) {
        if (!(nbtIn.get("scripts") instanceof ListNBT)) {
            sendTypeExceptionFeedback(player, "acquired_data_not_a_list");
        }
        ListNBT listNBT = (ListNBT) nbtIn.get("scripts");
        if (listNBT == null) {
            sendNullPointerFeedback(player, "list_is_null");
        }
        return listNBT;
    }

    private ListNBT getListOrSendFeedback(CompoundNBT nbtIn, String listKey, PlayerEntity player) {
        if (!(nbtIn.get(listKey) instanceof ListNBT)) {
            sendTypeExceptionFeedback(player, "acquired_data_not_a_list");
        }
        ListNBT listNBT = (ListNBT) nbtIn.get(listKey);
        if (listNBT == null) {
            sendNullPointerFeedback(player, "list_is_null");
        }
        return listNBT;
    }

    private boolean canExecute(PlayerEntity player) {
        return this.getScript().get("scripts") instanceof ListNBT;
    }

    private boolean canExecute(CompoundNBT nbtIn, String listKey, PlayerEntity player) {
        if (!(nbtIn.get(listKey) instanceof ListNBT)) {
            sendTypeExceptionFeedback(player, "acquired_data_not_a_list");
        }
        ListNBT listNBT = (ListNBT) nbtIn.get(listKey);
        if (listNBT == null) {
            sendNullPointerFeedback(player, "list_is_null");
        }
        return (nbtIn.get(listKey) instanceof ListNBT) || listNBT != null;
    }

    private void sendTypeExceptionFeedback(PlayerEntity player, String msg) {
        if (this.ticksExisted == 2) {
            player.sendMessage(GensokyoOntology.fromLocaleKey("script.", ".error.type_exception." + msg), player.getUniqueID());
        }
    }

    private void sendNullPointerFeedback(PlayerEntity player, String msg) {
        if (this.ticksExisted == 2) {
            player.sendMessage(GensokyoOntology.fromLocaleKey("script.", ".error.null_pointer_exception." + msg), player.getUniqueID());
        }
    }

    @Deprecated
    private void onScriptTickOld() {
        // ScriptEngine engine = new ScriptEngineManager().getEngineByName("Nashorn");
        // try {
        //     engine.eval(
        //             "var SmallShot = Java.type(\"github.thelawf.gensokyoontology.common.entity.projectile.SmallShotEntity\");\n" +
        //             "var Vector3d = Java.type(\"net.minecraft.util.math.vector.Vector3d\");\n" +
        //             "var DanmakuType = Java.type(\"github.thelawf.gensokyoontology.common.util.danmaku.DanmakuType\");\n" +
        //             "var DanmakuColor = Java.type(\"github.thelawf.gensokyoontology.common.util.danmaku.DanmakuColor\");\n" +
        //             "\n" +
        //             "function onScriptTick (world, owner, ticksExisted) {\n" +
        //             "    var center = new Vector3d(1, 0, 0);\n" +
        //             "    var local = center.add(4, 0, 0).rotateYaw(Math.PI / 60 * ticksExisted);\n" +
        //             "    var global = local.add(owner.getPositionVec());\n" +
        //             "\n" +
        //             "    for (var i = 0; i < 8; i++) {\n" +
        //             "        var smallShot = new SmallShot(owner, world, DanmakuType.SMALL_SHOT, DanmakuColor.RED);\n" +
        //             "        var vector3d = center.rotateYaw(Math.PI / 4 * i).rotateYaw(Math.PI / 100 * ticksExisted);\n" +
        //             "        smallShot.setLocationAndAngles(global.x, global.y, global.z, center.y, center.z);\n" +
        //             "        smallShot.setNoGravity(true);\n" +
        //             "\n" +
        //             "        smallShot.shoot(vector3d.x, 0, vector3d.z, 0.5, 0);\n" +
        //             "        world.addEntity(smallShot);\n" +
        //             "    }\n" +
        //             "};");
        //     Invocable invocable = (Invocable) engine;
        //     invocable.invokeFunction("onScriptTick", world, owner, ticksIn);
        // } catch (ScriptException | NoSuchMethodException e){
        //     e.printStackTrace();
        // }
    }
}

 */
