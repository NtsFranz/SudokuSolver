package com.franzco.sudokusolver;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by ntslu on 12/17/2015.
 * A version of Button that has additional states for use in Sudoku
 * unfinished
 */
public class GridButton extends Button {

	private static final int[] STATE_HIGHLIGHTED = {R.attr.state_highlighted};

	private boolean mIsHighlighted = false;


	public GridButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected int[] onCreateDrawableState(int extraSpace) {
		final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
		if (mIsHighlighted) {
			mergeDrawableStates(drawableState, STATE_HIGHLIGHTED);
		}
		return drawableState;
	}
}
