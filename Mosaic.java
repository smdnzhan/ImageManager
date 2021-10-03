package PCcamera;

import java.awt.*;

public class Mosaic implements PixelConfig{
    private int length=MOSAIC_SIZE;
    ;
    public PixelActionListener pal;
    public PixelMouse pm;
    boolean click;
    boolean method;
    public int width ;
    public int height ;

    Mosaic(PixelActionListener pal){
        this.click=false;
        this.pal = pal;
        this.method = false;
        this.width = pal.pixelArr.length;
        this.height = pal.pixelArr[0].length;
    }
    public void setMethod(){
        this.method ^=true; //开启和关闭交替
    }
    //鼠标点击位置
    public void drawMosaic(int x,int y,Graphics g){
        if (pal.pixelArr!=null&&click&&method){
            //取图片较小边边长的二十分之一作为马赛克边长
            int size = Math.min(width,height)/20;

            //限制画马赛克范围在图片内部
            int w_min = Math.min(x+size,pal.pixelArr.length);
            int h_min = Math.min(y+size,pal.pixelArr[0].length);
            int w_max = Math.max(x-size,X0);
            int h_max = Math.max(y-size,Y0);

            for(int i = w_max;i<w_min;i+=size){
                for (int j = h_max;j<h_min;j+=size){
                    g.setColor(new Color(pal.pixelArr[i][j]));
                    g.drawRect(i,j,size,size);
                    //g.drawImage(pal.bi_img,X0,Y0,null);
                    System.out.println("画图执行了2");

                }
            }
        }
    }
}
