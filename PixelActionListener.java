package PCcamera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;


public class PixelActionListener implements ActionListener,PixelConfig {
    public JPanel jp;
    public Graphics bg;
    public int[][] pixelArr ; //rgb值
    public File input ;
    public BufferedImage bi_img;
    public BufferedImage delta= new BufferedImage(400,500,BufferedImage.TYPE_INT_RGB);
    public PixelMouse pm;
    public Mosaic mc ;
    public File output;
    public Linked<BufferedImage> imageList ;
    JSlider js;
    public Webcam webcam = Webcam.getDefault();
    WebcamPanel wpanel ;
    //构造方法
    public PixelActionListener(JPanel jp) {
        this.jp = jp;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if (s.equals("打开图片")) {
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            //初始文件夹
            JFileChooser jfc = new JFileChooser("C:\\Users\\ZDX\\Pictures\\Voto");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("选取一张jpg或png图片","jpg","png");
            jfc.setFileFilter(filter);
            jfc.setSize(100,300);
            jfc.showOpenDialog(null);
            //选择的文件路径
            input =jfc.getSelectedFile();
            try {
                pixelArr = getImagePixel(input.getAbsolutePath());//打开图片时缓存图片

            } catch (Exception ioException) {
                System.out.println("未选择图片");
            }
        }
        else if(s.equals("灰度")){ //灰度就是没有色彩，RGB色彩分量全部相等
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            if (pixelArr!=null){
                //每个效果都新创建一个缓冲区
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
            for (int i = 0; i < pixelArr.length; i++) {
                for (int j = 0; j < pixelArr[0].length; j++) {
                    int[] c = getAllColor(pixelArr[i][j]);
                    int gray = (c[0]+c[1]+c[2])/3; //平均法取灰度值
                    bg.setColor(new Color(gray,gray,gray));
                    bg.drawRect(i, j, 1,1);
                    pixelArr[i][j]=gray<<16|gray<<8|gray;
                    //保存像素值变化
                }
            }
            jp.getGraphics().drawImage(bi_img,X0,Y0,null); //画笔的位置就是出现的位置
            imageList.addFirst(bi_img);
        }else
                System.out.println("必须先选定一张图片");
        }
        //亮度调整:三通道加同一个值
        else if (s.equals("亮度")){
            if (js!=null){
                jp.remove(js);
            }
            if (pixelArr!=null){
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
                js = new JSlider(SwingConstants.VERTICAL,-60,60,0);
                jp.add(js,BorderLayout.EAST);
                js.setPaintLabels(true);
                js.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        System.out.println("当前值:"+js.getValue());
                        int param = js.getValue();
                        for (int i = 0; i <pixelArr.length ; i++) {
                            for (int j = 0; j <pixelArr[0].length ; j++) {
                                int red = getAllColor(pixelArr[i][j])[0]+param;
                                int green = getAllColor(pixelArr[i][j])[1]+param;
                                int blue = getAllColor(pixelArr[i][j])[2]+param;
                                red = limit(red); //防止越界
                                green = limit(green);
                                blue = limit(blue);
                                bg.setColor(new Color(red,green,blue));
                                bg.drawLine(i , j , i , j );
                            }
                        }
                        //手动更新
                        jp.getGraphics().drawImage(bi_img,X0,Y0,null);
                    }
                });
                //只能添加一次?
                imageList.addFirst(bi_img);
            }else
                System.out.println("必须先选择一张图片");
        }
        else if (s.equals("黑白")){
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            if (pixelArr!=null){
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
                for (int i = 0; i <pixelArr.length ; i++) {
                    for (int j = 0; j <pixelArr[0].length ; j++) {
                        int[]temp = getAllColor(pixelArr[i][j]);
                        int bw = (temp[0]+temp[1]+temp[0])/3;
                        // 三通道平均值大于128 则设为白色 否则设为黑色
                        if (bw>=128){
                            bw=255;
                        }else
                            bw=0;
                        bg.setColor(new Color(bw,bw,bw));
                        bg.drawLine(i , j , i , j );
                        pixelArr[i][j]=bw<<16|bw<<8|bw;
                    }
                }
                jp.getGraphics().drawImage(bi_img,X0,Y0,null);
                imageList.addFirst(bi_img);
            }
            else
                System.out.println("必须先选择一张图片");
        }
        else if (s.equals("马赛克")) {
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            if (pixelArr != null) {
                bi_img =new BufferedImage(pixelArr.length, pixelArr[0].length, BufferedImage.TYPE_INT_RGB);
                bg = bi_img.getGraphics();
                for (int i = 0; i < pixelArr.length; i++) {
                    for (int j = 0; j < pixelArr[i].length; j += MOSAIC_SIZE) {
                        int rgb = pixelArr[i][j];
                        Color color = new Color(rgb);
                        bg.setColor(color);
                        bg.fillRect(i, j, MOSAIC_SIZE, MOSAIC_SIZE);
                    }
                }
                pixelArr=readImage(bi_img);
                jp.getGraphics().drawImage(bi_img, X0, Y0, null);
                imageList.addFirst(bi_img);
            }else
                System.out.println("必须先选择一张图片");
        }
        else if(s.equals("保存图片")){
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            JFileChooser jfc2 = new JFileChooser("C:\\Users\\ZDX\\Pictures\\Voto");
            jfc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("png","jpg","png");
            jfc2.setFileFilter(filter);
            jfc2.showSaveDialog(null);
            File fl=jfc2.getSelectedFile(); //选中的文件夹
            if(fl!=null){
            String path = fl.getAbsolutePath()+"\\"+SAVE_NAME+".png";
            try {
                save(path);
                System.out.println("保存成功");
            } catch (IOException ioException) {
                System.out.println("未保存");
            }
        }else
                System.out.println("未选取文件");
        }

        else if(s.equals("上一步")){
            if (js!=null){
                jp.remove(js);
                jp.repaint();
            }
            if (imageList!=null&&imageList.getSize()>1){
            this.imageList.deleteFirst();
            BufferedImage temp = this.imageList.getHead().getT();
            pixelArr=readImage(temp);
            System.out.println("效果列表长度："+imageList.getSize());
            jp.getGraphics().drawImage(temp,X0,Y0,null);
        } else
                System.out.println("不能回退");
        }

        else if(s.equals("三角")){
            delta(5,X0+100,Y0+100,600);
            //mountain(X0,Y0,X0+300,Y0+200, jp.getGraphics(), 5,5.6);

            moutain(X0,X0+600,Y0,Y0+600, jp.getGraphics(), 450,0.5);
        }

        else if(s.equals("相机")){
            if (webcam.isOpen()){
                webcam.close();
                return;}
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.addWebcamListener(new PhotoListener(webcam, jp));
                WebcamPanel wpanel = new WebcamPanel(webcam);
                wpanel.setVisible(true);
                jp.add(wpanel, BorderLayout.SOUTH);
                Graphics g = wpanel.getGraphics();
                g.setColor(Color.white);
                g.drawRect(X0,Y0,2,2);
                jp.repaint();


            /*
            JFrame frame=new JFrame();
            frame.setResizable(false);
            frame.setSize(400,400);
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            frame.setVisible(true);

            PhotoListener pl = new PhotoListener(webcam);
            JButton take= new JButton("确定");
            take.addActionListener(pl);
            take.setPreferredSize(new Dimension(120, 80));
            frame.add(take);

             */





        }
        else
            System.out.println("其他");

    }

    //画图片
    public int[][] getImagePixel(String s) throws Exception {
        //定义二维数组 存储RGB值
        BufferedImage bi ;
        imageList = new Linked<>() ;
        File file = new File(s);
        if (file.exists()) {
            bi = ImageIO.read(file);
            //获得图片长和宽
            int height = bi.getHeight();
            int width = bi.getWidth();
            int[][] pic = new int[width][height];
            bi_img =new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            bg = bi_img.getGraphics();
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = bi.getRGB(i, j);
                    pic[i][j] = rgb;
                    bg.setColor(new Color(rgb));
                    bg.drawRect(i, j, 1,1);
                }
            }
            System.out.println("画图成功");
            jp.getGraphics().drawImage(bi_img,X0,Y0,null);
            jp.repaint(); //手动刷新解决图片重叠问题
            imageList.addFirst(bi_img);
            return pic;
        } else
            System.out.println("路径错误");
        return null;
    }
    //根据RGB值 返回三原色值
    public int[] getAllColor(int image) {
        Color c = new Color(image);
        int[] rgb = new int[3];
        rgb[0] = c.getRed();//(image & 0xff0000) >> 16; //对应红色部分 右移16位
        rgb[1] = c.getGreen();//(image & 0xff00) >> 8; // 绿色部分
        rgb[2] = c.getBlue();//(image & 0xff); //蓝色部分
        return rgb;
    }

    //限定最大值
    public int limit(int rgb){
        if (rgb>255){
            rgb = 255;
        }else if(rgb<0){
            rgb = 0;
        }
        return rgb;
    }

    //从图片读取RGB数组
    public int[][] readImage(BufferedImage b){
        int [][] temp = new int[b.getWidth()][b.getHeight()];
        for (int i =0;i<b.getWidth();i++){
            for (int j =0;j<b.getHeight();j++){
                temp[i][j] = b.getRGB(i,j);
            }
        }
        return temp;
    }
    //从RGB数组读取图片
    public void printImage(int [][] image,Graphics g){
        if (image!=null){
            for (int i=0;i<image.length;i++){
                for (int j=0;j<image[0].length;j++){
                    int rgb = image[i][j];
                    g.setColor(new Color(rgb));
                    g.drawRect(i,j,1,1);
                }
            }
        }

    }
    // 传入三角形边长
    public void delta(int n ,int x,int y,int l){
        if (n==0){return;}
        //n是迭代次数
        double x1 = x-l/2;
        //根据初始顶点和边长关系计算出另外两个顶点坐标
        double args = Math.sqrt(3.0)/2; //等边三角形的高是1.5倍的边长
        double y1 = y+args*l;
        double x2 = x+l/2;
        double y2 = y+args*l;

        //进行强制类型转换 drawline需要int型
        int xx1 = (int)x1;
        int yy1 = (int)y1;
        int xx2 = (int)x2;
        int yy2 = (int)y2;

        Graphics g = jp.getGraphics();
        //画三角形
        g.drawLine(x, y, xx1, yy1);
        g.drawLine(x, y, xx2, yy2);
        g.drawLine(xx1,yy1,xx2,yy2);

        //三段线段的中点坐标
        int m1x = (x+xx1)/2;
        int m1y = (yy1+y)/2;
        int m2x = (xx2+x)/2;
        int m2y = (yy2+y)/2;
        int m3x = (xx2+xx1)/2; //底边纵坐标是yy2

        //连接所有中点
        g.drawLine(m1x, m1y, m2x, m2y);
        g.drawLine(m1x, m1y, m3x, yy2);
        g.drawLine(m2x,m2y,m3x, yy2);

        delta(n-1,m1x,m1y,l/2);
        delta(n-1,m2x,m2y,l/2);
        delta(n-1,x,y,l/2);
    }

    public void moutain(int xl,int xr,int yl,int yr,Graphics g,int range,double rate){

        if(xr-xl<1||range==0){//递归终止 也是开始画图的起点
            g.drawLine(xl, yl, xr, yr);
        }else{
            int x=(xr+xl)/2;
            int y=(yr+yl)/2;
            Random rand =new Random();
            int temp=rand.nextInt(range*2)-range;
            range=(int)(range*rate);
            //range*rate的作用类似于倒数计数器
            moutain(x,xr,y+temp,yr,g,range,rate);
            moutain(xl,x,yl,y+temp,g,range,rate);
        }
    }

    public void save(String s) throws IOException {
        output = new File(s);
        ImageIO.write(bi_img, "png", output);
    }
}
