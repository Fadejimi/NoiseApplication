package layout;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adegbulugbe.noiseapplication.Detect_noise;
import com.example.adegbulugbe.noiseapplication.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final int POLL_INTERVAL = 300;

    /** running state **/
    private boolean mRunning = false;

    /** config state **/
    private int mThreshold;

    private PowerManager.WakeLock mWakelock;

    private Handler mHandler = new Handler();

    private TextView statusView, noiseView;

    private Detect_noise noiseSensor;
    private ProgressBar progressBar;

    private GraphView graph;
    private LineGraphSeries<DataPoint> mSeries1;
    //private LineGraphSeries<> mSeries2;
    private double graph2LastXValue = 5d;
    private Button stopButton;
    private Button restartButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_home, container, false);

        statusView = (TextView) layout.findViewById(R.id.status_textview);
        noiseView = (TextView) layout.findViewById(R.id.noise_textview);
        progressBar = (ProgressBar) layout.findViewById(R.id.progress_bar);
        graph = (GraphView) layout.findViewById(R.id.graph);
        mSeries1 = new LineGraphSeries<>();
        graph.addSeries(mSeries1);
        //graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(-20);
        graph.getViewport().setMaxX(100);

        stopButton = (Button) layout.findViewById(R.id.stop_button);
        restartButton = (Button) layout.findViewById(R.id.reset_button);

        noiseSensor = new Detect_noise();
        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Noise Alert");

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Noise record has been stopped", Toast.LENGTH_LONG).show();
                stop();
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Noise record has been restarted", Toast.LENGTH_LONG)
                        .show();
                start();
            }
        });
        return layout;
    }

    /****** define runnable thread and detect noise ****/
    private Runnable mSleepTask = new Runnable() {
        @Override
        public void run() {
            start();
        }
    };

    /****** create runnable to monitor voice *****/
    private Runnable mPollTask = new Runnable() {
        @Override
        public void run() {
            double amp = noiseSensor.getAmplitude();
            graph2LastXValue += 1d;
            mSeries1.appendData(new DataPoint(graph2LastXValue, amp), true, 100);
            updateDisplay("Monitoring Voice...", amp);

            if (amp > mThreshold) {
                callForHelp(amp);
            }
            mHandler.postDelayed(mPollTask, POLL_INTERVAL);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        initializeApplicationConstants();
        if (!mRunning) {
            mRunning = true;
            start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stop();
    }

    private void start() {
        noiseSensor.start();
        if (!mWakelock.isHeld()) {
            mWakelock.acquire();
        }

        mHandler.postDelayed(mPollTask, POLL_INTERVAL);
    }

    private void stop() {
        Log.i("Noise", "==== Stop Noise Monitoring ====");
        if (mWakelock.isHeld()) {
            mWakelock.release();
        }
        mHandler.removeCallbacks(mSleepTask);
        mHandler.removeCallbacks(mPollTask);
        noiseSensor.stop();
        progressBar.setProgress(0);
        updateDisplay("Stopped...", 0.0);
        mRunning = false;
    }

    private void initializeApplicationConstants() {
        mThreshold = 10;
    }

    private void updateDisplay(String st, double signalEMA) {
        statusView.setText(st);
        progressBar.setProgress((int) signalEMA);
        Log.d("Sound", String.valueOf(signalEMA));

        String result = String.format("%.2f", signalEMA) + "dB";
        Log.d("UPDATE DISPLAY", result);
        noiseView.setText(result);
    }

    private void callForHelp(double signalEMA) {
        Toast.makeText(getActivity(), "Noise Threshold crossed", Toast.LENGTH_SHORT).show();
        Log.d("Sound", String.valueOf(signalEMA));
        String result = String.format("%.2f", signalEMA) + "dB";

        noiseView.setText(result);
    }

    public HomeFragment() {
        // Required empty public constructor
    }

}
