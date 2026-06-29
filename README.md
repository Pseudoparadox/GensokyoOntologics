<p align="center">
    <img width="600" src="https://ideal-ontology.github.io/icon_publish_ver_final.png" alt="title">
</p>

This mod is a derivative work of Touhou Project based on Minecraft 1.16.5. There will be a lot of differences from the original work. Please take it easy. If you can accept them, then here we go.
   
## Introduction:  
  *Gensokyo, a place where mankind and youkai live within side by side keeping a dedicated balance. It is said that things
  being forgotten by the outside world will come to gensokyo, where there will be the experts to solve the big crisis called the "Incient".*  
<br>
    *幻想乡，一个人与妖怪两相对立却又彼此依存的世界。*   
    *一直以来，妖怪袭击人类，人类退治妖怪的规律维持着幻想乡微妙的平衡。据说，被外界的人们所遗忘的事物便会来到幻想乡。而一旦幻想乡出现一种名为“异变”的巨大危机时，解决异变的专家们便开始出动，寻找发动异变的元凶。*

<img align="center" src="https://ideal-ontology.github.io/2023-07-14_01.08.04.png" alt="">
  
This mod is an adventure mod aiming at putting the terrains of gensokyo, altogether with its unique cultural panorama, combat mechanics and structure discovery
 to Minecraft. Gensokyo Ontology(GSKO) add Gensokyo dimension with its varieties of stylistic biomes. Some biomes will generate structure
for players to discover. Players can deal with incidents, fight against with fairies and collect items in th overworld and enter gensokyo, 
try to find out the initiator of the incidents.<br><br>
本模组是一个旨在还原幻想乡场景地形、人文风貌、战斗探索与建筑结构的冒险类模组，为Minecraft添加了一个幻想乡维度及其附带的多个生物群系，并且为部分生物群系添加了
幻想乡特有的建筑结构供玩家探索冒险。玩家需要在主世界处理异变，收集材料，制作道具前往幻想乡，找出发动异变的元凶。还有更多有趣的机制和美观的装饰方块等待着大家的探索哦。

## Game Features
- A story of realities and fantasies. A discovery with immortal and immemorial beings.
- A brand-new fantasy world with immersive experience for your journey in Gensokyo.<br>
- Retreating hostile creatures to find out their stories.<br>
- Get along with all the beings in Gensokyo though it lasts short.<br>

## Game Process (Developing)
- 节分
- 夏越大祓祭：消除所有负面效果，净化/提升/纯化物品，多用作将初始材料进一步进行加工为中间材料
- 驱魔/祈福仪式：使用魔法、祈福、舞蹈或吟唱驱动的魔法类物品合成，多为魔法伤害
- 唐伞妖怪锻造台：强化/合成物品，多用作物理伤害

### 物品合成树（主世界部分）
```mermaid
flowchart TD
    overworldM["主世界挖矿"]-->kaolin["高岭土"]
    overworldM-->kaolinite["高岭石"]
    
    overworldM-->obsidian["黑曜石"]
    overworldM-->izano["伊奘诺物质"]
    overworldE["主世界生物掉落"]-->enderman["末影人"]-->endPearl["末影珍珠"]
    overworldC["主世界战利品宝箱"]-->village["村庄"]-->GSKOtales["被遗忘的传说：幻想乡"]
    
    kaolin-->jigsawBlocks["用于合成拼图系列方块"]
    kaolinite-->jigsawBlocks
    netherM["下界挖矿"]-->quartz["石英"]
    
    quartz-->craftSorcery["合成魔力提取台"]
    obsidian-->craftSorcery
    izano-->craftSorcery-->obRecipe["神秘珠合成配方"]
    izano-->obRecipe
    endPearl-->obRecipe
    GSKOtales-->obRecipe
    obRecipe-->occultBall["神秘珠"]
```
### 物品合成树（玉石、石樱、绯色合金）
```mermaid
flowchart TD
  gskoC["幻想乡战利品宝箱"]-->jade["玉石"]
  gskoM["幻想乡挖矿"]-->jade
  gskoM-->dragonS["龙珠"]
  dragonS-->spiritRecipe["阴阳爻合成配方"]
  spiritRecipe-->spirit["阴阳爻"]
  
  gskoM-->kaolin["高岭土"]
  gskoM-->kaolinite["高岭石"]
  kaolin-->jigsaws["拼图系列方块"]
  kaolinite-->jigsaws
  
  gskoM-->obsidian["黑曜石"]
  obsidian-->cameraRecipe["文文的相机：祭坛合成"]
  cameraRecipe-->camera["文文的相机"]
  gskoM-->alloy["绯色合金"]
  
  alloy-->alloyEquips["绯色合金装备"]
  alloy-->alloyArms["绯色合金武器"]
  
  jade-->jadeGam["赌石"]-->lvlS["S级玉石"]
  jadeGam-->lvlSS["S级玉石"]-->inyoRecipe["阴阳玉：祭坛合成配方"]
  lvlSS-->jadeEquips["玉石装备"]
  lvlSS-->jadeArms["玉石武器"]
  
  inyoRecipe-->inyoBlack["阴阳玉：黑色"]
  spirit-->inyoRecipe
  
  alloy-->hakeiroRecipe["八卦炉：祭坛合成"]
  inyoBlack-->hakeiroRecipe
  hakeiroRecipe-->hakeiro["八卦炉"]
  
  gskoE["幻想乡：生物掉落物"]-->zako["杂鱼妖精"]
  zako-->dan["弹幕"]
  dan-->danRecipe["弹幕合成台合成"]-->smallDan["小弹"]
  danRecipe-->midDan["中型弹幕"]
  zako-->spectre["幽灵"]-->sufferSoul["痛苦的灵魂"]
  sufferSoul-->spiritRecipe
  
  zako-->kedama["毛玉"]-->kdmFur["毛玉的皮毛"]
  zako-->inyoE["阴阳玉实体"]-->fireCharge["火焰弹"]
  
  gskoB["幻想乡：方块掉落物"]-->sakura["樱花树叶"]-->cherry["樱花花瓣"]
  zako-->fairy["妖精"]-->ishiF["石樱碎片"]
  cherry-->ishiCRecipe["石樱结晶：工作台合成"]
  ishiF-->ishiCRecipe
  ishiCRecipe-->ishiC["石樱结晶"]
    
  ishiC-->ishiBRecipe
  lvlSS-->ishiBRecipe
  ishiBRecipe-->ishiB["石樱立方"]
```


```mermaid
flowchart LR
    LoginGensokyo-->Mines["玉石（独有）龙珠（独有）"]
    LoginGensokyo-->Crafts["装备、弹幕、道具"]
    LoginGensokyo-->Loots["夜雀食堂、三妖精大树"]
    LoginGensokyo-->Economics["人间之里交易"]
    LoginGensokyo-->Combats["妖精、阴阳玉、幽灵、毛玉 
  (基础材料)"]

Crafts-->弹幕
BaseMaterials-->弹幕
Mines-->Jade["赌石、玉石等级"]
Mines-->Alloy["古代合金"]
Mines-->CinnabarPowder["贤者之石原料：朱砂粉"]

Combats-->BaseMaterials["妖精-弹幕，阴阳玉-碎片，
幽灵-灵魂，毛玉-杂项"]
Combats-->|"中等怪物"|大蝴蝶
大蝴蝶-->石樱碎片
Yousei["妖精"]-->|"四季紊乱时"|石樱碎片

Combats-->|"中等怪物"|罪袋
Combats-->|"中等怪物"|鬼族小鬼头
Combats-->|"精英怪"|河童
Combats-->|"精英怪"|天狗卫兵
Combats-->|"精英怪"|月兔军
Combats-->|"精英怪"|大鬼头
Combats-->|"在夜晚喝下伊吹瓢中的美酒"|ByakuOni["触发百鬼夜行"]
Combats-->Boss
Boss-->莉莉白
Boss-->琪露诺
Boss-->露米娅

河童-->|"掉落"|尻子玉
河童-->|"掉落"|河童机械齿轮
河童-->|"掉落"|河童载具蓝图

月兔军-->|"掉落"|月兔子弹
月兔军-->|"掉落"|蓬莱玉枝的珍珠

莉莉白-->RetreatLilywhiteAwards["退治莉莉白奖励：
在春意盎然的生物群系时生命值每秒自然恢复2，获得樱花花瓣和报春风铃"]
-->SpringMemory["妖魔书【四季更迭】+
报春风铃触发春日回忆异变"]

莉莉白-->蕾米
琪露诺-->蕾米
露米娅-->蕾米
蕾米-->芙兰

LoginGensokyo-->人里村民-->Merchants["
2铜币收食物，
5铜币收S级玉石，
3银币收SS级玉石，
13银币收狐玉，
5金币收SSS级玉石，

1金币出售破魔矢，
5金币出售被遗忘的传说，
一种贤者之石*5换取一种妖魔书
25金币出售能面，
"]

Merchants-->|"妖魔书"|CinnabarMerchants["贤者之石【土】-百鬼夜行
贤者之石【木】-四季更迭
贤者之石【水】-永远之夜
贤者之石【火】-红雾之主
贤者之石【金】-怨灵作祟
"]
CinnabarMerchants-->Fantacify["五本妖魔书合成->妖魔书【现世幻想录】"]

LoginGensokyo-->铃仙-->LunaticMedicine["5000金币出售蓬莱之药，
3000金币出售永琳的弓，
1000金币出售永琳的箭）"]
蓬莱玉枝的珍珠-->|"+木棍"|蓬莱玉枝
蓬莱玉枝-->|"+羽毛"|Arrow["永琳的箭"]
蓬莱玉枝-->|"+线"|Bow["永琳的弓"]
Bow-->|"+永琳的箭"|虚假之月异变

ByakuOni-->PowerOf100Oni["百鬼夜行强度：
无视刷怪上限
每10秒必刷新一只精英怪
所有怪物获得10盔甲值和10盔甲抗性
鬼族获得500%额外血量
在午夜有概率刷新Boss"]
ByakuOni-->AwardsOf100Oni["百鬼夜行掉落稀有材料：
茨木童子的右臂碎片
弹幕：虚假之月
Bomb和1up
鬼之角
在午夜有概率刷新Boss"]
ByakuOni-->RetreatOniBoss["在百鬼夜行考验中击败Boss，获得：
妖刀【鬼切】的碎片"]

BaseMaterials-->幽灵
幽灵-->|"掉落"|灵魂
灵魂-->Absorb["右键勾玉吸收灵魂进入通灵状态，该状态下死亡时进入灵体效果"]
Absorb-->|"在灵体效果下前往3个不同维度"|迷失的灵魂
Absorb-->|"在灵体效果下进入冥界"|转世的灵魂
Absorb-->|"摆脱灵体状态"|有趣的灵魂
Absorb-->|"在灵体效果下受到超过500点火焰/岩浆伤害"|受难的灵魂
Absorb-->|"在灵体效果下受到超过500点精神/魔法伤害"|痛苦的灵魂

受难的灵魂-->|"+能面+贤者之石【火】"|AngryMask["愤怒之面：消耗p点释放火焰魔法"]
有趣的灵魂-->|"+能面+烟花火箭"|JoyMask["喜悦之面：消耗p点释放烟花魔法"]
痛苦的灵魂-->|"+能面+恶魂之瓶"|SadMask["忧伤之面：消耗p点使得实体获得抑郁的Capability"]
转世的灵魂-->|"+能面+永夜的辉光"|HopeMask["希望之面：佩戴时免疫所有精神伤害"]
SadMask-->Depress["抑郁：无法对别人造成伤害，每次试图攻击他人时自身将会反弹(造成的伤害值*1.5%*抑郁等级)的伤害"]

CinnabarPowder-->|"朱砂粉*9=朱砂*9=朱砂块"|CinnabarBlock["朱砂块"];
CinnabarBlock-->|"魔法合成：p点*2+朱砂块+绯色合金碎片+金锭"|Gold["贤者之石：金"]
CinnabarBlock-->|"魔法合成：p点*2+朱砂块+文文的扇子+任一树苗"|Wood["贤者之石：木"]
CinnabarBlock-->|"魔法合成：p点*2+朱砂块+琪露诺的冰晶+水桶"|Water["贤者之石：水"]
CinnabarBlock-->|"魔法合成：p点*2+朱砂块+不死鸟之羽+火焰弹"|Fire["贤者之石：火"]
CinnabarBlock-->|"魔法合成：p点*2+朱砂块+刚玉+土块"|Earth["贤者之石：土"]

Alloy-->AlloyArmor["古代合金装备：+物抗"]
Alloy-->AlloyWeapon["古代合金武器：附加百分比伤害"]
Alloy-->合金块

Jade-->LevelB["B级玉石只能用于合成圆石"]
Jade-->LevelA["A级玉石合成玉石系列装饰"]
Jade-->LevelS["S级玉石可以兑换银币"]
Jade-->Corundum["SS级玉石（刚玉）"]
Jade-->Opal["SSS级玉石（猫眼石）"]

Corundum-->JadeArmor["玉石装备：+魔抗"]
Corundum-->JadeWeapon["玉石武器：附加法强"]
Corundum-->兑换钻石或大量金币
Corundum-->|"+管狐的试管="|狐玉
狐玉-->|"+刚玉*4="|勾玉

Opal-->欧泊块
Opal-->|"魔法合成：p点*5+欧泊+
蓝色心弹+遗忘的传说：地底世界篇+迷失的灵魂"|Eye
Opal-->|+石樱碎片*4+樱花花瓣*4|IshizakuraCrystal["石樱结晶"]
Opal-->|"魔法合成：p点*10+阴阳玉*2+合金锭"|Gensokyo["三种神器【乡】"]
欧泊块-->|"+石樱碎片*8"|Mirror["八咫镜"]
IshizakuraCrystal-->|"石樱结晶*8+普通勾玉"|gouyu["八尺琼勾玉"]

AlloyWeapon-->Sword["古代合金剑"]-->|"三种神器【乡】：
p点*20+古代合金剑+八尺琼勾玉+八咫镜的照射"|Murakumo["天从云剑"]
Sword-->|"三种神器【乡】：
p点*20+古代合金剑+石樱结晶"|楼观剑
Sword-->|"三种神器【乡】：
p点*20+古代合金剑+转世的灵魂"|白楼剑
Sword-->|"三种神器【乡】：
p点*20+古代合金剑+妖刀【鬼切】碎片"|Onigiri["妖刀【鬼切丸】"]

Murakumo-->PowerOfMurakumo["能力：拥有三段突进位移，第一段位移突进一段距离，
如果命中实体则第二段位向前方释放从云剑风移击飞实体，如果成功击飞实体则第三段位移将突进至实体头上向下斩击"]
Onigiri-->PowerOfOnigiri["能力：斩杀血量低于15%的实体, 每斩杀100个生物，斩杀线提升0.1%，上限为50%"]

```
<br>
<br>
<br>
Original Author: ZUN/Team ShanghaiAlice
