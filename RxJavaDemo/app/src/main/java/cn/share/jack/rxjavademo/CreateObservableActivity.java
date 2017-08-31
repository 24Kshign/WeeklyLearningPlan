package cn.share.jack.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CreateObservableActivity extends Activity {

    private static final String TAG = "CreateObservable";

    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_observable);
        ButterKnife.bind(this);
        dataList.add("发射数据1");
        dataList.add("发射数据2");
        dataList.add("发射数据3");
    }

    @OnClick({R.id.aco_btn_rxjava_simple_use, R.id.aco_btn_rxjava_just, R.id.aco_btn_rxjava_fromIterable,
            R.id.aco_btn_rxjava_interval, R.id.aco_btn_rxjava_range})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aco_btn_rxjava_simple_use:
                /**
                 * 使用create()创建Observable最基本的创建方式。可以看到，这里传入了一个 ObservableOnSubscribe对象作为参数，
                 * 它的作用相当于一个计划表，当 Observable被订阅的时候，ObservableOnSubscribe的subscribe()方法会自动被调用，
                 * 事件序列就会依照设定依次触发（对于上面的代码，就是观察者Observer 将会被调用一次 onNext()）。
                 * 这样，由被观察者调用了观察者的回调方法，就实现了由被观察者向观察者的事件传递，即观察者模式。
                 */
                Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext("发射数据1");
                        e.onNext("发射数据2");
                        e.onNext("发射数据3");
                        e.onComplete();
                    }
                }).subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: Complete");
                    }
                });
                break;
            case R.id.aco_btn_rxjava_just:
                /**
                 * 使用just()，将为你创建一个Observable并自动为你调用onNext()发射数据。
                 * 通过just()方式 直接触发onNext()，
                 * just中传递的参数将直接在Observer的onNext()方法中接收到
                 */
                Observable.just("发射数据1", "发射数据2", "发射数据3").subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
                break;
            case R.id.aco_btn_rxjava_fromIterable:
                /**
                 * 使用fromIterable()，遍历集合，发送每个item。
                 * 相当于多次回调onNext()方法，每次传入一个item。
                 * 注意：Collection接口是Iterable接口的子接口，
                 * 所以所有Collection接口的实现类都可以作为Iterable对象直接传入fromIterable()方法。
                 */
                Observable.fromIterable(dataList).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
                break;
            case R.id.aco_btn_rxjava_interval:
                /**
                 * 创建一个按固定时间间隔发射整数序列的Observable，可用作定时器。
                 * 即按照固定2秒一次调用onNext()方法。默认从0开始，也可以自己设置开始值
                 */
                Observable.interval(2, TimeUnit.SECONDS).subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext: " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
            case R.id.aco_btn_rxjava_range:
                /**
                 * 创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，如果为0则不发送，负数则抛异常。
                 * 上述表示发射1到20的数。即调用20次nNext()方法，依次传入1-20数字。
                 */
                Observable.range(1, 20).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
                break;
        }
    }
}
