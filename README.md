Android UniversalVideoView

在Android上播放视频最简单的方法是使用SDK中内置的VideoView,然后加上MediaController来控制视频播放暂停等,
但是这样有一个缺点是无法定制自己的控制UI,所以这里提供一个自定义播放控件,它可以设置多种自定义属性,并且很容易在全屏与非全屏之间切换,
另外支持Android V2.3及以上系统.

先上图



使用方法

1. 在build.gradle文件上加入以下依赖包.
            dependencies {
                compile 'com.linsea:universalvideoview:1.0.0@aar'
            }

2. 在布局文件中加入自定义View,注意要使UniversalVideoView和UniversalMediaController位于同一个父Layout中, 这样控制条才会浮在视频之上.

            <FrameLayout
                android:id="@+id/video_layout"
                android:layout_width="fill_parent"
                android:layout_height="200dp"
                android:background="@android:color/black">

                <com.universalvideoview.UniversalVideoView
                    android:id="@+id/videoView"
                    android:layout_width="fill_parent"
                    android:layout_height="fill_parent"
                    android:layout_gravity="center"
                    app:uvv_fitXY="false" />

                <com.universalvideoview.UniversalMediaController
                    android:id="@+id/media_controller"
                    android:layout_width="fill_parent"
                    android:layout_height="fill_parent"
                    app:uvv_scalable="true" />

            </FrameLayout>

3. 在onCreate方法中设置相关事件的监听.

View mBottomLayout;
View mVideoLayout;
UniversalVideoView mVideoView;
UniversalMediaController mMediaController;

mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
mVideoView.setMediaController(mMediaController);

mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mVideoLayout.setLayoutParams(layoutParams);
            //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
            mBottomLayout.setVisibility(View.GONE);
        } else {
            ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = this.cachedHeight;
            mVideoLayout.setLayoutParams(layoutParams);
            mBottomLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) { // 视频暂停
        Log.d(TAG, "onPause UniversalVideoView callback");
    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) { // 视频开始播放或恢复播放
        Log.d(TAG, "onStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {// 视频开始缓冲
        Log.d(TAG, "onBufferingStart UniversalVideoView callback");
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {// 视频结束缓冲
        Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
    }

});

注意:
UniversalVideoView 没有保存播放的状态,如播放到第几分钟了,所以需要应用自己保存这些状态并恢复.
如果为了保证在旋转屏幕时系统重启Activity,需要添加Activity的属性:
android:configChanges="orientation|keyboardHidden|screenSize

4 定制属性
为了保证定制UI的灵活度,提供以下属性:
uvv_fitXY UniversalVideoView的属性,布尔值,true时设置视频缩放时在X,Y方向上铺满View设置的宽度和高度,这样可能使视频变形.false时则缩放时保持视频的长宽比例,与SDK中的VideoView类似.
uvv_scalable UniversalMediaController属性,布尔值,是否显示控制条右下方的缩放按钮,如果不想全屏播放时,可以设置为false不显示.

