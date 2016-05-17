
# Ganks for gank.io

**This project is simply modified from another project named [Ganks-for-andoirdweekly.net](https://github.com/hujiaweibujidao/Ganks-for-andoirdweekly.net) of mine.**

This project fetches daily newsletters created by [gank.io](http://gank.io/), which shares technical ganks(干货) every weekday.

`Ganks for gank.io` not only parses the post items in one daily issue, but also extracts the main content of each post item's web page for you. Sounds good?

This project also provides a web search API based on Lucene and these ganks, and the web application is now deployed on Heroku platform. [see the site](http://gankio.herokuapp.com/)   

Since I'm currently in free plan of Heroku, so this site is 16/24 housr available, good luck! 

**中文简介：`Ganks for gank.io`项目利用Gank的API来获取干货列表，不仅如此，该项目还利用dragnet开源工具提取每一个干货的目标网页内容，最终利用Lucene开源工具提供一个高效的干货搜索接口，并将其部署在Heroku平台。**

**目前我的Heroku账号处于free plan，所以应用每24个小时会有8个小时处于停止状态，所以祝你好运！**

## Two main models

The models are not changed from [Ganks-for-andoirdweekly.net](https://github.com/hujiaweibujidao/Ganks-for-andoirdweekly.net) in order to make future integration easy.

1. `GankIssue` represents a daily issue, eg, `gank.io daily Issue 2016-05-13`

2. `GankItem` represents a daily post item, eg, `MaryPopup - Expand your view with no problem`

## How to use

(1) Simply use file `src/main/resources/gankio.json` as the result data, but it will not auto update, the latest weekly issue in this json file is 2016-05-13 for now.

(2) Using Maven to compile and run this project

1.Run `mvn compile` to compile this project;  

2.Run `mvn exec:java -Dexec.mainClass="data.GankIOAPI"` to start loading these posts and generating the final result data.

3.Then you will see the result data in file `src/main/resources/gankio.json` in JSON format.

(3) Build and run this project in your favorite IDE

1.Run `src/main/java/data/GankIOAPI.java`;  

2.Then you will see the result data in file `src/main/resources/gankio.json` in JSON format.

## The result data

The root of the json data is a JSON array containing all the weekly issues posted in gank.io.

`items` in each weekly issue stands for the post items in this issue, and each item has its `url`、`summary`、`content`, etc.

```json
[
	{
		"id":"2016-05-13",
		"items":[
			{
				"content":"Using Blocks to Realize the Strategy Pattern Jul 10th, 2015 There’s this saying that the Strategy pattern can be realized in Swift using blocks. Without blocks, a Strategy object implements usually one required method of an interface (or protocol) to encapsulate a variation of behavior. This behavior can be switched at runtime. It’s like a\u00A0plug-in. Well, blocks can do the same. They can become attributes of an object and be switched out. They also capture context if necessary, which may sometimes be a bonus. The only drawback is that blocks can’t encapsulate state of their own except the captured\u00A0context. Here’s a real-world example from a recent project. It’s a work break timer. It deals with two types of timers, realized via dispatch queues: one for work, and one for breaks. The break timer should restart when it’s prolonged, the work timer should continue to tick if it’s started, or start itself if it wasn’t\u00A0already. Here’s a Strategy-based version of the difference in prolongation\u00A0behavior: protocol TimerProlongationStrategy { func prolong ( timer : TimerType ) } struct StartOnceTimerProlongationStrategy : TimerProlongationStrategy { func prolong ( timer : TimerType ) { if timer . isActive { return } timer . start () } } struct ResetTimerProlongationStrategy : TimerProlongationStrategy { func prolong ( timer : TimerType ) { if timer . isActive { timer . prolong () return } timer . stop () timer . start () } } That’s very verbose, but it’s straightforward to\u00A0use: class TimerCoordinator { var workTimer : Timer ! var breakTimer : Timer ! init ( workDuration : Minutes , breakDuration : Minutes ) { self . workTimer = Timer ( duration : workDuration . seconds , scheduler : self , prolongationStrategy : StartOnceTimerProlongationStrategy (), block : finishWork ) self . breakTimer = Timer ( duration : breakDuration . seconds , scheduler : self , prolongationStrategy : ResetTimerProlongationStrategy (), block : finishBreak ) } } Instead of setting up two Timer types, I can use one type and delegate variation to the prolongationStrategy \u00A0attribute. With blocks put in place of Strategy objects, it would look like\u00A0this: class TimerCoordinator { var workTimer : Timer ! var breakTimer : Timer ! init ( workDuration : Minutes , breakDuration : Minutes ) { self . workTimer = Timer ( duration : workDuration . seconds , scheduler : self , prolongationStrategy : { timer in if timer . isActive { return } timer . start () }, block : finishWork ) self . breakTimer = Timer ( duration : breakDuration . seconds , scheduler : self , prolongationStrategy : { timer in if timer . isActive { timer . prolong () return } timer . stop () timer . start () }, block : finishBreak ) } } That does read even worse than the version\u00A0before! But notice that I’ve referenced finishWork and finishBreak respectively as the last argument of the initializer. Instead of a () -> Void block, I pass in the reference to a method of\u00A0 TimerCoordinator . Strategies don’t have to be realized as in-line blocks or objects. They can be realized as methods or free functions,\u00A0too. Using functions (because methods don’t make much sense for this use case), the full code will look like\u00A0this: func startOnceTimerProlongationStrategy ( timer : Timer ) { if timer . isActive { return } timer . start () } func resetTimerProlongationStrategy ( timer : Timer ) { if timer . isActive { timer . prolong () return } timer . stop () timer . start () } class TimerCoordinator { var workTimer : Timer ! var breakTimer : Timer ! init ( workDuration : Minutes , breakDuration : Minutes ) { self . workTimer = Timer ( duration : workDuration . seconds , scheduler : self , prolongationStrategy : startOnceTimerProlongationStrategy , block : finishWork ) self . breakTimer = Timer ( duration : breakDuration . seconds , scheduler : self , prolongationStrategy : resetTimerProlongationStrategy , block : finishBreak ) } } This gets around inline blocks which are hard to read and doesn’t introduce unnecessary\u00A0objects. Blocks are nice as they are, but functions as first-class citizens of Swift are even nicer because handles to them can be passed instead of\u00A0blocks. Using functions for this will work only if you don’t need to have stateful Strategy instances. In my case, the Strategy objects were simple wrappers around real functions, so it worked\u00A0nicely.",
				"id":"56cc6d23421aa95caa707a1c",
				"source":"Gank.io #237 (2016-05-13)",
				"tags":[],
				"title":"使用 block 实现策略模式",
				"type":"iOS",
				"url":"http://christiantietze.de/posts/2015/07/strategy-blocks/"
			},
        ...
        
		],
		"num":237,
		"title":"Gank.io #237 (2016-05-13)",
		"url":"http://gank.io/2016/05/13"
	},
    ...
]
```

## The website included 

The website included is deployed to Heroku, [see the preview site](http://gankio.herokuapp.com/).

The simple website included in this project is just a page showing the statistics information about the ganks, besides that, it provides a web search API based on Lucene and these ganks.      
You can find out more interesting usages with the result data. :-)

1.Run `mvn exec:java -Dexec.mainClass="web.WebServer`   
2.Open `http://0.0.0.0:4567/` in your browser, and you will see this web page.

![image](gankio.png)

## The libraries used

Many famous open source libraries are used in this project, including `crawler4j`,  `fastjson`, `jsoup`, `velocity`, `spark` and so on.

Two tools are much more important, one is [dragnet](https://github.com/seomoz/dragnet), which is a Python library used to extract the content of a web page. The other is [sessiondb](https://github.com/ctriposs/sessdb), which is a Big, Fast, Persistent Key/Value Store based on a variant of LSM, you can find out more details about it [here](http://ctriposs.github.io/sessdb/).

## License

```
The MIT License (MIT)

Copyright (c) 2016 Hujiawei

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
