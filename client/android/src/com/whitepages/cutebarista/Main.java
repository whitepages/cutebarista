package com.whitepages.cutebarista;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Main extends Activity implements OnClickListener
{
	static final int DIALOG_CLICKED_CUTE = 0;
	static final int DIALOG_CLICKED_NOT_CUTE = 1;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ImageButton cuteButton = (ImageButton) findViewById(R.id.button_cute);
		cuteButton.setOnClickListener(this);

		ImageButton notCuteButton = (ImageButton) findViewById(R.id.button_not_cute);
		notCuteButton.setOnClickListener(this);	
        
		ImageButton findCuteButton = (ImageButton) findViewById(R.id.button_find_cute);
		findCuteButton.setOnClickListener(this);		

    }
    
	
    /**
     *  Custom onclick handler
     */
	public void onClick(View v) 
	{		
		switch (v.getId()) 
		{
			case R.id.button_cute:
				clickedCute();
				break;
			case R.id.button_not_cute:
				clickedNotCute();
				break;
			case R.id.button_find_cute:
				clickedFindCute();
				break;
		}
	}   

	/**
	 * This is where we create our different dialogs used on this screen/view.
	 */
	protected Dialog onCreateDialog(int id) 
	{
		switch(id)
		{
			case DIALOG_CLICKED_CUTE:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("You found a cute barista, nice!");
				builder.setCancelable(false);
				builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
				    }
				});
				return builder.create();
		}
		return null;
	}
	
	
    /**
     *  Handle clicking the "Cute" button
     */
	public void clickedCute()
	{
		showDialog(DIALOG_CLICKED_CUTE);

	}

    /**
     *  Handle clicking the "Not Cute" button
     */
	public void clickedNotCute()
	{
	}


    /**
     *  Handle clicking the "Find Cute Baristas" button
     */
	public void clickedFindCute()
	{
	}
  
}