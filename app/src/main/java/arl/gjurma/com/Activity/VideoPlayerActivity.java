package arl.gjurma.com.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import arl.gjurma.com.Extra.Constants;
import arl.gjurma.com.Gjurma;
import arl.gjurma.com.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by KRYTON on 11-10-2016.
 */
public class VideoPlayerActivity extends AppCompatActivity {
    @Bind(R.id.player_view)
    SimpleExoPlayerView playerView;
    /*@Bind(R.id.videoView1)
        VideoView videoView1;*/
    private String mVideoLink;
    private String TAG = "VideoPlayerActivity";
    InterstitialAd mInterstitialAd;
    private boolean isAddLoaded = false;
    private SimpleExoPlayer player;
    BandwidthMeter bandwidthMeter;
    LoadControl loadControl;
    private MappingTrackSelector trackSelector;
    DataSource.Factory dataSourceFactory;
    Handler mainHandler;

    // Code by James
    private class Timer extends Handler {
        @Override
        public void handleMessage(Message msg) {


            if(isBehind == false) {
                mInterstitialAd.loadAd(adRequest);
            }

            this.sendEmptyMessageDelayed(0, Constants.adLoadingDelay);
        }
    };

    private Timer adTimer;
    private boolean isBehind = false;
    AdRequest adRequest;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);

        /************* Admob Interstitial added here **********/

        if (!isAddLoaded) {
            isAddLoaded = true;


            mInterstitialAd = new InterstitialAd(this);

            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

            adRequest = new AdRequest.Builder()
                    .build();
            // Load ads into Interstitial Ads , Modified By James
            //Gjurma global = (Gjurma)(this.getApplication());
            //if(global.isReadyToLoadAds() == true) {
            //mInterstitialAd.loadAd(adRequest);
            //    global.triggerTimer();
            //}

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });


            //Code by James
            adTimer = new Timer();
            adTimer.sendEmptyMessage(0);
            /*****************************************************/
        }


        /*****************************************************/

        mVideoLink = getIntent().getStringExtra("link");
        Log.e(TAG, "Link=" + mVideoLink);
       /* try {
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView1);
            Uri video = Uri.parse(mVideoLink);
            videoView1.setMediaController(mediaController);
            videoView1.setVideoURI(video);
            videoView1.start();
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }*/

        Uri uri =  Uri.parse(mVideoLink);
        mainHandler = new Handler();
        bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        loadControl = new DefaultLoadControl();

        player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, ""), (TransferListener<? super DataSource>) bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(uri,
                dataSourceFactory, extractorsFactory, null, null);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        playerView.setPlayer(player);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    //Code by James

    @Override
    protected void onPause() {

        this.isBehind = true;

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.isBehind = false;
    }
    ////////////////
}
