本Demo是在作者Yrom的项目 [ScreenRecorder](https://github.com/yrom/ScreenRecorder) 的基础上额外增加了悬浮窗和通知栏等特性，详情见我的博文[Android实现录屏直播（二）需求才是硬道理之产品功能调研](http://blog.csdn.net/zxccxzzxz/article/details/54254244)，而后又参考了 LakeinChina 作者的 [librestreaming](https://github.com/lakeinchina/librestreaming) 实现推流，目前正在逐步完善中。

## Updated 7.12

年初买的阿里云的ECS服务器**已经到期**了,所以大家无法使用之前的IP进行测试了,请大家自行搭建服务器使用吧~

## Updated 5.31

工作较忙，一直没时间接着干活，终于抽了一晚上来凑合集成了音频，大家可以试试。

## Updated 4.25

目前只实现了录屏的 Video 推流，有朋友提到音频迟迟没有，还有摄像头采集也将纳入计划。

### TODO LIST

- 加入摄像头采集及推流（包含拍摄直播、录屏直播两种功能，目前 Camera 还不能用，需要完善）

- 代码重构，改善代码的可读性，并且往 SDK 的方向推进


- [x] ​ 加入音频采集及推流 (原谅我直接用的 librestreaming 里面的音频采集及软解码的代码，太忙了没时间自己搞)

- [x] ​ 修复视频端对端延迟过长的问题（目前测试 3~6 s 左右，如果还有问题的可以 M 我）


------

## Updated 3.12
Demo 中集成了录屏直播推流的功能，内含个人自己搭建的 Nginx + rtmp 流媒体服务器,去掉了录屏存入本地文件的部分代码，之后会重新建项目完全移植过去，并且计划拥有滤镜和OpenGL来控制帧率等功能。
需要的朋友可以自行使用，推流格式：[rtmp://59.130.110.19:1935/live/yourstreamingkey](rtmp://59.130.110.19:1935/live/yourstreamingkey)
### 仿Bilibili悬浮窗与通知栏
![效果图](https://raw.githubusercontent.com/eterrao/ScreenRecorder/master/images/screenRecorderDemo.gif)


### 录屏推流直播
![推流效果图](https://raw.githubusercontent.com/eterrao/ScreenRecorder/master/images/ScreenRecorderDemo.jpeg)


Screen Recorder
=====
> 这是个 DEMO APP 主要是实现了屏幕录制功能。
>
> 通过使用 [MediaProjectionManager](https://developer.android.com/reference/android/media/projection/MediaProjectionManager.html), [VirtualDisplay](https://developer.android.com/reference/android/hardware/display/VirtualDisplay.html), [MediaCodec](http://developer.android.com/reference/android/media/MediaCodec.html) 以及 [MediaMuxer](http://developer.android.com/reference/android/media/MediaMuxer.html) 等API，故而这个项目仅支持Android 5.0。
>
> 
>
> # 原理
>
> - `Display` 可以“投影”到一个 `VirtualDisplay`
> - 通过 `MediaProjectionManager` 取得的 `MediaProjection`创建`VirtualDisplay`
> - `VirtualDisplay` 会将图像渲染到 `Surface`中，而这个`Surface`是由`MediaCodec`所创建的
>
> ```
> mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
> ...
> mSurface = mEncoder.createInputSurface();
> ...
> mVirtualDisplay = mMediaProjection.createVirtualDisplay(name, mWidth, mHeight, mDpi, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mSurface, null, null);
> ```
>
> - `MediaMuxer` 将从 `MediaCodec` 得到的图像元数据封装并输出到MP4文件中
>
> ```
> int index = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_US);
> ...
> ByteBuffer encodedData = mEncoder.getOutputBuffer(index);
> ...
> mMuxer.writeSampleData(mVideoTrackIndex, encodedData, mBufferInfo);
> ```
>
> 所以其实在**Android 4.4**上可以通过`DisplayManager`来创建`VirtualDisplay`也是可以实现录屏，但因为权限限制需要**ROOT**。 (see [DisplayManager.createVirtualDisplay()][1])
>
> [1]: https://developer.android.com/reference/android/hardware/display/DisplayManager.html
>
> 
