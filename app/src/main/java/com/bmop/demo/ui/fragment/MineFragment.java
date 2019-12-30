package com.bmop.demo.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmop.demo.R;
import com.bmop.demo.data.PersonalData;
import com.bmop.demo.manager.UserManager;
import com.bmop.demo.ui.activity.MainActivity;
import com.bmop.demo.ui.adapter.MineAdapter;
import com.bmop.demo.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jsc.kit.wheel.dialog.DateTimeWheelDialog;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private ImageView avatarBtn;
    private Button cancelBtn;
    private Button galleryBtn;
    private Button photoBtn;
    private PopupWindow popupWindow;
    private RecyclerView rv;
    private MineAdapter adapter;
    private List<PersonalData> dataList = new ArrayList<PersonalData>();
    private DateTimeWheelDialog timeDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        avatarBtn = view.findViewById(R.id.avatar_btn);
        avatarBtn.setOnClickListener(this);

        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(context));
        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getResources().getDrawable(R.drawable.rv_divider));
        rv.addItemDecoration(divider); // 添加分割线
        String[] array = getResources().getStringArray(R.array.personal_data);
        for (String title : array) {
            dataList.add(new PersonalData(title));
        }
        adapter = new MineAdapter(context, dataList, R.layout.layout_mine_item);
        rv.setAdapter(adapter);

        initPopWindow();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar_btn:
                showPopWindow();
                break;
            case R.id.cancel_btn:
                hidePopWindow();
                break;
            case R.id.photo_btn:
                ((MainActivity) context).dispatchTakePictureIntent();
                hidePopWindow();
                break;
            case R.id.gallery_btn:
                ((MainActivity) context).dispatchPickImageIntent();
                hidePopWindow();
                break;
            default:
                break;
        }
    }

    private void initPopWindow() {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_avatar_pop, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.PopWindowStyle);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        cancelBtn = view.findViewById(R.id.cancel_btn);
        galleryBtn = view.findViewById(R.id.gallery_btn);
        photoBtn = view.findViewById(R.id.photo_btn);
        cancelBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        photoBtn.setOnClickListener(this);
    }

    private void showPopWindow() {
        popupWindow.showAtLocation(avatarBtn, Gravity.BOTTOM, 0, 0);
    }

    private void hidePopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void updateAvatar(final String photoPath) {
        if (TextUtils.isEmpty(photoPath)) {
            return;
        }
        ImageLoader.getInstance().loadImage("file://" + photoPath, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null && !loadedImage.isRecycled()) {
                    Bitmap icon = createCircleIcon(loadedImage);
                    avatarBtn.setImageBitmap(icon);

                    ((MainActivity)context).uploadFile(photoPath, new UserManager.OnUploadListener() {
                        @Override
                        public void onUploadSuccess(String url) {
                            Logger.e("url: " + url);
                        }

                        @Override
                        public void onUploadFailed(int errorCode) {

                        }
                    });
                }
            }
        });
    }

    private Bitmap createCircleIcon(Bitmap source) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int width = source.getWidth();
        int height = source.getWidth();
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle((float) width / 2, (float) height / 2, (float) width / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    private void showTimeDialog() {
        if (timeDialog != null) {
            timeDialog.show();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1990);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = calendar.getTime();

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        Date endDate = calendar.getTime();

        timeDialog = new DateTimeWheelDialog(context);
        timeDialog.show();
        timeDialog.setTitle(getString(R.string.select_time));
        int config = DateTimeWheelDialog.SHOW_YEAR_MONTH_DAY;
        timeDialog.configShowUI(config);
        timeDialog.setCancelButton(getString(R.string.cancel), null);
        timeDialog.setOKButton(getString(R.string.confirm), new DateTimeWheelDialog.OnClickCallBack() {
            @Override
            public boolean callBack(View v, @NonNull Date selectedDate) {
                Logger.e(SimpleDateFormat.getInstance().format(selectedDate));
//                tvResult.setText(SimpleDateFormat.getInstance().format(selectedDate));
                return false;
            }
        });
        timeDialog.setDateArea(startDate, endDate, true);
        timeDialog.updateSelectedDate(new Date());
    }
}
