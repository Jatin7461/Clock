package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.OnSwipe;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MotionEventCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clock.Fragments.SwipeAlarm;
import com.example.clock.provider.AlarmContract;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnTouchListener {


    //    private SwipeAlarmAdapter adapter;
    private static final String TAG = AlarmActivity.class.getName();
    private final int DAY = 24 * 60 * 60 * 1000;
    Ringtone r;
    Vibrator vibrator;
    TextView date;

    float dY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                dY = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (view.getY() < -200) {
                    view.animate().translationY(-500);
                    finish();
                }
                if (event.getRawY() + dY < 0) {
                    view.setY(event.getRawY() + dY);
                }
                break;
            default:
                return false;
        }
        view.animate().translationY(0);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_POWER) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.alarm);
        layout.setOnTouchListener(this);

        Intent intent = getIntent();
        Log.v(TAG, intent.toString());
        int code = intent.getIntExtra(AlarmContract.AlarmEntry._ID, -1);
        boolean multiple = intent.getBooleanExtra("multiple", false);
        int requestCode = intent.getIntExtra("code", -1);
//        int hour = intent.getIntExtra(AlarmContract.AlarmEntry.HOUR, -1);
//        int min = intent.getIntExtra(AlarmContract.AlarmEntry.MIN, -1);

        Calendar calendar1 = Calendar.getInstance();
        int dayNumber = calendar1.get(Calendar.DAY_OF_WEEK);
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        int min = calendar1.get(Calendar.MINUTE);
        date = findViewById(R.id.date);
        //set today's date
        setDate();

        TextView day = findViewById(R.id.day);
        if (dayNumber == 1) {
            day.setText(getResources().getString(R.string.day1));
        } else if (dayNumber == 2) {
            day.setText(getResources().getString(R.string.day2));
        } else if (dayNumber == 3) {
            day.setText(getResources().getString(R.string.day3));
        } else if (dayNumber == 4) {
            day.setText(getResources().getString(R.string.day4));
        } else if (dayNumber == 5) {
            day.setText(getResources().getString(R.string.day5));
        } else if (dayNumber == 6) {
            day.setText(getResources().getString(R.string.day6));
        } else {
            day.setText(getResources().getString(R.string.day7));
        }
        String h = String.format("%02d", hour);
        String m = String.format("%02d", min);

        TextView time = findViewById(R.id.time);

        time.setText(h + ":" + m);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {

            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this, null);
        } else {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(new long[]{1000, 1000}, 0));

            } else {
                //deprecated in API 26
                long a[] = new long[]{1000, 1000};

                vibrator.vibrate(new long[]{1000, 1000}, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

//        Intent i = new Intent(ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder build = new NotificationCompat.Builder(this, "hello")
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentTitle("alarm")
//                .setContentText("alarm")
//                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher_background))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setVibrate(new long[]{1000, 1000, 1000, 1000})
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setFullScreenIntent(pendingIntent, true);
//
//        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
//        manager.notify(1, build.build());


        //if the alarm is repeating then do not update that alarm is inactive
        if (multiple == false) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(AlarmContract.AlarmEntry.ACTIVE, 0);
            if (code != -1) {

                Uri uri = ContentUris.withAppendedId(AlarmContract.AlarmEntry.CONTENT_URI, code);
                getApplicationContext().getContentResolver().update(uri, contentValues, null, null);
            }
        } else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent1 = new Intent(getApplicationContext(), AlarmActivity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.DAY_OF_WEEK, requestCode / 10000);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), requestCode, intent1, 0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + DAY * 7, pendingIntent);

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        }, 30000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        r.stop();
        vibrator.cancel();


    }

    @Override
    protected void onResume() {
        super.onResume();
        vibrator.vibrate(new long[]{1000, 1000}, 0);
    }

    //    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getActionMasked();
//        if (action == MotionEvent.ACTION_BUTTON_PRESS) {
//            Log.v(TAG, "tapping");
//        } else if (action == MotionEvent.ACTION_SCROLL) {
//            Log.v(TAG, "swiping");
//            finish();
//            return true;
//        }
////        if(action == MotionEvent.ACTION_MOVE){
////
////        }
//        else {
//            Log.v(TAG, "nothing done");
//
//        }
//
//        return false;
//    }


    public class OnSwipeTouchListener implements View.OnTouchListener {
        private final GestureDetector gestureDetector;
        Context context;

        OnSwipeTouchListener(Context ctx, View mainView) {
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            mainView.setOnTouchListener(this);
            context = ctx;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        public class GestureListener extends
                GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();

                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {

                        } else {

                            finish();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }


    }

    //method to set today's date
    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        String alarmDate = new String();
        switch (month) {
            case 0:
                alarmDate += getResources().getString(R.string.jan);
                break;
            case 1:
                alarmDate += getResources().getString(R.string.feb);
                break;
            case 2:
                alarmDate += getResources().getString(R.string.mar);
                break;
            case 3:
                alarmDate += getResources().getString(R.string.april);
                break;
            case 4:
                alarmDate += getResources().getString(R.string.may);
                break;
            case 5:
                alarmDate += getResources().getString(R.string.june);
                break;

            case 6:
                alarmDate += getResources().getString(R.string.july);
                break;
            case 7:
                alarmDate += getResources().getString(R.string.aug);
                break;
            case 8:
                alarmDate += getResources().getString(R.string.sep);
                break;
            case 9:
                alarmDate += getResources().getString(R.string.oct);
                break;
            case 10:
                alarmDate += getResources().getString(R.string.nov);
                break;
            case 11:
                alarmDate += getResources().getString(R.string.dec);
                break;
            default:
                Log.v(TAG, "wrong month");

        }
        alarmDate += " " + dateOfMonth;
        date.setText(alarmDate);

    }


}


