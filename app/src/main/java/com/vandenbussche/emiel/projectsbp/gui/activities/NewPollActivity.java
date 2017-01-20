package com.vandenbussche.emiel.projectsbp.gui.activities;

import android.accounts.Account;
import android.animation.LayoutTransition;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vandenbussche.emiel.projectsbp.R;
import com.vandenbussche.emiel.projectsbp.auth.AuthHelper;
import com.vandenbussche.emiel.projectsbp.database.provider.Contract;
import com.vandenbussche.emiel.projectsbp.databinding.ActivityNewPollBinding;
import com.vandenbussche.emiel.projectsbp.databinding.ContentNewPollBinding;
import com.vandenbussche.emiel.projectsbp.models.requests.PollRequest;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewPollActivityViewModel;
import com.vandenbussche.emiel.projectsbp.viewmodel.NewsFragmentViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vandenbussche.emiel.projectsbp.database.provider.Contract.AUTHORITY;

public class NewPollActivity extends AppCompatActivity implements NewPollActivityViewModel.NewPollViewmodelListener {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_LOAD = 2;

    ActivityNewPollBinding binding;
    NewPollActivityViewModel newPollActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_poll);
        newPollActivityViewModel = new NewPollActivityViewModel(binding, this, this);
        binding.setPoll(new PollRequest());

        LinearLayout parentLayout = binding.content.parentLayout;
        LayoutTransition layoutTransition = parentLayout.getLayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void startTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void startLoadPictureIntent() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            //getContentResolver().notifyChange(Uri.parse(mCurrentPhotoPath), null);

            //setPic();
            resizePicture();


            newPollActivityViewModel.imageUrlAdded(mCurrentPhotoPath);
        }
    }

    String mCurrentPhotoPath;
    Uri mCurrentPhotoUri;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "votastic");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        mCurrentPhotoUri = Uri.fromFile(image);
        return image;
    }

    private void resizePicture(){
        try {
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = false;
            Bitmap photo = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int destWidth = (int)(1000f * ((float)photoW / (float)photoH));


            photo = Bitmap.createScaledBitmap(photo, destWidth, 1000, false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

            File f = new File(mCurrentPhotoPath);
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
