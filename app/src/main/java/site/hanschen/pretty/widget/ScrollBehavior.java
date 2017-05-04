package site.hanschen.pretty.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author HansChen
 */
public class ScrollBehavior extends FloatingActionButton.Behavior {

    public ScrollBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout,
                                       final FloatingActionButton child,
                                       final View directTargetChild,
                                       final View target,
                                       final int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(coordinatorLayout,
                                                                                                child,
                                                                                                directTargetChild,
                                                                                                target,
                                                                                                nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
                               final FloatingActionButton child,
                               final View target,
                               final int dxConsumed,
                               final int dyConsumed,
                               final int dxUnconsumed,
                               final int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d("Hans", "onNestedScroll");
        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.hide();
            Log.d("Hans", "hide");
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
            Log.d("Hans", "show");
        }
    }
}
