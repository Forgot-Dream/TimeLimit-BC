# TimeLimit-BC
Limit total online time.

## (默认)配置文件
```
last_boot_time: 0 #上次启动时间戳，！！请留空让它自行生成！！
enabled: false #是否启用 / Whether to enable
time_max: 7200 #以秒(s)为单位 / In seconds(s)
no_limited_players: #不受限制的玩家 / Unrestricted player
  - Steve
  - Alex
```

## 游戏指令
```
/timelimit reset_all 重置所有玩家游戏时长
/timelimit reset <玩家1> <玩家2> ... <玩家n>  重置指定玩家游戏时长，不同玩家间用`空格`分离
/timelimit add_whitelist <玩家1> <玩家2> ... <玩家n>  添加指定玩家至不限时名单，不同玩家间用`空格`分离
/timelimit del_whitelist <玩家1> <玩家2> ... <玩家n>  从不限时名单中删除指定玩家，不同玩家间用`空格`分离
/timelimit query 查询当前剩下游戏时间
```

## 权限节点
```
`timelimit.command.all` -> 执行`reset_all`,`reset`,`add_whitelist`,`del_whitelist`的权限
`query`是所有人都有权限执行的
```

## 分发许可证
GNU LESSER GENERAL PUBLIC LICENSE Version 3
