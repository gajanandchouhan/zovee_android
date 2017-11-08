package com.zoho.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.adapter.DrawerAdapter;
import com.zoho.app.fragment.AboutUsFragment;
import com.zoho.app.fragment.DeveloperDeskFragment;
import com.zoho.app.fragment.FeedbackFragment;
import com.zoho.app.fragment.HomeFragment;
import com.zoho.app.fragment.AskQuestionFragment;
import com.zoho.app.fragment.MyAccountFragment;
import com.zoho.app.fragment.SettingFragment;
import com.zoho.app.model.DrawerItem;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.DialogClickListnenr;
import com.zoho.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements DrawerAdapter.DrawerClickListener, View.OnClickListener {
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    RecyclerView mDrawerList;
    DrawerAdapter adapter;
    LinearLayout mainLayout;
    private ActionBar actionBar;
    private AdView mAdView;
    //SearchView searchView;
    int currentpos = 0;
    LinearLayout layoutDrawer;
    // MenuItem myActionMenuItem;
    private boolean doubleBackToExitPressedOnce = false;
    TextView toolbarTitle, welcomeTextView;
    private ImageView backImageView, profileImageView;
    TextView charTextView;
    private RelativeLayout headerLayout;

    ImageView imageViewNotification, imageViewSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindView();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        setAdapter();
        pushFragments(new HomeFragment());
        findViewById(R.id.textView_title).setVisibility(View.GONE);
    }

    private void setAdapter() {
        adapter = new DrawerAdapter(this, getDrawerItems(), this);
        mDrawerList.setAdapter(adapter);
    }

    private void bindView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        headerLayout = (RelativeLayout) findViewById(R.id.header_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.textView_title);
        charTextView = (TextView) findViewById(R.id.textView_char);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        profileImageView = (ImageView) findViewById(R.id.imageView);
        imageViewSearch = (ImageView) findViewById(R.id.search_imageView);
        mDrawerList = (RecyclerView) findViewById(R.id.left_drawer);
        welcomeTextView = (TextView) findViewById(R.id.textView_welcome);
        layoutDrawer = (LinearLayout) findViewById(R.id.layout_drawer);
       // imageViewFilter = (ImageView) findViewById(R.id.filter_imageView);
        imageViewNotification = (ImageView) findViewById(R.id.notification_imageView);
        imageViewNotification.setVisibility(View.VISIBLE);
     //   imageViewFilter.setVisibility(View.VISIBLE);
       // imageViewFilter.setOnClickListener(this);
        imageViewNotification.setOnClickListener(this);
        imageViewSearch.setOnClickListener(this);
        headerLayout.setOnClickListener(this);
        imageViewSearch.setVisibility(View.VISIBLE);
        mDrawerList.setLayoutManager(new LinearLayoutManager(this));
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()/*.addTestDevice("8844CD2195B98FF0C61FE1C753070338")*/.build();
        mAdView.loadAd(adRequest);
        String name = PrefManager.getInstance(this).getString(PrefConstants.NAME); /*+ " " + PrefManager.getInstance(this).getString(PrefConstants.LASTNAME);*/
        if (name.length() == 0) {
            name = "Guest";
            imageViewNotification.setVisibility(View.GONE);
            findViewById(R.id.login_imageView).setVisibility(View.VISIBLE);
            findViewById(R.id.login_imageView).setOnClickListener(this);
        }
        else{
            imageViewNotification.setVisibility(View.VISIBLE);
            findViewById(R.id.login_imageView).setVisibility(View.GONE);
        }
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        welcomeTextView.setText(String.format(getString(R.string.welcome_msg), name));
        String img = PrefManager.getInstance(this).getString(PrefConstants.IMAGE);
        if (img.equals("")) {
            charTextView.setText("" + name.charAt(0));
        } else {
            Picasso.with(this).load(img).into(profileImageView);
            charTextView.setVisibility(View.GONE);
        }

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private List<DrawerItem> getDrawerItems() {
        List<DrawerItem> drawerItemList = new ArrayList<DrawerItem>();
        drawerItemList.add(new DrawerItem(getString(R.string.home), R.drawable.home_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.help), R.drawable.help_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.share), R.drawable.share_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.feedback), R.drawable.feedback_selector));
        if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) != 0) {
            drawerItemList.add(new DrawerItem(getString(R.string.setting), R.drawable.feedback_selector));
        }
        drawerItemList.add(new DrawerItem(getString(R.string.filter), R.drawable.about_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.about_us), R.drawable.about_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.rate_us), R.drawable.rate_selector));
        drawerItemList.add(new DrawerItem(getString(R.string.dev_desk), R.drawable.developer_desk_selector));
        if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) != 0) {
            drawerItemList.add(new DrawerItem(getString(R.string.logout), R.drawable.feedback_selector));
        }
        return drawerItemList;
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.white));
        searchEditText.setHintTextColor(getResources().getColor(R.color.white));
        searchEditText.setCursorVisible(true);
        searchEditText.setBackgroundResource(R.drawable.search_box);
        searchEditText.setHint("Search");
        try {
            Field mDrawable = SearchView.class.getDeclaredField("mSearchHintIcon");
            mDrawable.setAccessible(true);
            Drawable drawable = (Drawable) mDrawable.get(searchView);
            drawable.setBounds(0, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Fragment fragment = getCurrentFragment();
                if (fragment instanceof HomeFragment) {
                    HomeFragment homeFragment = (HomeFragment) fragment;
                    homeFragment.filterCategoryList(s);
                }
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(layoutDrawer);
        }
        return super.onOptionsItemSelected(item);
    }

    public void pushFragments(Fragment fragment) {
       /* if (fragment instanceof HomeFragment) {
            imageViewFilter.setVisibility(View.VISIBLE);
        } else {
            imageViewFilter.setVisibility(View.GONE);
        }*/
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }

    @Override
    public void onClick(int position) {
        mDrawerLayout.closeDrawer(layoutDrawer);
        String title = getDrawerItems().get(position).getTitle();
       /* if (position == currentpos && currentpos != 2 && currentpos != 5) {
            return;
        }
        currentpos = position;
        //  myActionMenuItem.setVisible(false);
        if (position != 2 && position != 6) {
            adapter.setSelectedPosition(position);
            adapter.notifyDataSetChanged();
        }*/
        switch (title) {
            case "Home":
                toolbarTitle.setText(getString(R.string.app_name));
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
                backImageView.setVisibility(View.GONE);
                pushFragments(new HomeFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                // myActionMenuItem.setVisible(true);
                break;
            case "Help":
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeAsUpIndicator(null);
                backImageView.setVisibility(View.VISIBLE);
                toolbarTitle.setText(getString(R.string.help));
                pushFragments(new AskQuestionFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                break;
            case "Share":
                //  if (getCurrentFragment() instanceof HomeFragment)
                // myActionMenuItem.setVisible(true);
                shareApp();
                break;
            case "Feedback":
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeAsUpIndicator(null);
                backImageView.setVisibility(View.VISIBLE);
                toolbarTitle.setText(getString(R.string.feedback));
                pushFragments(new FeedbackFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                break;
            case "About Us":
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeAsUpIndicator(null);
                backImageView.setVisibility(View.VISIBLE);
                toolbarTitle.setText(getString(R.string.about_us));
                pushFragments(new AboutUsFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                break;
            case "Rate Us":
                //if (getCurrentFragment() instanceof HomeFragment)
                //  myActionMenuItem.setVisible(true);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantLib.APP_URL));
                startActivity(browserIntent);
                break;
            case "Developer's Desk":
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setHomeAsUpIndicator(null);
                backImageView.setVisibility(View.VISIBLE);
                toolbarTitle.setText(getString(R.string.dev_desk));
                pushFragments(new DeveloperDeskFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                break;
            case "Setting":
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.setHomeAsUpIndicator(null);
                    backImageView.setVisibility(View.VISIBLE);
                    toolbarTitle.setText(getString(R.string.setting));
                    pushFragments(new SettingFragment());
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();
                break;
            case "Logout":
                PrefManager.getInstance(MainActivity.this).logout();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityCompat.finishAffinity(MainActivity.this);
                finish();
               /* Utils.showAlert(MainActivity.this, getString(R.string.logout_msg), new DialogClickListnenr() {
                    @Override
                    public void onOkClick() {

                    }

                    @Override
                    public void onCancelClick() {

                    }
                });*/
                break;
            case "Filter":
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof HomeFragment) {
                    ((HomeFragment) currentFragment).showFilter();
                }
                break;
        }
    }

    private void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(getString(R.string.signup_msg));
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle("Need Signup").show();
    }

    private void shareApp() {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi. Now you can access Zoho quick learning videos on ZoVee. Download App here. :\n" + ConstantLib.APP_URL);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.frame);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getCurrentFragment();
        if (!(currentFragment instanceof HomeFragment)) {
            pushFragments(new HomeFragment());
            toolbarTitle.setText(getString(R.string.app_name));
            actionBar.setDisplayHomeAsUpEnabled(true);
            backImageView.setVisibility(View.GONE);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            // myActionMenuItem.setVisible(true);
            currentpos = 0;
            adapter.setSelectedPosition(0);
            adapter.notifyDataSetChanged();
            return;
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.filter_imageView:
                Fragment currentFragment = getCurrentFragment();
                if (currentFragment instanceof HomeFragment) {
                    ((HomeFragment) currentFragment).showFilter();
                }
                break;*/
            case R.id.notification_imageView:
                break;
            case R.id.header_layout:
                if (PrefManager.getInstance(this).getInt(PrefConstants.U_ID) == 0) {
                    return;
                }
                currentpos = -1;
                adapter.setSelectedPosition(-1);
                pushFragments(new MyAccountFragment());
                toolbarTitle.setText(getString(R.string.my_acnt));
                mDrawerLayout.closeDrawer(layoutDrawer);
                break;
            case R.id.search_imageView:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.login_imageView:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    public void refreshImage() {
        String img = PrefManager.getInstance(this).getString(PrefConstants.IMAGE);
        Picasso.with(this).load(img).into(profileImageView);
        charTextView.setVisibility(View.GONE);
    }
}
