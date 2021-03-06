package com.eomsbd.cutprice.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.eomsbd.cutprice.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AppUtil {

    private static String TAG = AppUtil.class.getSimpleName();

    public static <T> ArrayList<T> convertToArrayList(T[] objectArray) {
        return new ArrayList<T>(Arrays.asList(objectArray));
    }

    public static boolean hasNavigationBar(Context context) {
        boolean hasSoftwareKeys = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            hasSoftwareKeys = (realWidth > displayWidth) || (realHeight > displayHeight);
        } else {
            boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            hasSoftwareKeys = !hasMenuKey && !hasBackKey;
        }
        return hasSoftwareKeys;
    }

    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display d = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;

            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            int displayWidth = displayMetrics.widthPixels;

            navigationBarHeight = (realHeight > displayHeight) ? (realHeight - displayHeight) : 0;
        }
        return navigationBarHeight;
    }

    public static String get12HourTime(int hourOfDay, int minute, int second) {
        int hour = hourOfDay;
        int minutes = minute;
        String timeSet = "";
        if (hour > 12) {
            hour -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            hour += 12;
            timeSet = "AM";
        } else if (hour == 12) {
            timeSet = "PM";
        } else {
            timeSet = "AM";
        }

        String hr = "";
        if (hour < 10)
            hr = "0" + hour;
        else
            hr = String.valueOf(hour);
        String min = "";
        if (minutes < 10)
            min = "0" + minutes;
        else
            min = String.valueOf(minutes);
        String sec = "";
        if (second < 10)
            sec = "0" + second;
        else
            sec = String.valueOf(second);

        // Append in a StringBuilder
        String aTime = new StringBuilder()
                .append(hr).append(':')
                .append(min).append(':')
                .append(sec)
                .append(" ")
                .append(timeSet)
                .toString();
        return aTime;
    }

    public static boolean isSimSupport(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);
        }
        return false;
    }

    public static String convertToCamelCase(String str) {
        if (str == null) {
            return null;
        }

        boolean space = true;
        StringBuilder builder = new StringBuilder(str);
        final int len = builder.length();

        for (int i = 0; i < len; ++i) {
            char c = builder.charAt(i);
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    space = false;
                }
            } else if (Character.isWhitespace(c)) {
                space = true;
            } else {
                builder.setCharAt(i, Character.toLowerCase(c));
            }
        }

        return builder.toString();
    }


    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    //Toolbar
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int toPx(Context context, int value) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (value * density);
    }

    public static void doMarqueeTextView(TextView textView) {
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setSelected(true);
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.item_product_width);
        return Math.round(screenWidth / cellWidth);
    }

    public static String formatLocationInfo(String myString) {
        String location = "";
        if (myString != null && myString.trim().length() > 0) {
            location = myString.startsWith(",") ? myString.substring(1).trim().replaceAll(", ,", ",") : myString.replaceAll(", ,", ",");
        }
        return location;
    }

/*
    public static <T> void loadImage(Context context, ImageView imageView, T imageSource, boolean isGif, boolean isRoundedImage, boolean isPlaceHolder) {
        try {
            RequestManager requestManager = Glide.with(context);
            RequestBuilder requestBuilder = isGif ? requestManager.asGif() : requestManager.asBitmap();

            //Dynamic loading without caching while update need for each time loading
//            requestOptions.signature(new ObjectKey(System.currentTimeMillis()));
            //If Cache needed
//            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

            //If Cache needed
            RequestOptions requestOptionsCache = new RequestOptions();
            requestOptionsCache.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            requestBuilder.apply(requestOptionsCache);

            //For placeholder
            if (isPlaceHolder) {
                RequestOptions requestOptionsPlaceHolder = new RequestOptions();
                requestOptionsPlaceHolder.placeholder(R.mipmap.ic_launcher);
                requestBuilder.apply(requestOptionsPlaceHolder);
            }

            //For error
            RequestOptions requestOptionsError = new RequestOptions();
            requestOptionsError.error(R.mipmap.ic_launcher);
            requestBuilder.apply(requestOptionsError);

            //For rounded image
            if (isRoundedImage) {
                RequestOptions requestOptionsRounded = new RequestOptions();
                requestOptionsRounded.circleCrop();
                requestOptionsRounded.autoClone();
                requestBuilder.apply(requestOptionsRounded);
            }

            //Generic image source
            T mImageSource = null;
            if (imageSource instanceof String) {
                if (!AllSettingsManager.isNullOrEmpty((String) imageSource)) {
                    mImageSource = imageSource;
                }
            } else if (imageSource instanceof Integer) {
                mImageSource = imageSource;
            }
            requestBuilder.load((mImageSource != null) ? mImageSource : R.mipmap.ic_launcher);

            //Load into image view
            requestBuilder.into(imageView);

//            Glide
//                    .with(context)
//                    .asBitmap()
//                    .load((mImageSource != null) ? mImageSource : R.mipmap.ic_launcher)
//                    .apply(requestOptions)
//                    .into(imageView);

        } catch (Exception e) {
        }
    }
*/

    public static ValueAnimator flashView(final View viewGroup, long time) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewGroup.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration(time);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(-1);
        animator.start();
        return animator;
    }


    public static void makeFlyAnimation(Activity activity, View sourceView, View sourceViewCopy, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {
        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(sourceViewCopy)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }

    public static RotateAnimation makeRotateAnimation(final View view, final int rotationCount, final BaseUpdateListener baseUpdateListener) {
        int previousDegrees = 0;
        int degrees = 360;
        final RotateAnimation animation = new RotateAnimation(previousDegrees, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            int count = 0;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (rotationCount > 0) {
                    count++;
                    if (count == rotationCount) {
                        if (baseUpdateListener != null) {
                            baseUpdateListener.onUpdate(true);
                        }
                    } else {
                        view.startAnimation(animation);
                    }
                } else {
                    view.startAnimation(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
        return animation;
    }

    public static void makeFlyAnimation(Activity activity, View sourceView, Bitmap sourceViewBitmap, View destinationView, int timeMilliSecond, Animator.AnimatorListener animatorListener) {

        //Create a copy view
        ImageView animImg = new ImageView(activity);
//        Bitmap bm = ((BitmapDrawable) sourceView.getDrawable()).getBitmap();
        animImg.setImageBitmap(sourceViewBitmap);

        ViewGroup anim_mask_layout = CircleAnimationUtil.createAnimLayout(activity);
        anim_mask_layout.addView(animImg);

        int[] startXY = new int[2];
        sourceView.getLocationInWindow(startXY);
        final View v = CircleAnimationUtil.addViewToAnimLayout(activity, animImg, startXY, true);
        if (v == null) {
            return;
        }

        new CircleAnimationUtil().attachActivity(activity)
                .setTargetView(sourceView)
                .setTargetViewCopy(v)
                .setMoveDuration(timeMilliSecond)
                .setDestView(destinationView)
                .setAnimationListener(animatorListener).startAnimation();
    }


}
