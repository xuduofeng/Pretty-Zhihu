package site.hanschen.pretty.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.hanschen.pretty.R;
import site.hanschen.pretty.base.BaseActivity;
import site.hanschen.pretty.eventbus.NewQuestionEvent;

/**
 * @author HansChen
 */
public class QuestionActivity extends BaseActivity {

    @BindView(R.id.question_tab_layout)
    TabLayout            mTabLayout;
    @BindView(R.id.question_pager)
    ViewPager            mViewPager;
    @BindView(R.id.question_add)
    FloatingActionButton mFabBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.question_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        mViewPager.setOffscreenPageLimit(2);
        QuestionCategory[] categories = new QuestionCategory[]{
                QuestionCategory.HISTORY, QuestionCategory.FAVORITES, QuestionCategory.HOT};
        mViewPager.setAdapter(new QuestionPagerAdapter(getFragmentManager(), Arrays.asList(categories)));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mFabBtn.show();
                } else {
                    mFabBtn.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.question_add)
    void onFabClick() {
        EventBus.getDefault().post(new NewQuestionEvent());
    }
}
