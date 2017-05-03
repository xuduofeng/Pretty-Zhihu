package site.hanschen.pretty;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import site.hanschen.pretty.zhihu.ZhihuApi;
import site.hanschen.pretty.zhihu.bean.AnswerList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        initViews();
        initData();
    }

    private void initData() {
        final ZhihuApi api = new ZhihuApi();
        final int questionId = 58565859;
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext(api.getHtml(questionId));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).flatMap(new Function<String, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(final @NonNull String html) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                        int pageSize = 10;
                        int count = api.getAnswerCount(html);
                        for (int offset = 0; offset < count; offset += pageSize) {
                            emitter.onNext(offset);
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).map(new Function<Integer, AnswerList>() {
            @Override
            public AnswerList apply(@NonNull Integer offset) throws Exception {
                return api.getAnswerList(questionId, 10, offset);
            }
        }).observeOn(Schedulers.computation()).flatMap(new Function<AnswerList, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull final AnswerList answerList) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                        for (String answer : answerList.msg) {
                            emitter.onNext(answer);
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).map(new Function<String, List<String>>() {
            @Override
            public List<String> apply(@NonNull String answer) throws Exception {
                return api.getPictureList(answer);
            }
        }).flatMap(new Function<List<String>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final @NonNull List<String> pictures) throws Exception {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Exception {
                        for (String picture : pictures) {
                            emitter.onNext(picture);
                        }
                        emitter.onComplete();
                    }
                });
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull String picture) {
                Log.d("Hans", picture);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void initViews() {
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
    }

}
