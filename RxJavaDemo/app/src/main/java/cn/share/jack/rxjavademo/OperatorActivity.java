package cn.share.jack.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by jack on 2017/8/29
 */

public class OperatorActivity extends Activity {

    private static final String TAG = "OperatorActivity";
    private List<Integer> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);
        ButterKnife.bind(this);

        dataList.add(1);
        dataList.add(2);
        dataList.add(3);
    }

    @OnClick({R.id.ao_btn_operator_map, R.id.ao_btn_operator_flatmap, R.id.ao_btn_operator_filter, R.id.ao_btn_operator_take,
            R.id.ao_btn_operator_doonnext, R.id.ao_btn_operator_distinct, R.id.ao_btn_operator_merge, R.id.ao_btn_operator_reduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ao_btn_operator_map:
                /**
                 * map()操作符，就是把原来的Observable对象转换成另一个Observable对象，
                 * 同时将传输的数据进行一些灵活的操作，方便Observer获得想要的数据形式。
                 */
                Observable.just("hello", "hello world").map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(String s) throws Exception {
                        return s.length();
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "字符串长度为: " + integer);
                    }
                });
                break;
            case R.id.ao_btn_operator_flatmap:
                /**
                 * flatMap()对于数据的转换比map()更加彻底，如果发送的数据是集合，flatMap()重新生成一个Observable对象，
                 * 并把数据转换成Observer想要的数据形式。它可以返回任何它想返回的Observable对象。
                 */
                Observable.just(dataList).flatMap(new Function<List<Integer>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<Integer> integers) throws Exception {
                        final List<String> list = new ArrayList<>();
                        for (int i = 0; i < dataList.size(); i++) {
                            list.add("我是第" + dataList.get(i) + "个事件");
                        }
                        return Observable.fromIterable(list).delay(2, TimeUnit.SECONDS);
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "accept: " + s);
                    }
                });
                break;
            case R.id.ao_btn_operator_filter:
                /**
                 * filter是用于过滤数据的，返回false表示拦截此数据。
                 * 这里我们需要的是大于5的数据
                 */
                Observable.fromArray(1, 20, 5, 0, -1, 8).filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer > 5;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
                break;
            case R.id.ao_btn_operator_take:
                /**
                 * take用于指定订阅者最多收到多少数据。
                 */
                Observable.fromArray(1, 2, 3, 4).take(2).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
                break;
            case R.id.ao_btn_operator_doonnext:
                /**
                 * doOnNext允许我们在每次输出一个元素之前做一些额外的事情。
                 */
                Observable.just(1, 2, 3).doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: 输出一个元素之前做一些额外的事情");
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
                break;
            case R.id.ao_btn_operator_distinct:
                /**
                 * distinct操作符的作用是去除重复的观察者，一般我们用来对数据进行去重操作。
                 */
                Observable.fromArray(1, 2, 1, 3, 3, 2, 1, 4, 3, 6, 7, 8, 2)
                        .distinct()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "accept: " + integer);
                            }
                        });
                break;
            case R.id.ao_btn_operator_merge:
                /**
                 * merge操作符用来合并两个观察者，需要注意的是，合并之后的输出顺序是不固定的
                 */
                Observable.merge(Observable.fromArray(1, 2, 3, 4, 5, 9, 8), Observable.fromArray(3, 1, 2, 4, 5, 6, 8))
                        .distinct()
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "accept: " + integer);
                            }
                        });
                break;
            case R.id.ao_btn_operator_delay:
                /**
                 * delay操作符的作用就是延时发射Observable里面的事件，经过2秒钟才会收到Observable发出的事件
                 */
                Observable.fromArray(1, 2, 3).delay(2, TimeUnit.SECONDS)
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(@NonNull Integer integer) throws Exception {
                                Log.d(TAG, "accept: " + integer);
                            }
                        });
                break;
            case R.id.ao_btn_operator_reduce:
                /**
                 * reduce操作符可以把一个被观察者中的多个事件进行压缩，最后发射压缩后的事件
                 */
                Observable.fromArray(1, 2, 3, 4).reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        Log.d(TAG, "accept: " + integer);
                    }
                });
                break;
        }
    }
}