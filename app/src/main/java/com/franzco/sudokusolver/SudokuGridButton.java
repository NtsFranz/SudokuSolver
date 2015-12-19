package com.franzco.sudokusolver;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Nts on 12/19/2015.
 */
public class SudokuGridButton extends Button {
    private static final int[] STATE_HIGHLIGHTED = {R.attr.state_highlighted};
    private static final int[] STATE_ACTIVE = {R.attr.state_active};
    private static final int[] STATE_PERMANENT = {R.attr.state_permanent};

    private boolean mIsHighlighted = false;
    private boolean mIsActive = false;
    private boolean mIsPermanent = false;


    public SudokuGridButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setHighlighted(boolean isHighlighted) {mIsHighlighted = isHighlighted;}
    public void setActive(boolean isActive) {mIsActive = isActive;}
    public void setPermanent(boolean isPermanent) {mIsPermanent = isPermanent;}

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if (mIsHighlighted) {
            mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
        }
        if (mIsActive) {
            mergeDrawableStates(drawableState, STATE_ACTIVE);
        }
        if (mIsPermanent) {
            mergeDrawableStates(drawableState, STATE_PERMANENT);
        }
        return drawableState;
    }
}
