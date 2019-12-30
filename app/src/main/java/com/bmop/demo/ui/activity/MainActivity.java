package com.bmop.demo.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bmop.demo.R;
import com.bmop.demo.ui.fragment.BaseFragment;
import com.bmop.demo.ui.fragment.FriendsFragment;
import com.bmop.demo.ui.fragment.MineFragment;
import com.bmop.demo.ui.fragment.TrendFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private TextView headerTitle;
    private ImageView headerImg;
    private FrameLayout content;
    private BottomNavigationView navigationView;

    private Class<?>[] FRAGMENTS = {FriendsFragment.class, TrendFragment.class, MineFragment.class};
    private FragmentManager mFManager;
    private BaseFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFManager = getSupportFragmentManager();

        initViews();
    }

    private void initViews() {
        headerTitle = findViewById(R.id.header_title);
        headerImg = findViewById(R.id.header_img);
        content = findViewById(R.id.content);
        navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);

        changeFrag(FRAGMENTS[0]);
    }

    private void changeFrag(Class<?> fragmentClazz) {
        // hide all fragment
        FragmentTransaction transaction = mFManager.beginTransaction();
        for (Class<?> fc : FRAGMENTS) {
            /*if (fc == fragmentClazz) {
                continue;
            }*/
            Fragment fragment = mFManager.findFragmentByTag(fc
                    .getCanonicalName());
            if (fragment != null) {
                transaction.hide(fragment);
            }
        }

        Fragment lastFragment = mFManager.findFragmentByTag(fragmentClazz.getCanonicalName());
        if (lastFragment == null) {
            try {
                lastFragment = (Fragment) fragmentClazz.newInstance();
                mCurrentFragment = (BaseFragment) lastFragment;
                transaction.add(R.id.content, lastFragment,
                        fragmentClazz.getCanonicalName());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            transaction.show(lastFragment);
            mCurrentFragment = (BaseFragment) lastFragment;
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.friends:
                changeFrag(FRAGMENTS[0]);
                headerTitle.setText(R.string.friends);
                headerImg.setVisibility(View.VISIBLE);
                break;
            case R.id.trend:
                changeFrag(FRAGMENTS[1]);
                headerTitle.setText(R.string.trend);
                headerImg.setVisibility(View.GONE);
                break;
            case R.id.mine:
                changeFrag(FRAGMENTS[2]);
                headerTitle.setText(R.string.mine);
                headerImg.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                if (mCurrentFragment instanceof MineFragment) {
                    ((MineFragment) mCurrentFragment).updateAvatar(getCurrentPhotoPath());
                }
            } else if (requestCode == REQUEST_PICK_IMAGE) {
                if (data != null) {
                    Uri imgUri = data.getData();
                    currentPhotoPath = getPath(imgUri);
                    ((MineFragment) mCurrentFragment).updateAvatar(getCurrentPhotoPath());
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String path;
        Cursor cursor = getContentResolver().query(uri,
                new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if (null == cursor) {
            return null;
        }
        cursor.moveToFirst();
        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }
}
