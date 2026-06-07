
Installation information
=======

This template repository can be directly cloned to get you started with a new
mod. Simply create a new repository cloned from this one, by following the
instructions provided by [GitHub](https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-repository-from-a-template).

Once you have your clone, simply open the repository in the IDE of your choice. The usual recommendation for an IDE is either IntelliJ IDEA or Eclipse.

If at any point you are missing libraries in your IDE, or you've run into problems you can
run `gradlew --refresh-dependencies` to refresh the local cache. `gradlew clean` to reset everything 
{this does not affect your code} and then start the process again.

Mapping Names:
============
By default, the MDK is configured to use the official mapping names from Mojang for methods and fields 
in the Minecraft codebase. These names are covered by a specific license. All modders should be aware of this
license. For the latest license text, refer to the mapping file itself, or the reference copy here:
https://github.com/NeoForged/NeoForm/blob/main/Mojang.md

技能
===========
- 梦想封印：发射独立的六颗光球追踪最近的敌人
- 梦想天生：生成六个带有体术的环绕阴阳玉5s，如果六个阴阳玉在此期间全部都命中了则自人物中心处多次释放环状冲击波
- 八方龙杀阵：在自身处释放一道巨型光柱造成巨量伤害
- 博丽弹幕结界：制造一组允许弹射物从红色门飞向紫色门的弹幕结界，传送门距离为50码，宽度为10码，大于10码宽度的弹射物不能通过
- 
- 【星符】重力一击：召唤一枚从天而降的星体造成单体伤害 
- 极限火花：持续的大范围激光伤害加减速
- 邪恋火花：蓄力瞬发中范围爆发激光伤害
- 彗星：向前冲撞一名敌人并留下一串星星尾迹
- 
- 【时符】月时计：暂停一个区域的时间，在此区域内的敌方单位被施加眩晕，咲夜在此区域内获得150%移速
- 【速符】闪光弹跳：在一个矩形范围内扔出一把会反弹9次的飞刀
- 夜雾幻影杀人鬼：闪烁到一个位置，在该位置扔出飞刀形成一道持续2s的刃雾让敌人丢失100码之外的视野，选中刃雾中的敌人再次释放可从多个方向扔出飞刀形成刀影斩击选中的敌人
- 【伤魂】灵魂雕刻：向前方快速释放多把飞刀造成巨量暴击伤害
- 
- 【战操】玩偶战争：爱丽丝操纵两圈人偶环绕自己，内圈人偶击退敌人，外圈人偶造成6段魔法伤害
- 【侦符】探索人偶：爱丽丝可以释放上海、蓬莱、法国、荷兰、伦敦五个人偶作为视野使用，人偶对敌人隐形且踩上去后会发生爆炸造成减速和魔法伤害
- 【注力】拌线：如果探索人偶之间的距离小于120码，则释放这个技能时人偶之间会两两连线形成拌线造成减速和魔法伤害
- 
- 妖梦被动：妖梦共有六个技能，通过R切换
- （人类形态）现世斩：妖梦获得100%移速并强化下一次普攻
- （人类形态）【剑技】樱花闪闪：妖梦变为不可选取状态，向前快速突进，突进和返回时地上产生向上的刀光造成击飞
- （人类形态）迷津慈航斩：妖梦凝聚剑气形成一把巨剑的残影斩下
- （半灵形态）圆心流转斩：妖梦的下一次普攻必定能够造成暴击和击飞效果
- （半灵形态）成佛得脱斩：妖梦可以举刀斩出一道圆形轨迹，造成物理伤害，当敌人被击飞时，该技能必定暴击
- （半灵形态）六根清净斩：妖梦周围生成五个虚影同时进行斩击
- 
- 【红符】红色不夜城：向一个方向发射锥形冲击波击退敌人
- 【红符】恶女置乱：蕾米高高跃起，向一个方向突进，对落点造成魔法伤害
- 【命运】痛苦的宿命：蕾米生成锁链捆绑敌人，这个锁链会在结束时禁锢敌人并造成魔法伤害
- 【神枪】冈戈尼尔：蕾米蓄力向一个方向投掷巨型冈戈尼尔之枪
- 
- 被动-通向无寿国的期票：幽幽子的技能会标记敌人，标记达到3层时引爆印记并造成伤害
- 华胥的永眠：幽幽子释放一圈蝴蝶向周围扩散，造成魔法伤害和10%减速
- 返魂蝶：幽幽子开扇召唤蝴蝶飞向扇形区域，持续造成法术加成的真实伤害
- 【优雅】通向黄泉的诱蛾灯：幽幽子召唤一只魅惑幽灵，被幽灵点亮区域内的敌人会被魅惑
- 樱花之意志：幽幽子打开通往冥界的大门，减速敌人并对站在冥界的敌人持续造成百分比伤害
- 
- 被动-【废线】废弃车站下车之旅：紫死亡后触发人生啊能不能放过我这一次，如果火车杀死了敌人则紫妈将从隙间中复活
- 幻想狂想穴：紫进入不可选中状态，并打开一组隙间从一个位置瞬间闪现至另一处
- 【境界】四重结界：紫展开一道持续1.5秒的结界连接其内的敌人，紫可以再次释放来将被连接的敌人拉向结界中心
- 式神：点击一次进入选择式神状态，再点击一次释放式神，橙造成少量伤害并禁锢敌人，蓝造成中量伤害
- 飞光虫：紫打开多个隙间对指定敌人集火发射弹幕