package com.franzco.sudokusolver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import SudokuGrid.SudokuGrid;

public class MainActivity extends AppCompatActivity {

	TextView mainTextView;
	TextView attemptCounterTextView;
	GridLayout mainGridLayout;
	LinearLayout entryButtonLayout;
	Button clearGridItemButton;
	GridLayout currentGridLayout;
	TextView currentSquare;

	private int gridSize = 9;
	private Button[] gridButtons;
	private Button[] numberButtons;
	private SudokuGrid sudokuGrid;
	private int activeGridIndex = -1;
	private int highlightedNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// find the grid layout
		mainGridLayout = (GridLayout) findViewById(R.id.mainGridLayout);
		// find the number button layout
		entryButtonLayout = (LinearLayout) findViewById(R.id.entryButtonLayout);
		// find the clear grid item button
		clearGridItemButton = (Button) findViewById(R.id.clearGridItemButton);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		createButtonGrid(metrics);
		createNumberButtonRow(metrics);

		// create the grid object
		sudokuGrid = new SudokuGrid(gridSize);
		printGrid();
	}

	private void createButtonGrid(DisplayMetrics metrics) {
		gridButtons = new Button[gridSize * gridSize];
		for (int i = 0; i < gridSize * gridSize; i++) {
			gridButtons[i] = (Button) LayoutInflater.from(this).inflate(R.layout.sudoku_button, null);
			gridButtons[i].setWidth(metrics.widthPixels / gridSize);
			gridButtons[i].setHeight(metrics.widthPixels / gridSize);

			mainGridLayout.addView(gridButtons[i]);
		}
	}

	private void createNumberButtonRow(DisplayMetrics metrics) {
		numberButtons = new Button[gridSize];
		for (int i = 0; i < gridSize; i++) {
			// add the button to the array
			numberButtons[i] = (Button) LayoutInflater.from(this).inflate(R.layout.entry_button, null);
			// set the size of the buttons
			numberButtons[i].setWidth(metrics.widthPixels / gridSize);
			numberButtons[i].setHeight(metrics.widthPixels / gridSize);
			// set the text of the buttons
			numberButtons[i].setText("" + (i + 1));

			// add the button to the layout
			entryButtonLayout.addView(numberButtons[i]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show();
				break;
			case R.id.action_generate:
				sudokuGrid.solve();
				activeGridIndex = -1;
				printGrid();
				break;
			case R.id.action_reset:
				sudokuGrid.makeEmptyGrid();
				printGrid();
				break;
			default:
				break;
		}
		return true;
	}

	private void printGrid() {

		for (int i = 0; i < gridSize * gridSize; i++) {
			printGridItem(i);
		}
	}

	private void printGridItem(int i)
	{
		gridButtons[i].setText("" + sudokuGrid.getGridNumberPosition(i));
		if (sudokuGrid.getGridPositionPermanent(i)) {
			gridButtons[i].setBackgroundColor(Color.DKGRAY);
		} else {
			gridButtons[i].setBackgroundColor(Color.GRAY);
		}
	}

	private void highlightNumber(int number, boolean enable) {
		for (int i = 0; i < gridSize * gridSize; i++) {
			if (!enable) {
				printGridItem(i);
				if (i < gridSize)
					numberButtons[i].setBackgroundColor(Color.GRAY);
				highlightedNumber = 0;
			} else if ((sudokuGrid.getGridNumberPosition(i) == number)) {
				gridButtons[i].setBackgroundColor(Color.GREEN);
				if (i < gridSize)
					numberButtons[i].setBackgroundColor(Color.GREEN);
				highlightedNumber = number;
			} else {
				printGridItem(i);
				if (i < gridSize)
					numberButtons[i].setBackgroundColor(Color.GRAY);
			}
		}
	}

	public void sudokuButtonClicked(View v) {
		for (int i = 0; i < gridSize * gridSize; i++) {
			if (v.equals(gridButtons[i])) {
				activeGridIndex = i;
				v.setBackgroundColor(Color.DKGRAY);
			}
			else if (i<gridSize)
			{
				numberButtons[i].setBackgroundColor(Color.GRAY);
			} else {
				printGridItem(i);
			}
		}
	}

	public void entryButtonClicked(View v) {
		Button b = (Button) v;
		int number = Integer.parseInt("" + b.getText());

		if (activeGridIndex != -1) {
			sudokuGrid.setGridNumberPosition(activeGridIndex, number, true);
			printGrid();
			activeGridIndex = -1;
		} else if (highlightedNumber != number) {
			highlightNumber(number, true);
		} else {
			highlightNumber(number, false);
		}
	}

	public void clearGridItemButtonClicked(View v) {
		if (activeGridIndex != -1)
		{
			sudokuGrid.setGridNumberPosition(activeGridIndex, 0, false);
			printGrid();
			activeGridIndex = -1;
		}
	}
}


