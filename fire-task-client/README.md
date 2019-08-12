## Job client

    任务管理平台子任务客户端。不支持占用端口的程序。
    
子任务接入平台

    非常重要: 请每次对接都要认真阅读文档。按照文档内容顺序对接。不要想当然。
    无比重要: 请每次对接都要在测试环境进行测试,可以尽情的添加任务。保证在正式环境上线的任务是完全ok的。
    更更重要：如果按照文档对接不成功，那么请不要一目十行的阅读文档。你肯定遗漏了什么。

manven

```xml

<dependency>
    <groupId>io.github.FHfirehuo</groupId>
	<artifactId>fire-task-client</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```


java 

```java
	#暂时缺失
```

springboot 自停任务

```java
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {

		ConfigurableApplicationContext app = new SpringApplicationBuilder()
				.sources(Application.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
		
		Beginning begin = (Beginning) app.getBean("beginning");
		
		//非常重要 这里是 start 不是run
		begin.start(args);
		System.exit(SpringApplication.exit(app));
	}

}
```


springboot 非自停任务

begin.addListener(new CloseListener());

```java
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {

		ConfigurableApplicationContext app = new SpringApplicationBuilder()
				.sources(Application.class)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
		
		Beginning begin = app.getBean(Beginning.class);
		begin.addListener(new CloseListener());
		
		//非常重要 这里是 start 不是run
		begin.start(args);
	}

}
```

```java

public class CloseListener implements ShutDownListener {

	@Override
	public void ShutDown(ShutDownEvent event) {
		System.out.println("Listener 关闭");
		//调用关闭 流程
	}

}

```

```java
import org.springframework.stereotype.Service;

@Service
public class Beginning extends AbstractJob {

	@Override
	public void run(String[] arg0) {
         ...
	}
}
```


参数设置
 
    测试环境已默认添加：--spring.profiles.active=test

    正式环境已默认添加：--spring.profiles.active=prod
    
 虽然本平台把用户设置的参数放在前面进行传递,但是非常不建议进行自定义参数,更不建议按参数的位置取值进行判断。否则可能得到出乎意料的结果
 
    
本地试运行
    
    请在本地进行如下测试 java? -jar ***.jar --spring.profiles.active=env args
    
    

为了方便管理请使用如下日志路径

    /opt/system/job/logs/{projectName}/?.log

    日志文件如果是每天打一个包请把目录再logs下加一层

    
    
任务管理平台需求: 

# 添加注册中心进行代理服务注册管理
  1、添加代理服务注册表，保存所有代理服务ip 并进行ip管理
  2、添加文件管理中心，进行文件的上传和下载（文件备份中心）可以是单机
  3、统一接入sso进行文件管理
  4、区分springboot项目和非springboot项目的启动流程

1、支持可选择的强制停止 process 和pid 双重自动关停任务 并扫描pid查看修改状态
2、支持已停止程序的状态变更（避免手写sql改库）
   任务类型支持手动执行、定时执行、循环执行、常驻任务 四种类型
   定时任务开启小星通知（每次开始和结束）（可选）
   循环和常驻任务开启埋点钩子（和心跳不一样）
   循环3倍于休眠间隔发送微信通知，10倍于休眠间隔发短信通知
   常驻任务取10次执行时间的最大值（最大值实时更新）3倍于最大值发送微信通知，10倍于最大值短信通知
   定时任务再运行状态的已停止变更应该受限。或者使用心跳等判断出真正已停止然后变更为其它状态后才能设置已停止
3、支持循环间隔时间的设置（小于等于5分钟 300秒 最小暂停、休眠 1秒 防止cup消耗过大 大于5分钟的请使用 定时模式 无间隔的请使用常驻任务）
4、支持以部门为单位管理任务 （部门、组、名称 唯一性检测）
5、添加操作历史功能
6、非定时任务支持定时开启、定时关闭
7、提供日志（输入）定位。并可以实时查看日志。
8、代理服务定时扫描端口号。对于有端口号任务直接杀死并发送消息提醒。
9、agent 注册ip 复制文件（文件共享）
10、支持集群模式（长期规划）
11、quartz 集群模式（无论是水平集群还是垂直集群其实都是有问题的 不建议使用）
12、任务删除（废弃任务删除 只有在初始状态才可执行，只有手动设置的管理才可删除）
13、服务端循环扫描客户端状态 和 2有重叠功能
14、根据数据库ip占用量自动分配新服务执行位置
15、消息总线进行关停任务通知
16、重启服务和宕机重启服务器的区别运算
17、参数名配置（设置）例如： port
18、按部门划分服务器。任务运行在本部门服务器上。
19、非定时任务增加心跳
    