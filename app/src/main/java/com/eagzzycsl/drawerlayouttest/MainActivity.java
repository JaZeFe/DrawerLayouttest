package com.eagzzycsl.drawerlayouttest;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends ActionBarActivity {
    private Toolbar toolbar_main,toolbar_main0;
    private DrawerLayout drawerLayout_main;
    private ListView listView_menu;
    private TextView textView_test;
    private RelativeLayout relativeLayout_main;
    private CalendarView calendarView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar_main = (Toolbar) this.findViewById(R.id.toolbar_main);
        textView_test=(TextView)this.findViewById(R.id.textView_test);
        calendarView = (CalendarView) findViewById(R.id.calendarView_main);
        calendarView.setVisibility(View.GONE);
        setSupportActionBar(toolbar_main);

        relativeLayout_main = (RelativeLayout) findViewById(R.id.relativeLayout_main);
        calendarView.setBusiness(
                new Business[]{new Business("1:20-2:40", new MyTime(1, 20), new MyTime(2, 40)),
                        new Business("4:40-6:00", new MyTime(4, 50), new MyTime(6, 0)),
                        new Business("21:53-23:38", new MyTime(21, 53), new MyTime(23, 38)),
                        new Business("18:00-20:00", new MyTime(18, 0), new MyTime(20, 0))}
        );

        listView_menu = (ListView) this.findViewById(R.id.listView_menu);
        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, new String[]{"hello", "world"});
        listView_menu.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayList));
        listView_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = ((TextView) parent.getChildAt(position - listView_menu.getFirstVisiblePosition())).getText().toString();
                if(s.equals("hello") == true) {
                    textView_test.setVisibility(View.VISIBLE);
                    calendarView.setVisibility(View.INVISIBLE);
                    textView_test.setText(s);
                } else {
                    textView_test.setVisibility(View.INVISIBLE);
                    calendarView.setVisibility(View.VISIBLE);
                    //此处findViewById前面有.this会出错

                }
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                drawerLayout_main.closeDrawers();
            }
        });
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout_main = (DrawerLayout) this.findViewById(R.id.drawerLayout_main);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout_main, toolbar_main, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        drawerLayout_main.setDrawerListener(mDrawerToggle);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
