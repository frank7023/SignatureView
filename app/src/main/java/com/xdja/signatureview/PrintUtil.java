package com.xdja.signatureview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import com.printer.bluetooth.android.BluetoothPrinter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrintUtil {

	public static int ALIGN_LEFT = BluetoothPrinter.COMM_ALIGN_LEFT;
	public static int ALIGN_CENTER = BluetoothPrinter.COMM_ALIGN_CENTER;
	public static int ALIGN_RIGHT = BluetoothPrinter.COMM_ALIGN_RIGHT;

	private static final String TAG = "PrintUtil";

    public static boolean isInit = false;
    
    public static String PRINT_TYPE_T9 = "T9_PRINT";
    public static String PRINT_TYPE_DEFAULT = "TIII_PRINT";
    
    public static String printType = PRINT_TYPE_DEFAULT;

    public static void print_init(BluetoothPrinter mPrinter) {
        if(!mPrinter.isConnected())return;
        mPrinter.init();
        isInit = true;
    }
    public static void print_close() {
        isInit = false;
    }

    public static void setPrintType(String type) {
        printType = type;
    }

    public static void printTextLine_t9(BluetoothPrinter mPrinter, String content, int align){
        Log.i(TAG, "printTextLine_t9 ()");
        if(mPrinter==null) return;
        if (!isInit) print_init(mPrinter);
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, align);
        mPrinter.printText(content);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 1);
    }

    public static void printTextLine_default(BluetoothPrinter mPrinter, String content, int align){
        Log.i(TAG, "printTextLine_default ()");
        if(mPrinter==null) return;
        if(!mPrinter.isConnected()) return;
        mPrinter.init();
        mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, align);
        mPrinter.setPrinter(BluetoothPrinter.COMM_LINE_HEIGHT,28);
        mPrinter.printText(content);
        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_NEWLINE);
    }

	/**
	 * 打印文字信息并换行
	 * @param mPrinter 打印设备实例
	 * @param content 需打印的内容
	 * @param align 打印对齐方式   居左-ALIGN_LEFT 居中-ALIGN_CENTER 居右-ALIGN_RIGHT
	 */
	public static void printTextLine(BluetoothPrinter mPrinter, String content, int align){
        if (printType.equals(PRINT_TYPE_T9)) {
            printTextLine_t9(mPrinter, content, align);
        } else if (printType.equals(PRINT_TYPE_DEFAULT)){
            printTextLine_default(mPrinter, content, align);
        }
	}

	/**
	 * 打印文字信息并换行
	 * @param mPrinter 打印设备实例
	 * @param content 需打印的内容
	 * @param align 打印对齐方式   居左-ALIGN_LEFT 居中-ALIGN_CENTER 居右-ALIGN_RIGHT
	 */
	public static void printText(BluetoothPrinter mPrinter, String content, int align){
		if(mPrinter==null)return;
		if(!mPrinter.isConnected())return;
		mPrinter.printText(content);
	}

	/**
	 * 打印需要放大字体的文字信息并换行
	 * @param mPrinter 打印设备实例
	 * @param content 需打印的内容
	 * @param align 打印对齐方式   居左-ALIGN_LEFT 居中-ALIGN_CENTER 居右-ALIGN_RIGHT
	 * @param x 打印字体宽度需要放大的倍数(0~7)
	 * @param y 打印字体高度需要放大的倍数(0~7)
	 */
	public static void printTextMultiple(BluetoothPrinter mPrinter, String content, int align, int x, int y){
		mPrinter.init();
		mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, align);
		mPrinter.setPrinter(BluetoothPrinter.COMM_LINE_HEIGHT,28);
		mPrinter.setCharacterMultiple(x, y);
		mPrinter.printText(content);
		mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_NEWLINE);
	}
	
	/**
	 * 打印空行
	 * @param mPrinter 打印设备实例
	 */
	public static void printNullLine(BluetoothPrinter mPrinter){
//		mPrinter.init();
	    if (printType.equals(PRINT_TYPE_DEFAULT)) {
	        mPrinter.printText("\r\n");
	        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_NEWLINE);
	    } else {
	        mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 1);
	    }
	}

	public static void printNewLine(BluetoothPrinter mPrinter){
       mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, 1);
    }
	public static void printBarCode(BluetoothPrinter mPrinter, String codeNum, boolean isT9){
		mPrinter.init();
		mPrinter.setPrinter(BluetoothPrinter.COMM_LINE_HEIGHT,28);
		mPrinter.setCharacterMultiple(0, 0);
		mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN, BluetoothPrinter.COMM_ALIGN_CENTER);
		/**
		 * 设置左边距,nL,nH
		 * 设置宽度为(nL+nH*256)* 横向移动单位.
		 * 设置左边距对打印条码的注释位置有影响.
		 */
		//  mPrinter.setLeftMargin(15, 0);
		// mPrinter.setPrinter(BluetoothPrinter.COMM_ALIGN,BluetoothPrinter.COMM_ALIGN_LEFT);

		/**
		 * 参数1: 设置条码横向宽度 2<=n<=6,默认为2
		 * 参数2: 设置条码高度 1<=n<=255,默认162
		 * 参数3: 设置条码注释打印位置.0不打印,1上方,2下方,3上下方均有,默认为0
		 * 参数4: 设置条码类型.BluetoothPrinter.BAR_CODE_TYPE_ 开头的常量,默认为CODE128
		 */
		if(isT9){
			mPrinter.setBarCode(3, 60, 0, BluetoothPrinter.BAR_CODE_TYPE_CODE128);
		}else{
			mPrinter.setBarCode(3, 80, 0, BluetoothPrinter.BAR_CODE_TYPE_CODE128);
		}
		mPrinter.printBarCode(codeNum);
	}
	/**
	 * 打印条形码 (CODE_128)
	 * @param mPrinter
	 * @param codeNum
	 */
	public static void printBarCode(BluetoothPrinter mPrinter, String codeNum) {
		printBarCode(mPrinter,codeNum,false);
	}

	/**
	 * 图片转化为字节数组
	 * @param bitmap
	 * @return
	 */
	public static byte[] changeGrey(Bitmap bitmap)
	{
		//byte[] data=null;
		//bitmap =BitmapFactory.decodeFile("/sdcard/qianming.bmp");
		if(bitmap==null)
		{
			return null;
		}
		int imgwidth = bitmap.getWidth();
		int imgheight = bitmap.getHeight();
		int bufsize = imgwidth * ((imgheight - 1) / 8 + 1);
		int heightbyte = (imgheight - 1) / 8 + 1;
        byte[] maparray = new byte[bufsize];
        int m1, n1;
        //Canvas gc=new Canvas(bitmap);

        //Paint paint = new Paint();   
        // 绘制矩形区域-实心矩形   
        // 设置颜色   
        //paint.setColor(Color.WHITE);   
        // 设置样式-填充   
        //paint.setStyle(Style.FILL);   
        //gc.drawRect(new Rect(0, 0, imgwidth, imgheight), paint);  
        int[][] D  = { { 0, 8, 2, 10 }, { 12, 4, 14, 6 }, { 3, 11, 1, 9 }, { 15, 7, 13, 5 } };
        for (int j = 0; j < imgheight; j++)
             for (int i = 0; i < imgwidth; i++)
             {
            	
            	 int pixelColor = bitmap.getPixel(i, j);//bmp.GetPixel(i, j);
                 int red = Color.red(pixelColor);
                 int green = Color.green(pixelColor);
                 int blue = Color.blue(pixelColor);
                 int Gray = (red * 19595 + green * 38469 + blue * 7472) >> 16;
	             int Ii = i % 4;
	             int Jj = j % 4;
	             if (D[Ii][Jj] * 14 >= Gray ) {
	                 //g.FillRectangle(blackbrush, i, j, 1, 1);
	                 m1 = (j / 8) * imgwidth + i;
	                 n1 = j - (j / 8) * 8;
	                 maparray[m1] |= (byte)(1 << 7 - ((byte)n1));
	             }
             }
		return maparray;
		
	}

	/**
	 * 走纸多少行
	 * @param mPrinter 打印设备实例
	 * @param height 所要走纸的行数
	 */
    public static void printHeight(BluetoothPrinter mPrinter, int height){
		if(mPrinter==null)return;
		if(!mPrinter.isConnected())return;
		mPrinter.init();
		mPrinter.setPrinter(BluetoothPrinter.COMM_PRINT_AND_WAKE_PAPER_BY_LINE, height);
	}
	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveJPGE_After(Bitmap picBitMap) {
		String picTemp = Environment.getExternalStorageDirectory() + "/temp.jpg";
		saveJPGE_After(picBitMap, picTemp);
	}

	public static Bitmap createBitamp(String perfix, Bitmap bt, String postfix) {
		bt = Bitmap.createBitmap(150, 40, Bitmap.Config.ALPHA_8);
		//bt.

		return null;
	}
	public static Bitmap getBitmap(String perfix , Bitmap bitmap, String postfix) {

		if (bitmap == null) {
			return null;
		}

		int width, height;
		height = bitmap.getHeight();
		width = bitmap.getWidth()+580;
		int fontsize = 23;

		Bitmap retBitmap = Bitmap.createBitmap(width ,height, Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(retBitmap);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		paint.setTextSize(fontsize);

		canvas.drawText(perfix, 0, (fontsize+ height)/2, paint);

		canvas.drawBitmap(bitmap, 100,0, paint);

		canvas.drawText(postfix, 120 + bitmap.getWidth(), (fontsize+ height)/2, paint);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		return retBitmap;
	}

	public static Bitmap getBitmap(String perfix, Bitmap bitmap) {

		if (bitmap == null) {
			return null;
		}

		int width, height;
		height = bitmap.getHeight();
		width = bitmap.getWidth()+580;
		int fontsize = 23;

		Bitmap retBitmap = Bitmap.createBitmap(width ,height, Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(retBitmap);
		canvas.drawColor(Color.WHITE);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		paint.setTextSize(fontsize);

		canvas.drawText(perfix, 0, (fontsize+ height)/2, paint);

		canvas.drawBitmap(bitmap, 280,0, paint);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		return retBitmap;
	}
}
