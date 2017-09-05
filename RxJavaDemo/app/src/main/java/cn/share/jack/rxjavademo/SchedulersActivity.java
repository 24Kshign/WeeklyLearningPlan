package cn.share.jack.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jack on 2017/8/30
 */

public class SchedulersActivity extends Activity {

    private static final String TAG = "SchedulersActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedulers);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.as_btn_switch_io_thread, R.id.as_btn_switch_main_thread,
            R.id.as_btn_switch_new_thread})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.as_btn_switch_io_thread:
                /**
                 * I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler
                 */
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                        Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n发送的数据是：" + 1);
                        e.onNext(1);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n接收到的数据是：" + integer);
                            }
                        });
                break;
            case R.id.as_btn_switch_main_thread:
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                        Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n发送的数据是：" + 12);
                        e.onNext(12);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n接收到的数据是：" + integer);
                            }
                        });
                break;
            case R.id.as_btn_switch_new_thread:
                /**
                 * 总是启用新线程，并在新线程执行操作
                 */
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                        Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n发送的数据是：" + 123);
                        e.onNext(123);
                    }
                }).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.newThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "所在的线程是：" + Thread.currentThread().getName() + "\n接收到的数据是：" + integer);
                            }
                        });
                break;
        }
    }
}
