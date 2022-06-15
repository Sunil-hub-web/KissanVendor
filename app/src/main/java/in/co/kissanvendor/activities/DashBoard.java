package in.co.kissanvendor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import in.co.kissanvendor.R;
import in.co.kissanvendor.extras.SessionManager;
import in.co.kissanvendor.fragments.HomeFragment;
import in.co.kissanvendor.fragments.MyAccountFragment;
import in.co.kissanvendor.fragments.MyOrdersFragment;
import in.co.kissanvendor.fragments.PaymentFragment;
import in.co.kissanvendor.fragments.ProductListFragment;
import in.co.kissanvendor.fragments.SupplierFragment;

import com.google.android.material.navigation.NavigationView;


public class DashBoard extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout fl;
    RelativeLayout header;
    ImageView menu, search_icon;
    private Boolean exit = false;
    SessionManager session;
    public static TextView pagetitle, dashboard, supplier, orders, productlisting, paymenthistory, myaccount, logout, username, userlocation,text_Close;
    public static View dashboardview, supplierview, ordersview, productlistingview, paymenthistoryview, myaccountview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        session = new SessionManager(this);

        InIt();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    public void InIt(){

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        header = findViewById(R.id.header);
        fl = findViewById(R.id.nav_host_fragment);

        menu = (ImageView) findViewById(R.id.menu);
        search_icon = (ImageView) findViewById(R.id.search_icon);

        pagetitle = findViewById(R.id.pagetitle);
        dashboard = findViewById(R.id.dashboard);
        supplier = findViewById(R.id.supplier);
        orders = findViewById(R.id.orders);
        productlisting = findViewById(R.id.productlisting);
        paymenthistory = findViewById(R.id.paymenthistory);
        myaccount = findViewById(R.id.myaccount);
        logout = findViewById(R.id.logout);
        username = findViewById(R.id.username);
        userlocation = findViewById(R.id.userlocation);

        dashboardview = findViewById(R.id.dashboardview);
        supplierview = findViewById(R.id.supplierview);
        ordersview = findViewById(R.id.ordersview);
        productlistingview = findViewById(R.id.productlistingview);
        paymenthistoryview = findViewById(R.id.paymenthistoryview);
        myaccountview = findViewById(R.id.myaccountview);
        text_Close = findViewById(R.id.text_Close);

        username.setText(session.getUserName());
        userlocation.setText(session.getCity());

        dashboard.setOnClickListener(this);
        supplier.setOnClickListener(this);
        orders.setOnClickListener(this);
        productlisting.setOnClickListener(this);
        paymenthistory.setOnClickListener(this);
        myaccount.setOnClickListener(this);
        logout.setOnClickListener(this);
        text_Close.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        HomeFragment test = (HomeFragment) getSupportFragmentManager().findFragmentByTag("HomeFragment");

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (exit) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
        }

    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        Bundle args;
        switch (v.getId()) {
            case R.id.dashboard:
                setDefault();
                dashboardview.setVisibility(View.VISIBLE);
                dashboard.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new HomeFragment();
                callFragment(fragment);

                pagetitle.setText("Dashboard");
                header.setBackgroundColor(getResources().getColor(R.color.white));
                menu.setColorFilter(getResources().getColor(R.color.black));
                search_icon.setColorFilter(getResources().getColor(R.color.black));
                pagetitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.supplier:
                setDefault();
                supplierview.setVisibility(View.VISIBLE);
                supplier.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new SupplierFragment();
                callFragment(fragment);
                pagetitle.setText("Supplier");
                header.setBackgroundColor(getResources().getColor(R.color.white));
                menu.setColorFilter(getResources().getColor(R.color.black));
                search_icon.setColorFilter(getResources().getColor(R.color.black));
                pagetitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.orders:
                setDefault();
                ordersview.setVisibility(View.VISIBLE);
                orders.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new MyOrdersFragment();
                callFragment(fragment);
                pagetitle.setText("My Orders");
                header.setBackgroundColor(getResources().getColor(R.color.white));
                menu.setColorFilter(getResources().getColor(R.color.black));
                search_icon.setColorFilter(getResources().getColor(R.color.black));
                pagetitle.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;

            case R.id.productlisting:
                setDefault();
                productlistingview.setVisibility(View.VISIBLE);
                productlisting.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new ProductListFragment();
                callFragment(fragment);

                pagetitle.setText("Products");
                header.setBackgroundColor(getResources().getColor(R.color.white));
                menu.setColorFilter(getResources().getColor(R.color.black));
                search_icon.setColorFilter(getResources().getColor(R.color.black));
                pagetitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.paymenthistory:
                setDefault();
                paymenthistoryview.setVisibility(View.VISIBLE);
                paymenthistory.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new PaymentFragment();
                callFragment(fragment);

                pagetitle.setText("Payment History");
                header.setBackgroundColor(getResources().getColor(R.color.white));
                menu.setColorFilter(getResources().getColor(R.color.black));
                search_icon.setColorFilter(getResources().getColor(R.color.black));
                pagetitle.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;

            case R.id.myaccount:
                setDefault();
                myaccountview.setVisibility(View.VISIBLE);
                myaccount.setTextColor(getResources().getColor(R.color.colorPrimary));
                drawerLayout.closeDrawer(GravityCompat.START);
                fragment = new MyAccountFragment();
                callFragment(fragment);

                pagetitle.setText("My Account");
                header.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                menu.setColorFilter(getResources().getColor(R.color.white));
                search_icon.setColorFilter(getResources().getColor(R.color.white));
                pagetitle.setTextColor(getResources().getColor(R.color.white));
                break;

            case R.id.logout:

                    final CharSequence[] items = {"Yes", "Cancel"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
                    builder.setTitle("Are you sure,\nyou want to LOGOUT ?");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                            if (items[item].equals("Yes")) {

                                dialog.dismiss();
                                session.logoutUser();

                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                break;

            case R.id.text_Close:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

    }

    public void callFragment(Fragment fragmentClass) {
        fl.removeAllViews();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragmentClass).addToBackStack("adds").commit();

    }

    public void setDefault(){

        dashboardview.setVisibility(View.GONE);
        supplierview.setVisibility(View.GONE);
        ordersview.setVisibility(View.GONE);
        productlistingview.setVisibility(View.GONE);
        paymenthistoryview.setVisibility(View.GONE);
        myaccountview.setVisibility(View.GONE);

        dashboard.setTextColor(getResources().getColor(R.color.navtextcolor));
        supplier.setTextColor(getResources().getColor(R.color.navtextcolor));
        orders.setTextColor(getResources().getColor(R.color.navtextcolor));
        productlisting.setTextColor(getResources().getColor(R.color.navtextcolor));
        paymenthistory.setTextColor(getResources().getColor(R.color.navtextcolor));
        myaccount.setTextColor(getResources().getColor(R.color.navtextcolor));

    }

}