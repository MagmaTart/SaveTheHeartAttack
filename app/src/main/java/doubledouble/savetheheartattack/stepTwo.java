package doubledouble.savetheheartattack;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by leesm10413 on 2016-09-03.
 */
public class stepTwo extends AppCompatActivity {
    VideoView videoview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_two);

        Uri video = Uri.parse("android.resource://" + getPackageName() + "/raw/vdvd");
        videoview = (VideoView) findViewById(R.id.videoview);
        videoview.setVideoURI(video);
        //videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.vddd));
        // videoview.setVideoPath(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video_file));

        final MediaController mc = new MediaController(stepTwo.this);
        videoview.setMediaController(mc);
        videoview.postDelayed(new Runnable() {
            @Override
            public void run() {
                mc.show(0);
            }
        }, 100);

    }
}
