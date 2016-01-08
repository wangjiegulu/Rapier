package com.wangjie.rapier.app.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.wangjie.rapier.api.di.annotation.RInject;
import com.wangjie.rapier.api.di.annotation.RModule;
import com.wangjie.rapier.api.di.annotation.RNamed;
import com.wangjie.rapier.app.R;
import com.wangjie.rapier.app.main.module.MainModule;
import com.wangjie.rapier.app.model.FooData;

import java.util.Random;

@RModule(moduleClazz = MainModule.class)
public class MainActivity extends AppCompatActivity implements MainViewer, View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @RInject
    IMainPresenter presenter;
    //    Rapier<IMainPresenter> presenterRapier = new Rapier<>();
    private Random random;

    @RInject
    @RNamed(MainModule.FOO_DATA_A)
    FooData fooDataA;

    @RInject
    @RNamed(MainModule.FOO_DATA_B)
    FooData fooDataB;

    @RInject
//    @RNamed("error named")
    FooData fooDataNoNamed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        presenter = new MainPresenter(this);
//        MainRapier.create().inject(new MainModule(this), this);
        MainActivity_Rapier.create().inject(new MainModule(this), this);

        random = new Random();

//        Toast.makeText(this, "button is" + (null == button ? " " : " not ") + "null", Toast.LENGTH_SHORT).show();

        findViewById(R.id.activity_main_save_foo_data_btn).setOnClickListener(this);

        Log.i(TAG, "fooDataA: " + fooDataA);
        Log.i(TAG, "fooDataB: " + fooDataB);
        Log.i(TAG, "fooDataNoNamed: " + fooDataNoNamed);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_save_foo_data_btn:
                int id;
                presenter.saveFooData(new FooData(id = random.nextInt(100), "content_" + id));
                break;
            default:
                break;
        }
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

    @Override
    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
