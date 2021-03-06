# 任务管理平台(TASK MANGEMENT PLATFORM)

#### 说明

任务管理平台是一个集任务托管、任务调度、任务执行和任务状态监控为一体的综合平台

#### 模块说明

任务平台分 个模块

```moudle

    fire-registration-center    注册中心
    fire-task-fe                前端项目
    fire-task-web               业务交汇和前端交互服务
    fire-task-serving           数据服务和定时任务调度服务
    fire-task-agent             任务执行服务和状态监控服务
    fire-task-client            任务客户端各个任务依赖此包

```

#### 需求

```

1、支持可选择的强制停止pid 双重自动关停任务 并扫描pid查看修改状态
2、支持已停止程序的状态变更（避免手写sql改库）
   任务类型支持手动执行、定时执行、循环执行、常驻任务 四种类型
   定时任务开启小星通知（每次开始和结束）（可选）
   循环和常驻任务开启埋点钩子（和心跳不一样）
   循环3倍于休眠间隔发送微信通知，10倍于休眠间隔发短信通知
   常驻任务取10次执行时间的最大值（最大值实时更新）3倍于最大值发送微信通知，10倍于最大值短信通知
   定时任务再运行状态的已停止变更应该受限。或者使用心跳等判断出真正已停止然后变更为其它状态后才能设置已停止
3、支持循环间隔时间的设置（小于等于5分钟 300秒 最小暂停、休眠 1秒 防止cup消耗过大 大于5分钟的请使用 定时模式 无间隔的请使用常驻任务）
4、支持以部门为单位管理任务 （部门、组、名称 联合唯一性检测）
5、添加操作历史功能
6、非定时任务支持定时开启、定时关闭
8、代理服务定时扫描端口号。对于有端口号任务直接杀死并发送消息提醒（不支持web任务）。
9、agent 注册ip 复制文件（文件共享）
10、支持集群模式
11、quartz 集群模式（无论是水平集群还是垂直集群其实都是有问题的 不建议使用）
12、任务删除（废弃任务删除 只有在初始状态才可执行，只有手动设置的管理才可删除, 任务名称确认）
13、服务端循环扫描客户端状态 和 2有重叠功能
14、根据数据库ip占用量自动分配新服务执行位置
15、消息总线进行关停任务通知
16、重启服务和宕机重启服务器的区别运算
17、参数名配置（设置）例如： port
18、按部门划分服务器。任务运行在本部门服务器上。
19、非定时任务增加心跳
20、支持实时日志查看和历史日志下载
21、START 状态但是任务确实没启动的处理
23、多实例是指同一个jar包有多个进程启动。比多消息处理可能会建多个任务去处理同一个消息。但是对于每分钟都执行一次的任务肯能旧进行没死可能新进程是不允许启动的。防止冲突。
24、参数污染。系统参数不应该传给具体任务逻辑。（任务id除外）
25、对于定时任务可以添加单独的调度corn表达式
26、添加代理服务注册表，保存所有代理服务ip 并进行ip管理
27、添加文件管理中心，进行文件的上传和下载（文件备份中心）可以是单机
28、区分springboot项目和非springboot项目的启动流程
29、任务类型分为jar和url等 url 只能定时执行


```

#### 版本发布

2019.8.13
项目模块规划
项目初始化
开发计划