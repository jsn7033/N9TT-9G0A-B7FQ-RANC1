package com.suraksha;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ActivitySwipeDetector implements View.OnTouchListener {

    static final String logTag = "ActivitySwipeDetector";
    private SwipeInterface activity;
    static final int MIN_DISTANCE = 100;
    private float downX,upX;

    public ActivitySwipeDetector(SwipeInterface activity){
        this.activity = activity;
    }

    public void onRightToLeftSwipe(View v){
        activity.right2left(v);
    }

    public void onLeftToRightSwipe(View v){
        activity.left2right(v);
    }

    

    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
           
            return true;
        }
        case MotionEvent.ACTION_UP: {
            upX = event.getX();
           
            float deltaX = downX - upX;
          

            // swipe horizontal?
            if(Math.abs(deltaX) > MIN_DISTANCE)
            {
                // left or right
                if(deltaX < 0) { this.onLeftToRightSwipe(v); return true; }
                if(deltaX > 0) { this.onRightToLeftSwipe(v); return true; }
            }
            else {
            }

          
        }
        }
        return false;
    }

}
