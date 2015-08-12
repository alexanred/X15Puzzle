package com.example.asebastian.puzzletopuzzle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    enum PlayMode {NUMBER, IMAGE, IMAGE_FROM_GALLERY, TAKE_A_PICTURE}

    ;
    PlayMode mPlayMode = PlayMode.NUMBER;
    ImageView[] imageViews = new ImageView[16];
    int[] imageViewIDs = new int[16];
    int[] imageResourceIDs = new int[16];
    int[] position = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    Bitmap[] bitmaps = new Bitmap[16];

    Bitmap mImageBitMap;
    Bitmap numberBitMap;
    Bitmap numberBlankBitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageBitMap = ((BitmapDrawable) getResources().getDrawable(R.drawable.face_image)).getBitmap();
        numberBitMap = ((BitmapDrawable) getResources().getDrawable(R.drawable.round)).getBitmap();
        numberBlankBitMap = ((BitmapDrawable) getResources().getDrawable(R.drawable.round_blank)).getBitmap();
        initializeImageViewIDs();

        initializeImageView();
        initializeImageResourceIDs();
        intializeBitmapImageArray();
        randomizeImageView();
        initializeImageClickListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.numbers:
                mPlayMode = PlayMode.NUMBER;
                scramble();
                return true;

            case R.id.choose_stored_images:
                mPlayMode = PlayMode.IMAGE;
                scramble();
                return true;

            case R.id.choose_image_from_gallery:
                mPlayMode = PlayMode.IMAGE_FROM_GALLERY;
                scramble();
                return true;

            case R.id.take_picture_and_play:
                mPlayMode = PlayMode.TAKE_A_PICTURE;
                scramble();
                return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intializeBitmapImageArray() {

        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        int cellWidth = (screenWidth - 50) / 4;
        int cellHeight = (screenHeight * 2 / 3) / 4;


        if (mPlayMode == PlayMode.NUMBER) {
            Bitmap numScaledImage = Bitmap.createScaledBitmap(numberBitMap, cellWidth, cellHeight, false);
            numberBitMap = numScaledImage;
            Bitmap blankNumScaledImage = Bitmap.createScaledBitmap(numberBlankBitMap, cellWidth, cellHeight, false);
            numberBlankBitMap = blankNumScaledImage;

            for(int i=0; i<15; i++) {
                Bitmap bitmap = Bitmap.createBitmap(numberBitMap);
                Canvas canvas = new Canvas(bitmap);
                // new antialised Paint
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(Color.BLACK);
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                // text size in pixels
                float scale = getResources().getDisplayMetrics().density;
                paint.setTextSize((int) (18 * scale));
                // text shadow
                paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);

                // draw text to the Canvas center
                Rect bounds = new Rect();
                String gText = String.valueOf(position[i] + 1);
                paint.getTextBounds(gText, 0, gText.length(), bounds);
                int x = (bitmap.getWidth() - bounds.width()) / 2;
                int y = (bitmap.getHeight() + bounds.height()) / 2;

                canvas.drawText(gText, x, y, paint);
                bitmaps[i] = bitmap;
            }
            bitmaps[15] = numberBlankBitMap;
        } else {


            Bitmap scaledImage = Bitmap.createScaledBitmap(mImageBitMap, cellWidth * 4, cellHeight * 4, false);
            int width = scaledImage.getWidth();
            int height = scaledImage.getHeight();

            int chunkWidth = width / 4;
            int chunkHeight = height / 4;
            int i = 0;
            int yCoord = 0;
            for (int x = 0; x < 4; x++) {
                int xCoord = 0;
                for (int y = 0; y < 4; y++) {
                    bitmaps[i] = Bitmap.createBitmap(scaledImage, xCoord, yCoord, chunkWidth, chunkHeight);
                    i++;
                    xCoord += chunkWidth;
                }
                yCoord += chunkHeight;
            }


            Bitmap dest = Bitmap.createBitmap(cellWidth, cellHeight, bitmaps[15].getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bitmaps[15], bitmaps[15].getWidth(), bitmaps[15].getHeight(), null);
            bitmaps[15] = dest;
        }
    }


    private void initializeImageViewIDs() {
        imageViewIDs[0] = R.id.imageView1;
        imageViewIDs[1] = R.id.imageView2;
        imageViewIDs[2] = R.id.imageView3;
        imageViewIDs[3] = R.id.imageView4;
        imageViewIDs[4] = R.id.imageView5;
        imageViewIDs[5] = R.id.imageView6;
        imageViewIDs[6] = R.id.imageView7;
        imageViewIDs[7] = R.id.imageView8;
        imageViewIDs[8] = R.id.imageView9;
        imageViewIDs[9] = R.id.imageView10;
        imageViewIDs[10] = R.id.imageView11;
        imageViewIDs[11] = R.id.imageView12;
        imageViewIDs[12] = R.id.imageView13;
        imageViewIDs[13] = R.id.imageView14;
        imageViewIDs[14] = R.id.imageView15;
        imageViewIDs[15] = R.id.imageView16;
    }

    private void initializeImageView() {
        for (int i = 0; i < 16; i++) {
            imageViews[i] = (ImageView) findViewById(imageViewIDs[i]);
        }
    }

    private void initializeImageResourceIDs() {
        imageResourceIDs[0] = R.drawable.image1;
        imageResourceIDs[1] = R.drawable.image2;
        imageResourceIDs[2] = R.drawable.image3;
        imageResourceIDs[3] = R.drawable.image4;
        imageResourceIDs[4] = R.drawable.image5;
        imageResourceIDs[5] = R.drawable.image6;
        imageResourceIDs[6] = R.drawable.image7;
        imageResourceIDs[7] = R.drawable.image8;
        imageResourceIDs[8] = R.drawable.image9;
        imageResourceIDs[9] = R.drawable.image10;
        imageResourceIDs[10] = R.drawable.image11;
        imageResourceIDs[11] = R.drawable.image12;
        imageResourceIDs[12] = R.drawable.image13;
        imageResourceIDs[13] = R.drawable.image14;
        imageResourceIDs[14] = R.drawable.image15;
        imageResourceIDs[15] = R.drawable.image16;

    }

    private void randomizeImageView() {
        randomPostion();
        // if (mPlayMode == PlayMode.NUMBER)
        //     setImageResource();
        // else
        setImageBitMapResource();

    }

    private void setImageResource() {
        for (int i = 0; i < 16; i++) {
            imageViews[i].setImageResource(imageResourceIDs[position[i]]);

        }
    }

    private void setImageBitMapResource() {
        for (int i = 0; i < 16; i++) {

            imageViews[i].setImageBitmap(bitmaps[position[i]]);
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_CENTER);

          /*  imageViews[i].setMinimumWidth(bitmaps[position[i]].getWidth());
            imageViews[i].setMinimumHeight(bitmaps[position[i]].getHeight());
            imageViews[i].setMaxWidth(bitmaps[position[i]].getWidth());
            imageViews[i].setMaxHeight(bitmaps[position[i]].getHeight());
            imageViews[i].setScaleType(ImageView.ScaleType.FIT_XY); */
        }
    }

    private void randomPostion() {
        Random random = new Random();
        for (int i = 14; i > 0; i--) {
            int j = random.nextInt(i);
            int k = position[i];
            position[i] = position[j];
            position[j] = k;
        }
    }

    private void initializeImageClickListeners() {
        for (int i = 0; i < 16; i++) {
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    processImageClick(v);
                }
            });
        }
    }

    private void processImageClick(View view) {
        int imageId = view.getId();
        int pos = getImagePostion(imageId);
        int dest = -1;
        if (pos != -1) {
            if (position[pos] != 16) {
                if (pos == 0) {
                    if (position[1] == 15) {
                        dest = 1;
                    }
                    if (position[4] == 15) {
                        dest = 4;
                    }
                }

                if (pos == 1) {
                    if (position[2] == 15) {
                        dest = 2;
                    }
                    if (position[0] == 15) {
                        dest = 0;
                    }
                    if (position[5] == 15) {
                        dest = 5;
                    }
                }
                if (pos == 2) {
                    if (position[3] == 15) {
                        dest = 3;
                    }
                    if (position[1] == 15) {
                        dest = 1;
                    }
                    if (position[6] == 15) {
                        dest = 6;
                    }
                }
                if (pos == 3) {
                    if (position[2] == 15) {
                        dest = 2;
                    }
                    if (position[7] == 15) {
                        dest = 7;
                    }
                }
                if (pos == 4) {
                    if (position[0] == 15) {
                        dest = 0;
                    }
                    if (position[8] == 15) {
                        dest = 8;
                    }
                    if (position[5] == 15) {
                        dest = 5;
                    }
                }
                if (pos == 5) {
                    if (position[1] == 15) {
                        dest = 1;
                    }
                    if (position[9] == 15) {
                        dest = 9;
                    }
                    if (position[4] == 15) {
                        dest = 4;
                    }
                    if (position[6] == 15) {
                        dest = 6;
                    }
                }
                if (pos == 6) {
                    if (position[2] == 15) {
                        dest = 2;
                    }
                    if (position[10] == 15) {
                        dest = 10;
                    }
                    if (position[5] == 15) {
                        dest = 5;
                    }
                    if (position[7] == 15) {
                        dest = 7;
                    }
                }

                if (pos == 7) {
                    if (position[3] == 15) {
                        dest = 3;
                    }
                    if (position[6] == 15) {
                        dest = 6;
                    }
                    if (position[11] == 15) {
                        dest = 11;
                    }
                }
                if (pos == 8) {
                    if (position[4] == 15) {
                        dest = 4;
                    }
                    if (position[12] == 15) {
                        dest = 12;
                    }
                    if (position[9] == 15) {
                        dest = 9;
                    }
                }
                if (pos == 9) {
                    if (position[5] == 15) {
                        dest = 5;
                    }
                    if (position[10] == 15) {
                        dest = 10;
                    }
                    if (position[8] == 15) {
                        dest = 8;
                    }
                    if (position[13] == 15) {
                        dest = 13;
                    }
                }

                if (pos == 10) {
                    if (position[6] == 15) {
                        dest = 6;
                    }
                    if (position[14] == 15) {
                        dest = 14;
                    }
                    if (position[9] == 15) {
                        dest = 9;
                    }
                    if (position[11] == 15) {
                        dest = 11;
                    }
                }
                if (pos == 11) {
                    if (position[7] == 15) {
                        dest = 7;
                    }
                    if (position[10] == 15) {
                        dest = 10;
                    }
                    if (position[15] == 15) {
                        dest = 15;
                    }
                }
                if (pos == 12) {
                    if (position[8] == 15) {
                        dest = 8;
                    }
                    if (position[13] == 15) {
                        dest = 13;
                    }

                }
                if (pos == 13) {
                    if (position[9] == 15) {
                        dest = 9;
                    }
                    if (position[12] == 15) {
                        dest = 12;
                    }
                    if (position[14] == 15) {
                        dest = 14;
                    }

                }
                if (pos == 14) {
                    if (position[10] == 15) {
                        dest = 10;
                    }
                    if (position[13] == 15) {
                        dest = 13;
                    }
                    if (position[15] == 15) {
                        dest = 15;
                    }
                }
                if (pos == 15) {
                    if (position[11] == 15) {
                        dest = 11;
                    }
                    if (position[14] == 15) {
                        dest = 14;
                    }
                }


                if (dest != -1)
                    swapPostion(pos, dest);
                //if (mPlayMode == PlayMode.NUMBER)
                //     setImageResource();
                // else
                setImageBitMapResource();
            }
        }
    }

    private void swapPostion(int i, int j) {
        int k = position[i];
        position[i] = position[j];
        position[j] = k;
    }

    private int getImagePostion(int imageId) {
        for (int i = 0; i < 16; i++) {
            if (imageViewIDs[i] == imageId)
                return i;
        }
        return -1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void initializePostion() {
        for (int i = 0; i < 16; i++) {
            position[i] = i;
        }
    }

    public void scramble(View view) {
        scramble();
    }

    public void scramble() {
        initializePostion();
        intializeBitmapImageArray();
        randomizeImageView();
    }
}
