package example.com.two.news;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
public class Thumbnail {
    public static Bitmap getThumbnail(byte[] imageData,int width,int height){
        Bitmap img_bitmap = null;
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;/*设置让解码器以最佳方式解码*/
        options.inJustDecodeBounds = true;
        img_bitmap = BitmapFactory.decodeByteArray(imageData,0,imageData.length, options);
        options.inJustDecodeBounds = false;//设为 false
        //计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        img_bitmap = BitmapFactory.decodeByteArray(imageData,0,imageData.length, options);
        img_bitmap = ThumbnailUtils.extractThumbnail(img_bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return img_bitmap;
    }
}

