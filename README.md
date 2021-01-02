# 葫芦娃大战妖精
南京大学 2020 秋季学期高级 Java 程序设计大作业.



## 最新更新
### 0.3.2 (2021-01-02)
添加了游戏结束判断机制，以及游戏结束后的UI

### 0.3.1 (2021-01-01)
修复存档录像人物被杀死后不移除的bug

### 0.3.0 (2021-01-01)
完成了存档以及回放功能并融合进游戏模块

- 存档可设置名字
- 存档可进行管理，删除或添加

存档按照帧来存，一秒60帧，存下所有的实体以及变量信息

加载的时候按帧设置实体信息

TODO:

- 回放的进度条
- 回放时的功能限制
- 游戏结束的界面

### 0.2.1 (2021-01-01)

修正了一个联机游玩时, 单位死亡会导致游戏退出的恶性bug.

### 0.2.0 (2020-12-31)

设计了AI模块与近战攻击模块, 重新手写了碰撞检测逻辑, 加入了一个葫芦娃素材用作近战攻击测试.

存在bug: 

- 两个AI互相贴近时可能会卡死.

### 0.1.1 (2020-12-27)

网络通信模块已经完全融入Demo, 当前演示情况: 

- 在Server端按空格开始游戏, 不需要Client上再按. (Server会自动选择Client端的默认控制单位并在按下空格时发给Client)
- 当前Demo是一个2v2的战场, 左右两边为两个阵营. 按鼠标左键选择控制单位 (TODO: 控制单位的选择暂时还没有按阵营限制)
- Client端选择控制人物在本地进行, 剩余操作除了移动操作会进行预渲染外, 其他操作 (如攻击) 通过Server执行.
- 碰撞事件 (如弹道伤害结算) 在Serve端处理. (TODO: 将时间处理函数从Main函数中移出去, 封装为若干个类)

添加动作输入的方法 (备忘) : 

- `initInput`中绑定到热键.
- 创建`UserAction`类
- 在对应的执行方法中添加Server和Client两种情况下的处理 (Client端需要发消息给Server)
- 在`MultiplayerConnectionService`中添加Serve对该消息的处理

### 0.1.0 (2020-12-26)

添加网络通信与多人对战模块. 存在bug:

- 暂时只能使用移动功能, 攻击功能还在修改.

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