package com.example.fdayre.projet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.renderscript.Allocation;
//import android.support.v8.renderscript.Allocation;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private Bitmap bmp;
    private ImageView imgView;
    private TextView textView;
    private BitmapFactory.Options opt;
    private Button toGray;
    private Button colorize;
    private Button refresh;
    private Button toGrayAndOneColor;
    private Button contrastDynamicExtension;
    private Button histogramEqualizer;
    private Button averagingFilter;

    private boolean click = false;
    private Spinner spinner;
    private String color;
    private String picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Spinner permettant de choisir la couleur que l'on veut garder pour l'algorithme toGrayAndOneColor
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.color_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                color = (String) spinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Spinner permettant de choisir l'image que l'on veut afficher
        final Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.picture_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                picture = (String) spinner2.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Initialisation de l'image
        imgView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView2);
        opt = new BitmapFactory.Options();
        opt.inMutable = true;
        opt.inScaled = false;
        bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.index, opt);
        imgView.setImageBitmap(bmp);
        //Affichage de la taille de l'image
        textView.setText(Integer.toString(bmp.getWidth()) + " " + Integer.toString(bmp.getHeight()));

        //Initialisation des boutons de l'application
        toGray = (Button)findViewById(R.id.button);
        toGray.setOnClickListener(toGrayListener);

        colorize = (Button)findViewById(R.id.button2);
        colorize.setOnClickListener(colorizeListener);

        refresh = (Button)findViewById(R.id.button3);
        refresh.setOnClickListener(refreshListener);

        toGrayAndOneColor = (Button)findViewById(R.id.button4);
        toGrayAndOneColor.setOnClickListener(toGrayAndOneColorListener);

        contrastDynamicExtension = (Button)findViewById(R.id.button5);
        contrastDynamicExtension.setOnClickListener(contrastDynamicExtensionListener);

        histogramEqualizer = (Button)findViewById(R.id.button6);
        histogramEqualizer.setOnClickListener(histogramEqualizerListener);

        averagingFilter = (Button)findViewById(R.id.button7);
        averagingFilter.setOnClickListener(averagingFilterListener);
    }

    protected void toGray(Bitmap bmp){
        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for(int i = 0; i < bmp.getHeight()*bmp.getWidth(); i++){
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);

            int gl = (int) (r*0.3 + g*0.59 + b*0.11);
            pixels[i] = Color.rgb(gl, gl, gl);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }

/*
    private void toGreyRS(Bitmap bmp){
        RenderScript rs = RenderScript.create(this);

        Allocation input = Allocation.createFromBitmap(rs, bmp);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptC_grey greyScript = new ScriptC_grey(rs);

        greyScript.forEach_toGrey(input, output);

        output.copyTo(bmp);

        input.destroy();
        output.destroy();
        greyScript.destroy();
        rs.destroy();

    }
*/

    protected void colorize(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Random rand = new Random();
        int randomValue =  rand.nextInt((360 - 0) + 1) + 0;

        int[] pixels = new int[bmp.getWidth()*bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for(int i = 0; i < bmp.getHeight()*bmp.getWidth(); i++){
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);


            float[] hsv = new float[3];
            Color.RGBToHSV(r, g, b, hsv);

            float hue = randomValue;
            float sat = hsv[1];
            float val = hsv[2];

            int outputColor = Color.HSVToColor(new float[]{hue,sat, val});
            r = Color.red(outputColor);
            g = Color.green(outputColor);
            b = Color.blue(outputColor);

            pixels[i] = Color.rgb(r, g, b);
        }
        bmp.setPixels(pixels,0,bmp.getWidth(),0,0,bmp.getWidth(),bmp.getHeight());
    }


    protected void toGrayAndOneColor(Bitmap bmp) {
        int c = -1;
        if (color.equals("Rouge")) {
            
        } else if (color.equals("Jaune")) {
            c = 50;
        } else if (color.equals("Bleu")) {
            c = 180;
        } else if (color.equals("Vert")) {
            c = 80;
        }
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++) {
            int r = Color.red(pixels[i]);
            int g = Color.green(pixels[i]);
            int b = Color.blue(pixels[i]);


            float[] hsv = new float[3];
            Color.RGBToHSV(r, g, b, hsv);


            if (hsv[0] <= c - 20 || hsv[0] >= c + 20) {
                int gl = (int) (r * 0.3 + g * 0.59 + b * 0.11);

                pixels[i] = Color.rgb(gl, gl, gl);
            }
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

    protected void contrastDynamicExtension(Bitmap bmp) {
        int min = 255;
        int max = 0;

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int r;
        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++) {
            r = Color.red(pixels[i]);
            if (r < min)
                min = r;
            if (r > max)
                max = r;
        }

        int[] LUT = new int[256];

        for(int i=0; i<256; i++)
            LUT[i] = ((255*(i-min))/(max-min));

        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++)
            pixels[i] = Color.rgb(LUT[Color.red(pixels[i])], LUT[Color.red(pixels[i])], LUT[Color.red(pixels[i])]);

        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }

/*
    protected void contrastColor(Bitmap bmp) {
        int minR = 255;
        int maxR = 0;

        int minG = 255;
        int maxG = 0;

        int minB = 255;
        int maxB = 0;

        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int r, g, b;
        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++) {
            r = Color.red(pixels[i]);
            g = Color.green(pixels[i]);
            b = Color.blue(pixels[i]);

            if (r < minR)
                minR = r;
            if (r > maxR)
                maxR = r;

            if (g < minG)
                minG = g;
            if (g > maxG)
                maxG = g;

            if (b < minB)
                minB = b;
            if (b > maxB)
                maxB = b;
        }

        int[] LUTR = new int[256];
        int[] LUTG = new int[256];
        int[] LUTB = new int[256];


        for(int i=0; i<256; i++)
            LUTR[i] = ((255*(i-minR))/(maxR-minR));
        for(int i=0; i<256; i++)
            LUTG[i] = ((255*(i-minG))/(maxG-minG));
        for(int i=0; i<256; i++)
            LUTB[i] = ((255*(i-minB))/(maxB-minB));

        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++)
            pixels[i] = Color.rgb(LUTR[Color.red(pixels[i])], LUTG[Color.green(pixels[i])], LUTB[Color.blue(pixels[i])]);

        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
    }
*/

    protected int cumulativeHistogram(int[] hist, int i) {
        int cpt = 0;
        for(int j = 0; j<=i; j++)
            cpt += hist[j];
        return cpt;
    }

    protected void histogramEqualizer(Bitmap bmp) {
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        int[] hist = new int[256];

        int r;
        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++) {
            r = Color.red(pixels[i]);
            hist[r]++;
        }

        int[] histCumule = new int[256];
        for(int i = 0; i < 256; i++)
            histCumule[i] = cumulativeHistogram(hist, i);

        for (int i = 0; i < bmp.getHeight() * bmp.getWidth(); i++)
            pixels[i] = Color.rgb((histCumule[Color.red(pixels[i])]*255)/ (bmp.getHeight() * bmp.getWidth()), (histCumule[Color.red(pixels[i])] *255)/ (bmp.getHeight() * bmp.getWidth()), (histCumule[Color.red(pixels[i])] *255)/ (bmp.getHeight() * bmp.getWidth()));


        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

    }

    protected void averagingFilter(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[][] pixels = new int[w][h];

        for(int i = 0; i < w; i++)
            for(int j = 0; j < h; j++) {
                int col = bmp.getPixel(i, j);
                pixels[i][j] = Color.red(col);
            }

        for(int i = 1; i < w-1; i++)
            for(int j = 1; j < h-1; j++) {
                pixels[i][j] = (pixels[i-1][j-1] + pixels[i][j-1] + pixels[i+1][j-1] + pixels[i+1][j] + pixels[i+1][j+1] + pixels[i][j+1] + pixels[i-1][j+1] + pixels[i-1][j])/8;
                bmp.setPixel(i, j, Color.rgb(pixels[i][j], pixels[i][j], pixels[i][j]));
            }

    }

    private View.OnClickListener toGrayListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            toGray(bmp);
        }
    };

    private View.OnClickListener colorizeListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            colorize(bmp);
        }
    };

    private View.OnClickListener refreshListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(picture.equals("Plage"))
                bmp = BitmapFactory.decodeResource(view.getResources(), R.drawable.plage, opt);
            else if(picture.equals("Poivron"))
                bmp = BitmapFactory.decodeResource(view.getResources(), R.drawable.index, opt);
            imgView.setImageBitmap(bmp);
            textView.setText(Integer.toString(bmp.getWidth()) + " " + Integer.toString(bmp.getHeight()));
        }
    };

    private View.OnClickListener toGrayAndOneColorListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            toGrayAndOneColor(bmp);
        }
    };

    private View.OnClickListener contrastDynamicExtensionListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            contrastDynamicExtension(bmp);
        }
    };

    private View.OnClickListener histogramEqualizerListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            histogramEqualizer(bmp);
        }
    };

    private View.OnClickListener averagingFilterListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            averagingFilter(bmp);        }
    };
}
