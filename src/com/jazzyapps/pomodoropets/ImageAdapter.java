package com.jazzyapps.pomodoropets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {
	
	// SharedPreferences variables
	// pet name key: petName
    // first time app opened boolean key: firstTime
	
	EditText petName;
	
	Context context;
	
	private int[] petImages = {
		R.drawable.shiba,
		R.drawable.ditto
		};
	
	ImageAdapter(Context context){
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return petImages.length;
	}
	 
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageButton) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageButton imageButton = new ImageButton(context);
		int padding = 50;
		imageButton.setPadding(padding, padding, padding, padding);
		imageButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		imageButton.setBackgroundColor(Color.TRANSPARENT);
		imageButton.setImageResource(petImages[position]);
		
		switch(position) {
		case 0:
			imageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					adoptPetDialog(0);
				}
	        });
			
			break;
		case 1:
			imageButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
		            adoptPetDialog(1);
				}
	        });
			break;
		}
		
		((ViewPager) container).addView(imageButton, 0);
		return imageButton;
	}
	 
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageButton) object);
	}
	
	// Dialog congratulating user on adopting a pet
	public void adoptPetDialog(int position) {
		
		// Message to be displayed when pet is selected for adoption
		int petDialog[] = {
			R.string.dialog_adopted_dog,
			R.string.dialog_adopted_ditto
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(petDialog[position])
			   .setCancelable(true)
			   .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   //save pet type
					   namePetDialog();
				   }
			   });
			   /*.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   //delete pet type
					   dialog.cancel();
				   }
			   });*/

        AlertDialog alert = builder.create();
        alert.show();
	}
	
	// Dialog prompting user to pick a name for their pet
	public void namePetDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
				
		petName = new EditText(context);
		petName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		
		builder.setMessage(R.string.name_pet)
			   .setView(petName)
			   .setCancelable(false)
			   .setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   
					   SetSharedPreferences.setBoolean((Activity) context, "firstTime", false);
					   SetSharedPreferences.setString((Activity) context, "petName", petName.getText().toString());
					   Intent intent = new Intent(context, HomeActivity.class);
					   context.startActivity(intent);
					   
					   // finish PickPetActivity so the back button cannot go back to it
					   Activity activity = (Activity) context;
					   activity.finish();
				   }
			   });
			   /*.setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   dialog.cancel();
				   }
			   });*/

        AlertDialog alert = builder.create();
        alert.show();
	}
}

