package com.franzco.sudokusolver;

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
	private SudokuGridButton[] gridButtons;
	private SudokuGridButton[] entryButtons;
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
		// find the attempt counter text view
		attemptCounterTextView = (TextView) findViewById(R.id.attemptCounterTextView);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		createButtonGrid(metrics);
		createNumberButtonRow(metrics);

		// create the grid object
		sudokuGrid = new SudokuGrid(gridSize);
		printGrid(0);
	}

	private void createButtonGrid(DisplayMetrics metrics) {
		gridButtons = new SudokuGridButton[gridSize * gridSize];
		for (int i = 0; i < gridSize * gridSize; i++) {
			gridButtons[i] = (SudokuGridButton) LayoutInflater.from(this).inflate(R.layout.sudoku_button, null);
			gridButtons[i].setWidth(metrics.widthPixels / gridSize);
			gridButtons[i].setHeight(metrics.widthPixels / gridSize);

			mainGridLayout.addView(gridButtons[i]);
		}
	}

	private void createNumberButtonRow(DisplayMetrics metrics) {
		entryButtons = new SudokuGridButton[gridSize];
		for (int i = 0; i < gridSize; i++) {
			// add the button to the array
			entryButtons[i] = (SudokuGridButton) LayoutInflater.from(this).inflate(R.layout.entry_button, null);
			// set the size of the buttons
			entryButtons[i].setWidth(metrics.widthPixels / gridSize);
			entryButtons[i].setHeight(metrics.widthPixels / gridSize);
			// set the text of the buttons
			entryButtons[i].setText("" + (i + 1));

			// add the button to the layout
			entryButtonLayout.addView(entryButtons[i]);
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
				int reversals = sudokuGrid.solve();
				activeGridIndex = -1;
				printGrid(reversals);
				break;
			case R.id.action_reset:
				sudokuGrid.makeEmptyGrid();
				printGrid(0);
				break;
			default:
				break;
		}
		return true;
	}

	private void printEntryButtons()
	{
		for (int i = 0; i< gridSize; i++) {
			printEntryButton(i);
		}
	}

	private void printEntryButton(int i)
	{
		if (highlightedNumber == 0)
		{
			entryButtons[i].setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		}
	}

	private void printGrid(int reversals) {
		if (reversals != 0)
		{
			attemptCounterTextView.setText("" + reversals);
		}
		else
		{
			attemptCounterTextView.setText(R.string.attemptCounter_textview_string);
		}
		for (int i = 0; i < gridSize * gridSize; i++) {
			printGridItem(i);
		}
	}

	private void printGridItem(int i)
	{
		gridButtons[i].setText("" + sudokuGrid.getGridNumberPosition(i));
		if (sudokuGrid.getGridPositionPermanent(i)) {
			gridButtons[i].setBackgroundColor(getResources().getColor(R.color.Black));
			gridButtons[i].setTextColor(getResources().getColor(R.color.WhiteSmoke));
		} else {
			gridButtons[i].setBackgroundColor(getResources().getColor(R.color.normalButtonColor));
			gridButtons[i].setTextColor(getResources().getColor(R.color.Black));
		}
	}

	private void highlightNumber(int number, boolean enable) {
		for (int i = 0; i < gridSize * gridSize; i++) {
			if (!enable) {
				printGridItem(i);
				if (i < gridSize)
					printEntryButton(i);
				highlightedNumber = 0;
			} else if ((sudokuGrid.getGridNumberPosition(i) == number)) {
				gridButtons[i].setBackgroundColor(getResources().getColor(R.color.highlightNumber));
				if (i < gridSize)
					entryButtons[i].setBackgroundColor(getResources().getColor(R.color.highlightNumber));
				highlightedNumber = number;
			} else {
				printGridItem(i);
				if (i < gridSize)
					printEntryButton(i);
			}
		}
	}

	public void sudokuButtonClicked(View v) {
		for (int i = 0; i < gridSize * gridSize; i++) {
			if (v.equals(gridButtons[i])) {
				activeGridIndex = i;
				v.setBackgroundColor(getResources().getColor(R.color.activeButtonColor));
			}
			else if (i<gridSize)
			{
				printEntryButton(i);
			} else {
				printGridItem(i);
			}
		}
	}

	public void entryButtonClicked(View v) {
		SudokuGridButton b = (SudokuGridButton) v;
		int number = Integer.parseInt("" + b.getText());

		if (activeGridIndex != -1) {
			sudokuGrid.setGridNumberPosition(activeGridIndex, number, true);
			printGrid(0);
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
			printGrid(0);
			activeGridIndex = -1;
		}
	}
}


