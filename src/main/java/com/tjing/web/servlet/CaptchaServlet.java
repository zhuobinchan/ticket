package com.tjing.web.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author <a href="http://www.micmiu.com">Michael Sun</a>
 */
public class CaptchaServlet extends HttpServlet {

	private static final long serialVersionUID = -124247581620199710L;

	public static final String KEY_CAPTCHA = "SE_KEY_MM_CODE";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 设置相应类型,告诉浏览器输出的内容为图片
		resp.setContentType("image/jpeg");
		// 不缓存此内容
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expire", 0);
        int width=60, height=20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        Random random = new Random();
        g.setColor(getRandColor(200,250));
        g.fillRect(0, 0, width, height);
        
        g.setFont(new Font("Times New Roman",Font.PLAIN,18));

        g.setColor(getRandColor(160,200));
        for (int i=0;i<155;i++)
        {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g.drawLine(x,y,x+xl,y+yl);
        }
        
        String sRand="";
        for (int i=0;i<4;i++)
        {
            String rand=String.valueOf(random.nextInt(10));
            sRand+=rand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(rand,13*i+6,16);
        }
        HttpSession session = req.getSession();
        session.removeAttribute(KEY_CAPTCHA);
        session.setAttribute(KEY_CAPTCHA, sRand);
        g.dispose();
        ImageIO.write(image, "JPEG", resp.getOutputStream());


	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
	private Color getRandColor(int fc,int bc)
    {
            Random random = new Random();
            if(fc>255) fc=255;
            if(bc>255) bc=255;
            int r=fc+random.nextInt(bc-fc);
            int g=fc+random.nextInt(bc-fc);
            int b=fc+random.nextInt(bc-fc);
            return new Color(r,g,b);
    }
}