package cn.share.jack.rxjavademo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jack on 2017/8/29
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.am_btn_create_observable, R.id.am_btn_operator, R.id.am_btn_schedulers, R.id.am_btn_flowable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.am_btn_create_observable:
                startActivity(new Intent(this, CreateObservableActivity.class));
                break;
            case R.id.am_btn_operator:
                startActivity(new Intent(this, OperatorActivity.class));
                break;
            case R.id.am_btn_schedulers:
                startActivity(new Intent(this, SchedulersActivity.class));
                break;
            case R.id.am_btn_flowable:
                startActivity(new Intent(this, FlowableActivity.class));
                break;
        }
    }
}
