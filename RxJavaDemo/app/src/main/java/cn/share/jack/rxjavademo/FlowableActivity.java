package cn.share.jack.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jack on 2017/9/5
 */

public class FlowableActivity extends Activity {

    private static final String TAG = "FlowableActivity";

    private Flowable<Integer> mFlowable;

    private Subscriber<Integer> mSubscriber;

    private Subscription mSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable);
        ButterKnife.bind(this);

        initFlowable();
    }

    private void initFlowable() {
        /**
         * 当消费者处理不了事件，就丢弃。
         * 消费者通过request()传入其需求n，然后生产者把n个事件传递给消费者供其消费。其他消费不掉的事件就丢掉。
         */
        mFlowable = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP);

        mSubscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                mSubscription = s;
                s.request(50);
            }

            @Override
            public void onNext(Integer o) {
                Log.d(TAG, "onNext: " + o);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(TAG, "onError: " + t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: ");
            }
        };
    }

    @OnClick({R.id.af_btn_error, R.id.af_btn_drop_product, R.id.af_btn_drop_consume})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.af_btn_error:
                /**
                 * 在异步调用时，RxJava中有个缓存池，用来缓存消费者处理不了暂时缓存下来的数据，缓存池的默认大小为128，即只能缓存128个事件。
                 * 无论request()中传入的数字比128大或小，缓存池中在刚开始都会存入128个事件。
                 * 当然如果本身并没有这么多事件需要发送，则不会存128个事件。
                 * 在ERROR策略下，如果缓存池溢出，就会立刻抛出MissingBackpressureException异常。
                 */
                Flowable.create(new FlowableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                        for (int i = 0; i < 129; i++) {
                            Log.d(TAG, "subscribe: " + i);
                            e.onNext(i);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Integer>() {

                            @Override
                            public void onSubscribe(Subscription s) {
                            }

                            @Override
                            public void onNext(Integer integer) {
                                Log.d(TAG, "onNext: " + integer);
                            }

                            @Override
                            public void onError(Throwable t) {
                                Log.d(TAG, "onError: " + t);
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
                            }
                        });
                break;
            case R.id.af_btn_drop_product:
                mFlowable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mSubscriber);
                break;

            case R.id.af_btn_drop_consume:
                mSubscription.request(50);
                break;
        }
    }
}