package io.sweers.placeholder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button) Button button;
    @BindView(R.id.progress) ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void onClick() {
        Observable.interval(1, 1, TimeUnit.SECONDS)
                .take(5)
                .doOnNext(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("BLAH", "MainActivity.call - " + Thread.currentThread());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>buttonEnabledTransformer())
                .compose(this.<Long>progressTransformer())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        button.setText("Starting...");
                    }
                })
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        button.setText("Click me");
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        button.setText("Progress - " + aLong);
                    }
                });
    }

    public <T> Observable.Transformer<T, T> buttonEnabledTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                button.setEnabled(false);
                            }
                        })
                        .doOnUnsubscribe(new Action0() {
                            @Override
                            public void call() {
                                button.setEnabled(true);
                            }
                        });
            }
        };
    }

    public <T> Observable.Transformer<T, T> progressTransformer() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> source) {
                return source
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {
                                progress.setVisibility(View.VISIBLE);
                            }
                        })
                        .doOnUnsubscribe(new Action0() {
                            @Override
                            public void call() {
                                progress.setVisibility(View.GONE);
                            }
                        });
            }
        };
    }
}
