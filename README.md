# 葫芦娃大战妖精
南京大学 2020 秋季学期高级 Java 程序设计大作业.



## 最新更新

### 0.0.2

改写了部分代码, 修正了几个bug: 

- 将`BulletComponent`作为单独组件, 与`MovableComponent`分开.
- 修正了`ControllableComponent`的逻辑, 在单位进行攻击时无法控制单位转向.
- 修改了`OperableComponent`组件中`enable`和`disable`的逻辑, 调用一次函数可以禁用该角色的所有`OperableComponent`子类型组件.
- 修正了鼠标单击选择可以选中弹道的bug.

目前发现的bug:

- 角色转向时血条会跟随角色一起反转, 还没想到解决方案.

### 0.0.1

提交了第一版Demo



## 游戏应用程序

`Main`类中为游戏`GameApplication`主引擎, 通过`main`函数启动游戏. 启动时初始化以下部分: 

- 初始化游戏设置. 包括画面宽高, 游戏名和版本号等信息.
- 初始化游戏. 创建初始游戏角色, 加载视图与环境.
- 初始化输入. 通过方向键控制当前操作角色移动, A键进行攻击, 鼠标左键单击切换操作角色.
- 初始化物理引擎. 处理角色间的碰撞事件 (任两个角色位置不能重叠), 弹道和角色的碰撞事件.



## 单位仓库 `HvMFactory`

> TODO: 将`HvMFactory`改为多个仓库. 角色, 弹道使用不同的仓库.

负责不同单位的产生逻辑.



## 组件

通过实现不同的组件, 反映创建出单位的不同能力.

### 基础组件 (所有单位都具备)

#### OperableComponent

基本动作组件, 包含属性 `isOperable`, 当其为`true`时可对单位进行更新. 

#### DetailedTypeComponent

基本类型组件, 包含该单位的详细类型 (`enum`) 与所属阵营 (`enum`)



### 移动组件

#### MovableComponent

继承自OperableComponent

具有X, Y轴上的速度属性, 每帧对该单位的所处位置进行更新. 

不能进行控制的可移动单位拥有该组件. (如弹道轨迹)

#### ControllableComponent

继承自 MovableComponent

可进行移动控制的组件,  包含 moveUp/moveDown/moveLeft/moveRight/stop/... 等控制函数, move\*函数中设置对应轴的速度为maxSpeed, stop\*函数中将速度赋值为0. 

可以进行控制 **(或可以被AI控制)** 的角色拥有该组件.



### 攻击组件

#### AttackComponent

继承自OperableComponent

可攻击组件, 需要提供属性

- `attackAnimationTime`: 攻击前的等待时间.
- `attackBackSwingTime`: 攻击后的等待时间.
- `damage`: 攻击伤害

发出攻击指令后, 会将`isOperable`置为`false`, 在等待时间内其他的移动与攻击操作均无效. 

该组件本身不会产生任何攻击效果! 需要通过下面两个组件实现.

#### RangedAttackComponent

继承自AttackComponent

远程攻击组件, 需要额外传入属性

- `bulletEntityName`: 攻击产生弹道的实体名称
- `bulletSpeed`: 弹道速度

攻击逻辑: 在自身所在位置产生一个对应的弹道, 弹道朝自身面向位置飞行.

#### MeleeAttackComponent (待实现)

继承自AttackComponent

近战攻击组件



### 状态组件

#### HealthComponent

血量组件, UI 上利用 JavaFX 的进度条组件进行渲染, 使用属性 `health` 和 `maxHealth` 记录体力和最大生命值.



### 单位特征组件

#### BulletComponent

弹道实体拥有该组件, 包含属性`damage` (该弹道能造成的伤害) 以及 `source` (该弹道的发出者).



### 动画组件

#### AnimatedComponent

暂时只实现了`idle`和`walk`两个行为模式下的动画.



## 类型

### BasicEntityTypes

单位在TypeComponent中的类型.

### DetailedEntityType

单位需要添加到DetailedTypeComponent中的类型.

### CampType

阵营信息, 单位需要添加到DetailedTypeComponent中的类型.