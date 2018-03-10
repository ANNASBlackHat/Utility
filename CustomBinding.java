package id.uniq.uniqpos.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.uniq.uniqpos.R;
import id.uniq.uniqpos.app.GlideApp;

/**
 * Created by ANNASBlackHat on 06/10/2017.
 */

public class CustomBinding {

    @BindingAdapter(("imgUrl"))
    public static void setImageUrl(ImageView img, String url) {
        if (url != null) {
            Picasso.with(img.getContext())
                    .load(Constant.INSTANCE.getIMAGE_URL()+url)
                    .placeholder(R.drawable.default_image)
                    .into(img);
        }
    }

    @BindingAdapter({"imgUrlTextRound", "name"})
    public static void setImageUrlTextRound(ImageView img, String url, String name) {
        if (url != null) {
            Drawable placeholder = img.getResources().getDrawable(R.drawable.default_image);
            if (name != null) {
                name = name.trim();
                String teks = "A";
                if (name != null && name.length() > 1) teks = name.substring(0, 2);
                if (name != null && name.contains(" "))
                    teks = name.substring(0, 1) + name.substring(name.lastIndexOf(" ") + 1, name.lastIndexOf(" ") + 2);
                ColorGenerator generator = ColorGenerator.MATERIAL;
//                placeholder = TextDrawable.builder().buildRound(teks.toUpperCase(), generator.getRandomColor());
            }

            GlideApp.with(img.getContext())
                    .load(Constant.INSTANCE.getIMAGE_URL() +url)
                    .placeholder(placeholder)
                    .into(img);
        }
    }

    @BindingAdapter({"imgUrlText", "name"})
    public static void setImageUrlText(ImageView img, String url, String name) {
        if (url != null) {
            Drawable placeholder = img.getResources().getDrawable(R.drawable.default_image);
            if (name != null) {
                name = name.trim();
                String teks = "A";
                if (name != null && name.length() > 1) teks = name.substring(0, 2);
                if (name != null && name.contains(" "))
                    teks = name.substring(0, 1) + name.substring(name.lastIndexOf(" ") + 1, name.lastIndexOf(" ") + 2);
                ColorGenerator generator = ColorGenerator.MATERIAL;
                placeholder = TextDrawable.builder()
                        .buildRect(teks.toUpperCase(), generator.getColor(teks));
            }
            Picasso.with(img.getContext())
                    .load(Constant.INSTANCE.getIMAGE_URL() + url)
                    .placeholder(placeholder)
                    .into(img);
        }
    }

    @BindingAdapter("imgDrawable")
    public static void setImgDrawable(ImageView img, int drawable) {
        Glide.with(img.getContext())
                .load(drawable)
//                .placeholder(R.drawable.default_image)
                .into(img);
    }

    @BindingAdapter("timeFromDate")
    public static void setTimeFromDate(TextView txt, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        txt.setText(sdf.format(date));
    }

    @BindingAdapter("timeMillisToTime")
    public static void setTimeFromTimeMillis(TextView txt, Long timeMillis){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        txt.setText(sdf.format(new Date(timeMillis)));
    }

    @BindingAdapter(value = {"timeMillisToDate","format"}, requireAll = false)
    public static void setDateFromTimeMillis(TextView txt, Long timeMillis, String format){
        if(format == null){
            format = "dd-MM-yyyy HH:mm";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        txt.setText(sdf.format(new Date(timeMillis)));
    }

    @BindingAdapter(value = {"textCurrency","symbol"}, requireAll = false)
    public static void setTextCurrency(TextView txt, int value, String symbol) {
        if(symbol == null) symbol = "";
        txt.setText(symbol+NumberFormat.getNumberInstance().format(value));
    }

    @BindingAdapter(value = {"strCurrency","symbol"}, requireAll = false)
    public static void setStringCurrency(TextView txt, String value, String symbol) {
        if(symbol == null) symbol = "";
        int number = 0;
        if(value != null) number = Integer.parseInt(value);
        txt.setText(symbol+NumberFormat.getNumberInstance().format(number));
    }

    @BindingAdapter({"dateFromat","format"})
    public static void setTextDate(TextView txt, Date date,  String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        txt.setText(sdf.format(date));
    }

    @BindingAdapter("spinnerItem")
    public static void setBetterSpinnerItem(MaterialBetterSpinner spinner, String[] items){
        ArrayAdapter adapter = new ArrayAdapter(spinner.getContext(), android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(adapter);
    }
}
